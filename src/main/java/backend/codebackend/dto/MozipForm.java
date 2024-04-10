package backend.codebackend.dto;

import backend.codebackend.domain.Mozip;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
@Setter
@Data
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

    public void setPeoples(int peoples) {
        this.peoples = peoples;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    private String categories;
    private String store;

    private int usercount; //현재 유저 인원 수
    private int peoples; //최대 인원 수
    private LocalDateTime create_Date;

    private String nickname;

    public Mozip toEntity(){
        Mozip build = Mozip.builder()
                .title(title)
                .distance_limit(distance_limit)
                .categories(categories)
                .store(store)
                .peoples(peoples)
                .nickname(nickname)
                .status(Mozip.mozipStatus.정산전)
                .usercount(1)
                .build();
        return build;
    }

    public MozipForm(String title, Long distance_limit, String categories, String store, int usercount, int peoples, LocalDateTime create_Date, String nickname) {
        this.title = title;
        this.distance_limit = distance_limit;
        this.categories = categories;
        this.store = store;
        this.usercount = usercount;
        this.peoples = peoples;
        this.create_Date = create_Date;
        this.nickname = nickname;
    }
}
