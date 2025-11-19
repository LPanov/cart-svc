package app.cartsvc.cartItem;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cartItem.model.CartItem;
import app.cartsvc.cartItem.repository.CartItemRepository;
import app.cartsvc.cartItem.service.CartItemService;
import app.cartsvc.web.dto.CartItemRequest;
import app.cartsvc.web.dto.CartItemResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceUTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemService cartItemService;

    @Captor
    private ArgumentCaptor<CartItem> argumentCaptor;

    @Test
    void whenCreateCartItem_thenSaveCartItemToRepository() {
        CartItemRequest itemRequest = CartItemRequest.builder()
                .userId(java.util.UUID.randomUUID())
                .partId(java.util.UUID.randomUUID())
                .quantity(2)
                .build();
        Cart cart = Cart.builder().build();

        cartItemService.createCartItem(itemRequest, cart);

        verify(cartItemRepository).save(argumentCaptor.capture());
        CartItem savedCartItem = argumentCaptor.getValue();

        assertEquals(itemRequest.getPartId(), savedCartItem.getPartId());
        assertEquals(itemRequest.getQuantity(), savedCartItem.getQuantity());
        assertEquals(cart, savedCartItem.getCart());
    }

    @Test
    void whenUpdateCartItem_thenUpdateCartItemQuantity() {
        CartItemRequest itemRequest = CartItemRequest.builder()
                .userId(java.util.UUID.randomUUID())
                .partId(java.util.UUID.randomUUID())
                .quantity(2)
                .build();
        Cart cart = Cart.builder().items(List.of(CartItem.builder().partId(itemRequest.getPartId()).quantity(0).build())).build();

        cartItemService.updateQuantity(itemRequest, cart);

        verify(cartItemRepository).save(argumentCaptor.capture());
        CartItem updatedCartItem = argumentCaptor.getValue();

        assertEquals(itemRequest.getPartId(), updatedCartItem.getPartId());
        assertEquals(itemRequest.getQuantity(), updatedCartItem.getQuantity());
    }

    @Test
    void whenRemoveCartItem_thenCallDeleteCartItem() {
        UUID cartItemId = UUID.randomUUID();

        cartItemService.removeFromCart(cartItemId);

        verify(cartItemRepository).deleteCartItemById(cartItemId);
    }

    @Test
    void whenGetCartItemsByCart_thenReturnListOfCartItemResponses() {
        CartItem cartItem = CartItem.builder()
                .id(UUID.randomUUID())
                .partId(UUID.randomUUID())
                .quantity(2)
                .build();

        when(cartItemRepository.getCartItemsByCart(any(Cart.class))).thenReturn(List.of(cartItem));

        List<CartItemResponse> itemsByCart = cartItemService.getItemsByCart(Cart.builder().build());

        assertEquals(1, itemsByCart.size());
        verify(cartItemRepository).getCartItemsByCart(any(Cart.class));
    }

}
