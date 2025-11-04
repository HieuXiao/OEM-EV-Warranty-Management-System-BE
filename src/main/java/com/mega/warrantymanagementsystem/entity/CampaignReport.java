package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "campaign_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    // Cho phép lưu nhiều file PDF cùng lúc (URL Cloudinary hoặc link lưu trữ)
    @ElementCollection
    @CollectionTable(
            name = "campaign_report_files",
            joinColumns = @JoinColumn(name = "report_id")
    )
    @Column(name = "file_url")
    private List<String> reportFileUrls;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    // ========== Liên kết đến Campaign ==========
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    @JsonIgnore
    private Campaign campaign;

    // ========== Liên kết đến ServiceCenter ==========
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_center_id", nullable = false)
    @JsonIgnore
    private ServiceCenter serviceCenter;

    // ========== Liên kết đến các Account (submittedBy) ==========
    // Một report có thể được gửi bởi nhiều account (ví dụ: đồng ký tên)
    @ManyToMany
    @JoinTable(
            name = "campaign_report_submitters",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> submittedBy;

}
