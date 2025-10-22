package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor  // để tiện tạo object
public class InventoryRequest {

    //------------------Địa điểm lưu kho------------------------
    @NotEmpty(message = "Location cannot be empty!") // không được để trống
    private String location;
}
