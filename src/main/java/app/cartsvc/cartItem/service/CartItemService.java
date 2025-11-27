package app.cartsvc.cartItem.service;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cartItem.model.CartItem;
import app.cartsvc.cartItem.repository.CartItemRepository;
import app.cartsvc.web.dto.CartItemRequest;
import app.cartsvc.web.dto.CartItemResponse;
import app.cartsvc.web.mapper.DtoMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartItemService {


    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public CartItem createCartItem(CartItemRequest cartItemRequest, Cart cart) {
        CartItem cartItem = DtoMapper.getCartItem(cartItemRequest, cart);

        cartItemRepository.save(cartItem);

        return cartItem;
    }

    public void updateQuantity(CartItemRequest cartItemRequest, Cart cart) {
        CartItem cartItem = cart.getItems().stream().filter(item -> item.getPartId().equals(cartItemRequest.getPartId())).findFirst().get();
        cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());

        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeFromCart(UUID cartItemId) {
        cartItemRepository.deleteCartItemById(cartItemId);
    }

    public List<CartItemResponse> getItemsByCart(Cart cart) {

        List<CartItem> cartItems = cartItemRepository.getCartItemsByCart(cart);
        List<CartItemResponse> cartItemResponses = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            CartItemResponse cartItemResponse = DtoMapper.getCartItemResponses(cartItem);

            cartItemResponses.add(cartItemResponse);
        }

        return cartItemResponses;
    }
}
