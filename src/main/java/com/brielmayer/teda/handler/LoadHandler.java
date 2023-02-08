package com.brielmayer.teda.handler;

import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.exception.TedaException;
import com.brielmayer.teda.model.Bean;
import com.brielmayer.teda.parser.BeanParser;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;

public final class LoadHandler {

    private static final Logger log  = LoggerFactory.getLogger(LoadHandler.class);

    private static final String TABLE_BEAN = "#Table";

    public static void load(final BaseDatabase database, final XSSFSheet sheet) {
        final Bean beanToLoad = BeanParser.parse(sheet, TABLE_BEAN);
        log.info("Load table: {}", beanToLoad.getBeanName());

        int rowCount = 1;
        for (final Map<String, Object> row : beanToLoad.getData()) {
            try {
                database.insertRow(beanToLoad.getBeanName(), row);
            } catch (final SQLException e) {
                throw TedaException.builder()
                        .appendMessage("Failed to insert data into %s", beanToLoad.getBeanName())
                        .appendMessage("Row %d contains an error", rowCount)
                        .cause(e)
                        .build();
            }
            rowCount++;
        }
    }
}
