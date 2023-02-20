package com.codestates.order.service;

import com.codestates.coffee.service.CoffeeService;
import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.service.MemberService;
import com.codestates.order.entity.Order;
import com.codestates.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    final private OrderRepository orderRepository;
    final private MemberService memberService;
    final private CoffeeService coffeeService;

    public OrderService(OrderRepository orderRepository,
                        MemberService memberService,
                        CoffeeService coffeeService) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.coffeeService = coffeeService;
    }

    public Order createOrder(Order order) {
        memberService.findVerifiedMember(order.getMemberId().getId()); // 회원이 존재하는지 확인

        order.getOrderCoffees()
                .stream()
                .forEach(coffeeRef -> {
                    coffeeService.findVerifiedCoffee(coffeeRef.getCoffeeId()); // 커피가 존재하는지 조회
                });
        return orderRepository.save(order);
    }

    public Order findOrder(long orderId) {
        return findVerifiedOrder(orderId);
    }

    public List<Order> findOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    public void cancelOrder(long orderId) { // 주문취소 메서드
        Order findOrder = findVerifiedOrder(orderId);
        int step = findOrder.getOrderStatus().getStepNumber();

        if (step >= 2) { // OrderStatus의 step이 2미만일 경우 즉, ORDER_CONFIRM인 경우에만 취소 가능하게 한 것
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ORDER);
        }

        findOrder.setOrderStatus(Order.OrderStatus.ORDER_CANCEL); // 주문 취소
        orderRepository.save(findOrder);
    }

    private Order findVerifiedOrder(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order findOrder =
                optionalOrder.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
        return findOrder;
    }
}
