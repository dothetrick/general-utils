package utils.excel;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 简单读写excel的POI封装
 * 全部为static方法，无需创建对象直接使用
 * 适用于较小的文件，大文件读写时可能会oom
 */
public class ExcelSimpleUtil {
    /**
     * 读取sheet中全部数据
     *
     * @param path      excel文件路径
     * @param sheetName excel文件中的sheet名称
     * @return sheet中全部数据，一个二维list，包含标题行
     * @throws IOException
     */
    public static List<List<String>> readSheet(String path, String sheetName) throws IOException {
        FileInputStream fs = new FileInputStream(path);
        Workbook wk = new XSSFWorkbook(fs);
        Sheet sheet = wk.getSheet(sheetName);
        return readSheet(sheet);
    }

    /**
     * 按一个sheet读取数据
     *
     * @param sheet
     * @return
     */
    public static List<List<String>> readSheet(Sheet sheet) {
        List<List<String>> data = new ArrayList<>();
        for (int numRow = 0; numRow <= sheet.getLastRowNum(); numRow++) {
            Row row = sheet.getRow(numRow);
            if (row == null) {
                continue;
            }
            List<String> sheetData = new ArrayList<>();
            for (int numCell = 0; numCell < row.getLastCellNum(); numCell++) {
                Cell cell = row.getCell(numCell);
                if (cell == null) {
                    continue;
                }
                sheetData.add(cell.toString());
            }
            data.add(sheetData);
        }
        return data;
    }

    /**
     * 读取一个workboot中全部sheet
     *
     * @param path excel文件路径
     * @return 一个HashMap，key为sheet的名称，值为一个sheet的全部数据
     * @throws IOException
     */
    public static HashMap<String, List<List<String>>> readAll(String path) throws IOException {
        FileInputStream fs = new FileInputStream(path);
        Workbook wk = new XSSFWorkbook(fs);
        int sheetNums = wk.getNumberOfSheets();
        HashMap<String, List<List<String>>> data = new HashMap<String, List<List<String>>>();
        for (int i = 0; i < sheetNums; i++) {
            Sheet sheet = wk.getSheetAt(i);
            List<List<String>> sheetData = readSheet(sheet);
            data.put(sheet.getSheetName(), sheetData);
        }
        fs.close();
        wk.close();
        return data;
    }

    /**
     * 追加模式写excel文件，
     * 如果传入的文件和sheet名称都已存在，会在sheet原有数据的后面追加写入的数据。不存在会新建sheet或文件
     * 写入时如果原数据过大(内存放不下)，可将数据拆分，多次调用该方法写入
     *
     * @param path
     * @param sheetName
     * @param data
     * @return
     * @throws IOException
     */
    public static Boolean writeSheetAppend(String path, String sheetName, List<List<String>> data) throws IOException {
        Workbook wk = null;
        FileOutputStream fs = null;
        try {
            if (FileUtils.getFile(path).exists()) {
                wk = new XSSFWorkbook(new FileInputStream(path));
            } else {
                wk = new XSSFWorkbook();
            }
            Sheet sheet = wk.getSheet(sheetName);
            if (sheet == null) {
                sheet = wk.createSheet(sheetName);
            }
            for (List<String> rowData : data) {
                //在sheet的最后追加一行row
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                rowData.forEach(cellData -> {
                    row.createCell(row.getLastCellNum() + 1).setCellValue(cellData);
                });
            }
            fs = new FileOutputStream(path);
            wk.write(fs);
        } finally {
            if (wk != null) {
                wk.close();
            }
            if (fs != null) {
                fs.close();
            }
        }
        return true;
    }
}
