package utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ExcelUtil {

    private final Path filePath;

    public ExcelUtil(Path filePath) {
        this.filePath = filePath;
    }

    /** Returns all values under a given column header as a List<String>. */
    public List<String> getColumn(String sheetName, String columnHeader) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath.toFile());
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }

            // Find header row (assume first row is header)
            Row header = sheet.getRow(0);
            if (header == null) {
                throw new IllegalStateException("No header row in sheet: " + sheetName);
            }

            int colIndex = -1;
            for (Cell cell : header) {
                if (cell != null && columnHeader.equalsIgnoreCase(getCellText(cell).trim())) {
                    colIndex = cell.getColumnIndex();
                    break;
                }
            }
            if (colIndex == -1) {
                throw new IllegalArgumentException("Column header not found: " + columnHeader);
            }

            List<String> values = new ArrayList<>();
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                String text = cell == null ? "" : getCellText(cell).trim();
                if (!text.isEmpty()) values.add(text);
            }
            return values;
        }
    }

    /** Optionally: get all columns as Map<header, List<values>> */
    public Map<String, List<String>> getAllColumns(String sheetName) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath.toFile());
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) throw new IllegalArgumentException("Sheet not found: " + sheetName);

            Row header = sheet.getRow(0);
            if (header == null) throw new IllegalStateException("No header row in sheet: " + sheetName);

            Map<String, Integer> headerIndex = new LinkedHashMap<>();
            for (Cell cell : header) {
                String name = getCellText(cell).trim();
                if (!name.isEmpty()) headerIndex.put(name, cell.getColumnIndex());
            }

            Map<String, List<String>> data = new LinkedHashMap<>();
            headerIndex.keySet().forEach(h -> data.put(h, new ArrayList<>()));

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                for (Map.Entry<String, Integer> e : headerIndex.entrySet()) {
                    Cell c = row.getCell(e.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    String text = c == null ? "" : getCellText(c).trim();
                    if (!text.isEmpty()) data.get(e.getKey()).add(text);
                }
            }
            return data;
        }
    }

    private String getCellText(Cell cell) {
        DataFormatter fmt = new DataFormatter();
        return fmt.formatCellValue(cell);
    }
}