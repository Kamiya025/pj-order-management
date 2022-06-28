package com.training.hung.pj.pjordermanagement.dto;

import com.training.hung.pj.pjordermanagement.model.Order;
import com.training.hung.pj.pjordermanagement.model.OrderItem;
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
public class QueryOrderDTO {

    private Long id;
    private LocalDateTime orderDateTime;
    private Long customerId;
    private List<QueryOrderItemDTO> orderItems;
    private Double totalAmount;
    private OrderStatus status;

    public static class Mapper
    {
        public static QueryOrderDTO fromEntity(Order order)
        {
            return QueryOrderDTO.builder().id(order.getId())
                    .orderDateTime(order.getOrderDateTime())
                    .orderItems(order.getOrderItems().stream()
                                        .map(QueryOrderItemDTO.Mapper::fromEntity)
                                        .toList())
                    .customerId(order.getCustomer().getId())
                    .totalAmount(order.getTotalAmount())
                    .status(order.getStatus())
                    .build();
        }
    }
}
