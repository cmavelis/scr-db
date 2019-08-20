package com.bookerdimaio.scrabbledev.service.impl;

import com.bookerdimaio.scrabbledev.domain.Game;
import com.bookerdimaio.scrabbledev.repository.GameRepository;
import com.bookerdimaio.scrabbledev.service.GamePlayerService;
import com.bookerdimaio.scrabbledev.domain.GamePlayer;
import com.bookerdimaio.scrabbledev.repository.GamePlayerRepository;
import com.bookerdimaio.scrabbledev.service.dto.GamePlayerDTO;
import com.bookerdimaio.scrabbledev.service.mapper.GamePlayerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link GamePlayer}.
 */
@Service
@Transactional
public class GamePlayerServiceImpl implements GamePlayerService {

    private final Logger log = LoggerFactory.getLogger(GamePlayerServiceImpl.class);

    private final GamePlayerRepository gamePlayerRepository;

    private final GamePlayerMapper gamePlayerMapper;

    private final GameRepository gameRepository;

    public GamePlayerServiceImpl(GamePlayerRepository gamePlayerRepository, GamePlayerMapper gamePlayerMapper, GameRepository gameRepository) {
        this.gamePlayerRepository = gamePlayerRepository;
        this.gameRepository = gameRepository;
        this.gamePlayerMapper = gamePlayerMapper;
    }

    /**
     * Save a gamePlayer.
     *
     * @param gamePlayerDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public GamePlayerDTO save(GamePlayerDTO gamePlayerDTO) {
        log.debug("Request to save GamePlayer : {}", gamePlayerDTO);
        GamePlayer gamePlayer = gamePlayerMapper.toEntity(gamePlayerDTO);
        gamePlayer = gamePlayerRepository.save(gamePlayer);

        Game game = gameRepository.getOne(gamePlayer.getGame().getId());
        Game newGame = game.addGamePlayers(gamePlayer);
        gameRepository.save(newGame);
        log.debug("GamePlayer added to Game : {}", newGame.toString());

        return gamePlayerMapper.toDto(gamePlayer);
    }

    /**
     * Get all the gamePlayers.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GamePlayerDTO> findAll() {
        log.debug("Request to get all GamePlayers");
        return gamePlayerRepository.findAll().stream()
            .map(gamePlayerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one gamePlayer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GamePlayerDTO> findOne(Long id) {
        log.debug("Request to get GamePlayer : {}", id);
        return gamePlayerRepository.findById(id)
            .map(gamePlayerMapper::toDto);
    }

    /**
     * Delete the gamePlayer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GamePlayer : {}", id);
        gamePlayerRepository.deleteById(id);
    }
}
