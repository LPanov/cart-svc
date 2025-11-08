package app.cartsvc.web.mapper;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cartItem.model.CartItem;
import app.cartsvc.web.dto.CartItemRequest;
import app.cartsvc.web.dto.CartItemResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static CartItemResponse getCartItemResponses(CartItem cartItem) {
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .partId(cartItem.getPartId())
                .quantity(cartItem.getQuantity())
                .build();
    }

    public static CartItem getCartItem(CartItemRequest cartItemRequest, Cart cart) {
        return CartItem.builder()
                .partId(cartItemRequest.getPartId())
                .quantity(cartItemRequest.getQuantity())
                .cart(cart)
                .build();
    }
}
