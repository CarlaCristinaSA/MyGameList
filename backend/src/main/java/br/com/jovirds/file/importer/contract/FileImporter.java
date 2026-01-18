package br.com.jovirds.file.importer.contract;

import br.com.jovirds.data.dto.V1.GameDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<GameDTO> importFile(InputStream inputStream) throws Exception;
}
