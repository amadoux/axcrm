package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.ContractType;
import com.mycompany.myapp.domain.enumeration.StatusContract;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Contrat.
 * Ajouter un contrat
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * fieldName
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type")
    private ContractType contractType;

    @Column(name = "entry_date")
    private Instant entryDate;

    @Column(name = "release_date")
    private Instant releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_contract")
    private StatusContract statusContract;

    @Lob
    @Column(name = "upload_contract", nullable = false)
    private byte[] uploadContract;

    @NotNull
    @Column(name = "upload_contract_content_type", nullable = false)
    private String uploadContractContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "enterprise", "managerEmployee", "managers" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContractType getContractType() {
        return this.contractType;
    }

    public Contract contractType(ContractType contractType) {
        this.setContractType(contractType);
        return this;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public Instant getEntryDate() {
        return this.entryDate;
    }

    public Contract entryDate(Instant entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public Instant getReleaseDate() {
        return this.releaseDate;
    }

    public Contract releaseDate(Instant releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public StatusContract getStatusContract() {
        return this.statusContract;
    }

    public Contract statusContract(StatusContract statusContract) {
        this.setStatusContract(statusContract);
        return this;
    }

    public void setStatusContract(StatusContract statusContract) {
        this.statusContract = statusContract;
    }

    public byte[] getUploadContract() {
        return this.uploadContract;
    }

    public Contract uploadContract(byte[] uploadContract) {
        this.setUploadContract(uploadContract);
        return this;
    }

    public void setUploadContract(byte[] uploadContract) {
        this.uploadContract = uploadContract;
    }

    public String getUploadContractContentType() {
        return this.uploadContractContentType;
    }

    public Contract uploadContractContentType(String uploadContractContentType) {
        this.uploadContractContentType = uploadContractContentType;
        return this;
    }

    public void setUploadContractContentType(String uploadContractContentType) {
        this.uploadContractContentType = uploadContractContentType;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Contract employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return getId() != null && getId().equals(((Contract) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", contractType='" + getContractType() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", statusContract='" + getStatusContract() + "'" +
            ", uploadContract='" + getUploadContract() + "'" +
            ", uploadContractContentType='" + getUploadContractContentType() + "'" +
            "}";
    }
}
