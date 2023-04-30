package de.sgirke.neuearbeit.view;

import de.sgirke.neuearbeit.service.PdfService;
import de.sgirke.neuearbeit.service.WorkingDaysService;
import org.apache.fop.apps.FOPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final WorkingDaysService workingDaysService;

    private final PdfService pdfService;

    @Autowired
    public MainController(WorkingDaysService workingDaysService, PdfService pdfService) {
        this.workingDaysService = workingDaysService;
        this.pdfService = pdfService;
    }

    @GetMapping
    public String doIndexPageGet() {
        return "index";
    }

    @GetMapping("/working-days")
    public String doWorkingDaysGet(Model model) {
        this.prepareModel(model);
        return "working-days";
    }

    @PostMapping("/working-days")
    public ResponseEntity<ByteArrayResource> doWorkingDaysPost(Model model, @RequestParam int startYear, @RequestParam int endYear) {

        ByteArrayOutputStream workingDaysPdfStream = this.generateWorkingDaysPdfYears(startYear, endYear);
        ByteArrayResource pdfByteArrayResource = new ByteArrayResource(workingDaysPdfStream.toByteArray());

        this.prepareModel(model);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfByteArrayResource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Arbeitstage.pdf")
                .body(pdfByteArrayResource);
    }

    private void prepareModel(Model model) {
        if (model.getAttribute("yearList") == null)
            model.addAttribute("yearList", getYearList());
    }

    private List<Integer> getYearList() {
        List<Integer> yearList = new ArrayList<>();
        int year = LocalDate.now().getYear();

        for (int i = 0; i < 10; i++)
            yearList.add(year++);

        return yearList;
    }

    private ByteArrayOutputStream generateWorkingDaysPdfYears(int startYear, int endYear) {
        ByteArrayOutputStream workingDaysPdfStream = null;

        try {
            // Hole XML mit Arbeitstagen für gewünschten Zeitraum
            String workingDaysXml = workingDaysService.calculateWorkingDays(startYear, endYear);
            logger.debug("workingDaysXml={}", workingDaysXml);

            // Hole PDF-Dokument als Stream
            workingDaysPdfStream = pdfService.generateWorkingDaysPdfNonspecificSpaceOfTime(workingDaysXml);
        } catch (FOPException | TransformerException | IOException exc) {
            logger.error(exc.getMessage(), exc);
        }

        return workingDaysPdfStream;
    }
}