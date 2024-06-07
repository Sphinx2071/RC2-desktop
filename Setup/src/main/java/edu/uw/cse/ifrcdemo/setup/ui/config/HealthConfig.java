package edu.uw.cse.ifrcdemo.setup.ui.config;

import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;

import java.util.List;

public class HealthConfig {
    public Module module;
    public RegistrationMode registrationMode;
    public String beneficiaryEntityFormChooser;
    public String beneficiaryEntityIdColumnTextField;
    public String individualFormChooser;
    public List<RegistrationMode> registrationModeList;

    public HealthConfig() {
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public RegistrationMode getRegistrationMode() {
        return registrationMode;
    }

    public void setRegistrationMode(RegistrationMode registrationMode) {
        this.registrationMode = registrationMode;
    }

    public String getBeneficiaryEntityFormChooser() {
        return beneficiaryEntityFormChooser;
    }

    public void setBeneficiaryEntityFormChooser(String beneficiaryEntityFormChooser) {
        this.beneficiaryEntityFormChooser = beneficiaryEntityFormChooser;
    }

    public String getBeneficiaryEntityIdColumnTextField() {
        return beneficiaryEntityIdColumnTextField;
    }

    public void setBeneficiaryEntityIdColumnTextField(String beneficiaryEntityIdColumnTextField) {
        this.beneficiaryEntityIdColumnTextField = beneficiaryEntityIdColumnTextField;
    }

    public String getIndividualFormChooser() {
        return individualFormChooser;
    }

    public void setIndividualFormChooser(String individualFormChooser) {
        this.individualFormChooser = individualFormChooser;
    }

    public List<RegistrationMode> getRegistrationModeList() {
        return registrationModeList;
    }

    public void setRegistrationModeList(List<RegistrationMode> registrationModeList) {
        this.registrationModeList = registrationModeList;
    }

    @Override
    public String toString() {
        return "HealthConfig{" +
                "module=" + module +
                ", registrationMode=" + registrationMode +
                ", beneficiaryEntityFormChooser='" + beneficiaryEntityFormChooser + '\'' +
                ", beneficiaryEntityIdColumnTextField='" + beneficiaryEntityIdColumnTextField + '\'' +
                ", individualFormChooser='" + individualFormChooser + '\'' +
                ", registrationModeList=" + registrationModeList +
                '}';
    }
}
