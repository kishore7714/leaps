package com.leaps.entity.admin;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private Long companyId;
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "companyId", updatable = false, insertable = false)
//    private Company company;
    private String userGroupName;

    @CreationTimestamp
    private LocalDateTime createdDate;
    private Long createdBy;

    @UpdateTimestamp
    private LocalDateTime modifiedDate;
    private Long modifiedBy;

    private int validFlag;

}
