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
import java.io.*;
import java.util.Map;

public class TedaSuite {

    private final BaseDatabase inputDatabase;
    private final BaseDatabase outputDatabase;

    private final ExecutionHandler executionHandler;

    public TedaSuite(DataSource dataSource, ExecutionHandler executionHandler) {
        this.inputDatabase = DatabaseFactory.createDatabase(dataSource);
        this.outputDatabase = DatabaseFactory.createDatabase(dataSource);
        this.executionHandler = executionHandler;
    }

    public TedaSuite(DataSource inputDataSource, DataSource outputDataSource, ExecutionHandler executionHandler) {
        this.inputDatabase = DatabaseFactory.createDatabase(inputDataSource);
        this.outputDatabase = DatabaseFactory.createDatabase(outputDataSource);
        this.executionHandler = executionHandler;
    }

    public void executeSheet(String filePath) {
        executeSheet(new File(filePath));
    }

    public void executeSheet(File file) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new TedaException("File not found: %s", file.getAbsolutePath());
        }
        executeSheet(fileInputStream);
    }

    public void executeSheet(InputStream inputStream) {
        XSSFWorkbook workbook;

        try {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            String error =
                    "\nUnable to open Teda sheet" +
                            "\n%s";
            throw new TedaException(error, e.getMessage());
        }

        Bean bean = BeanParser.parse(workbook.getSheet("Cockpit"), "#Teda");
        for (Map<String, Object> row : bean.getData()) {
            for (Map.Entry<String, Object> entry : row.entrySet()) {

                // cockpit must only contain strings
                String header = entry.getKey();
                String value = (String) entry.getValue();

                // if cell is empty, no action will be performed
                if (value == null || value.isEmpty()) {
                    continue;
                }

                switch (Cockpit.valueOf(header)) {
                    case TRUNCATE:
                        TruncateHandler.truncate(outputDatabase, value);
                        break;
                    case LOAD:
                        XSSFSheet loadSheet = workbook.getSheet(value);
                        LoadHandler.load(inputDatabase, loadSheet);
                        break;
                    case EXECUTE:
                        executionHandler.execute(value);
                        break;
                    case TEST:
                        XSSFSheet testSheet = workbook.getSheet(value);
                        TestHandler.test(outputDatabase, testSheet);
                        break;
                }
            }
        }
    }
}
