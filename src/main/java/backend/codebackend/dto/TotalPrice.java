package backend.codebackend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Data
@NoArgsConstructor
@Builder
public class TotalPrice {
    private String username;
    private int totalPrice;

    public TotalPrice(String username, int totalPrice) {
        this.username = username;
        this.totalPrice = totalPrice;
    }
}
