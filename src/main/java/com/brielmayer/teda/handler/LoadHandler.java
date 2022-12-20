package com.brielmayer.teda.handler;

import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.exception.TedaException;
import com.brielmayer.teda.model.Bean;
import com.brielmayer.teda.parser.BeanParser;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.sql.SQLException;
import java.util.Map;

public class LoadHandler {

    private static final String TABLE_BEAN = "#Table";

    public static void load(BaseDatabase database, XSSFSheet sheet) {
        Bean beanToLoad = BeanParser.parse(sheet, TABLE_BEAN);
        int rowCount = 1;
        for (Map<String, Object> row : beanToLoad.getData()) {
            try {
                database.insertRow(beanToLoad.getBeanName(), row);
            } catch (SQLException e) {
                String error =
                        "\nFailed to insert data into %s" +
                        "\nRow %d contains an error: %s";
                throw new TedaException(error, beanToLoad.getBeanName(), rowCount, e.getMessage());
            }
            rowCount++;
        }
    }
}
