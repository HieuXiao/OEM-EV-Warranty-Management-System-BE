package com.mega.warrantymanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "policy")
@AllArgsConstructor
@NoArgsConstructor
public class Policy {

    //------------------ID------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private int policyId;

    //------------------Kilometer------------------------
    @Column(name = "kilometer", nullable = false)
    @NotNull(message = "Kilometer cannot be null")
    private int kilometer;

    //------------------Policy Part------------------------
    @Column(name = "policy_part", length = 10, nullable = false)
    @Size(max = 10, message = "Policy part must be less than or equal to 10 characters")
    private String policyPart;

    //------------------Policy Model------------------------
    @Column(name = "policy_model", length = 50, nullable = false)
    @Size(max = 50, message = "Policy model must be less than or equal to 50 characters")
    private String policyModel;

    //------------------Policy Year------------------------
    @Column(name = "policy_year", nullable = false)
    private int policyYear;

    //------------------(Bỏ FK employee_id vì chưa có bảng employee)------------------------
    // Khi có bảng Employee, thêm lại:
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "employee_id", nullable = false)
    // private Employee employee;
}
