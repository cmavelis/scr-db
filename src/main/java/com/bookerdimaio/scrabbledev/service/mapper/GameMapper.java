package com.bookerdimaio.scrabbledev.service.mapper;

import com.bookerdimaio.scrabbledev.domain.*;
import com.bookerdimaio.scrabbledev.service.dto.GameDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GameMapper extends EntityMapper<GameDTO, Game> {


    @Mapping(target = "gamePlayers", ignore = true)
    @Mapping(target = "removeGamePlayers", ignore = true)
    Game toEntity(GameDTO gameDTO);

    default Game fromId(Long id) {
        if (id == null) {
            return null;
        }
        Game game = new Game();
        game.setId(id);
        return game;
    }
}
