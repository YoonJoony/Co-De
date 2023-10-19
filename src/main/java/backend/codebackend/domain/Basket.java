package backend.codebackend.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.devtools.v100.domstorage.model.Item;

import java.util.List;

@Getter
@Setter
@Builder
public class Basket {

    private Long id;        //채팅방 번호
    private String productName;     // 항목 이름
    private int price;           // 가격
    private int quantity;        // 수량
    private String nickname;     // 파티원(누가 어느 것을 시켰는지 알기위해 해당 사람을 추적하는데 필요)

    public Basket(Long id, Long basketId, String productName, int price, int quantity, String nickname) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.nickname = nickname;
    }

}
