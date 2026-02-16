package utilities;

import java.io.IOException;
import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name = "LoginData")
    public String[][] getData() throws IOException {

        String path = ".\\testData\\OpenCartLoginData.xlsx";
        String sheetName = "Sheet1";

        ExcelUtility xlutil = new ExcelUtility(path);
        xlutil.setSheet(sheetName); 

        int totalRows = xlutil.getRowCount();   // data rows (excluding header)
        int totalCols = xlutil.getCellCount(0); // header row

        String[][] loginData = new String[totalRows][totalCols];

        for (int i = 1; i <= totalRows; i++) {
            for (int j = 0; j < totalCols; j++) {
                loginData[i - 1][j] = xlutil.getCellData(i, j);
            }
        }

        xlutil.close();
        return loginData;
    }
}
