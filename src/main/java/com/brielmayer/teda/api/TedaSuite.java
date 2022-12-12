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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class TedaSuite {
    private static final String INPUT_PREFIX = ".input";
    private static final String OUTPUT_PREFIX = ".output";

    private final ExecutionHandler executionHandler;

    public TedaSuite(ExecutionHandler executionHandler) {
        this.executionHandler = executionHandler;
    }

    public void executeSheet(InputStream tedaSheetInputStream) {
        Properties config = getProperties();
        Database inputDatabase = DatabaseCreator.createDatabase(getDatabaseConnection(config, INPUT_PREFIX));
        Database outputDatabase = DatabaseCreator.createDatabase(getDatabaseConnection(config, OUTPUT_PREFIX));
        executeSheet(tedaSheetInputStream, inputDatabase, outputDatabase);
    }

    public void executeSheet(InputStream tedaSheetInputStream, Database database) {
        executeSheet(tedaSheetInputStream, database, database);
    }

    public void executeSheet(InputStream tedaSheetInputStream, Database inputDatabase, Database outputDatabase) {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(tedaSheetInputStream);
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

    private static DatabaseConnection getDatabaseConnection(Properties config, String prefix) {

        // check valid url and read properties
        String jdbcUrl = config.getProperty("jdbc" + prefix + ".url");
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            throw new TedaException("No jdbc url found in test/resources/teda.properties");
        }
        String user = config.getProperty("jdbc" + prefix + ".user");
        String password = config.getProperty("jdbc" + prefix + ".password");

        // create database
        return new DatabaseConnection(jdbcUrl, user, password);
    }

    private static Properties getProperties() {
        try {
            // load teda.properties file
            Properties config = new Properties();
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String iconConfigPath = rootPath + "teda.properties";
            config.load(new FileInputStream(iconConfigPath));
            return config;
        } catch (IOException e) {
            throw new TedaException("No teda.properties file found in test/resources directory");
        }
    }
}
