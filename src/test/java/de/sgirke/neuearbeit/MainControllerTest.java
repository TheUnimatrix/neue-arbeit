package de.sgirke.neuearbeit;

import de.sgirke.neuearbeit.view.MainController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MainControllerTest {

    private final MainController mainController;

    @Autowired
    public MainControllerTest(MainController mainController) {
        this.mainController = mainController;
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(mainController).isNotNull();
    }

}