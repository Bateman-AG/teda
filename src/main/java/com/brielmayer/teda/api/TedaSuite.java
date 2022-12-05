package com.brielmayer.teda.api;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.type.DatabaseCreator;
import com.brielmayer.teda.exception.TedaException;
import com.brielmayer.teda.handler.ExecutionHandler;
import com.brielmayer.teda.handler.LoadHandler;
import com.brielmayer.teda.handler.TestHandler;
import com.brielmayer.teda.handler.TruncateHandler;
import com.brielmayer.teda.model.Bean;
import com.brielmayer.teda.model.Cockpit;
import com.brielmayer.teda.parser.BeanParser;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
public class TedaSuite {
    private final ExecutionHandler executionHandler;

    public void executeSheet(InputStream tedaSheetInputStream) {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(tedaSheetInputStream);
        } catch (IOException e) {
            String error =
                    "\nUnable to open Teda sheet" +
                            "\n%s";
            throw new TedaException(error, e.getMessage());
        }
        Database database = getDatabase();

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
                        TruncateHandler.truncate(database, value);
                        break;
                    case LOAD:
                        XSSFSheet loadSheet = workbook.getSheet(value);
                        LoadHandler.load(database, loadSheet);
                        break;
                    case EXECUTE:
                        executionHandler.execute(value);
                        break;
                    case TEST:
                        XSSFSheet testSheet = workbook.getSheet(value);
                        TestHandler.test(database, testSheet);
                        break;
                }
            }
        }
    }

    private static Database getDatabase() {
        try {
            // load teda.properties file
            Properties config = new Properties();
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String iconConfigPath = rootPath + "teda.properties";
            config.load(new FileInputStream(iconConfigPath));

            // check valid url and read properties
            String jdbcUrl = config.getProperty("jdbc.url");
            if (jdbcUrl == null || jdbcUrl.isEmpty()) {
                throw new TedaException("No jdbc url found in test teda.properties");
            }
            String user = config.getProperty("jdbc.user");
            String password = config.getProperty("jdbc.password");

            // create database
            DatabaseConnection databaseConnection = new DatabaseConnection(jdbcUrl, user, password);
            return DatabaseCreator.createDatabaseByUrlAndConfig(databaseConnection);
        } catch (IOException e) {
            throw new TedaException("No teda.properties file found in test directory");
        }
    }
}
