package com.bookerdimaio.scrabbledev.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Size(min = 225, max = 225)
    @Column(name = "state", length = 225)
    private String state;

    @Column(name = "start_time")
    private Instant start_time;

    @OneToMany(mappedBy = "game", cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Game name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public Game state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Instant getStart_time() {
        return start_time;
    }

    public Game start_time(Instant start_time) {
        this.start_time = start_time;
        return this;
    }

    public void setStart_time(Instant start_time) {
        this.start_time = start_time;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Game gamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
        return this;
    }

    public Game addGamePlayers(GamePlayer gamePlayer) {
        if (this.gamePlayers.contains(gamePlayer)) { return this; }
        this.gamePlayers.add(gamePlayer);
        gamePlayer.setGame(this);
        return this;
    }

    public Game removeGamePlayers(GamePlayer gamePlayer) {
        this.gamePlayers.remove(gamePlayer);
        gamePlayer.setGame(null);
        return this;
    }

    protected void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", state='" + getState() + "'" +
            ", start_time='" + getStart_time() + "'" +
            "}";
    }
}
