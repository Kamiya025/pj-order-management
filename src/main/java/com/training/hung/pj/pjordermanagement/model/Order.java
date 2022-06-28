package com.training.hung.pj.pjordermanagement.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@Table(name="tbl_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="order_datetime")
    private LocalDateTime orderDateTime;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @Column(name="total_amount")
    private Double totalAmount;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        this.setTotalAmount(orderItems.stream().mapToDouble(orderI -> orderI.getAmount()).sum());
    }
    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        this.setTotalAmount(orderItems.stream().mapToDouble(orderI -> orderI.getAmount()).sum());

    }
}
