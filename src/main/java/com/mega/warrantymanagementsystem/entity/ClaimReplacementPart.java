package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity//đánh dấu là 1 thực thể
@Data//tự động sinh getter setter
@Table(name = "claim_replacement_parts") //tên bảng trong DB
@AllArgsConstructor//tự động sinh constructor có tham số
@NoArgsConstructor//tự động sinh constructor không tham số
public class ClaimReplacementPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_user_id", nullable = false, unique = true)
    private int partUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id")
    @JsonIgnore
    private WarrantyClaim warrantyClaim;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false  )
    @JsonIgnore
    private Part part;

    @Column(name = "quantity")
    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

}
