package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Absence;
import com.mycompany.myapp.domain.enumeration.ConfirmationAbsence;
import com.mycompany.myapp.domain.enumeration.TypeAbsence;
import com.mycompany.myapp.repository.AbsenceRepository;
import com.mycompany.myapp.service.AbsenceService;
import com.mycompany.myapp.service.dto.AbsenceDTO;
import com.mycompany.myapp.service.mapper.AbsenceMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AbsenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AbsenceResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_NUMBER_DAY_ABSENCE = 1L;
    private static final Long UPDATED_NUMBER_DAY_ABSENCE = 2L;

    private static final TypeAbsence DEFAULT_TYPE_ABSENCE = TypeAbsence.RTT;
    private static final TypeAbsence UPDATED_TYPE_ABSENCE = TypeAbsence.CONGES_PAYES;

    private static final ConfirmationAbsence DEFAULT_CONFIRMATION_ABSENCE = ConfirmationAbsence.ENCOURS;
    private static final ConfirmationAbsence UPDATED_CONFIRMATION_ABSENCE = ConfirmationAbsence.REJETE;

    private static final Long DEFAULT_CONGE_RESTANT = 1L;
    private static final Long UPDATED_CONGE_RESTANT = 2L;

    private static final String ENTITY_API_URL = "/api/absences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AbsenceRepository absenceRepository;

    @Mock
    private AbsenceRepository absenceRepositoryMock;

    @Autowired
    private AbsenceMapper absenceMapper;

    @Mock
    private AbsenceService absenceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAbsenceMockMvc;

    private Absence absence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Absence createEntity(EntityManager em) {
        Absence absence = new Absence()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .numberDayAbsence(DEFAULT_NUMBER_DAY_ABSENCE)
            .typeAbsence(DEFAULT_TYPE_ABSENCE)
            .confirmationAbsence(DEFAULT_CONFIRMATION_ABSENCE)
            .congeRestant(DEFAULT_CONGE_RESTANT);
        return absence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Absence createUpdatedEntity(EntityManager em) {
        Absence absence = new Absence()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .numberDayAbsence(UPDATED_NUMBER_DAY_ABSENCE)
            .typeAbsence(UPDATED_TYPE_ABSENCE)
            .confirmationAbsence(UPDATED_CONFIRMATION_ABSENCE)
            .congeRestant(UPDATED_CONGE_RESTANT);
        return absence;
    }

    @BeforeEach
    public void initTest() {
        absence = createEntity(em);
    }

    @Test
    @Transactional
    void createAbsence() throws Exception {
        int databaseSizeBeforeCreate = absenceRepository.findAll().size();
        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);
        restAbsenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(absenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeCreate + 1);
        Absence testAbsence = absenceList.get(absenceList.size() - 1);
        assertThat(testAbsence.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAbsence.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAbsence.getNumberDayAbsence()).isEqualTo(DEFAULT_NUMBER_DAY_ABSENCE);
        assertThat(testAbsence.getTypeAbsence()).isEqualTo(DEFAULT_TYPE_ABSENCE);
        assertThat(testAbsence.getConfirmationAbsence()).isEqualTo(DEFAULT_CONFIRMATION_ABSENCE);
        assertThat(testAbsence.getCongeRestant()).isEqualTo(DEFAULT_CONGE_RESTANT);
    }

    @Test
    @Transactional
    void createAbsenceWithExistingId() throws Exception {
        // Create the Absence with an existing ID
        absence.setId(1L);
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        int databaseSizeBeforeCreate = absenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbsenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(absenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAbsences() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        // Get all the absenceList
        restAbsenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(absence.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].numberDayAbsence").value(hasItem(DEFAULT_NUMBER_DAY_ABSENCE.intValue())))
            .andExpect(jsonPath("$.[*].typeAbsence").value(hasItem(DEFAULT_TYPE_ABSENCE.toString())))
            .andExpect(jsonPath("$.[*].confirmationAbsence").value(hasItem(DEFAULT_CONFIRMATION_ABSENCE.toString())))
            .andExpect(jsonPath("$.[*].congeRestant").value(hasItem(DEFAULT_CONGE_RESTANT.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAbsencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(absenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAbsenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(absenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAbsencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(absenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAbsenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(absenceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAbsence() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        // Get the absence
        restAbsenceMockMvc
            .perform(get(ENTITY_API_URL_ID, absence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(absence.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.numberDayAbsence").value(DEFAULT_NUMBER_DAY_ABSENCE.intValue()))
            .andExpect(jsonPath("$.typeAbsence").value(DEFAULT_TYPE_ABSENCE.toString()))
            .andExpect(jsonPath("$.confirmationAbsence").value(DEFAULT_CONFIRMATION_ABSENCE.toString()))
            .andExpect(jsonPath("$.congeRestant").value(DEFAULT_CONGE_RESTANT.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAbsence() throws Exception {
        // Get the absence
        restAbsenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAbsence() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();

        // Update the absence
        Absence updatedAbsence = absenceRepository.findById(absence.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAbsence are not directly saved in db
        em.detach(updatedAbsence);
        updatedAbsence
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .numberDayAbsence(UPDATED_NUMBER_DAY_ABSENCE)
            .typeAbsence(UPDATED_TYPE_ABSENCE)
            .confirmationAbsence(UPDATED_CONFIRMATION_ABSENCE)
            .congeRestant(UPDATED_CONGE_RESTANT);
        AbsenceDTO absenceDTO = absenceMapper.toDto(updatedAbsence);

        restAbsenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, absenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
        Absence testAbsence = absenceList.get(absenceList.size() - 1);
        assertThat(testAbsence.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAbsence.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAbsence.getNumberDayAbsence()).isEqualTo(UPDATED_NUMBER_DAY_ABSENCE);
        assertThat(testAbsence.getTypeAbsence()).isEqualTo(UPDATED_TYPE_ABSENCE);
        assertThat(testAbsence.getConfirmationAbsence()).isEqualTo(UPDATED_CONFIRMATION_ABSENCE);
        assertThat(testAbsence.getCongeRestant()).isEqualTo(UPDATED_CONGE_RESTANT);
    }

    @Test
    @Transactional
    void putNonExistingAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(longCount.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, absenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(longCount.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(longCount.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(absenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAbsenceWithPatch() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();

        // Update the absence using partial update
        Absence partialUpdatedAbsence = new Absence();
        partialUpdatedAbsence.setId(absence.getId());

        partialUpdatedAbsence
            .numberDayAbsence(UPDATED_NUMBER_DAY_ABSENCE)
            .typeAbsence(UPDATED_TYPE_ABSENCE)
            .congeRestant(UPDATED_CONGE_RESTANT);

        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbsence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbsence))
            )
            .andExpect(status().isOk());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
        Absence testAbsence = absenceList.get(absenceList.size() - 1);
        assertThat(testAbsence.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAbsence.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAbsence.getNumberDayAbsence()).isEqualTo(UPDATED_NUMBER_DAY_ABSENCE);
        assertThat(testAbsence.getTypeAbsence()).isEqualTo(UPDATED_TYPE_ABSENCE);
        assertThat(testAbsence.getConfirmationAbsence()).isEqualTo(DEFAULT_CONFIRMATION_ABSENCE);
        assertThat(testAbsence.getCongeRestant()).isEqualTo(UPDATED_CONGE_RESTANT);
    }

    @Test
    @Transactional
    void fullUpdateAbsenceWithPatch() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();

        // Update the absence using partial update
        Absence partialUpdatedAbsence = new Absence();
        partialUpdatedAbsence.setId(absence.getId());

        partialUpdatedAbsence
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .numberDayAbsence(UPDATED_NUMBER_DAY_ABSENCE)
            .typeAbsence(UPDATED_TYPE_ABSENCE)
            .confirmationAbsence(UPDATED_CONFIRMATION_ABSENCE)
            .congeRestant(UPDATED_CONGE_RESTANT);

        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbsence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbsence))
            )
            .andExpect(status().isOk());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
        Absence testAbsence = absenceList.get(absenceList.size() - 1);
        assertThat(testAbsence.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAbsence.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAbsence.getNumberDayAbsence()).isEqualTo(UPDATED_NUMBER_DAY_ABSENCE);
        assertThat(testAbsence.getTypeAbsence()).isEqualTo(UPDATED_TYPE_ABSENCE);
        assertThat(testAbsence.getConfirmationAbsence()).isEqualTo(UPDATED_CONFIRMATION_ABSENCE);
        assertThat(testAbsence.getCongeRestant()).isEqualTo(UPDATED_CONGE_RESTANT);
    }

    @Test
    @Transactional
    void patchNonExistingAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(longCount.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, absenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(longCount.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAbsence() throws Exception {
        int databaseSizeBeforeUpdate = absenceRepository.findAll().size();
        absence.setId(longCount.incrementAndGet());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(absenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Absence in the database
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAbsence() throws Exception {
        // Initialize the database
        absenceRepository.saveAndFlush(absence);

        int databaseSizeBeforeDelete = absenceRepository.findAll().size();

        // Delete the absence
        restAbsenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, absence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Absence> absenceList = absenceRepository.findAll();
        assertThat(absenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
