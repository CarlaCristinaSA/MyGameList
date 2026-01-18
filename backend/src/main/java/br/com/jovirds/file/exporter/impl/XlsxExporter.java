package br.com.jovirds.file.exporter.impl;

import br.com.jovirds.data.dto.V1.GameDTO;
import br.com.jovirds.file.exporter.contract.FileExporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class XlsxExporter implements FileExporter {

    @Override
    public Resource exportFile(List<GameDTO> games) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Games");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"id","name","developer","year","finished"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowIndex = 1;

            for(GameDTO game: games) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(game.getId());
                row.createCell(1).setCellValue(game.getName());
                row.createCell(2).setCellValue(game.getDeveloper());
                row.createCell(3).setCellValue(game.getYear());
                row.createCell(4).setCellValue(game.getFinished() != null && game.getFinished() ? "SIM" : "NAO");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
