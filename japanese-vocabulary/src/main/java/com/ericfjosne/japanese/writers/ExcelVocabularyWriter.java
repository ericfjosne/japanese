package com.ericfjosne.japanese.writers;

import com.ericfjosne.japanese.model.VocabularyItem;
import org.apache.poi.sl.usermodel.ColorStyle;
import org.apache.poi.sl.usermodel.FillStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ExcelVocabularyWriter implements IVocabularyWriter {

    private final String outputFilePath;
    private String sheetName;
    XSSFWorkbook workbook = new XSSFWorkbook();

    private short borderColor = IndexedColors.GREY_25_PERCENT.getIndex();

    public ExcelVocabularyWriter(String outputFilePath, String sheetName) {
        this.outputFilePath = outputFilePath;
        this.sheetName = sheetName;
    }

    @Override
    public void write(List<VocabularyItem> items) throws IOException {

        XSSFSheet sheet = workbook.createSheet(sheetName);

        // Add header row
        addRow(sheet, 0, "漢字", "かな", "英語", getHeaderCellStyle());

        CellStyle normalCellStyle = getNormalCellStyle();
        for (int r=0; r<items.size(); r++) {
            VocabularyItem item = items.get(r);
            addRow(sheet, r+1, item.getKanji(), item.getKana(), item.getEnglish(), normalCellStyle);
        }

        try(OutputStream out = Files.newOutputStream(Paths.get(outputFilePath))){
            workbook.write(out);
        }
    }

    private Cell createRowCell(Row row, int i, CellStyle style){
        Cell c = row.createCell(i);
        c.setCellStyle(style);
        return c;
    }

    private void addRow(XSSFSheet sheet, int i, String kanji, String kana, String english, CellStyle style) {
        Row row = sheet.createRow(i);
        createRowCell(row, 0, style).setCellValue(kanji);
        createRowCell(row, 1, style).setCellValue(kana);
        createRowCell(row, 2, style).setCellValue(english);
    }

    private CellStyle getHeaderCellStyle(){
        CellStyle style = workbook.createCellStyle();

        // Make font bold
        XSSFFont defaultFont = workbook.createFont();
        defaultFont.setBold(true);
        style.setFont(defaultFont);

        // Add fill color for top bar
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(borderColor);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(borderColor);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(borderColor);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(borderColor);
        return style;
    }

    private CellStyle getNormalCellStyle(){
        CellStyle style = workbook.createCellStyle();

        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(borderColor);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(borderColor);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(borderColor);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(borderColor);
        return style;
    }
}
