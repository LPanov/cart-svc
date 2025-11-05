package app.cartsvc.cartItem.repository;

import app.cartsvc.cartItem.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteById(UUID cartItemId);
}
