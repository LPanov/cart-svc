package app.cartsvc.web;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cart.service.CartService;
import app.cartsvc.cartItem.service.CartItemService;
import app.cartsvc.web.dto.CartItemRequest;
import app.cartsvc.web.dto.CartItemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerApiTest {
    @MockitoBean
    private CartItemService cartItemService;
    @MockitoBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    void createCart_thenReturnStatusCreated() throws Exception {
        MockHttpServletRequestBuilder request = post("/api/v1/cart")
                .param("userId", userId.toString());

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        verify(cartService, times(1)).createCart(userId);
    }

    @Test
    void getCartItemsByUserId_returnStatusOkAndListOfItems() throws Exception {
        CartItemResponse item1 = randomCartItemResponse();
        CartItemResponse item2 = randomCartItemResponse();
        List<CartItemResponse> mockResponse = List.of(item1, item2);

        Cart cart = Cart.builder().id(UUID.randomUUID()).userId(userId).items(new ArrayList<>()).build();
        when(cartService.getCartByUserId(userId)).thenReturn(cart);

        when(cartItemService.getItemsByCart(cart)).thenReturn(mockResponse);

        MockHttpServletRequestBuilder request = get("/api/v1/cart")
                .param("userId", userId.toString());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(cartService).getCartByUserId(userId);
        verify(cartItemService).getItemsByCart(cart);
    }

    @Test
    void addToCart_ShouldReturn200OkAndCallService() throws Exception {
        CartItemRequest itemRequest = CartItemRequest.builder()
                .userId(userId)
                .partId(UUID.randomUUID())
                .quantity(2)
                .build();

        MockHttpServletRequestBuilder request = post("/api/v1/cart/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(itemRequest));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void deleteCartItem_ShouldReturn200OkAndCallService() throws Exception {
        UUID testCartItemId = UUID.randomUUID();

        doNothing().when(cartItemService).removeFromCart(any(UUID.class));

        MockHttpServletRequestBuilder request = delete("/api/v1/cart/item")
                .param("itemId", testCartItemId.toString());

        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(cartItemService).removeFromCart(testCartItemId);
    }

    private static CartItemResponse randomCartItemResponse() {
        return CartItemResponse.builder()
                .id(UUID.randomUUID())
                .partId(UUID.randomUUID())
                .quantity(new Random().nextInt())
                .build();
    }
}
