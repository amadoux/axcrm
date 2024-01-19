package com.mycompany.myapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.PaySlip} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaySlipDTO implements Serializable {

    private Long id;

    /**
     * fieldName
     */
    @Schema(description = "fieldName")
    private Long netSalaryPay;

    private Instant paySlipDate;

    @Lob
    private byte[] uploadPaySlip;

    private String uploadPaySlipContentType;
    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNetSalaryPay() {
        return netSalaryPay;
    }

    public void setNetSalaryPay(Long netSalaryPay) {
        this.netSalaryPay = netSalaryPay;
    }

    public Instant getPaySlipDate() {
        return paySlipDate;
    }

    public void setPaySlipDate(Instant paySlipDate) {
        this.paySlipDate = paySlipDate;
    }

    public byte[] getUploadPaySlip() {
        return uploadPaySlip;
    }

    public void setUploadPaySlip(byte[] uploadPaySlip) {
        this.uploadPaySlip = uploadPaySlip;
    }

    public String getUploadPaySlipContentType() {
        return uploadPaySlipContentType;
    }

    public void setUploadPaySlipContentType(String uploadPaySlipContentType) {
        this.uploadPaySlipContentType = uploadPaySlipContentType;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaySlipDTO)) {
            return false;
        }

        PaySlipDTO paySlipDTO = (PaySlipDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paySlipDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaySlipDTO{" +
            "id=" + getId() +
            ", netSalaryPay=" + getNetSalaryPay() +
            ", paySlipDate='" + getPaySlipDate() + "'" +
            ", uploadPaySlip='" + getUploadPaySlip() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
