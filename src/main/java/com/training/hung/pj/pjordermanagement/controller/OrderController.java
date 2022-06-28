package com.training.hung.pj.pjordermanagement.controller;

import com.training.hung.pj.pjordermanagement.dto.*;
import com.training.hung.pj.pjordermanagement.model.OrderStatus;
import com.training.hung.pj.pjordermanagement.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public Page<QueryOrderDTO> findbyPage(@PathVariable(name = "pageNumber") Integer pageNumber,
                                          @PathVariable(name = "pageSize") Integer pageSize)
    {
        log.info("Query all Order. PageNunber:{}, PageSize: {}", pageNumber, pageSize);
        return orderService.findAll(PageRequest.of(pageNumber,pageSize));
    }

    @GetMapping("/{id}")
    public QueryOrderDTO findById(@PathVariable Long id)
    {
        log.info("Query Order has id: {}", id);
        return orderService.findById(id);
    }

    @PutMapping("/")
    public QueryOrderDTO createOder(@RequestBody CreateOrderDTO createOrderDTO)
    {
        log.info("Create new  order");
        return orderService.createOrder(createOrderDTO);
    }
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id)
    {
        log.info("Delete Order has id: {}", id);
        orderService.removeOrder(id);
        return "Delete success";
    }
    @PostMapping("/addOrderItem")
    public QueryOrderDTO addOrderItem(@RequestBody CreateOrderItemDTO createOrderItemDTO)
    {
        log.info("Add new order item of order id :{}",createOrderItemDTO.getOrderId());
        return orderService.addOrderItem(createOrderItemDTO);
    }
    @PostMapping("/removeOrderItem")
    public QueryOrderDTO removeOrderItem(@RequestBody RemoveOrderItemDTO removeOrderItemDTO)
    {
        log.info("remove order item id: {} of order id :{}",removeOrderItemDTO,removeOrderItemDTO.getOrderId());
        return orderService.removeOrderItem(removeOrderItemDTO);
    }
    @PostMapping("/paid/{id}")
    public QueryOrderDTO updateStatusToPaid(@PathVariable Long id)
    {
        log.info("Update status change to paid, order id :{}",id);
        return orderService.updateStatusOrder(id, OrderStatus.PAID);
    }

    @PostMapping("/cancel/{id}")
    public QueryOrderDTO updateStatusToCancel(@PathVariable Long id)
    {
        log.info("Update status change to cancel, order id :{}",id);
        return orderService.updateStatusOrder(id, OrderStatus.CANCELLED);
    }





}
