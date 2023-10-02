package backend.codebackend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Restuarant {
    private String title;
    private String minPrice;
    private String imageUrl;
    private String deliveryTime;
    private String icoStar;
    private String review_num;

    public Restuarant(String title, String minPrice, String imageUrl, String deliveryTime, String icoStar, String review_num) {
        this.title = title;
        this.minPrice = minPrice;
        this.imageUrl = imageUrl;
        this.deliveryTime = deliveryTime;
        this.icoStar = icoStar;
        this.review_num = review_num;
    }
}
