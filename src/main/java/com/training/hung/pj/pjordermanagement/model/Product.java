package com.training.hung.pj.pjordermanagement.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@Table(name="tbl_product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false, length = 100)
    private String name;

    @Column(name="price", nullable = false)
    private Double price;

    @Column(name="avaiable", nullable = false)
    private Long avaiable;

}
