package com.leaps.entity.flc;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leaps.entity.admin.Company;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlcTransactionDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long companyId;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "companyId", updatable = false, insertable = false)
	private Company company;

	private Long flcPolicyNo;
	private Long flcTransNo;
	private String flcReqDate;
	private String flcLogDate;
	private String uinNumber;
	private Double flcPremRefund; // non ulip policy
	private Double flcTotalPrem; // ulip policy
	private Double flcPolicyDop;
	private Double penalIntrest;
	private Double grossFlcPay;
	private Double medicalFee;
	private Double stamDuty;
	private Double riskPremRecov; // non ulip policy
	private Double mortChargeRefund; // ulip policy
	private Double totalRecov;
	private Double netFlcPay;
	private String effDate; // ulip policy
	private Double fundValue; // ulip policy
	private String flcApprovalDate;
	private String medicalCategory;
	private String medicalCenter;
	private String medicatTpaCode;
	private String makerFlag;
	private String checkerFlag;
	private String leapApprovalFlag;
	private String leapApprovalRemark;
	private String leapApprovalDate;
	private Long approvalQcUserId;
	private String interimStatus;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@CreationTimestamp
	private LocalDateTime createdDate;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long createdBy;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@UpdateTimestamp
	private LocalDateTime modifiedDate;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long modifiedBy;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private int validFlag;
}
