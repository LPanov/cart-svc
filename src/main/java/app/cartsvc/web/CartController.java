package app.cartsvc.web;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cart.service.CartService;
import app.cartsvc.cartItem.service.CartItemService;
import app.cartsvc.web.dto.CartItemRequest;
import app.cartsvc.web.dto.CartItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartItemService cartItemService;
    private final CartService cartService;

    public CartController(CartItemService cartItemService, CartService cartService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> getHelloWorld(@RequestParam(name = "name") String name) {

        return ResponseEntity.ok("Hello, " + name + " user!");
    }

    @PostMapping
    public ResponseEntity<Void> createCart(@RequestParam(name = "userId") UUID userId) {
        cartService.createCart(userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItemsByUserId(@RequestParam(name = "userId") UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);

        List<CartItemResponse> itemsByCart = cartItemService.getItemsByCart(cart);

        return ResponseEntity.status(200).body(itemsByCart);
    }

    @PostMapping("/item")
    public ResponseEntity<Void> addToCart(@RequestBody CartItemRequest cartItemRequest) {
        cartService.addToCart(cartItemRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/item")
    public ResponseEntity<Void> deleteCartItem(@RequestParam(name = "itemId") UUID cartItemId) {
        cartItemService.removeFromCart(cartItemId);

        return ResponseEntity.status(200).build();
    }
}
