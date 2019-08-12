package com.bookerdimaio.scrabbledev.service.mapper;

import com.bookerdimaio.scrabbledev.domain.*;
import com.bookerdimaio.scrabbledev.service.dto.GamePlayerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link GamePlayer} and its DTO {@link GamePlayerDTO}.
 */
@Mapper(componentModel = "spring", uses = {GameMapper.class, PlayerMapper.class})
public interface GamePlayerMapper extends EntityMapper<GamePlayerDTO, GamePlayer> {

    @Mapping(source = "game.id", target = "gameId")
    @Mapping(source = "player.id", target = "playerId")
    GamePlayerDTO toDto(GamePlayer gamePlayer);

    @Mapping(source = "gameId", target = "game")
    @Mapping(source = "playerId", target = "player")
    GamePlayer toEntity(GamePlayerDTO gamePlayerDTO);

    default GamePlayer fromId(Long id) {
        if (id == null) {
            return null;
        }
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setId(id);
        return gamePlayer;
    }
}
