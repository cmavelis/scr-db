package com.bookerdimaio.scrabbledev.web.rest;

import com.bookerdimaio.scrabbledev.Scrabbledb2App;
import com.bookerdimaio.scrabbledev.domain.GamePlayer;
import com.bookerdimaio.scrabbledev.repository.GamePlayerRepository;
import com.bookerdimaio.scrabbledev.service.GamePlayerService;
import com.bookerdimaio.scrabbledev.service.dto.GamePlayerDTO;
import com.bookerdimaio.scrabbledev.service.mapper.GamePlayerMapper;
import com.bookerdimaio.scrabbledev.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.bookerdimaio.scrabbledev.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link GamePlayerResource} REST controller.
 */
@SpringBootTest(classes = Scrabbledb2App.class)
public class GamePlayerResourceIT {

    private static final Integer DEFAULT_TURN_ORDER = 1;
    private static final Integer UPDATED_TURN_ORDER = 2;

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final String DEFAULT_RACK = "AAAAAAA";
    private static final String UPDATED_RACK = "BBBBBBB";

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private GamePlayerMapper gamePlayerMapper;

    @Autowired
    private GamePlayerService gamePlayerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restGamePlayerMockMvc;

    private GamePlayer gamePlayer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GamePlayerResource gamePlayerResource = new GamePlayerResource(gamePlayerService);
        this.restGamePlayerMockMvc = MockMvcBuilders.standaloneSetup(gamePlayerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GamePlayer createEntity(EntityManager em) {
        GamePlayer gamePlayer = new GamePlayer()
            .turnOrder(DEFAULT_TURN_ORDER)
            .score(DEFAULT_SCORE)
            .rack(DEFAULT_RACK);
        return gamePlayer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GamePlayer createUpdatedEntity(EntityManager em) {
        GamePlayer gamePlayer = new GamePlayer()
            .turnOrder(UPDATED_TURN_ORDER)
            .score(UPDATED_SCORE)
            .rack(UPDATED_RACK);
        return gamePlayer;
    }

    @BeforeEach
    public void initTest() {
        gamePlayer = createEntity(em);
    }

    @Test
    @Transactional
    public void createGamePlayer() throws Exception {
        int databaseSizeBeforeCreate = gamePlayerRepository.findAll().size();

        // Create the GamePlayer
        GamePlayerDTO gamePlayerDTO = gamePlayerMapper.toDto(gamePlayer);
        restGamePlayerMockMvc.perform(post("/api/game-players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamePlayerDTO)))
            .andExpect(status().isCreated());

        // Validate the GamePlayer in the database
        List<GamePlayer> gamePlayerList = gamePlayerRepository.findAll();
        assertThat(gamePlayerList).hasSize(databaseSizeBeforeCreate + 1);
        GamePlayer testGamePlayer = gamePlayerList.get(gamePlayerList.size() - 1);
        assertThat(testGamePlayer.getTurnOrder()).isEqualTo(DEFAULT_TURN_ORDER);
        assertThat(testGamePlayer.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testGamePlayer.getRack()).isEqualTo(DEFAULT_RACK);
    }

    @Test
    @Transactional
    public void createGamePlayerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gamePlayerRepository.findAll().size();

        // Create the GamePlayer with an existing ID
        gamePlayer.setId(1L);
        GamePlayerDTO gamePlayerDTO = gamePlayerMapper.toDto(gamePlayer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGamePlayerMockMvc.perform(post("/api/game-players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamePlayerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GamePlayer in the database
        List<GamePlayer> gamePlayerList = gamePlayerRepository.findAll();
        assertThat(gamePlayerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTurnOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = gamePlayerRepository.findAll().size();
        // set the field null
        gamePlayer.setTurnOrder(null);

        // Create the GamePlayer, which fails.
        GamePlayerDTO gamePlayerDTO = gamePlayerMapper.toDto(gamePlayer);

        restGamePlayerMockMvc.perform(post("/api/game-players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamePlayerDTO)))
            .andExpect(status().isBadRequest());

        List<GamePlayer> gamePlayerList = gamePlayerRepository.findAll();
        assertThat(gamePlayerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGamePlayers() throws Exception {
        // Initialize the database
        gamePlayerRepository.saveAndFlush(gamePlayer);

        // Get all the gamePlayerList
        restGamePlayerMockMvc.perform(get("/api/game-players?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gamePlayer.getId().intValue())))
            .andExpect(jsonPath("$.[*].turnOrder").value(hasItem(DEFAULT_TURN_ORDER)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].rack").value(hasItem(DEFAULT_RACK.toString())));
    }
    
    @Test
    @Transactional
    public void getGamePlayer() throws Exception {
        // Initialize the database
        gamePlayerRepository.saveAndFlush(gamePlayer);

        // Get the gamePlayer
        restGamePlayerMockMvc.perform(get("/api/game-players/{id}", gamePlayer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gamePlayer.getId().intValue()))
            .andExpect(jsonPath("$.turnOrder").value(DEFAULT_TURN_ORDER))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.rack").value(DEFAULT_RACK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGamePlayer() throws Exception {
        // Get the gamePlayer
        restGamePlayerMockMvc.perform(get("/api/game-players/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGamePlayer() throws Exception {
        // Initialize the database
        gamePlayerRepository.saveAndFlush(gamePlayer);

        int databaseSizeBeforeUpdate = gamePlayerRepository.findAll().size();

        // Update the gamePlayer
        GamePlayer updatedGamePlayer = gamePlayerRepository.findById(gamePlayer.getId()).get();
        // Disconnect from session so that the updates on updatedGamePlayer are not directly saved in db
        em.detach(updatedGamePlayer);
        updatedGamePlayer
            .turnOrder(UPDATED_TURN_ORDER)
            .score(UPDATED_SCORE)
            .rack(UPDATED_RACK);
        GamePlayerDTO gamePlayerDTO = gamePlayerMapper.toDto(updatedGamePlayer);

        restGamePlayerMockMvc.perform(put("/api/game-players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamePlayerDTO)))
            .andExpect(status().isOk());

        // Validate the GamePlayer in the database
        List<GamePlayer> gamePlayerList = gamePlayerRepository.findAll();
        assertThat(gamePlayerList).hasSize(databaseSizeBeforeUpdate);
        GamePlayer testGamePlayer = gamePlayerList.get(gamePlayerList.size() - 1);
        assertThat(testGamePlayer.getTurnOrder()).isEqualTo(UPDATED_TURN_ORDER);
        assertThat(testGamePlayer.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testGamePlayer.getRack()).isEqualTo(UPDATED_RACK);
    }

    @Test
    @Transactional
    public void updateNonExistingGamePlayer() throws Exception {
        int databaseSizeBeforeUpdate = gamePlayerRepository.findAll().size();

        // Create the GamePlayer
        GamePlayerDTO gamePlayerDTO = gamePlayerMapper.toDto(gamePlayer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGamePlayerMockMvc.perform(put("/api/game-players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamePlayerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GamePlayer in the database
        List<GamePlayer> gamePlayerList = gamePlayerRepository.findAll();
        assertThat(gamePlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGamePlayer() throws Exception {
        // Initialize the database
        gamePlayerRepository.saveAndFlush(gamePlayer);

        int databaseSizeBeforeDelete = gamePlayerRepository.findAll().size();

        // Delete the gamePlayer
        restGamePlayerMockMvc.perform(delete("/api/game-players/{id}", gamePlayer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GamePlayer> gamePlayerList = gamePlayerRepository.findAll();
        assertThat(gamePlayerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GamePlayer.class);
        GamePlayer gamePlayer1 = new GamePlayer();
        gamePlayer1.setId(1L);
        GamePlayer gamePlayer2 = new GamePlayer();
        gamePlayer2.setId(gamePlayer1.getId());
        assertThat(gamePlayer1).isEqualTo(gamePlayer2);
        gamePlayer2.setId(2L);
        assertThat(gamePlayer1).isNotEqualTo(gamePlayer2);
        gamePlayer1.setId(null);
        assertThat(gamePlayer1).isNotEqualTo(gamePlayer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GamePlayerDTO.class);
        GamePlayerDTO gamePlayerDTO1 = new GamePlayerDTO();
        gamePlayerDTO1.setId(1L);
        GamePlayerDTO gamePlayerDTO2 = new GamePlayerDTO();
        assertThat(gamePlayerDTO1).isNotEqualTo(gamePlayerDTO2);
        gamePlayerDTO2.setId(gamePlayerDTO1.getId());
        assertThat(gamePlayerDTO1).isEqualTo(gamePlayerDTO2);
        gamePlayerDTO2.setId(2L);
        assertThat(gamePlayerDTO1).isNotEqualTo(gamePlayerDTO2);
        gamePlayerDTO1.setId(null);
        assertThat(gamePlayerDTO1).isNotEqualTo(gamePlayerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(gamePlayerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(gamePlayerMapper.fromId(null)).isNull();
    }
}
