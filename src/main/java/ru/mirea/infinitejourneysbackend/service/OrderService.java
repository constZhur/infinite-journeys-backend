package ru.mirea.infinitejourneysbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.infinitejourneysbackend.domain.dto.order.BuyOrderRequest;
import ru.mirea.infinitejourneysbackend.domain.model.Order;
import ru.mirea.infinitejourneysbackend.exception.tour.TourNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.user.InsufficientBalanceException;
import ru.mirea.infinitejourneysbackend.repository.OrderRepository;
import ru.mirea.infinitejourneysbackend.repository.TourRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserService userService;
    private final TourRepository tourRepository;
    private final OrderRepository orderRepository;

    public List<Order> getOrders() {
        var user = userService.getCurrentUser();
        return user.getOrders();
    }

    @Transactional
    public Order createOrder(BuyOrderRequest request) {
        var buyer = userService.getCurrentUser();
        var tour = tourRepository.findById(request.tourId())
                .orElseThrow(() -> new TourNotFoundProblem(request.tourId().toString()));

        Double price = tour.getPrice();

        if (buyer.getBalance() < price) {
            throw new InsufficientBalanceException();
        }

        buyer.setBalance(buyer.getBalance() - price);
        tour.getSeller().setBalance(tour.getSeller().getBalance() + price);

        Order order = Order.builder()
                .buyer(buyer)
                .tour(tour)
                .build();

        orderRepository.save(order);
        return order;
    }
}
