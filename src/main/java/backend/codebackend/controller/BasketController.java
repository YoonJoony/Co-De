package backend.codebackend.controller;

import backend.codebackend.domain.Basket;
import backend.codebackend.service.BasketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BasketController {
    private final BasketService basketService;

//    @GetMapping("")
//    public String listBasketItems(@PathVariable Long id, Model model) {
//        List<Basket> basketItems = basketService.findAll(id);
//        model.addAttribute("basketItems", basketItems);
//        return "basket/list";
//    }
}
