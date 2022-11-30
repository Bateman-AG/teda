package com.brielmayer.teda.handler;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.exception.TedaException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoadHandlerTest {

    @Test
    public void loadHandler_validData_success() throws SQLException {
        //Mocks
        Database database = Mockito.mock(Database.class);
        doNothing().when(database).insertRow(any(), any());

        // Testdata
        InputStream resourceAsStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/LOAD_TEST.xlsx");
        XSSFSheet sheet = getWorkbook(resourceAsStream).getSheet("STUDENT_IN");

        LoadHandler.load(database, sheet);

        verify(database, times(4)).insertRow(any(), any());
    }

    @Test
    public void loadHandler_sqlException_throwsTeDaException() throws SQLException {
        //Mocks
        Database database = Mockito.mock(Database.class);
        doThrow(SQLException.class)
                .when(database)
                .insertRow(any(), any());

        // Testdata
        InputStream resourceAsStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/LOAD_TEST.xlsx");
        XSSFSheet sheet = getWorkbook(resourceAsStream).getSheet("STUDENT_IN");

        assertThrows(TedaException.class, () -> LoadHandler.load(database, sheet));
    }

    private XSSFWorkbook getWorkbook(InputStream tedaSheetInputStream) {
        try {
            return new XSSFWorkbook(tedaSheetInputStream);
        } catch (IOException e) {
            String error =
                    "\nUnable to open Teda sheet" +
                            "\n%s";
            throw new TedaException(error, e.getMessage());
        }
    }

}
