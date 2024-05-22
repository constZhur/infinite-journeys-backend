package ru.mirea.infinitejourneysbackend.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.infinitejourneysbackend.domain.dto.order.BuyOrderRequest;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.domain.model.Order;
import ru.mirea.infinitejourneysbackend.domain.model.Tour;
import ru.mirea.infinitejourneysbackend.exception.tour.TourNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.user.InsufficientBalanceProblem;
import ru.mirea.infinitejourneysbackend.repository.OrderRepository;
import ru.mirea.infinitejourneysbackend.repository.TourRepository;
import ru.mirea.infinitejourneysbackend.service.UserService;
import ru.mirea.infinitejourneysbackend.service.OrderService;
import ru.mirea.infinitejourneysbackend.service.email.EmailService;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @Mock
    private TourRepository tourRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testGetOrders() {
        User user = mock(User.class);
        Order order = mock(Order.class);
        when(userService.getCurrentUser()).thenReturn(user);
        when(user.getOrders()).thenReturn(Collections.singletonList(order));

        var orders = orderService.getOrders();

        assertThat(orders).hasSize(1);
        assertThat(orders).contains(order);
        verify(userService, times(1)).getCurrentUser();
        verify(user, times(1)).getOrders();
    }

    @Test
    @Transactional
    void testCreateOrderSuccess() {
        User buyer = mock(User.class);
        Tour tour = mock(Tour.class);
        User seller = mock(User.class);
        double price = 100.0;

        when(userService.getCurrentUser()).thenReturn(buyer);
        when(tourRepository.findById(anyLong())).thenReturn(Optional.of(tour));
        when(tour.getPrice()).thenReturn(price);
        when(tour.getSeller()).thenReturn(seller);
        when(buyer.getBalance()).thenReturn(price + 50);

        BuyOrderRequest request = mock(BuyOrderRequest.class);
        when(request.tourId()).thenReturn(1L);

        Order order = new Order();
        order.setBuyer(buyer);
        order.setTour(tour);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        var createdOrder = orderService.createOrder(request);

        verify(buyer, times(1)).getBalance();
        verify(buyer).setBalance(price + 50 - price);
        verify(seller).setBalance(price);
        verify(orderRepository).save(any(Order.class));
        verify(emailService).sendEmail(any(Order.class));

        assertThat(createdOrder.getBuyer()).isEqualTo(buyer);
        assertThat(createdOrder.getTour()).isEqualTo(tour);
    }

    @Test
    void testCreateOrderInsufficientBalance() {
        User buyer = mock(User.class);
        Tour tour = mock(Tour.class);
        double price = 100.0;

        when(userService.getCurrentUser()).thenReturn(buyer);
        when(tourRepository.findById(anyLong())).thenReturn(Optional.of(tour));
        when(tour.getPrice()).thenReturn(price);
        when(buyer.getBalance()).thenReturn(price - 50);

        BuyOrderRequest request = mock(BuyOrderRequest.class);
        when(request.tourId()).thenReturn(1L);

        assertThrows(InsufficientBalanceProblem.class, () -> orderService.createOrder(request));

        verify(buyer, never()).setBalance(anyDouble());
        verify(orderRepository, never()).save(any(Order.class));
        verify(emailService, never()).sendEmail(any(Order.class));
    }

    @Test
    void testCreateOrderTourNotFound() {
        when(userService.getCurrentUser()).thenReturn(mock(User.class));
        when(tourRepository.findById(anyLong())).thenReturn(Optional.empty());

        BuyOrderRequest request = mock(BuyOrderRequest.class);
        when(request.tourId()).thenReturn(1L);

        assertThrows(TourNotFoundProblem.class, () -> orderService.createOrder(request));

        verify(orderRepository, never()).save(any(Order.class));
        verify(emailService, never()).sendEmail(any(Order.class));
    }
}
