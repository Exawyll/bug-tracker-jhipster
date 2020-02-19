package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Interview;
import com.mycompany.myapp.repository.InterviewRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Interview}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InterviewResource {

    private final Logger log = LoggerFactory.getLogger(InterviewResource.class);

    private static final String ENTITY_NAME = "interview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterviewRepository interviewRepository;

    public InterviewResource(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    /**
     * {@code POST  /interviews} : Create a new interview.
     *
     * @param interview the interview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interview, or with status {@code 400 (Bad Request)} if the interview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interviews")
    public ResponseEntity<Interview> createInterview(@RequestBody Interview interview) throws URISyntaxException {
        log.debug("REST request to save Interview : {}", interview);
        if (interview.getId() != null) {
            throw new BadRequestAlertException("A new interview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Interview result = interviewRepository.save(interview);
        return ResponseEntity.created(new URI("/api/interviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interviews} : Updates an existing interview.
     *
     * @param interview the interview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interview,
     * or with status {@code 400 (Bad Request)} if the interview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interviews")
    public ResponseEntity<Interview> updateInterview(@RequestBody Interview interview) throws URISyntaxException {
        log.debug("REST request to update Interview : {}", interview);
        if (interview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Interview result = interviewRepository.save(interview);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interview.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /interviews} : get all the interviews.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interviews in body.
     */
    @GetMapping("/interviews")
    public List<Interview> getAllInterviews() {
        log.debug("REST request to get all Interviews");
        return interviewRepository.findAll();
    }

    /**
     * {@code GET  /interviews/:id} : get the "id" interview.
     *
     * @param id the id of the interview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interviews/{id}")
    public ResponseEntity<Interview> getInterview(@PathVariable Long id) {
        log.debug("REST request to get Interview : {}", id);
        Optional<Interview> interview = interviewRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(interview);
    }

    /**
     * {@code DELETE  /interviews/:id} : delete the "id" interview.
     *
     * @param id the id of the interview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interviews/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        log.debug("REST request to delete Interview : {}", id);
        interviewRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
