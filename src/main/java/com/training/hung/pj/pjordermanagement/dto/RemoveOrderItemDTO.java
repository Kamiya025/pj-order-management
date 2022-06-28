package com.training.hung.pj.pjordermanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveOrderItemDTO {
    private Long orderItemId;
    private Long orderId;

}
