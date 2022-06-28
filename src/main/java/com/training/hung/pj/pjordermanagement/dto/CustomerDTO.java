package com.training.hung.pj.pjordermanagement.dto;

import com.training.hung.pj.pjordermanagement.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {

    private Long id;
    private String name;
    private String mobile;
    private String address;

    public static class Mapper
    {
        public static CustomerDTO fromEntity(Customer customer)
        {
            return CustomerDTO.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .mobile(customer.getMobile())
                    .address(customer.getAddress())
                    .build();
        }
    }
}
