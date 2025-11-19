package app.cartsvc.cart;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cart.repository.CartRepository;
import app.cartsvc.cart.service.CartService;
import app.cartsvc.cartItem.model.CartItem;
import app.cartsvc.cartItem.service.CartItemService;
import app.cartsvc.web.dto.CartItemRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceUTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemService cartItemService;

    @InjectMocks
    private CartService cartService;

    @Captor
    private ArgumentCaptor<Cart> argumentCaptor;

    @Test
    void whenCreateCart_thenSaveCartToRepository() {
        UUID userId = UUID.randomUUID();

        cartService.createCart(userId);

        verify(cartRepository).save(argumentCaptor.capture());
        Cart savedCart = argumentCaptor.getValue();

        assertEquals(userId, savedCart.getUserId());
    }

    @Test
    void givenValidDto_andCartItemDoesNotExist_whenAddToCart_thenCallCreateCartItem() {
        CartItemRequest itemRequest = CartItemRequest.builder()
                .userId(UUID.randomUUID())
                .partId(UUID.randomUUID())
                .quantity(2)
                .build();

        Cart cart = Cart.builder()
                .id(UUID.randomUUID())
                .userId(itemRequest.getUserId())
                .items(new ArrayList<>())
                .build();

        when(cartRepository.findCartByUserId(itemRequest.getUserId())).thenReturn(java.util.Optional.of(cart));

        cartService.addToCart(itemRequest);

        verify(cartItemService).createCartItem(itemRequest, cart);
        verify(cartItemService, never()).updateQuantity(any(), any());

        verify(cartRepository).save(cart);
    }

    @Test
    void givenValidDto_andCartItemExists_whenAddToCart_thenCallUpdateCartItemQuantity() {
        CartItemRequest itemRequest = CartItemRequest.builder()
                .userId(UUID.randomUUID())
                .partId(UUID.randomUUID())
                .quantity(2)
                .build();

        Cart cart = Cart.builder()
                .id(UUID.randomUUID())
                .userId(itemRequest.getUserId())
                .items(List.of(CartItem.builder().partId(itemRequest.getPartId()).build()))
                .build();

        when(cartRepository.findCartByUserId(itemRequest.getUserId())).thenReturn(java.util.Optional.of(cart));

        cartService.addToCart(itemRequest);

        verify(cartItemService, never()).createCartItem(any(CartItemRequest.class), any(Cart.class));
        verify(cartItemService).updateQuantity(itemRequest, cart);

        verify(cartRepository).save(cart);
    }

    @Test
    void givenInvalidUserId_whenAddToCart_thenThrowException() {
        CartItemRequest itemRequest = CartItemRequest.builder()
                .userId(UUID.randomUUID())
                .build();

        when(cartRepository.findCartByUserId(any(UUID.class))).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.addToCart(itemRequest));
    }
}
