package ru.mirea.infinitejourneysbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.mirea.infinitejourneysbackend.domain.dto.order.OrderResponse;
import ru.mirea.infinitejourneysbackend.domain.model.Order;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "tour.title", target = "tourTitle")
    @Mapping(source = "tour.description", target = "tourDescription")
    @Mapping(source = "tour.country.name", target = "countryName")
    @Mapping(source = "tour.price", target = "price")
    @Mapping(source = "tour.seller.username", target = "sellerName")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponse(List<Order> orders);
}
