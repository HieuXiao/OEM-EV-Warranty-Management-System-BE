package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class WarrantyClaimResponse {
    private String claimId;
    private LocalDate claimDate;
    private String status;
    private String description;
    private String evmDescription;
    private boolean technicianDone;
    private boolean scStaffDone;
    private Boolean isRepair;

    // Chỉ giữ VIN thay vì toàn bộ vehicle
    private String vin;

    // Chỉ giữ ID thay vì toàn bộ account
    private String serviceCenterStaffId;
    private String serviceCenterTechnicianId;
    private String evmId;

    // Danh sách partNumber thay vì toàn bộ ClaimPartCheckResponse
    private List<String> partNumbers;

    // Danh sách fileId thay vì toàn bộ WarrantyFileResponse (nếu bạn muốn)
    private List<String> fileIds;

    // ID các campaign gắn với claim (nếu có)
    private List<Integer> campaignIds;

    private List<String> timeline;

}
