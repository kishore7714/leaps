package com.leaps.entity.admin;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long addressId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId", updatable = false, insertable = false)
    private Address address;

    private String companyCode;
    private String companyName;
    private String companyShortName;
    private String companyLongName;
    private String gst;

    private String cin;

    private String cinDate;

    private String tin;

    private String pan;

    @Lob
    private String companyLogo;

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
