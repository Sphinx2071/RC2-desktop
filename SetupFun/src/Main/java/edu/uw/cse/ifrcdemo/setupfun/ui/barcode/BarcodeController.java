package edu.uw.cse.ifrcdemo.setupfun.ui.barcode;

import edu.uw.cse.ifrcdemo.setupfun.ui.barcode.RangeUtil;
import edu.uw.cse.ifrcdemo.setupfun.ui.barcode.Range;
import edu.uw.cse.ifrcdemo.setupfun.ui.barcode.ControllerPdfUtil;
import edu.uw.cse.ifrcdemo.sharedlib.generator.BarcodeGeneratorConst;
import edu.uw.cse.ifrcdemo.sharedlib.generator.BarcodeGeneratorSingleton;
import edu.uw.cse.ifrcdemo.sharedlib.generator.BarcodeVoucher;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/barcode")
@SessionAttributes(types = BarcodeFormModel.class)
public class BarcodeController {
    private static final String BARCODE_GENERATOR = "barcode/barcodeGenerator";
    private static final String BARCODE_OUTPUT = "barcode/barcodeOutput";

    private static final String FILE_LOCATION_QUERY_PARAM = "fileLocation";


    private final Logger logger;
    private final TemplateEngine templateEngine;

    public BarcodeController(Logger logger, TemplateEngine templateEngine) {
        this.logger = logger;
        this.templateEngine = templateEngine;
    }

    @ModelAttribute("barcodeFormModel")
    public BarcodeFormModel newBarcodeFormModel() {
        BarcodeFormModel barcodeFormModel= new BarcodeFormModel();
        return barcodeFormModel;
    }


    @GetMapping("")
    public ModelAndView showBarcodeGenerator(
            @ModelAttribute("barcodeFormModel") BarcodeFormModel barcodeFormModel) {
        return new ModelAndView(BARCODE_GENERATOR);
    }

    @PostMapping("")
    public ModelAndView generateBarcodes(
            @Valid @ModelAttribute("barcodeFormModel") BarcodeFormModel barcodeFormModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView(BARCODE_GENERATOR);
        }

        // Display the generated barcodes
        return new ModelAndView("redirect:barcode/barcodeOutput");
    }

    @GetMapping("barcodeOutput")
    public ModelAndView outputBarcodes(
            @Valid @ModelAttribute("barcodeFormModel") BarcodeFormModel barcodeFormModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView(BARCODE_GENERATOR);
        }

        generateBarcodes(barcodeFormModel);

        // Display the generated barcodes
        return new ModelAndView(BARCODE_OUTPUT);
    }

    @PostMapping("barcodeOutput")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> outputBarcodesDocument(
            @ModelAttribute("barcodeFormModel") BarcodeFormModel barcodeFormModel,
            HttpServletRequest request, HttpServletResponse response) {

        WebContext webContext = new WebContext(request, response, request.getServletContext());

        generateBarcodes(barcodeFormModel);

        // Write out the report
        webContext.setVariable("barcodeFormModel", barcodeFormModel);

        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append("barcode");

        if (barcodeFormModel.getText() != null) {
            fileNameBuilder.append("_").append(barcodeFormModel.getText());
        }

        if (barcodeFormModel.getRangeStart() != null) {
            fileNameBuilder.append("_").append(barcodeFormModel.getRangeStart());
        }

        if (barcodeFormModel.getRangeEnd() != null) {
            fileNameBuilder.append("_").append(barcodeFormModel.getRangeEnd());
        }

        String filename = fileNameBuilder.toString();
        writeReport(webContext, filename, "barcode/barcodeOutputDocument.html");

        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }

    private void generateBarcodes(
            @ModelAttribute("barcodeFormModel") BarcodeFormModel barcodeFormModel) {
        Integer start = barcodeFormModel.getRangeStart();
        Integer end = barcodeFormModel.getRangeEnd();

        if (start == null) {
            return;
        }

        if (end == null) {
            end = start;
        }

        if (end < start) {
            Integer temp = start;
            start = end;
            end = temp;
        }

        List<BarcodeVoucher> barcodes = IntStream
                .rangeClosed(start, end)
                .mapToObj(String::valueOf).parallel().map(
                        i -> new BarcodeVoucher(i, barcodeFormModel.getText(),
                                BarcodeGeneratorConst.DATA_URI_PREFIX + BarcodeGeneratorSingleton
                                        .generateBase64Barcode(i))).collect(Collectors.toList());

        barcodeFormModel.setVoucherList(barcodes);
    }

    private void writeReport(WebContext webContext, String reportName, String templateLocation) {
        ControllerPdfUtil.writeControllerPdf(webContext, reportName, templateLocation, logger,
                templateEngine);
    }

    @GetMapping("getCsvRange")
    @ResponseBody
    public List<Range> importCsvRange(HttpServletRequest request) {

        String csvFilePath = request.getParameter(FILE_LOCATION_QUERY_PARAM);
        if (csvFilePath == null) {
            return null;
        }

        // Read in CSV file
        List<Integer> values = CsvFileUtil.readRangeCsv(new File(csvFilePath));//CsvFileUtil.readRangeCsv(new File(csvFilePath));

        if (values == null || values.size() <= 0) {
            return null;
        }

        ArrayList<Range> ranges = RangeUtil.calculateRangesFromInts(values);
        return ranges;
    }

}