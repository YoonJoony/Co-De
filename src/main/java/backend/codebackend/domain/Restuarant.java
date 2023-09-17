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

    public Restuarant(String title, String minPrice, String imageUrl) {
        this.title = title;
        this.minPrice = minPrice;
        this.imageUrl = imageUrl;
    }
}
