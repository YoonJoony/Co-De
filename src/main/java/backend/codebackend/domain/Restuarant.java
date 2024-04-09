package backend.codebackend.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restuarant {
    private String title;
    private String minPrice;
    private String imageUrl;
    private String deliveryTime;
    private String icoStar;
    private String review_num;
}
