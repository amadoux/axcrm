package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PaySlip;
import com.mycompany.myapp.repository.PaySlipRepository;
import com.mycompany.myapp.service.PaySlipService;
import com.mycompany.myapp.service.dto.PaySlipDTO;
import com.mycompany.myapp.service.mapper.PaySlipMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
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
 * Integration tests for the {@link PaySlipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaySlipResourceIT {

    private static final Long DEFAULT_NET_SALARY_PAY = 1L;
    private static final Long UPDATED_NET_SALARY_PAY = 2L;

    private static final Instant DEFAULT_PAY_SLIP_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAY_SLIP_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_UPLOAD_PAY_SLIP = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_UPLOAD_PAY_SLIP = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_UPLOAD_PAY_SLIP_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_UPLOAD_PAY_SLIP_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/pay-slips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaySlipRepository paySlipRepository;

    @Mock
    private PaySlipRepository paySlipRepositoryMock;

    @Autowired
    private PaySlipMapper paySlipMapper;

    @Mock
    private PaySlipService paySlipServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaySlipMockMvc;

    private PaySlip paySlip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaySlip createEntity(EntityManager em) {
        PaySlip paySlip = new PaySlip()
            .netSalaryPay(DEFAULT_NET_SALARY_PAY)
            .paySlipDate(DEFAULT_PAY_SLIP_DATE)
            .uploadPaySlip(DEFAULT_UPLOAD_PAY_SLIP)
            .uploadPaySlipContentType(DEFAULT_UPLOAD_PAY_SLIP_CONTENT_TYPE);
        return paySlip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaySlip createUpdatedEntity(EntityManager em) {
        PaySlip paySlip = new PaySlip()
            .netSalaryPay(UPDATED_NET_SALARY_PAY)
            .paySlipDate(UPDATED_PAY_SLIP_DATE)
            .uploadPaySlip(UPDATED_UPLOAD_PAY_SLIP)
            .uploadPaySlipContentType(UPDATED_UPLOAD_PAY_SLIP_CONTENT_TYPE);
        return paySlip;
    }

    @BeforeEach
    public void initTest() {
        paySlip = createEntity(em);
    }

    @Test
    @Transactional
    void createPaySlip() throws Exception {
        int databaseSizeBeforeCreate = paySlipRepository.findAll().size();
        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);
        restPaySlipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paySlipDTO)))
            .andExpect(status().isCreated());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeCreate + 1);
        PaySlip testPaySlip = paySlipList.get(paySlipList.size() - 1);
        assertThat(testPaySlip.getNetSalaryPay()).isEqualTo(DEFAULT_NET_SALARY_PAY);
        assertThat(testPaySlip.getPaySlipDate()).isEqualTo(DEFAULT_PAY_SLIP_DATE);
        assertThat(testPaySlip.getUploadPaySlip()).isEqualTo(DEFAULT_UPLOAD_PAY_SLIP);
        assertThat(testPaySlip.getUploadPaySlipContentType()).isEqualTo(DEFAULT_UPLOAD_PAY_SLIP_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createPaySlipWithExistingId() throws Exception {
        // Create the PaySlip with an existing ID
        paySlip.setId(1L);
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        int databaseSizeBeforeCreate = paySlipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaySlipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paySlipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaySlips() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        // Get all the paySlipList
        restPaySlipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paySlip.getId().intValue())))
            .andExpect(jsonPath("$.[*].netSalaryPay").value(hasItem(DEFAULT_NET_SALARY_PAY.intValue())))
            .andExpect(jsonPath("$.[*].paySlipDate").value(hasItem(DEFAULT_PAY_SLIP_DATE.toString())))
            .andExpect(jsonPath("$.[*].uploadPaySlipContentType").value(hasItem(DEFAULT_UPLOAD_PAY_SLIP_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].uploadPaySlip").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_UPLOAD_PAY_SLIP))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaySlipsWithEagerRelationshipsIsEnabled() throws Exception {
        when(paySlipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaySlipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paySlipServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaySlipsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paySlipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaySlipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paySlipRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPaySlip() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        // Get the paySlip
        restPaySlipMockMvc
            .perform(get(ENTITY_API_URL_ID, paySlip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paySlip.getId().intValue()))
            .andExpect(jsonPath("$.netSalaryPay").value(DEFAULT_NET_SALARY_PAY.intValue()))
            .andExpect(jsonPath("$.paySlipDate").value(DEFAULT_PAY_SLIP_DATE.toString()))
            .andExpect(jsonPath("$.uploadPaySlipContentType").value(DEFAULT_UPLOAD_PAY_SLIP_CONTENT_TYPE))
            .andExpect(jsonPath("$.uploadPaySlip").value(Base64.getEncoder().encodeToString(DEFAULT_UPLOAD_PAY_SLIP)));
    }

    @Test
    @Transactional
    void getNonExistingPaySlip() throws Exception {
        // Get the paySlip
        restPaySlipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaySlip() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        int databaseSizeBeforeUpdate = paySlipRepository.findAll().size();

        // Update the paySlip
        PaySlip updatedPaySlip = paySlipRepository.findById(paySlip.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaySlip are not directly saved in db
        em.detach(updatedPaySlip);
        updatedPaySlip
            .netSalaryPay(UPDATED_NET_SALARY_PAY)
            .paySlipDate(UPDATED_PAY_SLIP_DATE)
            .uploadPaySlip(UPDATED_UPLOAD_PAY_SLIP)
            .uploadPaySlipContentType(UPDATED_UPLOAD_PAY_SLIP_CONTENT_TYPE);
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(updatedPaySlip);

        restPaySlipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paySlipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paySlipDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeUpdate);
        PaySlip testPaySlip = paySlipList.get(paySlipList.size() - 1);
        assertThat(testPaySlip.getNetSalaryPay()).isEqualTo(UPDATED_NET_SALARY_PAY);
        assertThat(testPaySlip.getPaySlipDate()).isEqualTo(UPDATED_PAY_SLIP_DATE);
        assertThat(testPaySlip.getUploadPaySlip()).isEqualTo(UPDATED_UPLOAD_PAY_SLIP);
        assertThat(testPaySlip.getUploadPaySlipContentType()).isEqualTo(UPDATED_UPLOAD_PAY_SLIP_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPaySlip() throws Exception {
        int databaseSizeBeforeUpdate = paySlipRepository.findAll().size();
        paySlip.setId(longCount.incrementAndGet());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paySlipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paySlipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaySlip() throws Exception {
        int databaseSizeBeforeUpdate = paySlipRepository.findAll().size();
        paySlip.setId(longCount.incrementAndGet());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paySlipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaySlip() throws Exception {
        int databaseSizeBeforeUpdate = paySlipRepository.findAll().size();
        paySlip.setId(longCount.incrementAndGet());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paySlipDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaySlipWithPatch() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        int databaseSizeBeforeUpdate = paySlipRepository.findAll().size();

        // Update the paySlip using partial update
        PaySlip partialUpdatedPaySlip = new PaySlip();
        partialUpdatedPaySlip.setId(paySlip.getId());

        partialUpdatedPaySlip.netSalaryPay(UPDATED_NET_SALARY_PAY).paySlipDate(UPDATED_PAY_SLIP_DATE);

        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaySlip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaySlip))
            )
            .andExpect(status().isOk());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeUpdate);
        PaySlip testPaySlip = paySlipList.get(paySlipList.size() - 1);
        assertThat(testPaySlip.getNetSalaryPay()).isEqualTo(UPDATED_NET_SALARY_PAY);
        assertThat(testPaySlip.getPaySlipDate()).isEqualTo(UPDATED_PAY_SLIP_DATE);
        assertThat(testPaySlip.getUploadPaySlip()).isEqualTo(DEFAULT_UPLOAD_PAY_SLIP);
        assertThat(testPaySlip.getUploadPaySlipContentType()).isEqualTo(DEFAULT_UPLOAD_PAY_SLIP_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePaySlipWithPatch() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        int databaseSizeBeforeUpdate = paySlipRepository.findAll().size();

        // Update the paySlip using partial update
        PaySlip partialUpdatedPaySlip = new PaySlip();
        partialUpdatedPaySlip.setId(paySlip.getId());

        partialUpdatedPaySlip
            .netSalaryPay(UPDATED_NET_SALARY_PAY)
            .paySlipDate(UPDATED_PAY_SLIP_DATE)
            .uploadPaySlip(UPDATED_UPLOAD_PAY_SLIP)
            .uploadPaySlipContentType(UPDATED_UPLOAD_PAY_SLIP_CONTENT_TYPE);

        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaySlip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaySlip))
            )
            .andExpect(status().isOk());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeUpdate);
        PaySlip testPaySlip = paySlipList.get(paySlipList.size() - 1);
        assertThat(testPaySlip.getNetSalaryPay()).isEqualTo(UPDATED_NET_SALARY_PAY);
        assertThat(testPaySlip.getPaySlipDate()).isEqualTo(UPDATED_PAY_SLIP_DATE);
        assertThat(testPaySlip.getUploadPaySlip()).isEqualTo(UPDATED_UPLOAD_PAY_SLIP);
        assertThat(testPaySlip.getUploadPaySlipContentType()).isEqualTo(UPDATED_UPLOAD_PAY_SLIP_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPaySlip() throws Exception {
        int databaseSizeBeforeUpdate = paySlipRepository.findAll().size();
        paySlip.setId(longCount.incrementAndGet());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paySlipDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paySlipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaySlip() throws Exception {
        int databaseSizeBeforeUpdate = paySlipRepository.findAll().size();
        paySlip.setId(longCount.incrementAndGet());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paySlipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaySlip() throws Exception {
        int databaseSizeBeforeUpdate = paySlipRepository.findAll().size();
        paySlip.setId(longCount.incrementAndGet());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paySlipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaySlip in the database
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaySlip() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        int databaseSizeBeforeDelete = paySlipRepository.findAll().size();

        // Delete the paySlip
        restPaySlipMockMvc
            .perform(delete(ENTITY_API_URL_ID, paySlip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaySlip> paySlipList = paySlipRepository.findAll();
        assertThat(paySlipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
