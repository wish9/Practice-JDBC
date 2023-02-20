package com.codestates.order.controller;

import com.codestates.coffee.entity.Coffee;
import com.codestates.coffee.service.CoffeeService;
import com.codestates.order.entity.Order;
import com.codestates.order.service.OrderService;
import com.codestates.order.dto.OrderPostDto;
import com.codestates.order.dto.OrderResponseDto;
import com.codestates.order.mapper.OrderMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v10/orders")
@Validated
public class OrderController {
    private final static String ORDER_DEFAULT_URL = "/v10/orders"; // Default URL 경로 설정
    private final OrderService orderService;
    private final OrderMapper mapper;
    private final CoffeeService coffeeService;

    public OrderController(OrderService orderService,
                           OrderMapper mapper,
                           CoffeeService coffeeService) {
        this.orderService = orderService;
        this.mapper = mapper;
        this.coffeeService = coffeeService;
    }

    @PostMapping
    public ResponseEntity postOrder(@Valid @RequestBody OrderPostDto orderPostDto) {
        Order order =
                orderService.createOrder(mapper.orderPostDtoToOrder(orderPostDto));

        URI location = //  등록된 주문(Resource)에 해당하는 URI 객체
                UriComponentsBuilder
                        .newInstance()
                        .path(ORDER_DEFAULT_URL + "/{order-id}")
                        .buildAndExpand(order.getOrderId())
                        .toUri();

        return ResponseEntity.created(location).build(); // response header에 리소스의 위치정보 추가해서 응답
        // .created() 메서드가 `201 Created` HTTP Status를 추가해줌
    }

    @GetMapping("/{order-id}")
    public ResponseEntity getOrder(@PathVariable("order-id") @Positive long orderId) {
        Order order = orderService.findOrder(orderId);
        return new ResponseEntity<>(mapper.orderToOrderResponseDto(coffeeService, order),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getOrders() {
        List<Order> orders = orderService.findOrders();

        List<OrderResponseDto> response =
                orders
                    .stream()
                    .map(order -> mapper.orderToOrderResponseDto(coffeeService, order))
                    .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity cancelOrder(@PathVariable("order-id") @Positive long orderId) {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
