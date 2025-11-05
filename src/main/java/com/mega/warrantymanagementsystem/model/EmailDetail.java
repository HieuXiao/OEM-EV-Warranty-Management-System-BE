package com.mega.warrantymanagementsystem.model;

import lombok.Data;

@Data
public class EmailDetail {
    String recipient;
    String subject;
    String fullName;
    String url;
}
