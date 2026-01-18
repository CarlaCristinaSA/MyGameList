package br.com.jovirds.service;

import br.com.jovirds.controllers.GameController;
import br.com.jovirds.controllers.GameControllerV2;
import br.com.jovirds.controllers.TesteLogController;
import br.com.jovirds.data.dto.V1.GameDTO;
import br.com.jovirds.data.dto.V2.GameDTOV2;
import br.com.jovirds.exception.BadRequestException;
import br.com.jovirds.exception.FileStorageException;
import br.com.jovirds.exception.RequiredObjectIsNullException;
import br.com.jovirds.exception.ResourceNotFoundException;
import br.com.jovirds.file.exporter.contract.FileExporter;
import br.com.jovirds.file.exporter.factory.FileExporterFactory;
import br.com.jovirds.file.importer.contract.FileImporter;
import br.com.jovirds.file.importer.factory.FileImporterFactory;
import br.com.jovirds.mapper.custom.GameMapper;
import br.com.jovirds.model.Game;
import br.com.jovirds.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static br.com.jovirds.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class GameService {

    private static final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(TesteLogController.class.getName());

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PagedResourcesAssembler<GameDTO> assembler;

    @Autowired
    PagedResourcesAssembler<GameDTOV2> assemblerV2;

    @Autowired
    FileImporterFactory importer;

    @Autowired
    FileExporterFactory exporter;

    public GameDTO findById(Long id){
        logger.info("Finding one Game");
        var entity = gameRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No records found for this ID"));

        var dto =  parseObject(entity, GameDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public GameDTO create(GameDTO game){

        if (game == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one Game");

        var entity = gameRepository.save(parseObject(game, Game.class));
        var dto = parseObject(entity, GameDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<GameDTO> massCreation(MultipartFile file){
        logger.info("Import Games from csv File");

        if (file.isEmpty()) throw new BadRequestException("Set a Valid file");

        try (InputStream inputStream = file.getInputStream()){
            String filename = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null"));

            FileImporter importer = this.importer.getImporter(filename);

            List<Game> entities = importer.importFile(inputStream).stream()
                    .map(dto -> gameRepository.save(parseObject(dto, Game.class)))
                    .toList();


            return entities.stream()
                    .map(games -> {
                    var dto =  parseObject(games, GameDTO.class);
                    addHateoasLinks(dto);
                    return dto;
                })
                    .toList();

        } catch (Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
    }

    public GameDTOV2 createv2(GameDTOV2 game){
        logger.info("Creating one Game V2");

        var entity = gameRepository.save(gameMapper.convertDTOV2ToEntity(game));
        var dto = gameMapper.convertEntityToDTOV2(entity);
        addHateoasLinksV2(dto);
        return dto;
    }

    public GameDTOV2 findByIdV2(Long id){
        logger.info("Finding one Game V2");
        var entity = gameRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No records found for this ID"));

        var dto = gameMapper.convertEntityToDTOV2(entity);
        addHateoasLinksV2(dto);
        return dto;
    }

    public PagedModel<EntityModel<GameDTOV2>> findAllV2(Pageable pageable) {
        logger.info("Finding all games V2");

        var gamePage = gameRepository.findAll(pageable)
                .map(gameMapper::convertEntityToDTOV2);

        return buildPagedModelV2(pageable, gamePage);
    }

    public PagedModel<EntityModel<GameDTOV2>> findByNameV2(String name, Pageable pageable){
        logger.info("Finding Games by Name V2");

        var gamePage = gameRepository.findGamesByName(name, pageable)
                .map(gameMapper::convertEntityToDTOV2);

        return buildPagedModelV2(pageable, gamePage);
    }

    public GameDTO update(GameDTO game){

        if (game == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Game");

        var entity = gameRepository.findById(game.getId())
                .orElseThrow(()-> new ResourceNotFoundException("No records found for this ID"));

        entity.setDeveloper(game.getDeveloper());
        entity.setName(game.getName());
        entity.setYear(game.getYear());
        entity.setFinished(game.getFinished());
        var dto = parseObject(gameRepository.save(entity), GameDTO.class);
        addHateoasLinks(dto);
        return dto;
    }
    public GameDTOV2 updatev2(GameDTOV2 game){

        if (game == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Game V2");

        var entity = gameRepository.findById(game.getId())
                .orElseThrow(()->new ResourceNotFoundException("No records found for this ID"));

        entity.setDeveloper(game.getDeveloper());
        entity.setName(game.getName());
        entity.setYear(game.getYear());
        entity.setStarRating(game.getStarRating());
        entity.setFinished(game.getFinished());
        var dto = parseObject(gameRepository.save(entity), GameDTOV2.class);
        addHateoasLinksV2(dto);
        return dto;
    }
    public void delete(Long id){
        logger.info("Deleting one GameDTO");

        Game entity = gameRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No records found for this ID"));

        gameRepository.delete(entity);
    }

    @Transactional
    public GameDTO finishGame(Long id){
        logger.info("Finish one GameDTO");

        gameRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No records found for this ID"));

        gameRepository.finishGame(id);

        var entity = gameRepository.findById(id).get();
        var dto = parseObject(entity, GameDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PagedModel<EntityModel<GameDTO>> findAll(Pageable pageable) {
        logger.info("Finding all books");

        var gamePage = gameRepository.findAll(pageable)
                .map(game -> parseObject(game, GameDTO.class));

        return buildPagedModel(pageable, gamePage);
    }

    public PagedModel<EntityModel<GameDTO>> findByName(String name, Pageable pageable){
        logger.info("Finding Games by Name");

        var gamePage = gameRepository.findGamesByName(name, pageable)
                .map(game -> parseObject(game, GameDTO.class));

        return buildPagedModel(pageable, gamePage);
    }

    public Resource exportPage(Pageable pageable, String acceptHeader) {
        logger.info("Export Game Page");

        var gamePage = gameRepository.findAll(pageable)
                .map(game -> parseObject(game, GameDTO.class))
                .getContent();

        try {
            FileExporter exporter = this.exporter.getExporter(acceptHeader);
            return exporter.exportFile(gamePage);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export", e);
        }

    }

    private PagedModel<EntityModel<GameDTO>> buildPagedModel(Pageable pageable, Page<GameDTO> gamePage) {
        gamePage.forEach(this::addHateoasLinks);

        Link selfLink = linkTo(methodOn(GameController.class)
                .findAll(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSort().toString().contains("DESC") ? "desc" : "asc")
        ).withSelfRel();

        return assembler.toModel(gamePage, selfLink);
    }

    private void addHateoasLinks(GameDTO dto) {
        dto.add(linkTo(methodOn(GameController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(GameController.class).findAll(1, 12,"asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(GameController.class).findByName("",1, 12,"asc")).withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(GameController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(GameController.class)).slash("massCreation").withRel("massCreation").withType("POST"));
        dto.add(linkTo(methodOn(GameController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(GameController.class).finishGame(dto.getId())).withRel("finish").withType("PATCH"));
        dto.add(linkTo(methodOn(GameController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(GameController.class).exportPage(1, 12,"asc", null)).withRel("exportPage").withType("GET").withTitle("Export Games"));
    }

    private PagedModel<EntityModel<GameDTOV2>> buildPagedModelV2(Pageable pageable, Page<GameDTOV2> gamePage) {
        gamePage.forEach(this::addHateoasLinksV2);
        
        Link selfLink = linkTo(methodOn(GameControllerV2.class)
                .findAllV2(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSort().toString().contains("DESC") ? "desc" : "asc")
        ).withSelfRel();

        return assemblerV2.toModel(gamePage, selfLink);
    }

    private void addHateoasLinksV2(GameDTOV2 dto) {
        dto.add(linkTo(methodOn(GameControllerV2.class).findByIdV2(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(GameControllerV2.class).findAllV2(0, 12,"asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(GameControllerV2.class).findByNameV2("",0, 12,"asc")).withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(GameControllerV2.class).createV2(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(GameControllerV2.class).updateV2(dto)).withRel("update").withType("PUT"));
    }

}
