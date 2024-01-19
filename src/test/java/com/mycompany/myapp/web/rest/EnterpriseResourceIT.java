package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Enterprise;
import com.mycompany.myapp.domain.enumeration.Pays;
import com.mycompany.myapp.repository.EnterpriseRepository;
import com.mycompany.myapp.service.dto.EnterpriseDTO;
import com.mycompany.myapp.service.mapper.EnterpriseMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EnterpriseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnterpriseResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_REGISTER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_REGISTER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_UNIQUE_IDENTIFICATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_UNIQUE_IDENTIFICATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_DOMICILE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_DOMICILE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "EMq9@W.fdez3i";
    private static final String UPDATED_EMAIL = ".s@TD.7IW$VV";

    private static final String DEFAULT_BUSINESS_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_PHONE = "BBBBBBBBBB";

    private static final Pays DEFAULT_COUNTRY = Pays.CAMEROON;
    private static final Pays UPDATED_COUNTRY = Pays.SENEGAL;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_BUSINESS_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BUSINESS_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BUSINESS_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BUSINESS_LOGO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_MAP_LOCATOR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MAP_LOCATOR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_MAP_LOCATOR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MAP_LOCATOR_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/enterprises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnterpriseMockMvc;

    private Enterprise enterprise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enterprise createEntity(EntityManager em) {
        Enterprise enterprise = new Enterprise()
            .companyName(DEFAULT_COMPANY_NAME)
            .businessRegisterNumber(DEFAULT_BUSINESS_REGISTER_NUMBER)
            .uniqueIdentificationNumber(DEFAULT_UNIQUE_IDENTIFICATION_NUMBER)
            .businessDomicile(DEFAULT_BUSINESS_DOMICILE)
            .email(DEFAULT_EMAIL)
            .businessPhone(DEFAULT_BUSINESS_PHONE)
            .country(DEFAULT_COUNTRY)
            .city(DEFAULT_CITY)
            .businessLogo(DEFAULT_BUSINESS_LOGO)
            .businessLogoContentType(DEFAULT_BUSINESS_LOGO_CONTENT_TYPE)
            .mapLocator(DEFAULT_MAP_LOCATOR)
            .mapLocatorContentType(DEFAULT_MAP_LOCATOR_CONTENT_TYPE);
        return enterprise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enterprise createUpdatedEntity(EntityManager em) {
        Enterprise enterprise = new Enterprise()
            .companyName(UPDATED_COMPANY_NAME)
            .businessRegisterNumber(UPDATED_BUSINESS_REGISTER_NUMBER)
            .uniqueIdentificationNumber(UPDATED_UNIQUE_IDENTIFICATION_NUMBER)
            .businessDomicile(UPDATED_BUSINESS_DOMICILE)
            .email(UPDATED_EMAIL)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .country(UPDATED_COUNTRY)
            .city(UPDATED_CITY)
            .businessLogo(UPDATED_BUSINESS_LOGO)
            .businessLogoContentType(UPDATED_BUSINESS_LOGO_CONTENT_TYPE)
            .mapLocator(UPDATED_MAP_LOCATOR)
            .mapLocatorContentType(UPDATED_MAP_LOCATOR_CONTENT_TYPE);
        return enterprise;
    }

    @BeforeEach
    public void initTest() {
        enterprise = createEntity(em);
    }

    @Test
    @Transactional
    void createEnterprise() throws Exception {
        int databaseSizeBeforeCreate = enterpriseRepository.findAll().size();
        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);
        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterpriseDTO)))
            .andExpect(status().isCreated());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeCreate + 1);
        Enterprise testEnterprise = enterpriseList.get(enterpriseList.size() - 1);
        assertThat(testEnterprise.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testEnterprise.getBusinessRegisterNumber()).isEqualTo(DEFAULT_BUSINESS_REGISTER_NUMBER);
        assertThat(testEnterprise.getUniqueIdentificationNumber()).isEqualTo(DEFAULT_UNIQUE_IDENTIFICATION_NUMBER);
        assertThat(testEnterprise.getBusinessDomicile()).isEqualTo(DEFAULT_BUSINESS_DOMICILE);
        assertThat(testEnterprise.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEnterprise.getBusinessPhone()).isEqualTo(DEFAULT_BUSINESS_PHONE);
        assertThat(testEnterprise.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testEnterprise.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testEnterprise.getBusinessLogo()).isEqualTo(DEFAULT_BUSINESS_LOGO);
        assertThat(testEnterprise.getBusinessLogoContentType()).isEqualTo(DEFAULT_BUSINESS_LOGO_CONTENT_TYPE);
        assertThat(testEnterprise.getMapLocator()).isEqualTo(DEFAULT_MAP_LOCATOR);
        assertThat(testEnterprise.getMapLocatorContentType()).isEqualTo(DEFAULT_MAP_LOCATOR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createEnterpriseWithExistingId() throws Exception {
        // Create the Enterprise with an existing ID
        enterprise.setId(1L);
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        int databaseSizeBeforeCreate = enterpriseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setCompanyName(null);

        // Create the Enterprise, which fails.
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBusinessRegisterNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setBusinessRegisterNumber(null);

        // Create the Enterprise, which fails.
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUniqueIdentificationNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setUniqueIdentificationNumber(null);

        // Create the Enterprise, which fails.
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setEmail(null);

        // Create the Enterprise, which fails.
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBusinessPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setBusinessPhone(null);

        // Create the Enterprise, which fails.
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnterprises() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        // Get all the enterpriseList
        restEnterpriseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enterprise.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].businessRegisterNumber").value(hasItem(DEFAULT_BUSINESS_REGISTER_NUMBER)))
            .andExpect(jsonPath("$.[*].uniqueIdentificationNumber").value(hasItem(DEFAULT_UNIQUE_IDENTIFICATION_NUMBER)))
            .andExpect(jsonPath("$.[*].businessDomicile").value(hasItem(DEFAULT_BUSINESS_DOMICILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].businessPhone").value(hasItem(DEFAULT_BUSINESS_PHONE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].businessLogoContentType").value(hasItem(DEFAULT_BUSINESS_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].businessLogo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BUSINESS_LOGO))))
            .andExpect(jsonPath("$.[*].mapLocatorContentType").value(hasItem(DEFAULT_MAP_LOCATOR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].mapLocator").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_MAP_LOCATOR))));
    }

    @Test
    @Transactional
    void getEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        // Get the enterprise
        restEnterpriseMockMvc
            .perform(get(ENTITY_API_URL_ID, enterprise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enterprise.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.businessRegisterNumber").value(DEFAULT_BUSINESS_REGISTER_NUMBER))
            .andExpect(jsonPath("$.uniqueIdentificationNumber").value(DEFAULT_UNIQUE_IDENTIFICATION_NUMBER))
            .andExpect(jsonPath("$.businessDomicile").value(DEFAULT_BUSINESS_DOMICILE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.businessPhone").value(DEFAULT_BUSINESS_PHONE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.businessLogoContentType").value(DEFAULT_BUSINESS_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.businessLogo").value(Base64.getEncoder().encodeToString(DEFAULT_BUSINESS_LOGO)))
            .andExpect(jsonPath("$.mapLocatorContentType").value(DEFAULT_MAP_LOCATOR_CONTENT_TYPE))
            .andExpect(jsonPath("$.mapLocator").value(Base64.getEncoder().encodeToString(DEFAULT_MAP_LOCATOR)));
    }

    @Test
    @Transactional
    void getNonExistingEnterprise() throws Exception {
        // Get the enterprise
        restEnterpriseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();

        // Update the enterprise
        Enterprise updatedEnterprise = enterpriseRepository.findById(enterprise.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnterprise are not directly saved in db
        em.detach(updatedEnterprise);
        updatedEnterprise
            .companyName(UPDATED_COMPANY_NAME)
            .businessRegisterNumber(UPDATED_BUSINESS_REGISTER_NUMBER)
            .uniqueIdentificationNumber(UPDATED_UNIQUE_IDENTIFICATION_NUMBER)
            .businessDomicile(UPDATED_BUSINESS_DOMICILE)
            .email(UPDATED_EMAIL)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .country(UPDATED_COUNTRY)
            .city(UPDATED_CITY)
            .businessLogo(UPDATED_BUSINESS_LOGO)
            .businessLogoContentType(UPDATED_BUSINESS_LOGO_CONTENT_TYPE)
            .mapLocator(UPDATED_MAP_LOCATOR)
            .mapLocatorContentType(UPDATED_MAP_LOCATOR_CONTENT_TYPE);
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(updatedEnterprise);

        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enterpriseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enterpriseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
        Enterprise testEnterprise = enterpriseList.get(enterpriseList.size() - 1);
        assertThat(testEnterprise.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testEnterprise.getBusinessRegisterNumber()).isEqualTo(UPDATED_BUSINESS_REGISTER_NUMBER);
        assertThat(testEnterprise.getUniqueIdentificationNumber()).isEqualTo(UPDATED_UNIQUE_IDENTIFICATION_NUMBER);
        assertThat(testEnterprise.getBusinessDomicile()).isEqualTo(UPDATED_BUSINESS_DOMICILE);
        assertThat(testEnterprise.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEnterprise.getBusinessPhone()).isEqualTo(UPDATED_BUSINESS_PHONE);
        assertThat(testEnterprise.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEnterprise.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testEnterprise.getBusinessLogo()).isEqualTo(UPDATED_BUSINESS_LOGO);
        assertThat(testEnterprise.getBusinessLogoContentType()).isEqualTo(UPDATED_BUSINESS_LOGO_CONTENT_TYPE);
        assertThat(testEnterprise.getMapLocator()).isEqualTo(UPDATED_MAP_LOCATOR);
        assertThat(testEnterprise.getMapLocatorContentType()).isEqualTo(UPDATED_MAP_LOCATOR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(longCount.incrementAndGet());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enterpriseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enterpriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(longCount.incrementAndGet());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enterpriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(longCount.incrementAndGet());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterpriseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnterpriseWithPatch() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();

        // Update the enterprise using partial update
        Enterprise partialUpdatedEnterprise = new Enterprise();
        partialUpdatedEnterprise.setId(enterprise.getId());

        partialUpdatedEnterprise.businessRegisterNumber(UPDATED_BUSINESS_REGISTER_NUMBER).email(UPDATED_EMAIL).city(UPDATED_CITY);

        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnterprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnterprise))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
        Enterprise testEnterprise = enterpriseList.get(enterpriseList.size() - 1);
        assertThat(testEnterprise.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testEnterprise.getBusinessRegisterNumber()).isEqualTo(UPDATED_BUSINESS_REGISTER_NUMBER);
        assertThat(testEnterprise.getUniqueIdentificationNumber()).isEqualTo(DEFAULT_UNIQUE_IDENTIFICATION_NUMBER);
        assertThat(testEnterprise.getBusinessDomicile()).isEqualTo(DEFAULT_BUSINESS_DOMICILE);
        assertThat(testEnterprise.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEnterprise.getBusinessPhone()).isEqualTo(DEFAULT_BUSINESS_PHONE);
        assertThat(testEnterprise.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testEnterprise.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testEnterprise.getBusinessLogo()).isEqualTo(DEFAULT_BUSINESS_LOGO);
        assertThat(testEnterprise.getBusinessLogoContentType()).isEqualTo(DEFAULT_BUSINESS_LOGO_CONTENT_TYPE);
        assertThat(testEnterprise.getMapLocator()).isEqualTo(DEFAULT_MAP_LOCATOR);
        assertThat(testEnterprise.getMapLocatorContentType()).isEqualTo(DEFAULT_MAP_LOCATOR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateEnterpriseWithPatch() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();

        // Update the enterprise using partial update
        Enterprise partialUpdatedEnterprise = new Enterprise();
        partialUpdatedEnterprise.setId(enterprise.getId());

        partialUpdatedEnterprise
            .companyName(UPDATED_COMPANY_NAME)
            .businessRegisterNumber(UPDATED_BUSINESS_REGISTER_NUMBER)
            .uniqueIdentificationNumber(UPDATED_UNIQUE_IDENTIFICATION_NUMBER)
            .businessDomicile(UPDATED_BUSINESS_DOMICILE)
            .email(UPDATED_EMAIL)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .country(UPDATED_COUNTRY)
            .city(UPDATED_CITY)
            .businessLogo(UPDATED_BUSINESS_LOGO)
            .businessLogoContentType(UPDATED_BUSINESS_LOGO_CONTENT_TYPE)
            .mapLocator(UPDATED_MAP_LOCATOR)
            .mapLocatorContentType(UPDATED_MAP_LOCATOR_CONTENT_TYPE);

        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnterprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnterprise))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
        Enterprise testEnterprise = enterpriseList.get(enterpriseList.size() - 1);
        assertThat(testEnterprise.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testEnterprise.getBusinessRegisterNumber()).isEqualTo(UPDATED_BUSINESS_REGISTER_NUMBER);
        assertThat(testEnterprise.getUniqueIdentificationNumber()).isEqualTo(UPDATED_UNIQUE_IDENTIFICATION_NUMBER);
        assertThat(testEnterprise.getBusinessDomicile()).isEqualTo(UPDATED_BUSINESS_DOMICILE);
        assertThat(testEnterprise.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEnterprise.getBusinessPhone()).isEqualTo(UPDATED_BUSINESS_PHONE);
        assertThat(testEnterprise.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEnterprise.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testEnterprise.getBusinessLogo()).isEqualTo(UPDATED_BUSINESS_LOGO);
        assertThat(testEnterprise.getBusinessLogoContentType()).isEqualTo(UPDATED_BUSINESS_LOGO_CONTENT_TYPE);
        assertThat(testEnterprise.getMapLocator()).isEqualTo(UPDATED_MAP_LOCATOR);
        assertThat(testEnterprise.getMapLocatorContentType()).isEqualTo(UPDATED_MAP_LOCATOR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(longCount.incrementAndGet());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enterpriseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enterpriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(longCount.incrementAndGet());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enterpriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(longCount.incrementAndGet());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(enterpriseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        int databaseSizeBeforeDelete = enterpriseRepository.findAll().size();

        // Delete the enterprise
        restEnterpriseMockMvc
            .perform(delete(ENTITY_API_URL_ID, enterprise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
