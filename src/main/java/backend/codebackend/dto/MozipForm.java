package backend.codebackend.dto;

import backend.codebackend.domain.Mozip;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MozipForm {
    //private Long id;
    private Long title;
    private String distanceLimit;
    private int category;
    private int numberOfPeople;

    public Long getTitle() {
        return title;
    }

    public void setTitle(Long title) {
        this.title = title;
    }

    public String getDistanceLimit() {
        return distanceLimit;
    }

    public void setDistanceLimit(String distanceLimit) {
        this.distanceLimit = distanceLimit;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    @Builder
    public MozipForm(Long title, String distanceLimit, int category, int numberOfPeople) {
        this.title = title;
        this.distanceLimit = distanceLimit;
        this.category = category;
        this.numberOfPeople = numberOfPeople;

    }
    public Mozip toEntity(){
        Mozip build = Mozip.builder()
                //.id(id)
                .title(title)
                .distanceLimit(distanceLimit)
                .category(category)
                .numberOfPeople(numberOfPeople)
                .build();
        return build;
    }

}
