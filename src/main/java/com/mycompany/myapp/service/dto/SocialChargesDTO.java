package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.SPentType;
import com.mycompany.myapp.domain.enumeration.StatusCharges;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.SocialCharges} entity.
 */
@Schema(description = "Ajouter les charges sociales")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialChargesDTO implements Serializable {

    private Long id;

    private Instant spentDate;

    private SPentType spentType;

    private StatusCharges statusCharges;

    @NotNull
    private Long amount;

    private String purchaseManager;

    @Lob
    private String commentText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSpentDate() {
        return spentDate;
    }

    public void setSpentDate(Instant spentDate) {
        this.spentDate = spentDate;
    }

    public SPentType getSpentType() {
        return spentType;
    }

    public void setSpentType(SPentType spentType) {
        this.spentType = spentType;
    }

    public StatusCharges getStatusCharges() {
        return statusCharges;
    }

    public void setStatusCharges(StatusCharges statusCharges) {
        this.statusCharges = statusCharges;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getPurchaseManager() {
        return purchaseManager;
    }

    public void setPurchaseManager(String purchaseManager) {
        this.purchaseManager = purchaseManager;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialChargesDTO)) {
            return false;
        }

        SocialChargesDTO socialChargesDTO = (SocialChargesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, socialChargesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialChargesDTO{" +
            "id=" + getId() +
            ", spentDate='" + getSpentDate() + "'" +
            ", spentType='" + getSpentType() + "'" +
            ", statusCharges='" + getStatusCharges() + "'" +
            ", amount=" + getAmount() +
            ", purchaseManager='" + getPurchaseManager() + "'" +
            ", commentText='" + getCommentText() + "'" +
            "}";
    }
}
