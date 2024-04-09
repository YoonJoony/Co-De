package backend.codebackend.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private String menuName;
    private String menuPrice;
    private String menuDesc;
    private String menuPhoto;
    private String delivery_fee;
    private String minPrice;
    private List<List<Menu>> menuList_Title;
    private List<String> menuList_Title_Name;
}
