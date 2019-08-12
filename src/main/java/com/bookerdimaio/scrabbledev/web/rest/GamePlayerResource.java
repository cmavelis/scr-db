package com.bookerdimaio.scrabbledev.web.rest;

import com.bookerdimaio.scrabbledev.service.GamePlayerService;
import com.bookerdimaio.scrabbledev.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.bookerdimaio.scrabbledev.domain.GamePlayer}.
 */
@RestController
@RequestMapping("/api")
public class GamePlayerResource {

    private final Logger log = LoggerFactory.getLogger(GamePlayerResource.class);

    private static final String ENTITY_NAME = "scrabbledb2GamePlayer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GamePlayerService gamePlayerService;

    public GamePlayerResource(GamePlayerService gamePlayerService) {
        this.gamePlayerService = gamePlayerService;
    }

    /**
     * {@code POST  /game-players} : Create a new gamePlayer.
     *
     * @param gamePlayerDTO the gamePlayerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gamePlayerDTO, or with status {@code 400 (Bad Request)} if the gamePlayer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-players")
    public ResponseEntity<GamePlayerDTO> createGamePlayer(@Valid @RequestBody GamePlayerDTO gamePlayerDTO) throws URISyntaxException {
        log.debug("REST request to save GamePlayer : {}", gamePlayerDTO);
        if (gamePlayerDTO.getId() != null) {
            throw new BadRequestAlertException("A new gamePlayer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GamePlayerDTO result = gamePlayerService.save(gamePlayerDTO);
        return ResponseEntity.created(new URI("/api/game-players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-players} : Updates an existing gamePlayer.
     *
     * @param gamePlayerDTO the gamePlayerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gamePlayerDTO,
     * or with status {@code 400 (Bad Request)} if the gamePlayerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gamePlayerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-players")
    public ResponseEntity<GamePlayerDTO> updateGamePlayer(@Valid @RequestBody GamePlayerDTO gamePlayerDTO) throws URISyntaxException {
        log.debug("REST request to update GamePlayer : {}", gamePlayerDTO);
        if (gamePlayerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GamePlayerDTO result = gamePlayerService.save(gamePlayerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gamePlayerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /game-players} : get all the gamePlayers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gamePlayers in body.
     */
    @GetMapping("/game-players")
    public List<GamePlayerDTO> getAllGamePlayers() {
        log.debug("REST request to get all GamePlayers");
        return gamePlayerService.findAll();
    }

    /**
     * {@code GET  /game-players/:id} : get the "id" gamePlayer.
     *
     * @param id the id of the gamePlayerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gamePlayerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-players/{id}")
    public ResponseEntity<GamePlayerDTO> getGamePlayer(@PathVariable Long id) {
        log.debug("REST request to get GamePlayer : {}", id);
        Optional<GamePlayerDTO> gamePlayerDTO = gamePlayerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gamePlayerDTO);
    }

    /**
     * {@code DELETE  /game-players/:id} : delete the "id" gamePlayer.
     *
     * @param id the id of the gamePlayerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-players/{id}")
    public ResponseEntity<Void> deleteGamePlayer(@PathVariable Long id) {
        log.debug("REST request to delete GamePlayer : {}", id);
        gamePlayerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
