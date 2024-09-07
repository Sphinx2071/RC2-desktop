/*
 * Copyright (c) 2016-2022 University of Washington
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  Neither the name of the University of Washington nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE UNIVERSITY OF WASHINGTON AND CONTRIBUTORS “AS IS” AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE UNIVERSITY OF WASHINGTON OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package edu.uw.cse.ifrcdemo.setup.gointosharedlib.ui.other;

import edu.uw.cse.ifrcdemo.setup.gointosharedlib.model.range.Range;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.ui.util.ControllerPdfUtil;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.util.CsvFileUtil;
import edu.uw.cse.ifrcdemo.setup.gointosharedlib.util.RangeUtil;
import edu.uw.cse.ifrcdemo.sharedlib.generator.BarcodeGeneratorConst;
import edu.uw.cse.ifrcdemo.sharedlib.generator.BarcodeGeneratorSingleton;
import edu.uw.cse.ifrcdemo.sharedlib.generator.BarcodeVoucher;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

/**
 * OtherController handles requests related to miscellaneous functionalities,
 * primarily focusing on barcode generation and CSV range processing.
 *
 * This controller is mapped to the "/other" path and manages session attributes
 * of type BarcodeFormModel.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [The release or version this class was introduced]
 */
@Controller
@RequestMapping("/other")
@SessionAttributes(types = { BarcodeFormModel.class })
public class OtherController {
  private static final String OTHER_ITEMS_MENU = "other/otherItemsMenu";
  private static final String OTHER_BARCODE_GENERATOR = "other/barcodeGenerator";
  private static final String OTHER_BARCODE_OUTPUT = "other/barcodeOutput";

  private static final String FILE_LOCATION_QUERY_PARAM = "fileLocation";

  private final Logger logger;

  private final TemplateEngine templateEngine;

  /**
   * Constructs an OtherController with the specified logger and template engine.
   *
   * @param logger The logger to be used for logging
   * @param templateEngine The template engine for processing views
   */
  public OtherController(Logger logger, TemplateEngine templateEngine) {
    this.logger = logger;
    this.templateEngine = templateEngine;
  }

  /**
   * Creates a new BarcodeFormModel for use in the session.
   *
   * @return A new BarcodeFormModel instance
   */
  @ModelAttribute("barcodeFormModel")
  public BarcodeFormModel newBarcodeFormModel() {
    BarcodeFormModel barcodeFormModel = new BarcodeFormModel();
    return barcodeFormModel;
  }

  /**
   * Handles GET requests to show other options.
   *
   * @return The view name for other items menu
   */
  @GetMapping("")
  public String showOtherOptions() {
    return OTHER_ITEMS_MENU;
  }

  /**
   * Handles GET requests to show the barcode generator form.
   *
   * @param barcodeFormModel The model attribute for barcode form data
   * @return ModelAndView for the barcode generator page
   */
  @GetMapping("barcodes")
  public ModelAndView showBarcodeGenerator(
      @ModelAttribute("barcodeFormModel") BarcodeFormModel barcodeFormModel) {
    return new ModelAndView(OTHER_BARCODE_GENERATOR);
  }

  /**
   * Handles POST requests to generate barcodes.
   *
   * @param barcodeFormModel The model attribute for barcode form data
   * @param bindingResult The binding result for validation errors
   * @return ModelAndView redirecting to barcode output page
   */
  @PostMapping("barcodes")
  public ModelAndView generateBarcodes(
      @Valid @ModelAttribute("barcodeFormModel") BarcodeFormModel barcodeFormModel,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ModelAndView(OTHER_BARCODE_GENERATOR);
    }

    // Display the generated barcodes
    return new ModelAndView("redirect:barcodeOutput");
  }

  /**
   * Handles GET requests to output generated barcodes.
   *
   * @param barcodeFormModel The model attribute for barcode form data
   * @param bindingResult The binding result for validation errors
   * @return ModelAndView for the barcode output page
   */
  @GetMapping("barcodeOutput")
  public ModelAndView outputBarcodes(
      @Valid @ModelAttribute("barcodeFormModel") BarcodeFormModel barcodeFormModel,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ModelAndView(OTHER_BARCODE_GENERATOR);
    }

    generateBarcodes(barcodeFormModel);

    // Display the generated barcodes
    return new ModelAndView(OTHER_BARCODE_OUTPUT);
  }

  /**
   * Handles POST requests to output barcode documents.
   *
   * @param barcodeFormModel The model attribute for barcode form data
   * @param request The HTTP servlet request
   * @param response The HTTP servlet response
   * @return ResponseEntity with success message
   */
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
    writeReport(webContext, filename, "other/barcodeOutputDocument.html");

    return new ResponseEntity<>("Success!", HttpStatus.OK);
  }

  /**
   * Generates a list of BarcodeVoucher objects based on the given BarcodeFormModel.
   * This method creates barcodes for a range of numbers, applying any specified text prefix.
   *
   * @param barcodeFormModel The model containing barcode generation parameters
   */
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

  /**
   * Writes a report to a PDF file using the provided WebContext, report name, and template location.
   * This method utilizes ControllerPdfUtil to generate the PDF.
   *
   * @param webContext The WebContext containing data for the report
   * @param reportName The name to be used for the generated report file
   * @param templateLocation The location of the template to be used for the report
   */
  private void writeReport(WebContext webContext, String reportName, String templateLocation) {
    ControllerPdfUtil.writeControllerPdf(webContext, reportName, templateLocation, logger,
        templateEngine);
  }

  /**
   * Handles GET requests to import CSV range data.
   *
   * @param request The HTTP servlet request
   * @return List of Range objects parsed from the CSV file
   */
  @GetMapping("getCsvRange")
  @ResponseBody
  public List<Range> importCsvRange(HttpServletRequest request) {

    String csvFilePath = request.getParameter(FILE_LOCATION_QUERY_PARAM);
    if (csvFilePath == null) {
      return null;
    }

    // Read in CSV file
    List<Integer> values = CsvFileUtil.readRangeCsv(new File(csvFilePath));

    if (values == null || values.size() <= 0) {
      return null;
    }

    ArrayList<Range> ranges = RangeUtil.calculateRangesFromInts(values);

    return ranges;
  }

}
