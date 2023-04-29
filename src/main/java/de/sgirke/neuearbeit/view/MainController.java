package de.sgirke.neuearbeit.view;

import de.sgirke.neuearbeit.service.PdfService;
import de.sgirke.neuearbeit.service.ValidationService;
import de.sgirke.neuearbeit.service.WorkingDaysService;
import org.apache.fop.apps.FOPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private WorkingDaysService workingDaysService;

    @Autowired
    private PdfService pdfService;

    @GetMapping
    public String indexPage() {
        return "index";
    }

    @GetMapping("/working-days")
    public String workingDaysGetMapping(Model model) {
        if (model.getAttribute("yearList") == null)
            model.addAttribute("yearList", getYearList());
        return "working-days";
    }

    @PostMapping(value = "/working-days", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] workingDaysPostMapping(Model model, @RequestParam int startYear, @RequestParam int endYear) {

        byte[] test = this.generateWorkingDaysPdfYears(startYear, endYear);

        if (model.getAttribute("yearList") == null)
            model.addAttribute("yearList", getYearList());
        return test;
    }

    private List<Integer> getYearList() {
        List<Integer> yearList = new ArrayList<>();
        int year = LocalDate.now().getYear();

        for (int i = 0; i < 10; i++)
            yearList.add(year++);

        return yearList;
    }

    private byte[] generateWorkingDaysPdfYears(int startYear, int endYear) {
        try {
            // Hole XML mit Arbeitstagen für gewünschten Zeitraum
            String workingDaysXml = workingDaysService.calculateWorkingDays(startYear, endYear);
            logger.debug("workingDaysXml={}", workingDaysXml);

            // Hole PDF-Dokument als Stream
            ByteArrayOutputStream workingDaysPdfStream =
                    pdfService.generateWorkingDaysPdfNonspecificSpaceOfTime(workingDaysXml);

            // Erstelle Dateinamen und sende Datei an Browser
//            String fileName = "Arbeitstage.pdf";
            byte[] test = workingDaysPdfStream.toByteArray();

            logger.debug("PDF-Länge={}", test.length);

            return test;
        } catch (FOPException | TransformerException | IOException exc) {
            logger.error(exc.getMessage(), exc);
        }

        return null;
    }

}