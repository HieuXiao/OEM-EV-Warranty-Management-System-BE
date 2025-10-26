package com.mega.warrantymanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "evm_assignment_state")
@Data
public class EvmAssignmentState {
    @Id
    private String id = "EVM_ASSIGNMENT_TRACKER";

    @Column(name = "last_index")
    private int lastIndex;
}
