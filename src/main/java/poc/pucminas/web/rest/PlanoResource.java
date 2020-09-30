package poc.pucminas.web.rest;

import poc.pucminas.service.PlanoService;
import poc.pucminas.web.rest.errors.BadRequestAlertException;
import poc.pucminas.service.dto.PlanoDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link poc.pucminas.domain.Plano}.
 */
@RestController
@RequestMapping("/api")
public class PlanoResource {

    private final Logger log = LoggerFactory.getLogger(PlanoResource.class);

    private static final String ENTITY_NAME = "segurancaComunicacaoPlano";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanoService planoService;

    public PlanoResource(PlanoService planoService) {
        this.planoService = planoService;
    }

    /**
     * {@code POST  /planos} : Create a new plano.
     *
     * @param planoDTO the planoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planoDTO, or with status {@code 400 (Bad Request)} if the plano has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/planos")
    public ResponseEntity<PlanoDTO> createPlano(@Valid @RequestBody PlanoDTO planoDTO) throws URISyntaxException {
        log.debug("REST request to save Plano : {}", planoDTO);
        if (planoDTO.getId() != null) {
            throw new BadRequestAlertException("A new plano cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanoDTO result = planoService.save(planoDTO);
        return ResponseEntity.created(new URI("/api/planos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /planos} : Updates an existing plano.
     *
     * @param planoDTO the planoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoDTO,
     * or with status {@code 400 (Bad Request)} if the planoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/planos")
    public ResponseEntity<PlanoDTO> updatePlano(@Valid @RequestBody PlanoDTO planoDTO) throws URISyntaxException {
        log.debug("REST request to update Plano : {}", planoDTO);
        if (planoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanoDTO result = planoService.save(planoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, planoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /planos} : get all the planos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planos in body.
     */
    @GetMapping("/planos")
    public ResponseEntity<List<PlanoDTO>> getAllPlanos(Pageable pageable) {
        log.debug("REST request to get a page of Planos");
        Page<PlanoDTO> page = planoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /planos/:id} : get the "id" plano.
     *
     * @param id the id of the planoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/planos/{id}")
    public ResponseEntity<PlanoDTO> getPlano(@PathVariable Long id) {
        log.debug("REST request to get Plano : {}", id);
        Optional<PlanoDTO> planoDTO = planoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planoDTO);
    }

    /**
     * {@code DELETE  /planos/:id} : delete the "id" plano.
     *
     * @param id the id of the planoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/planos/{id}")
    public ResponseEntity<Void> deletePlano(@PathVariable Long id) {
        log.debug("REST request to delete Plano : {}", id);
        planoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
