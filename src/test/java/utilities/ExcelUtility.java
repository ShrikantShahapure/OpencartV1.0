package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;

public class ExcelUtility {

    private String path;
    private FileInputStream fi;
    private FileOutputStream fo;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private DataFormatter formatter;

    // Constructor
    public ExcelUtility(String path) throws IOException {
        this.path = path;
        this.formatter = new DataFormatter();
        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
    }

    // Load a sheet
    public void setSheet(String sheetName) {
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new RuntimeException("Sheet not found: " + sheetName);
        }
    }

    // Get row count (excluding header)
    public int getRowCount() {
        return sheet.getLastRowNum(); // 0-based
    }

    // Get cell count from a specific row
    public int getCellCount(int rowNum) {
        XSSFRow row = sheet.getRow(rowNum);
        if (row == null) return 0;
        return row.getLastCellNum();
    }

    // Get cell data as String
    public String getCellData(int rowNum, int colNum) {
        XSSFRow row = sheet.getRow(rowNum);
        if (row == null) return "";

        XSSFCell cell = row.getCell(colNum);
        if (cell == null) return "";

        return formatter.formatCellValue(cell);
    }

    // Write data to cell
    public void setCellData(String sheetName, int rowNum, int colNum, String value)
            throws IOException {

        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }

        XSSFRow row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }

        XSSFCell cell = row.createCell(colNum);
        cell.setCellValue(value);

        fo = new FileOutputStream(path);
        workbook.write(fo);
        fo.close();
    }

    // Close workbook & streams
    public void close() throws IOException {
        workbook.close();
        fi.close();
    }
}
