package com.bookerdimaio.scrabbledev.web.rest;

import com.bookerdimaio.scrabbledev.service.GameService;
import com.bookerdimaio.scrabbledev.service.GamePlayerService;
import com.bookerdimaio.scrabbledev.service.dto.GameWithPlayersDTO;
import com.bookerdimaio.scrabbledev.web.rest.errors.BadRequestAlertException;
import com.bookerdimaio.scrabbledev.service.dto.GameDTO;
import com.bookerdimaio.scrabbledev.service.dto.GamePlayerDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.bookerdimaio.scrabbledev.domain.Game}.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

    private final Logger log = LoggerFactory.getLogger(GameResource.class);

    private static final String ENTITY_NAME = "scrabbledb2Game";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameService gameService;

    private final GamePlayerService gamePlayerService;

    public GameResource(GameService gameService, GamePlayerService gamePlayerService) {
        this.gameService = gameService;
        this.gamePlayerService = gamePlayerService;
    }

    /**
     * {@code POST  /games} : Create a new game.
     *
     * @param gameDTO the gameDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameDTO, or with status {@code 400 (Bad Request)} if the game has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/games")
    public ResponseEntity<GameDTO> createGame(@Valid @RequestBody GameDTO gameDTO) throws URISyntaxException {
        log.debug("REST request to save Game : {}", gameDTO);
        if (gameDTO.getId() != null) {
            throw new BadRequestAlertException("A new game cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameDTO result = gameService.save(gameDTO);
        return ResponseEntity.created(new URI("/api/games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /games/players} : Create a new game with players and GamePlayers.
     *
     * @param gameWithPlayersDTO the gameDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameDTO, or with status {@code 400 (Bad Request)} if the game could not be created
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/games/players")
    public ResponseEntity<GameDTO> createGameWithPlayers(@Valid @RequestBody GameWithPlayersDTO gameWithPlayersDTO) throws URISyntaxException {
        log.debug("REST request to create Game : {} with players", gameWithPlayersDTO);
        if (gameWithPlayersDTO.getId() != null) {
            throw new BadRequestAlertException("A new game cannot already have an ID", ENTITY_NAME, "idexists");
        }

        gameWithPlayersDTO.setState("_".repeat(225));
        GameDTO result = gameService.save(gameWithPlayersDTO);

        List<Long> playersToAdd = gameWithPlayersDTO.getPlayersToAdd();
        log.debug("Incoming list : {}", playersToAdd);
        GamePlayerDTO gamePlayerDTO = new GamePlayerDTO();
        gamePlayerDTO.setScore(0);
        gamePlayerDTO.setRack("TESTRAK");
        gamePlayerDTO.setGameId(result.getId());

        for (int i = 0; i < playersToAdd.size(); i++) {
            Long playerId = playersToAdd.get(i);
            if (playerId == null) { continue; }
            gamePlayerDTO.setTurnOrder(i);
            gamePlayerDTO.setPlayerId(playersToAdd.get(i));
            gamePlayerService.save(gamePlayerDTO);
            log.debug("Creating GamePlayer for : {}", playersToAdd.get(i));
        }

        return ResponseEntity.created(new URI("/api/games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /games} : Updates an existing game.
     *
     * @param gameDTO the gameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameDTO,
     * or with status {@code 400 (Bad Request)} if the gameDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/games")
    public ResponseEntity<GameDTO> updateGame(@Valid @RequestBody GameDTO gameDTO) throws URISyntaxException {
        log.debug("REST request to update Game : {}", gameDTO);
        if (gameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GameDTO result = gameService.save(gameDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /games} : get all the games.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of games in body.
     */
    @GetMapping("/games")
    public List<GameDTO> getAllGames() {
        log.debug("REST request to get all Games");
        return gameService.findAll();
    }

    /**
     * {@code GET  /games/:id} : get the "id" game.
     *
     * @param id the id of the gameDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/games/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        log.debug("REST request to get Game : {}", id);
        Optional<GameDTO> gameDTO = gameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameDTO);
    }

    /**
     * {@code DELETE  /games/:id} : delete the "id" game.
     *
     * @param id the id of the gameDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        log.debug("REST request to delete Game : {}", id);
        gameService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
