package br.com.jovirds.file.exporter.contract;

import br.com.jovirds.data.dto.V1.GameDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {

     Resource exportFile(List<GameDTO> games) throws Exception;
}
