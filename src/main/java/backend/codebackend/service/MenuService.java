package backend.codebackend.service;

import backend.codebackend.domain.Menu;
import backend.codebackend.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    public void menuSave(List<Menu> menus) {
        menuRepository.saveAll(menus);
    }

    public List<List<Menu>> menuListSelect(Long mozipId) {
        List<Menu> menus = menuRepository.findByMozipId_Id(mozipId);

        List<List<Menu>> menusList = new ArrayList<>();
        List<Menu> menuEqualsTitle = new ArrayList<>();

        String menuTitle = "🔥 인기메뉴";
        for (Menu value : menus) {
            if (value.getMenuTitle().equals(menuTitle)) {
                menuEqualsTitle.add(value);
            } else {
                menuTitle = value.getMenuTitle();
                menusList.add(menuEqualsTitle);
                menuEqualsTitle = new ArrayList<>();
                menuEqualsTitle.add(value);
            }
        }

        return menusList;
    }
}
