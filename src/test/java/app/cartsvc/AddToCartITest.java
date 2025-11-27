package app.cartsvc;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cart.service.CartService;
import app.cartsvc.cartItem.service.CartItemService;
import app.cartsvc.web.dto.CartItemRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AddToCartITest {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartService cartService;

    private UUID userId;
    private Cart cart;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        cart = cartService.createCart(userId);
    }

    @Test
    void addToCartWithExistingCartItem_shouldCreateNewCartItem() {
        cart.setItems(new ArrayList<>());
        cartService.updateCart(cart);

        CartItemRequest itemRequest = CartItemRequest.builder()
                .userId(userId)
                .partId(UUID.randomUUID())
                .quantity(2)
                .build();


        cartService.addToCart(itemRequest);

        Cart cart = cartService.getCartByUserId(userId);

        assertEquals(1, cart.getItems().size());
        assertEquals(itemRequest.getQuantity(), cart.getItems().get(0).getQuantity());
        assertEquals(itemRequest.getPartId(), cart.getItems().get(0).getPartId());
        assertEquals(userId, cart.getUserId());
    }

    @Test
    void addToCart_whenCartItemDoesNotExist_shouldUpdateQuantity() {
        cart.setItems(new ArrayList<>());
        cartService.updateCart(cart);

        CartItemRequest itemRequest = CartItemRequest.builder()
                .userId(userId)
                .partId(UUID.randomUUID())
                .quantity(2)
                .build();


        cartService.addToCart(itemRequest);
        cartService.addToCart(itemRequest);

        Cart cart = cartService.getCartByUserId(userId);

        assertEquals(1, cart.getItems().size());
        assertEquals(itemRequest.getQuantity() * 2, cart.getItems().get(0).getQuantity());
        assertEquals(itemRequest.getPartId(), cart.getItems().get(0).getPartId());
        assertEquals(userId, cart.getUserId());
    }
}
