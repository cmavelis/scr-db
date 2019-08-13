package com.bookerdimaio.scrabbledev.service;

import com.bookerdimaio.scrabbledev.service.dto.PlayerDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bookerdimaio.scrabbledev.domain.Player}.
 */
public interface PlayerService {

    /**
     * Save a player.
     *
     * @param playerDTO the entity to save.
     * @return the persisted entity.
     */
    PlayerDTO save(PlayerDTO playerDTO);

    /**
     * Get all the players.
     *
     * @return the list of entities.
     */
    List<PlayerDTO> findAll();


    /**
     * Get the "id" player.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayerDTO> findOne(Long id);

    /**
     * Get the "name" player.
     *
     * @param name the name of the entity.
     * @return the entity.
     */
    Optional<PlayerDTO> findOneByName(String name);

    /**
     * Delete the "id" player.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
