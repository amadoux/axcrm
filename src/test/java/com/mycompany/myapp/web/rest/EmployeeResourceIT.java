package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.enumeration.ContractType;
import com.mycompany.myapp.domain.enumeration.Level;
import com.mycompany.myapp.domain.enumeration.Pays;
import com.mycompany.myapp.domain.enumeration.SalaryType;
import com.mycompany.myapp.domain.enumeration.TypeEmployed;
import com.mycompany.myapp.repository.EmployeeRepository;
import com.mycompany.myapp.service.EmployeeService;
import com.mycompany.myapp.service.dto.EmployeeDTO;
import com.mycompany.myapp.service.mapper.EmployeeMapper;
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
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTITY_CARD = "AAAAAAAAAA";
    private static final String UPDATED_IDENTITY_CARD = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_INSPIRATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INSPIRATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Pays DEFAULT_NATIONALITY = Pays.CAMEROON;
    private static final Pays UPDATED_NATIONALITY = Pays.SENEGAL;

    private static final byte[] DEFAULT_UPLOAD_IDENTITY_CARD = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_UPLOAD_IDENTITY_CARD = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_UPLOAD_IDENTITY_CARD_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_UPLOAD_IDENTITY_CARD_CONTENT_TYPE = "image/png";

    private static final TypeEmployed DEFAULT_TYPE_EMPLOYED = TypeEmployed.MARKETER;
    private static final TypeEmployed UPDATED_TYPE_EMPLOYED = TypeEmployed.SALARY;

    private static final String DEFAULT_CITY_AGENCY = "AAAAAAAAAA";
    private static final String UPDATED_CITY_AGENCY = "BBBBBBBBBB";

    private static final String DEFAULT_RESIDENCE_CITY = "AAAAAAAAAA";
    private static final String UPDATED_RESIDENCE_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SOCIAL_SECURITY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SOCIAL_SECURITY_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BIRTH_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_PLACE = "BBBBBBBBBB";

    private static final Instant DEFAULT_ENTRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENTRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_WORKSTATION = "AAAAAAAAAA";
    private static final String UPDATED_WORKSTATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_WORKSTATION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_WORKSTATION = "BBBBBBBBBB";

    private static final Level DEFAULT_LEVEL = Level.A;
    private static final Level UPDATED_LEVEL = Level.B;

    private static final Long DEFAULT_COEFFICIENT = 1L;
    private static final Long UPDATED_COEFFICIENT = 2L;

    private static final String DEFAULT_NUMBER_HOURS = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER_HOURS = "BBBBBBBBBB";

    private static final String DEFAULT_AVERAGE_HOURLY_COST = "AAAAAAAAAA";
    private static final String UPDATED_AVERAGE_HOURLY_COST = "BBBBBBBBBB";

    private static final Long DEFAULT_MONTHLY_GROSS_AMOUNT = 1L;
    private static final Long UPDATED_MONTHLY_GROSS_AMOUNT = 2L;

    private static final Long DEFAULT_COMMISSION_AMOUNT = 1L;
    private static final Long UPDATED_COMMISSION_AMOUNT = 2L;

    private static final ContractType DEFAULT_CONTRACT_TYPE = ContractType.CDD;
    private static final ContractType UPDATED_CONTRACT_TYPE = ContractType.CDI;

    private static final SalaryType DEFAULT_SALARY_TYPE = SalaryType.EXECUTIVE_SALARIED;
    private static final SalaryType UPDATED_SALARY_TYPE = SalaryType.ASSOCIATE;

    private static final Instant DEFAULT_HIRE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HIRE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeService employeeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .identityCard(DEFAULT_IDENTITY_CARD)
            .dateInspiration(DEFAULT_DATE_INSPIRATION)
            .nationality(DEFAULT_NATIONALITY)
            .uploadIdentityCard(DEFAULT_UPLOAD_IDENTITY_CARD)
            .uploadIdentityCardContentType(DEFAULT_UPLOAD_IDENTITY_CARD_CONTENT_TYPE)
            .typeEmployed(DEFAULT_TYPE_EMPLOYED)
            .cityAgency(DEFAULT_CITY_AGENCY)
            .residenceCity(DEFAULT_RESIDENCE_CITY)
            .address(DEFAULT_ADDRESS)
            .socialSecurityNumber(DEFAULT_SOCIAL_SECURITY_NUMBER)
            .birthDate(DEFAULT_BIRTH_DATE)
            .birthPlace(DEFAULT_BIRTH_PLACE)
            .entryDate(DEFAULT_ENTRY_DATE)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .workstation(DEFAULT_WORKSTATION)
            .descriptionWorkstation(DEFAULT_DESCRIPTION_WORKSTATION)
            .level(DEFAULT_LEVEL)
            .coefficient(DEFAULT_COEFFICIENT)
            .numberHours(DEFAULT_NUMBER_HOURS)
            .averageHourlyCost(DEFAULT_AVERAGE_HOURLY_COST)
            .monthlyGrossAmount(DEFAULT_MONTHLY_GROSS_AMOUNT)
            .commissionAmount(DEFAULT_COMMISSION_AMOUNT)
            .contractType(DEFAULT_CONTRACT_TYPE)
            .salaryType(DEFAULT_SALARY_TYPE)
            .hireDate(DEFAULT_HIRE_DATE);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .identityCard(UPDATED_IDENTITY_CARD)
            .dateInspiration(UPDATED_DATE_INSPIRATION)
            .nationality(UPDATED_NATIONALITY)
            .uploadIdentityCard(UPDATED_UPLOAD_IDENTITY_CARD)
            .uploadIdentityCardContentType(UPDATED_UPLOAD_IDENTITY_CARD_CONTENT_TYPE)
            .typeEmployed(UPDATED_TYPE_EMPLOYED)
            .cityAgency(UPDATED_CITY_AGENCY)
            .residenceCity(UPDATED_RESIDENCE_CITY)
            .address(UPDATED_ADDRESS)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .workstation(UPDATED_WORKSTATION)
            .descriptionWorkstation(UPDATED_DESCRIPTION_WORKSTATION)
            .level(UPDATED_LEVEL)
            .coefficient(UPDATED_COEFFICIENT)
            .numberHours(UPDATED_NUMBER_HOURS)
            .averageHourlyCost(UPDATED_AVERAGE_HOURLY_COST)
            .monthlyGrossAmount(UPDATED_MONTHLY_GROSS_AMOUNT)
            .commissionAmount(UPDATED_COMMISSION_AMOUNT)
            .contractType(UPDATED_CONTRACT_TYPE)
            .salaryType(UPDATED_SALARY_TYPE)
            .hireDate(UPDATED_HIRE_DATE);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployee.getIdentityCard()).isEqualTo(DEFAULT_IDENTITY_CARD);
        assertThat(testEmployee.getDateInspiration()).isEqualTo(DEFAULT_DATE_INSPIRATION);
        assertThat(testEmployee.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testEmployee.getUploadIdentityCard()).isEqualTo(DEFAULT_UPLOAD_IDENTITY_CARD);
        assertThat(testEmployee.getUploadIdentityCardContentType()).isEqualTo(DEFAULT_UPLOAD_IDENTITY_CARD_CONTENT_TYPE);
        assertThat(testEmployee.getTypeEmployed()).isEqualTo(DEFAULT_TYPE_EMPLOYED);
        assertThat(testEmployee.getCityAgency()).isEqualTo(DEFAULT_CITY_AGENCY);
        assertThat(testEmployee.getResidenceCity()).isEqualTo(DEFAULT_RESIDENCE_CITY);
        assertThat(testEmployee.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEmployee.getSocialSecurityNumber()).isEqualTo(DEFAULT_SOCIAL_SECURITY_NUMBER);
        assertThat(testEmployee.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testEmployee.getBirthPlace()).isEqualTo(DEFAULT_BIRTH_PLACE);
        assertThat(testEmployee.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testEmployee.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testEmployee.getWorkstation()).isEqualTo(DEFAULT_WORKSTATION);
        assertThat(testEmployee.getDescriptionWorkstation()).isEqualTo(DEFAULT_DESCRIPTION_WORKSTATION);
        assertThat(testEmployee.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testEmployee.getCoefficient()).isEqualTo(DEFAULT_COEFFICIENT);
        assertThat(testEmployee.getNumberHours()).isEqualTo(DEFAULT_NUMBER_HOURS);
        assertThat(testEmployee.getAverageHourlyCost()).isEqualTo(DEFAULT_AVERAGE_HOURLY_COST);
        assertThat(testEmployee.getMonthlyGrossAmount()).isEqualTo(DEFAULT_MONTHLY_GROSS_AMOUNT);
        assertThat(testEmployee.getCommissionAmount()).isEqualTo(DEFAULT_COMMISSION_AMOUNT);
        assertThat(testEmployee.getContractType()).isEqualTo(DEFAULT_CONTRACT_TYPE);
        assertThat(testEmployee.getSalaryType()).isEqualTo(DEFAULT_SALARY_TYPE);
        assertThat(testEmployee.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setEmail(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setPhoneNumber(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentityCardIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setIdentityCard(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].identityCard").value(hasItem(DEFAULT_IDENTITY_CARD)))
            .andExpect(jsonPath("$.[*].dateInspiration").value(hasItem(DEFAULT_DATE_INSPIRATION.toString())))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())))
            .andExpect(jsonPath("$.[*].uploadIdentityCardContentType").value(hasItem(DEFAULT_UPLOAD_IDENTITY_CARD_CONTENT_TYPE)))
            .andExpect(
                jsonPath("$.[*].uploadIdentityCard").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_UPLOAD_IDENTITY_CARD)))
            )
            .andExpect(jsonPath("$.[*].typeEmployed").value(hasItem(DEFAULT_TYPE_EMPLOYED.toString())))
            .andExpect(jsonPath("$.[*].cityAgency").value(hasItem(DEFAULT_CITY_AGENCY)))
            .andExpect(jsonPath("$.[*].residenceCity").value(hasItem(DEFAULT_RESIDENCE_CITY)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].socialSecurityNumber").value(hasItem(DEFAULT_SOCIAL_SECURITY_NUMBER)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].workstation").value(hasItem(DEFAULT_WORKSTATION)))
            .andExpect(jsonPath("$.[*].descriptionWorkstation").value(hasItem(DEFAULT_DESCRIPTION_WORKSTATION)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].coefficient").value(hasItem(DEFAULT_COEFFICIENT.intValue())))
            .andExpect(jsonPath("$.[*].numberHours").value(hasItem(DEFAULT_NUMBER_HOURS)))
            .andExpect(jsonPath("$.[*].averageHourlyCost").value(hasItem(DEFAULT_AVERAGE_HOURLY_COST)))
            .andExpect(jsonPath("$.[*].monthlyGrossAmount").value(hasItem(DEFAULT_MONTHLY_GROSS_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].commissionAmount").value(hasItem(DEFAULT_COMMISSION_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].salaryType").value(hasItem(DEFAULT_SALARY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employeeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.identityCard").value(DEFAULT_IDENTITY_CARD))
            .andExpect(jsonPath("$.dateInspiration").value(DEFAULT_DATE_INSPIRATION.toString()))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY.toString()))
            .andExpect(jsonPath("$.uploadIdentityCardContentType").value(DEFAULT_UPLOAD_IDENTITY_CARD_CONTENT_TYPE))
            .andExpect(jsonPath("$.uploadIdentityCard").value(Base64.getEncoder().encodeToString(DEFAULT_UPLOAD_IDENTITY_CARD)))
            .andExpect(jsonPath("$.typeEmployed").value(DEFAULT_TYPE_EMPLOYED.toString()))
            .andExpect(jsonPath("$.cityAgency").value(DEFAULT_CITY_AGENCY))
            .andExpect(jsonPath("$.residenceCity").value(DEFAULT_RESIDENCE_CITY))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.socialSecurityNumber").value(DEFAULT_SOCIAL_SECURITY_NUMBER))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.birthPlace").value(DEFAULT_BIRTH_PLACE))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.workstation").value(DEFAULT_WORKSTATION))
            .andExpect(jsonPath("$.descriptionWorkstation").value(DEFAULT_DESCRIPTION_WORKSTATION))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.coefficient").value(DEFAULT_COEFFICIENT.intValue()))
            .andExpect(jsonPath("$.numberHours").value(DEFAULT_NUMBER_HOURS))
            .andExpect(jsonPath("$.averageHourlyCost").value(DEFAULT_AVERAGE_HOURLY_COST))
            .andExpect(jsonPath("$.monthlyGrossAmount").value(DEFAULT_MONTHLY_GROSS_AMOUNT.intValue()))
            .andExpect(jsonPath("$.commissionAmount").value(DEFAULT_COMMISSION_AMOUNT.intValue()))
            .andExpect(jsonPath("$.contractType").value(DEFAULT_CONTRACT_TYPE.toString()))
            .andExpect(jsonPath("$.salaryType").value(DEFAULT_SALARY_TYPE.toString()))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .identityCard(UPDATED_IDENTITY_CARD)
            .dateInspiration(UPDATED_DATE_INSPIRATION)
            .nationality(UPDATED_NATIONALITY)
            .uploadIdentityCard(UPDATED_UPLOAD_IDENTITY_CARD)
            .uploadIdentityCardContentType(UPDATED_UPLOAD_IDENTITY_CARD_CONTENT_TYPE)
            .typeEmployed(UPDATED_TYPE_EMPLOYED)
            .cityAgency(UPDATED_CITY_AGENCY)
            .residenceCity(UPDATED_RESIDENCE_CITY)
            .address(UPDATED_ADDRESS)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .workstation(UPDATED_WORKSTATION)
            .descriptionWorkstation(UPDATED_DESCRIPTION_WORKSTATION)
            .level(UPDATED_LEVEL)
            .coefficient(UPDATED_COEFFICIENT)
            .numberHours(UPDATED_NUMBER_HOURS)
            .averageHourlyCost(UPDATED_AVERAGE_HOURLY_COST)
            .monthlyGrossAmount(UPDATED_MONTHLY_GROSS_AMOUNT)
            .commissionAmount(UPDATED_COMMISSION_AMOUNT)
            .contractType(UPDATED_CONTRACT_TYPE)
            .salaryType(UPDATED_SALARY_TYPE)
            .hireDate(UPDATED_HIRE_DATE);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployee.getIdentityCard()).isEqualTo(UPDATED_IDENTITY_CARD);
        assertThat(testEmployee.getDateInspiration()).isEqualTo(UPDATED_DATE_INSPIRATION);
        assertThat(testEmployee.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testEmployee.getUploadIdentityCard()).isEqualTo(UPDATED_UPLOAD_IDENTITY_CARD);
        assertThat(testEmployee.getUploadIdentityCardContentType()).isEqualTo(UPDATED_UPLOAD_IDENTITY_CARD_CONTENT_TYPE);
        assertThat(testEmployee.getTypeEmployed()).isEqualTo(UPDATED_TYPE_EMPLOYED);
        assertThat(testEmployee.getCityAgency()).isEqualTo(UPDATED_CITY_AGENCY);
        assertThat(testEmployee.getResidenceCity()).isEqualTo(UPDATED_RESIDENCE_CITY);
        assertThat(testEmployee.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployee.getSocialSecurityNumber()).isEqualTo(UPDATED_SOCIAL_SECURITY_NUMBER);
        assertThat(testEmployee.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testEmployee.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testEmployee.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testEmployee.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testEmployee.getWorkstation()).isEqualTo(UPDATED_WORKSTATION);
        assertThat(testEmployee.getDescriptionWorkstation()).isEqualTo(UPDATED_DESCRIPTION_WORKSTATION);
        assertThat(testEmployee.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testEmployee.getCoefficient()).isEqualTo(UPDATED_COEFFICIENT);
        assertThat(testEmployee.getNumberHours()).isEqualTo(UPDATED_NUMBER_HOURS);
        assertThat(testEmployee.getAverageHourlyCost()).isEqualTo(UPDATED_AVERAGE_HOURLY_COST);
        assertThat(testEmployee.getMonthlyGrossAmount()).isEqualTo(UPDATED_MONTHLY_GROSS_AMOUNT);
        assertThat(testEmployee.getCommissionAmount()).isEqualTo(UPDATED_COMMISSION_AMOUNT);
        assertThat(testEmployee.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
        assertThat(testEmployee.getSalaryType()).isEqualTo(UPDATED_SALARY_TYPE);
        assertThat(testEmployee.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(longCount.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(longCount.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(longCount.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .identityCard(UPDATED_IDENTITY_CARD)
            .dateInspiration(UPDATED_DATE_INSPIRATION)
            .nationality(UPDATED_NATIONALITY)
            .uploadIdentityCard(UPDATED_UPLOAD_IDENTITY_CARD)
            .uploadIdentityCardContentType(UPDATED_UPLOAD_IDENTITY_CARD_CONTENT_TYPE)
            .cityAgency(UPDATED_CITY_AGENCY)
            .residenceCity(UPDATED_RESIDENCE_CITY)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .workstation(UPDATED_WORKSTATION)
            .descriptionWorkstation(UPDATED_DESCRIPTION_WORKSTATION)
            .level(UPDATED_LEVEL)
            .numberHours(UPDATED_NUMBER_HOURS)
            .monthlyGrossAmount(UPDATED_MONTHLY_GROSS_AMOUNT)
            .commissionAmount(UPDATED_COMMISSION_AMOUNT)
            .salaryType(UPDATED_SALARY_TYPE)
            .hireDate(UPDATED_HIRE_DATE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployee.getIdentityCard()).isEqualTo(UPDATED_IDENTITY_CARD);
        assertThat(testEmployee.getDateInspiration()).isEqualTo(UPDATED_DATE_INSPIRATION);
        assertThat(testEmployee.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testEmployee.getUploadIdentityCard()).isEqualTo(UPDATED_UPLOAD_IDENTITY_CARD);
        assertThat(testEmployee.getUploadIdentityCardContentType()).isEqualTo(UPDATED_UPLOAD_IDENTITY_CARD_CONTENT_TYPE);
        assertThat(testEmployee.getTypeEmployed()).isEqualTo(DEFAULT_TYPE_EMPLOYED);
        assertThat(testEmployee.getCityAgency()).isEqualTo(UPDATED_CITY_AGENCY);
        assertThat(testEmployee.getResidenceCity()).isEqualTo(UPDATED_RESIDENCE_CITY);
        assertThat(testEmployee.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEmployee.getSocialSecurityNumber()).isEqualTo(DEFAULT_SOCIAL_SECURITY_NUMBER);
        assertThat(testEmployee.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testEmployee.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testEmployee.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testEmployee.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testEmployee.getWorkstation()).isEqualTo(UPDATED_WORKSTATION);
        assertThat(testEmployee.getDescriptionWorkstation()).isEqualTo(UPDATED_DESCRIPTION_WORKSTATION);
        assertThat(testEmployee.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testEmployee.getCoefficient()).isEqualTo(DEFAULT_COEFFICIENT);
        assertThat(testEmployee.getNumberHours()).isEqualTo(UPDATED_NUMBER_HOURS);
        assertThat(testEmployee.getAverageHourlyCost()).isEqualTo(DEFAULT_AVERAGE_HOURLY_COST);
        assertThat(testEmployee.getMonthlyGrossAmount()).isEqualTo(UPDATED_MONTHLY_GROSS_AMOUNT);
        assertThat(testEmployee.getCommissionAmount()).isEqualTo(UPDATED_COMMISSION_AMOUNT);
        assertThat(testEmployee.getContractType()).isEqualTo(DEFAULT_CONTRACT_TYPE);
        assertThat(testEmployee.getSalaryType()).isEqualTo(UPDATED_SALARY_TYPE);
        assertThat(testEmployee.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .identityCard(UPDATED_IDENTITY_CARD)
            .dateInspiration(UPDATED_DATE_INSPIRATION)
            .nationality(UPDATED_NATIONALITY)
            .uploadIdentityCard(UPDATED_UPLOAD_IDENTITY_CARD)
            .uploadIdentityCardContentType(UPDATED_UPLOAD_IDENTITY_CARD_CONTENT_TYPE)
            .typeEmployed(UPDATED_TYPE_EMPLOYED)
            .cityAgency(UPDATED_CITY_AGENCY)
            .residenceCity(UPDATED_RESIDENCE_CITY)
            .address(UPDATED_ADDRESS)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .workstation(UPDATED_WORKSTATION)
            .descriptionWorkstation(UPDATED_DESCRIPTION_WORKSTATION)
            .level(UPDATED_LEVEL)
            .coefficient(UPDATED_COEFFICIENT)
            .numberHours(UPDATED_NUMBER_HOURS)
            .averageHourlyCost(UPDATED_AVERAGE_HOURLY_COST)
            .monthlyGrossAmount(UPDATED_MONTHLY_GROSS_AMOUNT)
            .commissionAmount(UPDATED_COMMISSION_AMOUNT)
            .contractType(UPDATED_CONTRACT_TYPE)
            .salaryType(UPDATED_SALARY_TYPE)
            .hireDate(UPDATED_HIRE_DATE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployee.getIdentityCard()).isEqualTo(UPDATED_IDENTITY_CARD);
        assertThat(testEmployee.getDateInspiration()).isEqualTo(UPDATED_DATE_INSPIRATION);
        assertThat(testEmployee.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testEmployee.getUploadIdentityCard()).isEqualTo(UPDATED_UPLOAD_IDENTITY_CARD);
        assertThat(testEmployee.getUploadIdentityCardContentType()).isEqualTo(UPDATED_UPLOAD_IDENTITY_CARD_CONTENT_TYPE);
        assertThat(testEmployee.getTypeEmployed()).isEqualTo(UPDATED_TYPE_EMPLOYED);
        assertThat(testEmployee.getCityAgency()).isEqualTo(UPDATED_CITY_AGENCY);
        assertThat(testEmployee.getResidenceCity()).isEqualTo(UPDATED_RESIDENCE_CITY);
        assertThat(testEmployee.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployee.getSocialSecurityNumber()).isEqualTo(UPDATED_SOCIAL_SECURITY_NUMBER);
        assertThat(testEmployee.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testEmployee.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testEmployee.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testEmployee.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testEmployee.getWorkstation()).isEqualTo(UPDATED_WORKSTATION);
        assertThat(testEmployee.getDescriptionWorkstation()).isEqualTo(UPDATED_DESCRIPTION_WORKSTATION);
        assertThat(testEmployee.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testEmployee.getCoefficient()).isEqualTo(UPDATED_COEFFICIENT);
        assertThat(testEmployee.getNumberHours()).isEqualTo(UPDATED_NUMBER_HOURS);
        assertThat(testEmployee.getAverageHourlyCost()).isEqualTo(UPDATED_AVERAGE_HOURLY_COST);
        assertThat(testEmployee.getMonthlyGrossAmount()).isEqualTo(UPDATED_MONTHLY_GROSS_AMOUNT);
        assertThat(testEmployee.getCommissionAmount()).isEqualTo(UPDATED_COMMISSION_AMOUNT);
        assertThat(testEmployee.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
        assertThat(testEmployee.getSalaryType()).isEqualTo(UPDATED_SALARY_TYPE);
        assertThat(testEmployee.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(longCount.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(longCount.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(longCount.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
