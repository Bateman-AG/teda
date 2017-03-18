package com.brielmayer.teda.api;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.exception.TedaException;
import com.brielmayer.teda.handler.ExecutionHandlerI;
import com.brielmayer.teda.handler.LoadHandler;
import com.brielmayer.teda.handler.TestHandler;
import com.brielmayer.teda.handler.TruncateHandler;
import com.brielmayer.teda.model.Bean;
import com.brielmayer.teda.model.Cockpit;
import com.brielmayer.teda.parser.BeanParser;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class TedaSuite {

    private ExecutionHandlerI executionHandler;

    public TedaSuite(ExecutionHandlerI executionHandler) {
        this.executionHandler = executionHandler;
    }

    public void executeSheet(InputStream tedaSheetInputStream, Database database) {
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
        for(Map<String, Object> row : bean.getData()) {
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
}
