package com.training.hung.pj.pjordermanagement.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@Table(name="tbl_customer")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name",nullable = false, length = 100)
    private String name;

    @Column(name="mobile",nullable = false, length = 10)
    private String mobile;

    @Column(name="address",nullable = false, length = 100)
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Order> order;

}
