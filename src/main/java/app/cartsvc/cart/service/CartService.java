package app.cartsvc.cart.service;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cart.repository.CartRepository;
import app.cartsvc.cartItem.service.CartItemService;
import app.cartsvc.web.dto.CartItemRequest;
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
        return cartRepository.getCartByUserId(userId);
    }

    public void createCart(UUID userId) {
        Cart cart = Cart.builder()
                .userId(userId)
                .build();

        cartRepository.save(cart);
    }

    public void addToCart(CartItemRequest cartItemRequest, UUID userId) {
        UUID partId = cartItemRequest.getPartId();
        Cart cart = getCartByUserId(userId);

        if (cart.getItems().stream().anyMatch(cartItem -> cartItem.getPartId().equals(partId))) {
            cartItemService.updateQuantity(cartItemRequest, cart, partId);
        }
        else {
            cartItemService.createCartItem(partId, cartItemRequest.getQuantity(), cart);
        }
        updateCart(cart);
    }

    public void updateCart(Cart cart) {
        cartRepository.save(cart);
    }
}
