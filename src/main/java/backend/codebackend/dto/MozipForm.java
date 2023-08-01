package backend.codebackend.dto;

import backend.codebackend.domain.Mozip;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

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
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    private String categories;
    private String peoples;
    private LocalDateTime create_Date;
    private LocalDateTime modified_Date;

    private String nickname;

    public Mozip toEntity(){
        Mozip build = Mozip.builder() //date할려면 Mozip에 생성자에 @builder를 해야 builder()실행이 되네
                .title(title)
                .distance_limit(distance_limit)
                .categories(categories)
                .peoples(peoples)
                .nickname(nickname)
                .build();
        return build;
    }

    public MozipForm(String title, Long distance_limit, String categories, String peoples, LocalDateTime create_Date, LocalDateTime modified_Date, String nickname) {
        this.title = title;
        this.distance_limit = distance_limit;
        this.categories = categories;
        this.peoples = peoples;
        this.create_Date = create_Date;
        this.modified_Date = modified_Date;
        this.nickname = nickname;
    }

//    private static class TIME_MAXIMUM {
//        public static final int SEC = 60;
//        public static final int MIN = 60;
//        public static final int HOUR = 24;
//        public static final int DAY = 30;
//        public static final int MONTH = 12;
//    }
//
//    public String calculateTime(Date date) {
//        long curTime = create_Date;
//        long regTime = date.getTime();
//        long diffTime = (curTime - regTime) / 1000;
//        String msg = null;
//        if (diffTime < TIME_MAXIMUM.SEC) {
//            // sec
//            msg = diffTime + "초 전";
//        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
//            // min
//            msg = diffTime + "분 전";
//        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
//            // hour
//            msg = (diffTime) + "시간 전";
//        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
//            // day
//            msg = (diffTime) + "일 전";
//        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
//            // day
//            msg = (diffTime) + "달 전";
//        } else {
//            msg = (diffTime) + "년 전";
//        }
//        return msg;
//    }
}
