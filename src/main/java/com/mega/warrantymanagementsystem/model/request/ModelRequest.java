package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ModelRequest {
    @NotEmpty(message = "Model name cannot be empty")
    private String modelName;
}