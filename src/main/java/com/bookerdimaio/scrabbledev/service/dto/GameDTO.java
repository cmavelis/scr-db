package com.bookerdimaio.scrabbledev.service.dto;
import com.bookerdimaio.scrabbledev.domain.GamePlayer;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.bookerdimaio.scrabbledev.domain.Game} entity.
 */
public class GameDTO implements Serializable {

    private Long id;

    private String name;

    @Size(min = 225, max = 225)
    private String state;

    private Instant start_time;

    private Set<GamePlayer> gamePlayers = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Instant getStart_time() {
        return start_time;
    }

    public void setStart_time(Instant start_time) {
        this.start_time = start_time;
    }

    public Set<GamePlayer> getGamePlayers() { return gamePlayers; }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) { this.gamePlayers = gamePlayers; }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameDTO gameDTO = (GameDTO) o;
        if (gameDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gameDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GameDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", state='" + getState() + "'" +
            ", start_time='" + getStart_time() + "'" +
            "}";
    }
}
