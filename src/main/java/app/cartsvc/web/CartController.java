package app.cartsvc.web;

import app.cartsvc.cart.service.CartService;
import app.cartsvc.cartItem.service.CartItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartItemService cartItemService;
    private final CartService cartService;

    public CartController(CartItemService cartItemService, CartService cartService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }
}
