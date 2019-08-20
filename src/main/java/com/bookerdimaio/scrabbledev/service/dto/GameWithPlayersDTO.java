package com.bookerdimaio.scrabbledev.service.dto;

import com.bookerdimaio.scrabbledev.domain.Player;

import java.util.ArrayList;
import java.util.List;


public class GameWithPlayersDTO extends GameDTO {

    private List<Player> playersToAdd = new ArrayList<>();

    public List<Player> getPlayersToAdd() { return playersToAdd; }

    public void setPlayersToAdd(List<Player> playersToAdd) { this.playersToAdd = playersToAdd; }

}
