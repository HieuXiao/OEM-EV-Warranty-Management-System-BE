package com.mega.warrantymanagementsystem.model.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCenterRequest {

//    private int centerId;

    @NotEmpty(message = "centerName cannot be empty")
    private String centerName;

    @NotEmpty(message = "location cannot be empty")
    private String location;
}
