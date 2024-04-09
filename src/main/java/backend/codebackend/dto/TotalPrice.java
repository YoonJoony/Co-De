package backend.codebackend.dto;

import lombok.*;


@Getter
@Setter
@Builder
public class TotalPrice {
    private String username;
    private int totalPrice;
}
