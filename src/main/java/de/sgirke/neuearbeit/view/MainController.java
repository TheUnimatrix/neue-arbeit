package de.sgirke.neuearbeit.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping
    public String indexPage() {
        return "index";
    }

    @GetMapping("/working-days")
    public String workingDaysPage() {
        return "working-days";
    }

}