package app.cartsvc.cart.model;

import app.cartsvc.cartItem.model.CartItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cart")
@Builder
@Getter
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)
    private List<CartItem> items;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    public Cart() {
        this.items = new ArrayList<>();
    }

}
