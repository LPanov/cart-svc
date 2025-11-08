package app.cartsvc.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
public class CartItemRequest {
    private UUID userId;
    private int quantity;
    private UUID partId;
}
