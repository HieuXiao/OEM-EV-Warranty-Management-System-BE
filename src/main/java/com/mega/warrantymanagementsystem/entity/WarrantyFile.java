package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warranty_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyFile {

    @Id
    @Column(name = "file_id", length = 100)
    @NotEmpty(message = "File ID cannot be empty")
    private String fileId;

    // Liên kết đến bảng warranty_claim
    @ManyToOne
    @JoinColumn(name = "claim_id", nullable = false)
    @JsonIgnore
    private WarrantyClaim warrantyClaim;

    // Lưu danh sách các URL ảnh,video
    @ElementCollection
    @CollectionTable(name = "file_media")
    @Column(name = "media_url")
    private List<String> mediaUrls = new ArrayList<>();

}
