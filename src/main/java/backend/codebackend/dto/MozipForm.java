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
    private Long id;
    private Long title;
    private String distanceLimit;
    private int category;
    private int numberOfPeople;

    public Mozip toEntity(){
        Mozip build = Mozip.builder()
                .id(id)
                .title(title)
                .distanceLimit(distanceLimit)
                .category(category)
                .numberOfPeople(numberOfPeople)
                .build();
        return build;
    }

    public MozipForm(Long id, Long title, String distanceLimit, int category, int numberOfPeople) {
        this.id = id;
        this.title = title;
        this.distanceLimit = distanceLimit;
        this.category = category;
        this.numberOfPeople = numberOfPeople;

    }

}
