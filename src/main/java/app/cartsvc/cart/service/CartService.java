package app.cartsvc.cart.service;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cart.repository.CartRepository;
import app.cartsvc.cartItem.service.CartItemService;
import app.cartsvc.web.dto.CartItemRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;

    public CartService(CartRepository cartRepository, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
    }

    public Cart getCartByUserId(UUID userId) {
        return cartRepository.findCartByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public void createCart(UUID userId) {
        Cart cart = Cart.builder()
                .userId(userId)
                .build();

        cartRepository.save(cart);
    }

    @Transactional
    public void addToCart(CartItemRequest cartItemRequest) {
        UUID partId = cartItemRequest.getPartId();
        Cart cart = getCartByUserId(cartItemRequest.getUserId());

        if (cart.getItems().stream().anyMatch(cartItem -> cartItem.getPartId().equals(partId))) {
            cartItemService.updateQuantity(cartItemRequest, cart);
        }
        else {
            cartItemService.createCartItem(cartItemRequest, cart);
        }
        updateCart(cart);
    }

    public void updateCart(Cart cart) {
        cartRepository.save(cart);
    }
}
