package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

<<<<<<< HEAD

import java.util.List;


=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
@Entity// đánh dấu đây là 1 thực thể (entity)
@Table(name = "vehicles")// tên bảng trong DB
@Data// tự động sinh getter setter
@NoArgsConstructor// tự động sinh constructor không tham số
@AllArgsConstructor// tự động sinh constructor có tham số
public class Vehicle {

    @Id
    @Pattern(
            regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN valid"
    )//- cho phép các chữ cái từ A đến Z, loại trừ I, O, Q. 17 ký tự
    @Column(name = "vin", nullable = false, length = 17)
    @NotEmpty(message = "vin can not be empty")
    private String vin;

<<<<<<< HEAD

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
    @Column(name = "campaign_id", nullable = false)
    @NotNull(message = "Campaign Id cannot be empty!")
    private int campaignId;

<<<<<<< HEAD

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
    @Column(name = "year", nullable = false)
    @NotNull(message = "Year cannot be empty!")
    private int year;

    @Column(name = "color", nullable = false, length = 30)
    @NotEmpty(message = "Color cannot be empty!")
    private String color;

    @Column(name = "model", nullable = false, length = 50)
    @NotEmpty(message = "Model cannot be empty!")
    private String model;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD

    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id", nullable = true)
    @JsonIgnore
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

<<<<<<< HEAD

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceRecord> serviceRecords;


=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
}
