package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Pays;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entreprise
 */
@Entity
@Table(name = "enterprise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enterprise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * fieldName
     */
    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotNull
    @Column(name = "business_register_number", nullable = false)
    private String businessRegisterNumber;

    @NotNull
    @Column(name = "unique_identification_number", nullable = false)
    private String uniqueIdentificationNumber;

    @Column(name = "business_domicile")
    private String businessDomicile;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "business_phone", nullable = false)
    private String businessPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Pays country;

    @Column(name = "city")
    private String city;

    @Lob
    @Column(name = "business_logo")
    private byte[] businessLogo;

    @Column(name = "business_logo_content_type")
    private String businessLogoContentType;

    @Lob
    @Column(name = "map_locator")
    private byte[] mapLocator;

    @Column(name = "map_locator_content_type")
    private String mapLocatorContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "managers", "enterprises", "employee" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enterprise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Enterprise companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessRegisterNumber() {
        return this.businessRegisterNumber;
    }

    public Enterprise businessRegisterNumber(String businessRegisterNumber) {
        this.setBusinessRegisterNumber(businessRegisterNumber);
        return this;
    }

    public void setBusinessRegisterNumber(String businessRegisterNumber) {
        this.businessRegisterNumber = businessRegisterNumber;
    }

    public String getUniqueIdentificationNumber() {
        return this.uniqueIdentificationNumber;
    }

    public Enterprise uniqueIdentificationNumber(String uniqueIdentificationNumber) {
        this.setUniqueIdentificationNumber(uniqueIdentificationNumber);
        return this;
    }

    public void setUniqueIdentificationNumber(String uniqueIdentificationNumber) {
        this.uniqueIdentificationNumber = uniqueIdentificationNumber;
    }

    public String getBusinessDomicile() {
        return this.businessDomicile;
    }

    public Enterprise businessDomicile(String businessDomicile) {
        this.setBusinessDomicile(businessDomicile);
        return this;
    }

    public void setBusinessDomicile(String businessDomicile) {
        this.businessDomicile = businessDomicile;
    }

    public String getEmail() {
        return this.email;
    }

    public Enterprise email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinessPhone() {
        return this.businessPhone;
    }

    public Enterprise businessPhone(String businessPhone) {
        this.setBusinessPhone(businessPhone);
        return this;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public Pays getCountry() {
        return this.country;
    }

    public Enterprise country(Pays country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Pays country) {
        this.country = country;
    }

    public String getCity() {
        return this.city;
    }

    public Enterprise city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public byte[] getBusinessLogo() {
        return this.businessLogo;
    }

    public Enterprise businessLogo(byte[] businessLogo) {
        this.setBusinessLogo(businessLogo);
        return this;
    }

    public void setBusinessLogo(byte[] businessLogo) {
        this.businessLogo = businessLogo;
    }

    public String getBusinessLogoContentType() {
        return this.businessLogoContentType;
    }

    public Enterprise businessLogoContentType(String businessLogoContentType) {
        this.businessLogoContentType = businessLogoContentType;
        return this;
    }

    public void setBusinessLogoContentType(String businessLogoContentType) {
        this.businessLogoContentType = businessLogoContentType;
    }

    public byte[] getMapLocator() {
        return this.mapLocator;
    }

    public Enterprise mapLocator(byte[] mapLocator) {
        this.setMapLocator(mapLocator);
        return this;
    }

    public void setMapLocator(byte[] mapLocator) {
        this.mapLocator = mapLocator;
    }

    public String getMapLocatorContentType() {
        return this.mapLocatorContentType;
    }

    public Enterprise mapLocatorContentType(String mapLocatorContentType) {
        this.mapLocatorContentType = mapLocatorContentType;
        return this;
    }

    public void setMapLocatorContentType(String mapLocatorContentType) {
        this.mapLocatorContentType = mapLocatorContentType;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Enterprise employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enterprise)) {
            return false;
        }
        return getId() != null && getId().equals(((Enterprise) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enterprise{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", businessRegisterNumber='" + getBusinessRegisterNumber() + "'" +
            ", uniqueIdentificationNumber='" + getUniqueIdentificationNumber() + "'" +
            ", businessDomicile='" + getBusinessDomicile() + "'" +
            ", email='" + getEmail() + "'" +
            ", businessPhone='" + getBusinessPhone() + "'" +
            ", country='" + getCountry() + "'" +
            ", city='" + getCity() + "'" +
            ", businessLogo='" + getBusinessLogo() + "'" +
            ", businessLogoContentType='" + getBusinessLogoContentType() + "'" +
            ", mapLocator='" + getMapLocator() + "'" +
            ", mapLocatorContentType='" + getMapLocatorContentType() + "'" +
            "}";
    }
}
