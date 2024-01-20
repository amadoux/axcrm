package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.SPentType;
import com.mycompany.myapp.domain.enumeration.StatusCharges;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Ajouter les charges sociales
 */
@Entity
@Table(name = "social_charges")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialCharges implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "spent_date")
    private Instant spentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "spent_type")
    private SPentType spentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_charges")
    private StatusCharges statusCharges;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    @Lob
    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "enterprise", "employee", "managers" }, allowSetters = true)
    private Employee responsableDepense;

    @ManyToOne(fetch = FetchType.LAZY)
    private Enterprise enterprise;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SocialCharges id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSpentDate() {
        return this.spentDate;
    }

    public SocialCharges spentDate(Instant spentDate) {
        this.setSpentDate(spentDate);
        return this;
    }

    public void setSpentDate(Instant spentDate) {
        this.spentDate = spentDate;
    }

    public SPentType getSpentType() {
        return this.spentType;
    }

    public SocialCharges spentType(SPentType spentType) {
        this.setSpentType(spentType);
        return this;
    }

    public void setSpentType(SPentType spentType) {
        this.spentType = spentType;
    }

    public StatusCharges getStatusCharges() {
        return this.statusCharges;
    }

    public SocialCharges statusCharges(StatusCharges statusCharges) {
        this.setStatusCharges(statusCharges);
        return this;
    }

    public void setStatusCharges(StatusCharges statusCharges) {
        this.statusCharges = statusCharges;
    }

    public Long getAmount() {
        return this.amount;
    }

    public SocialCharges amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCommentText() {
        return this.commentText;
    }

    public SocialCharges commentText(String commentText) {
        this.setCommentText(commentText);
        return this;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Employee getResponsableDepense() {
        return this.responsableDepense;
    }

    public void setResponsableDepense(Employee employee) {
        this.responsableDepense = employee;
    }

    public SocialCharges responsableDepense(Employee employee) {
        this.setResponsableDepense(employee);
        return this;
    }

    public Enterprise getEnterprise() {
        return this.enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public SocialCharges enterprise(Enterprise enterprise) {
        this.setEnterprise(enterprise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialCharges)) {
            return false;
        }
        return getId() != null && getId().equals(((SocialCharges) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialCharges{" +
            "id=" + getId() +
            ", spentDate='" + getSpentDate() + "'" +
            ", spentType='" + getSpentType() + "'" +
            ", statusCharges='" + getStatusCharges() + "'" +
            ", amount=" + getAmount() +
            ", commentText='" + getCommentText() + "'" +
            "}";
    }
}
