package br.com.jovirds.file.importer.impl;

import br.com.jovirds.data.dto.V1.GameDTO;
import br.com.jovirds.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class CsvImporter implements FileImporter {
    @Override
    public List<GameDTO> importFile(InputStream inputStream) throws Exception {
        CSVFormat format = CSVFormat.Builder.create()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();

        // Use a try-with-resources statement to ensure the parser is closed properly
        try (Reader reader = new InputStreamReader(inputStream);
             CSVParser parser = new CSVParser(reader, format)) {

            List<CSVRecord> records = parser.getRecords();
            return parseRecordsToGameDTOs(records);
        }
    }

    private List<GameDTO> parseRecordsToGameDTOs(Iterable<CSVRecord> records) {
        List<GameDTO> games = new ArrayList<>();

        for(CSVRecord record : records) {
            GameDTO game = new GameDTO();
            game.setName(record.get("name"));
            game.setDeveloper(record.get("developer"));
            game.setYear(Long.parseLong(record.get("year")));
            game.setFinished(Boolean.parseBoolean(record.get("finished").toLowerCase(Locale.ROOT)));

            games.add(game);
        }
        return games;
    }
}
