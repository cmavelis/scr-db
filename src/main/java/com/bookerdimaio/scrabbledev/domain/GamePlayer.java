package com.bookerdimaio.scrabbledev.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A GamePlayer.
 */
@Entity
@Table(name = "game_player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GamePlayer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(0)
    @Max(3)
    @Column(name = "turn_order", nullable = false)
    private Integer turnOrder;

    @Column(name = "score")
    private Integer score = 0;

    @Size(min = 7, max = 7)
    @Pattern(regexp = "^[A-Z?_]*$")
    @Column(name = "rack", length = 7)
    private String rack = "_______";

    @ManyToOne
    @JsonIgnoreProperties("gamePlayers")
    private Game game;

    @ManyToOne
    @JsonIgnoreProperties("gamePlayers")
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTurnOrder() {
        return turnOrder;
    }

    public GamePlayer turnOrder(Integer turnOrder) {
        this.turnOrder = turnOrder;
        return this;
    }

    public void setTurnOrder(Integer turnOrder) {
        this.turnOrder = turnOrder;
    }

    public Integer getScore() {
        return score;
    }

    public GamePlayer score(Integer score) {
        this.score = score;
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRack() {
        return rack;
    }

    public GamePlayer rack(String rack) {
        this.rack = rack;
        return this;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public Game getGame() {
        return game;
    }

    public GamePlayer game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        if (game == this.game) {
            return;
        }
        this.game = game;
        game.addGamePlayers(this);
    }

    public Player getPlayer() {
        return player;
    }

    public GamePlayer player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GamePlayer)) {
            return false;
        }
        return id != null && id.equals(((GamePlayer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GamePlayer{" +
            "id=" + getId() +
            ", turnOrder=" + getTurnOrder() +
            ", score=" + getScore() +
            ", rack='" + getRack() + "'" +
            "}";
    }
}
