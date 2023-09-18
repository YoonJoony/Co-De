package backend.codebackend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Menu {
    private String menuName;
    private String menuPrice;
    private String menuDesc;
    private String menuPhoto;

    public Menu(String menuName, String menuPrice, String menuDesc, String menuPhoto) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuDesc = menuDesc;
        this.menuPhoto = menuPhoto;
    }
}
