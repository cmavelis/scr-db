package com.bookerdimaio.scrabbledev.service;

import com.bookerdimaio.scrabbledev.service.dto.GamePlayerDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bookerdimaio.scrabbledev.domain.GamePlayer}.
 */
public interface GamePlayerService {

    /**
     * Save a gamePlayer.
     *
     * @param gamePlayerDTO the entity to save.
     * @return the persisted entity.
     */
    GamePlayerDTO save(GamePlayerDTO gamePlayerDTO);

    /**
     * Get all the gamePlayers.
     *
     * @return the list of entities.
     */
    List<GamePlayerDTO> findAll();


    /**
     * Get the "id" gamePlayer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GamePlayerDTO> findOne(Long id);

    /**
     * Delete the "id" gamePlayer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
