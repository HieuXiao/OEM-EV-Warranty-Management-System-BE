package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecordResponse {

    private int recordId;

    // Thông tin liên kết, hiển thị ở mức ID hoặc tên ngắn gọn
    private String vin; // Vehicle
    private int serviceAppointmentId;
    private int campaignId;
    private int serviceCenterId;

    private String result;
    private String status;
    private LocalDate serviceDate;
    private String description;

}
