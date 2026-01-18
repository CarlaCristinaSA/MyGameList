package br.com.jovirds.file.exporter.impl;

import br.com.jovirds.data.dto.V1.GameDTO;
import br.com.jovirds.file.exporter.contract.FileExporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvExporter implements FileExporter {

    @Override
    public Resource exportFile(List<GameDTO> games) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader("id", "name", "developer", "year", "finished")
                .setSkipHeaderRecord(false)
                .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)){
            for(GameDTO game: games) {
                csvPrinter.printRecord(
                        game.getId().toString(),
                        game.getName(),
                        game.getDeveloper(),
                        game.getYear(),
                        game.getFinished()
                );
            }
        }
        return new ByteArrayResource(outputStream.toByteArray());
    }
}
