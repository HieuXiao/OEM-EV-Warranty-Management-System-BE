package com.mega.warrantymanagementsystem.entity;

<<<<<<< HEAD

import com.fasterxml.jackson.annotation.JsonIgnore;

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

<<<<<<< HEAD

import java.util.ArrayList;
import java.util.List;

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
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
<<<<<<< HEAD

    @JsonIgnore
    private Inventory inventoryId;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ClaimReplacementPart> claimReplacementParts = new ArrayList<>();

    private Inventory inventoryId;

=======
    private Inventory inventoryId;
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
}
