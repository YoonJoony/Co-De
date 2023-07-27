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
    private String title;
    private String distanceLimit;
    private String category;
    private String people;

    public Mozip toEntity(){
        Mozip build = Mozip.builder()
                .id(id)
                .title(title)
                .distanceLimit(distanceLimit)
                .category(category)
                .people(people)
                .build();
        return build;
    }

    public MozipForm(Long id, String title, String distanceLimit, String category, String numberOfPeople) {
        this.id = id;
        this.title = title;
        this.distanceLimit = distanceLimit;
        this.category = category;
        this.people = people;

    }

}
