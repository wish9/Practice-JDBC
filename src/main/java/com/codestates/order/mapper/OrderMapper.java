package com.codestates.order.mapper;

import com.codestates.coffee.dto.CoffeeResponseDto;
import com.codestates.coffee.entity.Coffee;
import com.codestates.coffee.service.CoffeeService;
import com.codestates.order.dto.OrderCoffeeResponseDto;
import com.codestates.order.entity.CoffeeRef;
import com.codestates.order.entity.Order;
import com.codestates.order.dto.OrderPostDto;
import com.codestates.order.dto.OrderResponseDto;
import org.mapstruct.Mapper;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) {
        Order order = new Order();
        order.setMemberId(new AggregateReference.IdOnlyAggregateReference(
                                                        orderPostDto.getMemberId()));
        Set<CoffeeRef> orderCoffees = orderPostDto.getOrderCoffees()
                .stream()
                .map(orderCoffeeDto -> CoffeeRef.builder()
                        .coffeeId(orderCoffeeDto.getCoffeeId())
                        .quantity(orderCoffeeDto.getQuantity())
                        .build())
                .collect(Collectors.toSet());

        order.setOrderCoffees(orderCoffees);
        return order;
    }

    default OrderResponseDto orderToOrderResponseDto(CoffeeService coffeeService, Order order) {

        List<OrderCoffeeResponseDto> orderCoffees = orderToOrderCoffeeResponseDto(coffeeService, order.getOrderCoffees());

        long memberId = order.getMemberId().getId();

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderCoffees(orderCoffees);
        orderResponseDto.setMemberId(memberId);
        orderResponseDto.setCreatedAt(order.getCreatedAt());
        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setOrderStatus(order.getOrderStatus());


        return orderResponseDto;
    }

    default List<OrderCoffeeResponseDto> orderToOrderCoffeeResponseDto(
            CoffeeService coffeeService,
            Set<CoffeeRef> orderCoffees) {
        return orderCoffees.stream()
                .map(coffeeRef -> {
                    Coffee coffee = coffeeService.findCoffee(coffeeRef.getCoffeeId());

                    return new OrderCoffeeResponseDto(coffee.getCoffeeId(),
                            coffee.getKorName(),
                            coffee.getEngName(),
                            coffee.getPrice(),
                            coffeeRef.getQuantity());
                }).collect(Collectors.toList());
    }
}
