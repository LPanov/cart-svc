package app.cartsvc.cartItem.service;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cartItem.model.CartItem;
import app.cartsvc.cartItem.repository.CartItemRepository;
import app.cartsvc.web.dto.CartItemRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartItemService {


    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public void createCartItem(UUID partId, int quantity, Cart cart) {
        CartItem cartItem = CartItem.builder()
                .partId(partId)
                .quantity(quantity)
                .cart(cart)
                .build();

        cartItemRepository.save(cartItem);
    }

    public void updateQuantity(CartItemRequest cartItemRequest, Cart cart, UUID partId) {
        CartItem cartItem = cart.getItems().stream().filter(item -> item.getPartId().equals(cartItemRequest.getPartId())).findFirst().get();
        cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());

        cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(UUID cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
