package com.mega.warrantymanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "whId")
    private int whId;

    @Column(name = "name", length = 100, nullable = false)
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Column(name = "location", length = 100, nullable = false)
    @NotEmpty(message = "Location cannot be empty")
    private String location;

    // Lưu danh sách low_part (các part số lượng thấp)
    @ElementCollection
    @CollectionTable(name = "warehouse_low_part", joinColumns = @JoinColumn(name = "whId"))
    @Column(name = "part_name")
    private List<String> lowPart;

}
