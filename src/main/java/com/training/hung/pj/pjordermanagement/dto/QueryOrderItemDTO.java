package com.training.hung.pj.pjordermanagement.dto;

import com.training.hung.pj.pjordermanagement.model.OrderItem;
import com.training.hung.pj.pjordermanagement.model.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class QueryOrderItemDTO {
    private Long orderItemid;
    private Long orderId;
    private Product product;
    private Integer quanTity;

    public static class Mapper{
        public static QueryOrderItemDTO fromEntity(OrderItem orderItem) {
            return QueryOrderItemDTO.builder()
                    .orderItemid(orderItem.getId())
                    .orderId(orderItem.getOrder().getId())
                    .quanTity(orderItem.getQuantity())
                    .product(orderItem.getProduct())
                    .build();
        }
    }
}
