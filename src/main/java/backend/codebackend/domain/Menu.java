package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 ID를 자동으로 생성해 주는 전략 : IDENTITY
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mozipId", referencedColumnName = "id", nullable = true)
    Mozip mozipId;

    private String menuName;
    private String menuPrice;
    private String menuDesc;

    @Lob // Large data를 저장하기 위한 필드임을 나타냄. 안쓰면 저장되지 않음.
    @Column(name = "menu_photo", columnDefinition = "MEDIUMTEXT")
    private String menuPhoto;
    private String menuTitle;
}
