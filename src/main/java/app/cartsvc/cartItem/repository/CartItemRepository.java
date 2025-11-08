package app.cartsvc.cartItem.repository;

import app.cartsvc.cart.model.Cart;
import app.cartsvc.cartItem.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteCartItemById(UUID id);

    List<CartItem> getCartItemsByCart(Cart cart);
}
