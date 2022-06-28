package com.training.hung.pj.pjordermanagement.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@Table(name="tbl_order_item")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="order_id", nullable = false)
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="amount")
    private Double amount;


}
