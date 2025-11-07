package com.mega.warrantymanagementsystem.model;

import lombok.Data;

@Data
public class EmailDetail {
    String recipient;
    String subject;
    String fullName;
    String url;
    String campaignName;
    private String modelName;      // Tên mẫu xe
    private String startDate;      // Ngày bắt đầu chiến dịch
    private String endDate;        // Ngày kết thúc chiến dịch
}
