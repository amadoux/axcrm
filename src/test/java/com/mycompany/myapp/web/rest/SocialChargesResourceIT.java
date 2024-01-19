package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SocialCharges;
import com.mycompany.myapp.domain.enumeration.SPentType;
import com.mycompany.myapp.domain.enumeration.StatusCharges;
import com.mycompany.myapp.repository.SocialChargesRepository;
import com.mycompany.myapp.service.SocialChargesService;
import com.mycompany.myapp.service.dto.SocialChargesDTO;
import com.mycompany.myapp.service.mapper.SocialChargesMapper;
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
 * Integration tests for the {@link SocialChargesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SocialChargesResourceIT {

    private static final Instant DEFAULT_SPENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SPENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SPentType DEFAULT_SPENT_TYPE = SPentType.MATERIAL;
    private static final SPentType UPDATED_SPENT_TYPE = SPentType.ADMINISTRATIVE;

    private static final StatusCharges DEFAULT_STATUS_CHARGES = StatusCharges.IN_PROGRESS;
    private static final StatusCharges UPDATED_STATUS_CHARGES = StatusCharges.ACCEPTED;

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final String DEFAULT_PURCHASE_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_PURCHASE_MANAGER = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/social-charges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocialChargesRepository socialChargesRepository;

    @Mock
    private SocialChargesRepository socialChargesRepositoryMock;

    @Autowired
    private SocialChargesMapper socialChargesMapper;

    @Mock
    private SocialChargesService socialChargesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocialChargesMockMvc;

    private SocialCharges socialCharges;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialCharges createEntity(EntityManager em) {
        SocialCharges socialCharges = new SocialCharges()
            .spentDate(DEFAULT_SPENT_DATE)
            .spentType(DEFAULT_SPENT_TYPE)
            .statusCharges(DEFAULT_STATUS_CHARGES)
            .amount(DEFAULT_AMOUNT)
            .purchaseManager(DEFAULT_PURCHASE_MANAGER)
            .commentText(DEFAULT_COMMENT_TEXT);
        return socialCharges;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialCharges createUpdatedEntity(EntityManager em) {
        SocialCharges socialCharges = new SocialCharges()
            .spentDate(UPDATED_SPENT_DATE)
            .spentType(UPDATED_SPENT_TYPE)
            .statusCharges(UPDATED_STATUS_CHARGES)
            .amount(UPDATED_AMOUNT)
            .purchaseManager(UPDATED_PURCHASE_MANAGER)
            .commentText(UPDATED_COMMENT_TEXT);
        return socialCharges;
    }

    @BeforeEach
    public void initTest() {
        socialCharges = createEntity(em);
    }

    @Test
    @Transactional
    void createSocialCharges() throws Exception {
        int databaseSizeBeforeCreate = socialChargesRepository.findAll().size();
        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);
        restSocialChargesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialChargesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeCreate + 1);
        SocialCharges testSocialCharges = socialChargesList.get(socialChargesList.size() - 1);
        assertThat(testSocialCharges.getSpentDate()).isEqualTo(DEFAULT_SPENT_DATE);
        assertThat(testSocialCharges.getSpentType()).isEqualTo(DEFAULT_SPENT_TYPE);
        assertThat(testSocialCharges.getStatusCharges()).isEqualTo(DEFAULT_STATUS_CHARGES);
        assertThat(testSocialCharges.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testSocialCharges.getPurchaseManager()).isEqualTo(DEFAULT_PURCHASE_MANAGER);
        assertThat(testSocialCharges.getCommentText()).isEqualTo(DEFAULT_COMMENT_TEXT);
    }

    @Test
    @Transactional
    void createSocialChargesWithExistingId() throws Exception {
        // Create the SocialCharges with an existing ID
        socialCharges.setId(1L);
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        int databaseSizeBeforeCreate = socialChargesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialChargesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialChargesRepository.findAll().size();
        // set the field null
        socialCharges.setAmount(null);

        // Create the SocialCharges, which fails.
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        restSocialChargesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialChargesDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocialCharges() throws Exception {
        // Initialize the database
        socialChargesRepository.saveAndFlush(socialCharges);

        // Get all the socialChargesList
        restSocialChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialCharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].spentDate").value(hasItem(DEFAULT_SPENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].spentType").value(hasItem(DEFAULT_SPENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].statusCharges").value(hasItem(DEFAULT_STATUS_CHARGES.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].purchaseManager").value(hasItem(DEFAULT_PURCHASE_MANAGER)))
            .andExpect(jsonPath("$.[*].commentText").value(hasItem(DEFAULT_COMMENT_TEXT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSocialChargesWithEagerRelationshipsIsEnabled() throws Exception {
        when(socialChargesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSocialChargesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(socialChargesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSocialChargesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(socialChargesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSocialChargesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(socialChargesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSocialCharges() throws Exception {
        // Initialize the database
        socialChargesRepository.saveAndFlush(socialCharges);

        // Get the socialCharges
        restSocialChargesMockMvc
            .perform(get(ENTITY_API_URL_ID, socialCharges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socialCharges.getId().intValue()))
            .andExpect(jsonPath("$.spentDate").value(DEFAULT_SPENT_DATE.toString()))
            .andExpect(jsonPath("$.spentType").value(DEFAULT_SPENT_TYPE.toString()))
            .andExpect(jsonPath("$.statusCharges").value(DEFAULT_STATUS_CHARGES.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.purchaseManager").value(DEFAULT_PURCHASE_MANAGER))
            .andExpect(jsonPath("$.commentText").value(DEFAULT_COMMENT_TEXT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSocialCharges() throws Exception {
        // Get the socialCharges
        restSocialChargesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSocialCharges() throws Exception {
        // Initialize the database
        socialChargesRepository.saveAndFlush(socialCharges);

        int databaseSizeBeforeUpdate = socialChargesRepository.findAll().size();

        // Update the socialCharges
        SocialCharges updatedSocialCharges = socialChargesRepository.findById(socialCharges.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSocialCharges are not directly saved in db
        em.detach(updatedSocialCharges);
        updatedSocialCharges
            .spentDate(UPDATED_SPENT_DATE)
            .spentType(UPDATED_SPENT_TYPE)
            .statusCharges(UPDATED_STATUS_CHARGES)
            .amount(UPDATED_AMOUNT)
            .purchaseManager(UPDATED_PURCHASE_MANAGER)
            .commentText(UPDATED_COMMENT_TEXT);
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(updatedSocialCharges);

        restSocialChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialChargesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialChargesDTO))
            )
            .andExpect(status().isOk());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeUpdate);
        SocialCharges testSocialCharges = socialChargesList.get(socialChargesList.size() - 1);
        assertThat(testSocialCharges.getSpentDate()).isEqualTo(UPDATED_SPENT_DATE);
        assertThat(testSocialCharges.getSpentType()).isEqualTo(UPDATED_SPENT_TYPE);
        assertThat(testSocialCharges.getStatusCharges()).isEqualTo(UPDATED_STATUS_CHARGES);
        assertThat(testSocialCharges.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testSocialCharges.getPurchaseManager()).isEqualTo(UPDATED_PURCHASE_MANAGER);
        assertThat(testSocialCharges.getCommentText()).isEqualTo(UPDATED_COMMENT_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingSocialCharges() throws Exception {
        int databaseSizeBeforeUpdate = socialChargesRepository.findAll().size();
        socialCharges.setId(longCount.incrementAndGet());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialChargesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSocialCharges() throws Exception {
        int databaseSizeBeforeUpdate = socialChargesRepository.findAll().size();
        socialCharges.setId(longCount.incrementAndGet());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSocialCharges() throws Exception {
        int databaseSizeBeforeUpdate = socialChargesRepository.findAll().size();
        socialCharges.setId(longCount.incrementAndGet());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialChargesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocialChargesWithPatch() throws Exception {
        // Initialize the database
        socialChargesRepository.saveAndFlush(socialCharges);

        int databaseSizeBeforeUpdate = socialChargesRepository.findAll().size();

        // Update the socialCharges using partial update
        SocialCharges partialUpdatedSocialCharges = new SocialCharges();
        partialUpdatedSocialCharges.setId(socialCharges.getId());

        partialUpdatedSocialCharges
            .spentDate(UPDATED_SPENT_DATE)
            .purchaseManager(UPDATED_PURCHASE_MANAGER)
            .commentText(UPDATED_COMMENT_TEXT);

        restSocialChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialCharges))
            )
            .andExpect(status().isOk());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeUpdate);
        SocialCharges testSocialCharges = socialChargesList.get(socialChargesList.size() - 1);
        assertThat(testSocialCharges.getSpentDate()).isEqualTo(UPDATED_SPENT_DATE);
        assertThat(testSocialCharges.getSpentType()).isEqualTo(DEFAULT_SPENT_TYPE);
        assertThat(testSocialCharges.getStatusCharges()).isEqualTo(DEFAULT_STATUS_CHARGES);
        assertThat(testSocialCharges.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testSocialCharges.getPurchaseManager()).isEqualTo(UPDATED_PURCHASE_MANAGER);
        assertThat(testSocialCharges.getCommentText()).isEqualTo(UPDATED_COMMENT_TEXT);
    }

    @Test
    @Transactional
    void fullUpdateSocialChargesWithPatch() throws Exception {
        // Initialize the database
        socialChargesRepository.saveAndFlush(socialCharges);

        int databaseSizeBeforeUpdate = socialChargesRepository.findAll().size();

        // Update the socialCharges using partial update
        SocialCharges partialUpdatedSocialCharges = new SocialCharges();
        partialUpdatedSocialCharges.setId(socialCharges.getId());

        partialUpdatedSocialCharges
            .spentDate(UPDATED_SPENT_DATE)
            .spentType(UPDATED_SPENT_TYPE)
            .statusCharges(UPDATED_STATUS_CHARGES)
            .amount(UPDATED_AMOUNT)
            .purchaseManager(UPDATED_PURCHASE_MANAGER)
            .commentText(UPDATED_COMMENT_TEXT);

        restSocialChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialCharges))
            )
            .andExpect(status().isOk());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeUpdate);
        SocialCharges testSocialCharges = socialChargesList.get(socialChargesList.size() - 1);
        assertThat(testSocialCharges.getSpentDate()).isEqualTo(UPDATED_SPENT_DATE);
        assertThat(testSocialCharges.getSpentType()).isEqualTo(UPDATED_SPENT_TYPE);
        assertThat(testSocialCharges.getStatusCharges()).isEqualTo(UPDATED_STATUS_CHARGES);
        assertThat(testSocialCharges.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testSocialCharges.getPurchaseManager()).isEqualTo(UPDATED_PURCHASE_MANAGER);
        assertThat(testSocialCharges.getCommentText()).isEqualTo(UPDATED_COMMENT_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingSocialCharges() throws Exception {
        int databaseSizeBeforeUpdate = socialChargesRepository.findAll().size();
        socialCharges.setId(longCount.incrementAndGet());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, socialChargesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSocialCharges() throws Exception {
        int databaseSizeBeforeUpdate = socialChargesRepository.findAll().size();
        socialCharges.setId(longCount.incrementAndGet());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSocialCharges() throws Exception {
        int databaseSizeBeforeUpdate = socialChargesRepository.findAll().size();
        socialCharges.setId(longCount.incrementAndGet());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialChargesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialCharges in the database
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSocialCharges() throws Exception {
        // Initialize the database
        socialChargesRepository.saveAndFlush(socialCharges);

        int databaseSizeBeforeDelete = socialChargesRepository.findAll().size();

        // Delete the socialCharges
        restSocialChargesMockMvc
            .perform(delete(ENTITY_API_URL_ID, socialCharges.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SocialCharges> socialChargesList = socialChargesRepository.findAll();
        assertThat(socialChargesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
