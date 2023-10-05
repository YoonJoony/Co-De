package backend.codebackend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Menu {
    private String menuName;
    private String menuPrice;
    private String menuDesc;
    private String menuPhoto;
    private final String delivery_fee;

    private List<List<Menu>> menuList_Title;
    private List<String> menuList_Title_Name;

    public Menu(String menuName, String menuPrice, String menuDesc, String menuPhoto, String delivery_fee, List<List<Menu>> menuList_Title, List<String> menuList_Title_Name) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuDesc = menuDesc;
        this.menuPhoto = menuPhoto;
        this.delivery_fee = delivery_fee;
        this.menuList_Title = menuList_Title;
        this.menuList_Title_Name = menuList_Title_Name;
    }
}
