package poc.pucminas.web.rest;

import poc.pucminas.SegurancaComunicacaoApp;
import poc.pucminas.domain.Plano;
import poc.pucminas.repository.PlanoRepository;
import poc.pucminas.service.PlanoService;
import poc.pucminas.service.dto.PlanoDTO;
import poc.pucminas.service.mapper.PlanoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PlanoResource} REST controller.
 */
@SpringBootTest(classes = SegurancaComunicacaoApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PlanoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private PlanoMapper planoMapper;

    @Autowired
    private PlanoService planoService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanoMockMvc;

    private Plano plano;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plano createEntity(EntityManager em) {
        Plano plano = new Plano()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO);
        return plano;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plano createUpdatedEntity(EntityManager em) {
        Plano plano = new Plano()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO);
        return plano;
    }

    @BeforeEach
    public void initTest() {
        plano = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlano() throws Exception {
        int databaseSizeBeforeCreate = planoRepository.findAll().size();
        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);
        restPlanoMockMvc.perform(post("/api/planos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planoDTO)))
            .andExpect(status().isCreated());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeCreate + 1);
        Plano testPlano = planoList.get(planoList.size() - 1);
        assertThat(testPlano.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPlano.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createPlanoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planoRepository.findAll().size();

        // Create the Plano with an existing ID
        plano.setId(1L);
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanoMockMvc.perform(post("/api/planos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoRepository.findAll().size();
        // set the field null
        plano.setNome(null);

        // Create the Plano, which fails.
        PlanoDTO planoDTO = planoMapper.toDto(plano);


        restPlanoMockMvc.perform(post("/api/planos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planoDTO)))
            .andExpect(status().isBadRequest());

        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanos() throws Exception {
        // Initialize the database
        planoRepository.saveAndFlush(plano);

        // Get all the planoList
        restPlanoMockMvc.perform(get("/api/planos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plano.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getPlano() throws Exception {
        // Initialize the database
        planoRepository.saveAndFlush(plano);

        // Get the plano
        restPlanoMockMvc.perform(get("/api/planos/{id}", plano.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plano.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }
    @Test
    @Transactional
    public void getNonExistingPlano() throws Exception {
        // Get the plano
        restPlanoMockMvc.perform(get("/api/planos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlano() throws Exception {
        // Initialize the database
        planoRepository.saveAndFlush(plano);

        int databaseSizeBeforeUpdate = planoRepository.findAll().size();

        // Update the plano
        Plano updatedPlano = planoRepository.findById(plano.getId()).get();
        // Disconnect from session so that the updates on updatedPlano are not directly saved in db
        em.detach(updatedPlano);
        updatedPlano
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO);
        PlanoDTO planoDTO = planoMapper.toDto(updatedPlano);

        restPlanoMockMvc.perform(put("/api/planos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planoDTO)))
            .andExpect(status().isOk());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
        Plano testPlano = planoList.get(planoList.size() - 1);
        assertThat(testPlano.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPlano.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingPlano() throws Exception {
        int databaseSizeBeforeUpdate = planoRepository.findAll().size();

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoMockMvc.perform(put("/api/planos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlano() throws Exception {
        // Initialize the database
        planoRepository.saveAndFlush(plano);

        int databaseSizeBeforeDelete = planoRepository.findAll().size();

        // Delete the plano
        restPlanoMockMvc.perform(delete("/api/planos/{id}", plano.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plano> planoList = planoRepository.findAll();
        assertThat(planoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
