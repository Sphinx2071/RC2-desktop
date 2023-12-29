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

package edu.uw.cse.ifrcdemo.setupfun.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import edu.uw.cse.ifrcdemo.setupfun.consts.ViewConsts;
import edu.uw.cse.ifrcdemo.sharedlib.consts.GenConsts;
import edu.uw.cse.ifrcdemo.sharedlib.consts.ModuleConsts;
import edu.uw.cse.ifrcdemo.sharedlib.model.config.AuxiliaryProperty;
import edu.uw.cse.ifrcdemo.sharedlib.model.config.SaveDirectory;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;
import edu.uw.cse.ifrcdemo.sharedlib.util.DialogUtil;
import edu.uw.cse.ifrcdemo.sharedlib.util.FileUtil;
import edu.uw.cse.ifrcdemo.sharedlib.util.OdkPathUtil;
import edu.uw.cse.ifrcdemo.sharedlib.util.StringUtil;
import edu.uw.cse.ifrcdemo.sharedlib.util.SwingUtil;
import edu.uw.cse.ifrcdemo.sharedlib.util.XlsxUtil;
import edu.uw.cse.ifrcdemo.sharedlib.util.ZipUtil;
import edu.uw.cse.ifrcdemo.sharedlib.view.MainPanel;
import edu.uw.cse.ifrcdemo.sharedlib.view.SyncActionListener;
import edu.uw.cse.ifrcdemo.sharedlib.view.SyncActionPanel;
import edu.uw.cse.ifrcdemo.translations.TranslationConsts;
import edu.uw.cse.ifrcdemo.translations.TranslationUtil;
import edu.uw.cse.ifrcdemo.xlsxconverterserver.XlsxConverterServer;
import edu.uw.cse.ifrcdemo.xlsxconverterserver.handler.MappingFormUploadConsumer;
import edu.uw.cse.ifrcdemo.xlsxconverterserver.model.ConvertedXlsx;
import io.sentry.Sentry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public abstract class AbsConfigModel{
    private final Module module;
    private final MainPanel mainPanel;
    private final SaveDirectory saveDirectory;
    private final MappingFormUploadConsumer formUploadConsumer;
    private final Logger logger;
    private final SyncActionPanel syncPanel;

    private String beneficiaryEntityFormPath;
    private String individualFormPath;
    //todo:ensure removal of variable configview doesn't cause issues.  ModelAndView object should handle this function
    //private final AbsConfigView configView;

    public abstract void addAuxiliaryProperty(AuxiliaryProperty auxProp);
    public abstract String getBaseFilesZipName();

    public AbsConfigModel(Module module, MainPanel mainPanel, SaveDirectory saveDirectory, MappingFormUploadConsumer formUploadConsumer, SyncActionPanel syncPanel) {
        this.module = module;
        this.mainPanel = mainPanel;
        this.saveDirectory = saveDirectory;
        this.formUploadConsumer = formUploadConsumer;
        //this.configView = configView; AbsConfigView configView,
        this.syncPanel = syncPanel;

        this.logger = LogManager.getLogger(AbsConfigModel.class);
        this.beneficiaryEntityFormPath = null;
        this.individualFormPath = null;
    }

    private static Set<String> getAllXlsx(Path tablesPath) throws IOException {
        return Files
                .walk(tablesPath, 1)
                .map(OdkPathUtil::getFormsPath)
                .filter(Files::exists)
                .flatMap(path -> { // get list of forms for each table
                    try {
                        return Files.walk(path, 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(path -> { // get name of xlsx file for each form
                    try {
                        return Files
                                .find(path, 1, (file, __) -> file.getFileName().toString().endsWith(GenConsts.XLSX_FILE_EXTENSION))
                                .findFirst(); // assume there will be at most be 1 xlsx file
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(path -> path.toAbsolutePath().toString())
                .collect(Collectors.toSet());
    }

    public MainPanel getMainPanel() {
        return this.mainPanel;
    }

    public String getBeneficiaryEntityFormPath() {
        return beneficiaryEntityFormPath;
    }

    public void setBeneficiaryEntityFormPath(String beneficiaryEntityFormPath) {
        this.beneficiaryEntityFormPath = beneficiaryEntityFormPath;
    }

    public String getIndividualFormPath() {
        return individualFormPath;
    }

    public void setIndividualFormPath(String individualFormPath) {
        this.individualFormPath = individualFormPath;
    }

    public MappingFormUploadConsumer getFormUploadConsumer() {
        return formUploadConsumer;
    }



}