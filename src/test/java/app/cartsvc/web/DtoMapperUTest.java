package app.cartsvc.web;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cartItem.model.CartItem;
import app.cartsvc.web.dto.CartItemRequest;
import app.cartsvc.web.dto.CartItemResponse;
import app.cartsvc.web.mapper.DtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUTest {
    @Test
    void whenGetCartItemResponses_thenMapCartItemsToCartItemResponses() {
        CartItem cartItem = CartItem.builder()
                .id(UUID.randomUUID())
                .partId(UUID.randomUUID())
                .quantity(2)
                .build();

        CartItemResponse cartItemResponse = DtoMapper.getCartItemResponses(cartItem);

        assertEquals(cartItem.getId(), cartItemResponse.getId());
        assertEquals(cartItem.getPartId(), cartItemResponse.getPartId());
        assertEquals(cartItem.getQuantity(), cartItemResponse.getQuantity());
    }

    @Test
    void whenGetCartItem_thenMapCartItemRequestToCartItem() {
        CartItemRequest itemRequest = CartItemRequest.builder()
                .userId(java.util.UUID.randomUUID())
                .partId(java.util.UUID.randomUUID())
                .quantity(2)
                .build();

        UUID cartId = UUID.randomUUID();

        CartItem cartItem = DtoMapper.getCartItem(itemRequest, Cart.builder().id(cartId).userId(itemRequest.getUserId()).build());

        assertEquals(itemRequest.getPartId(), cartItem.getPartId());
        assertEquals(itemRequest.getQuantity(), cartItem.getQuantity());
        assertEquals(itemRequest.getUserId(), cartItem.getCart().getUserId());
        assertEquals(cartId, cartItem.getCart().getId());
    }
}
