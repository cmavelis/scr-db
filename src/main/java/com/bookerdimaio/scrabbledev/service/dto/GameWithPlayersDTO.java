package com.bookerdimaio.scrabbledev.service.dto;

import com.bookerdimaio.scrabbledev.domain.Player;

import java.util.ArrayList;
import java.util.List;


public class GameWithPlayersDTO extends GameDTO {

    private List<Long> playersToAdd = new ArrayList<>();

    public List<Long> getPlayersToAdd() { return playersToAdd; }

    public void setPlayersToAdd(List<Long> playersToAdd) { this.playersToAdd = playersToAdd; }

}
