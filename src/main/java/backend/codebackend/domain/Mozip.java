package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)  // JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알림.
public class Mozip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 ID를 자동으로 생성해 주는 전략 : IDENTITY
    private Long id;

    @Column(length = 20, nullable = false)
    private Long title;
    @Column(length = 10, nullable = false)
    private String distanceLimit;

    private int category;
    private int numberOfPeople;

    public Mozip(Long id, Long title, String distanceLimit, int category, int numberOfPeople) {
        this.id = id;
        this.title = title;
        this.distanceLimit = distanceLimit;
        this.category = category;
        this.numberOfPeople = numberOfPeople;
    }

    public Mozip(Mozip.Builder builder){
        //this.id = builder.id;
        this.title = builder.title;
        this.distanceLimit = builder.distanceLimit;
        this.category = builder.category;
        this.numberOfPeople = builder.numberOfPeople;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public class Builder {
        //private Long id;
        private Long title;
        private String distanceLimit;
        private int category;
        private int numberOfPeople;

        public Builder(Long title, String distanceLimit, int category, int numberOfPeople){
            this.title = title;
            this.distanceLimit = distanceLimit;
            this.category = category;
            this.numberOfPeople = numberOfPeople;
        }
    }

}
