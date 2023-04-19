package de.sgirke.neuearbeit.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @GetMapping
    public String indexPage() {
        return "index";
    }

    @GetMapping("/working-days")
    public String workingDaysPage(Model model) {
        List<Integer> yearList = new ArrayList<>();
        int year = LocalDate.now().getYear();

        for (int i = 0; i < 10; i++) {
            yearList.add(year++);
        }

        model.addAttribute("yearList", yearList);
        return "working-days";
    }

}