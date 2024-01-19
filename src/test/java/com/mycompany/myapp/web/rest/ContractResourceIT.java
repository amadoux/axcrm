package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Contract;
import com.mycompany.myapp.domain.enumeration.ContractType;
import com.mycompany.myapp.domain.enumeration.StatusContract;
import com.mycompany.myapp.repository.ContractRepository;
import com.mycompany.myapp.service.ContractService;
import com.mycompany.myapp.service.dto.ContractDTO;
import com.mycompany.myapp.service.mapper.ContractMapper;
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
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final ContractType DEFAULT_CONTRACT_TYPE = ContractType.CDD;
    private static final ContractType UPDATED_CONTRACT_TYPE = ContractType.CDI;

    private static final Instant DEFAULT_ENTRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENTRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final StatusContract DEFAULT_STATUS_CONTRACT = StatusContract.ENCOURS;
    private static final StatusContract UPDATED_STATUS_CONTRACT = StatusContract.RUPTURE_CONVENTIONNELLE;

    private static final byte[] DEFAULT_UPLOAD_CONTRACT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_UPLOAD_CONTRACT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_UPLOAD_CONTRACT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractRepository contractRepository;

    @Mock
    private ContractRepository contractRepositoryMock;

    @Autowired
    private ContractMapper contractMapper;

    @Mock
    private ContractService contractServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .contractType(DEFAULT_CONTRACT_TYPE)
            .entryDate(DEFAULT_ENTRY_DATE)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .statusContract(DEFAULT_STATUS_CONTRACT)
            .uploadContract(DEFAULT_UPLOAD_CONTRACT)
            .uploadContractContentType(DEFAULT_UPLOAD_CONTRACT_CONTENT_TYPE);
        return contract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity(EntityManager em) {
        Contract contract = new Contract()
            .contractType(UPDATED_CONTRACT_TYPE)
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .statusContract(UPDATED_STATUS_CONTRACT)
            .uploadContract(UPDATED_UPLOAD_CONTRACT)
            .uploadContractContentType(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);
        return contract;
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();
        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getContractType()).isEqualTo(DEFAULT_CONTRACT_TYPE);
        assertThat(testContract.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testContract.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testContract.getStatusContract()).isEqualTo(DEFAULT_STATUS_CONTRACT);
        assertThat(testContract.getUploadContract()).isEqualTo(DEFAULT_UPLOAD_CONTRACT);
        assertThat(testContract.getUploadContractContentType()).isEqualTo(DEFAULT_UPLOAD_CONTRACT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId(1L);
        ContractDTO contractDTO = contractMapper.toDto(contract);

        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].statusContract").value(hasItem(DEFAULT_STATUS_CONTRACT.toString())))
            .andExpect(jsonPath("$.[*].uploadContractContentType").value(hasItem(DEFAULT_UPLOAD_CONTRACT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].uploadContract").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_UPLOAD_CONTRACT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contractServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contractRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.contractType").value(DEFAULT_CONTRACT_TYPE.toString()))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.statusContract").value(DEFAULT_STATUS_CONTRACT.toString()))
            .andExpect(jsonPath("$.uploadContractContentType").value(DEFAULT_UPLOAD_CONTRACT_CONTENT_TYPE))
            .andExpect(jsonPath("$.uploadContract").value(Base64.getEncoder().encodeToString(DEFAULT_UPLOAD_CONTRACT)));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .contractType(UPDATED_CONTRACT_TYPE)
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .statusContract(UPDATED_STATUS_CONTRACT)
            .uploadContract(UPDATED_UPLOAD_CONTRACT)
            .uploadContractContentType(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
        assertThat(testContract.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testContract.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testContract.getStatusContract()).isEqualTo(UPDATED_STATUS_CONTRACT);
        assertThat(testContract.getUploadContract()).isEqualTo(UPDATED_UPLOAD_CONTRACT);
        assertThat(testContract.getUploadContractContentType()).isEqualTo(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .releaseDate(UPDATED_RELEASE_DATE)
            .statusContract(UPDATED_STATUS_CONTRACT)
            .uploadContract(UPDATED_UPLOAD_CONTRACT)
            .uploadContractContentType(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getContractType()).isEqualTo(DEFAULT_CONTRACT_TYPE);
        assertThat(testContract.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testContract.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testContract.getStatusContract()).isEqualTo(UPDATED_STATUS_CONTRACT);
        assertThat(testContract.getUploadContract()).isEqualTo(UPDATED_UPLOAD_CONTRACT);
        assertThat(testContract.getUploadContractContentType()).isEqualTo(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .contractType(UPDATED_CONTRACT_TYPE)
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .statusContract(UPDATED_STATUS_CONTRACT)
            .uploadContract(UPDATED_UPLOAD_CONTRACT)
            .uploadContractContentType(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
        assertThat(testContract.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testContract.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testContract.getStatusContract()).isEqualTo(UPDATED_STATUS_CONTRACT);
        assertThat(testContract.getUploadContract()).isEqualTo(UPDATED_UPLOAD_CONTRACT);
        assertThat(testContract.getUploadContractContentType()).isEqualTo(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
