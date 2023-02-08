package com.brielmayer.teda;

import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.database.DatabaseFactory;
import com.brielmayer.teda.exception.TedaException;
import com.brielmayer.teda.handler.ExecutionHandler;
import com.brielmayer.teda.handler.LoadHandler;
import com.brielmayer.teda.handler.TestHandler;
import com.brielmayer.teda.handler.TruncateHandler;
import com.brielmayer.teda.model.Bean;
import com.brielmayer.teda.model.Cockpit;
import com.brielmayer.teda.parser.BeanParser;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class TedaSuite {

    private final BaseDatabase inputDatabase;
    private final BaseDatabase outputDatabase;

    private final ExecutionHandler executionHandler;

    public TedaSuite(final DataSource dataSource) {
        this(dataSource, dataSource, new LogExecutionHandler());
    }

    public TedaSuite(final DataSource dataSource, final ExecutionHandler executionHandler) {
        this(dataSource, dataSource, executionHandler);
    }

    public TedaSuite(final DataSource inputDataSource, final DataSource outputDataSource, final ExecutionHandler executionHandler) {
        this.inputDatabase = DatabaseFactory.createDatabase(inputDataSource);
        this.outputDatabase = DatabaseFactory.createDatabase(outputDataSource);
        this.executionHandler = executionHandler;
    }

    public void executeSheet(final String filePath) {
        executeSheet(Paths.get(filePath));
    }

    public void executeSheet(final Path filePath) {
        try {
            executeSheet(Files.newInputStream(filePath));
        } catch (IOException e) {
            throw TedaException.builder()
                    .appendMessage("Unable to read file %s", filePath)
                    .cause(e)
                    .build();
        }
    }

    public void executeSheet(final InputStream inputStream) {
        final XSSFWorkbook workbook;

        try {
            workbook = new XSSFWorkbook(inputStream);
        } catch (final IOException e) {
            throw TedaException.builder()
                    .appendMessage("Unable to read InputStream")
                    .cause(e)
                    .build();
        }

        final Bean bean = BeanParser.parse(workbook.getSheet("Cockpit"), "#Teda");
        for (final Map<String, Object> row : bean.getData()) {
            for (final Map.Entry<String, Object> entry : row.entrySet()) {

                // cockpit must only contain strings
                final String header = entry.getKey();
                final String value = (String) entry.getValue();

                // if cell is empty, no action will be performed
                if (value == null || value.isEmpty()) {
                    continue;
                }

                switch (Cockpit.valueOf(header)) {
                    case TRUNCATE:
                        TruncateHandler.truncate(outputDatabase, value);
                        break;
                    case LOAD:
                        final XSSFSheet loadSheet = workbook.getSheet(value);
                        LoadHandler.load(inputDatabase, loadSheet);
                        break;
                    case EXECUTE:
                        executionHandler.execute(value);
                        break;
                    case TEST:
                        final XSSFSheet testSheet = workbook.getSheet(value);
                        TestHandler.test(outputDatabase, testSheet);
                        break;
                }
            }
        }
    }
}
