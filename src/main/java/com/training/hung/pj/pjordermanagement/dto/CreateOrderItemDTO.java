package com.training.hung.pj.pjordermanagement.dto;

import com.training.hung.pj.pjordermanagement.model.OrderItem;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderItemDTO {
    private Long orderId;
    private Long productId;
    private Integer quantity;

}
