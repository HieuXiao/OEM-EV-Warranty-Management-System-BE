package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity//đánh dấu là 1 thực thể
@Data//tự động sinh getter setter
@Table(name = "claim_attachments") //tên bảng trong DB
@AllArgsConstructor//tự động sinh constructor có tham số
@NoArgsConstructor//tự động sinh constructor không tham số
public class ClaimAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id", nullable = false, unique = true)
    private int attachmentId;

    @Column(name = "file_type", nullable = false, length = 50)
    @NotEmpty(message = "file_type cannot be empty")
    private String fileType;

    @Column(name = "file_path", nullable = false, length = 50)
    @NotEmpty(message = "file_path cannot be empty")
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", referencedColumnName = "claim_id")
    @JsonIgnore
    private WarrantyClaim warrantyClaim;


}
