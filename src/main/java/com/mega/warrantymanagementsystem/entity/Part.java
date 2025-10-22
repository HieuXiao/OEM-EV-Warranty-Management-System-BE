package com.mega.warrantymanagementsystem.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "part")
@AllArgsConstructor
@NoArgsConstructor
public class Part {

    //------------------Id------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_id")
    private int partId;

    //------------------Tên part------------------------
    @Column(name = "name", nullable = false, length = 100)
    @NotEmpty(message = "Part name cannot be empty!")
    private String name;

    //------------------Số serial------------------------
    @Column(name = "serial_number", length = 50, unique = true)
    @NotEmpty(message = "Serial number cannot be empty!")
    private String serialNumber;

    //------------------Mô tả------------------------
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    //------------------Liên kết inventory------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)

    @JsonIgnore
    private Inventory inventoryId;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ClaimReplacementPart> claimReplacementParts = new ArrayList<>();

    private Inventory inventoryId;

}
