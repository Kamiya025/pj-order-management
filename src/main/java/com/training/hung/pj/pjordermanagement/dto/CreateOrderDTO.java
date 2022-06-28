package com.training.hung.pj.pjordermanagement.dto;

import com.training.hung.pj.pjordermanagement.model.Order;
import com.training.hung.pj.pjordermanagement.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderDTO {
    private Long customerId;
    private List<CreateOrderItemDTO> orderItems;
}
