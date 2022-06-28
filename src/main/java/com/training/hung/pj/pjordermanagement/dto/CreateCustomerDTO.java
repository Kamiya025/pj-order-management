package com.training.hung.pj.pjordermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerDTO {
    private String name;
    private String mobile;
    private String address;
}
