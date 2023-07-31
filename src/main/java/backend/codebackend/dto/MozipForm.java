package backend.codebackend.dto;

import backend.codebackend.domain.Mozip;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
public class MozipForm {

    private String title;
    private Long distance_limit;

    //모집글 select oprion 선택값 받기 위해 선언
    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    private String categories;
    private String peoples;

    public Mozip toEntity(){
        Mozip build = Mozip.builder()
                .title(title)
                .distance_limit(distance_limit)
                .categories(categories)
                .peoples(peoples)
                .build();
        return build;
    }

    public MozipForm(String title, Long distance_limit, String categories, String peoples) {
        this.title = title;
        this.distance_limit = distance_limit;
        this.categories = categories;
        this.peoples = peoples;
    }
}
