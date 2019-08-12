package com.bookerdimaio.scrabbledev.repository;

import com.bookerdimaio.scrabbledev.domain.GamePlayer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GamePlayer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {

}
