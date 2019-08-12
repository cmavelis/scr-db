package com.bookerdimaio.scrabbledev.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bookerdimaio.scrabbledev.domain.GamePlayer} entity.
 */
public class GamePlayerDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer turnOrder;


    private Long gameId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(Integer turnOrder) {
        this.turnOrder = turnOrder;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GamePlayerDTO gamePlayerDTO = (GamePlayerDTO) o;
        if (gamePlayerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gamePlayerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GamePlayerDTO{" +
            "id=" + getId() +
            ", turnOrder=" + getTurnOrder() +
            ", game=" + getGameId() +
            "}";
    }
}
