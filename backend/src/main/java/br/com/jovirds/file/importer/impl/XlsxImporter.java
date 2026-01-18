package br.com.jovirds.file.importer.impl;

import br.com.jovirds.data.dto.V1.GameDTO;
import br.com.jovirds.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Component
public class XlsxImporter implements FileImporter {



    @Override
    public List<GameDTO> importFile(InputStream inputStream) throws Exception {

        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)){
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) rowIterator.next();

            return parseRowsToGameDtoList(rowIterator);

        }
    }

    private List<GameDTO> parseRowsToGameDtoList(Iterator<Row> rowIterator) {
        List<GameDTO> games = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowValid(row)) {
                games.add(parseRowToGameGameDto(row));
            }
        }

        return games;
    }

    private GameDTO parseRowToGameGameDto(Row row) {
        GameDTO game = new GameDTO();
        game.setName(row.getCell(0).getStringCellValue());
        game.setDeveloper(row.getCell(1).getStringCellValue());
        game.setYear(Long.parseLong(row.getCell(2).getStringCellValue()));
        game.setFinished(Boolean.parseBoolean(row.getCell(3).getStringCellValue().toLowerCase(Locale.ROOT)));

        return game;
    }

    private static boolean isRowValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}
