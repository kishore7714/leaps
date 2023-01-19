package com.leaps.entity.surrender;

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
public class SurrenderLeapDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private  Long companyId;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "companyId",updatable = false,insertable = false)
    private Company company;
    
    private  Long policyNo;
    private Long transNo;
    private String uinNumber;
    private String reqDate;
    private String logDate;
    private Double noOfDues;
    private Double totalPremium;
    private Double valueOfBonus;
    private Double cvbFactor;
    private Double gsvFactor;
    private Double gsvGross;
    private Double sbPaid;
    private Double gsvNet;
    private Double paidUpValue;
    private Double reversionaryBonus;
    private Double interimBonus;
    private Double guaranteedBonus;
    private Double terminalBonus;
    private Double ssvGrossAmount;
    private Double ssvFactor;
    private Double ssvNet;
    private String ssvOrGsv;
    private Double fundValue;
    private Double effDate;
    private Double policyDeposit;
    private Double penalInterest;
    private Double grossPay;
    private Double cdaCharge;
    private Double ulipSurrenderCharge;	
    private Double totalRecovery;
    private Double tds;
    private Double netPayable;
    private Double policyLoan;
    private Double loanInterest;
    private Double cashValueBonus;
    private String makerFlag;
    private String checkerFlag;
    private String pfFlag;
    private String pfRemarks;
    private String leapApprovalFlag;
    private String leapApprovalRemarks;
    private String leapApprovalDate;
    private Long approvalQcUserId;
    
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











