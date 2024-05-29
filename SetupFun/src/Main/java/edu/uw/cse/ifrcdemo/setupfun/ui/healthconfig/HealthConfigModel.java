package edu.uw.cse.ifrcdemo.setupfun.ui.healthconfig;

import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.AuthorizationType;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;

import java.util.List;

public class HealthConfigModel {
    private RegistrationMode registrationMode;
    private String  beneficiaryEntityFormChooser;
    private String  beneficiaryEntityIdColumnTextField;
    private String  individualFormChooser;

    private List<RegistrationMode> registrationModeList;

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
        return "HealthConfigModel{" +
                "registrationMode=" + registrationMode +
                ", beneficiaryEntityFormChooser='" + beneficiaryEntityFormChooser + '\'' +
                ", beneficiaryEntityIdColumnTextField='" + beneficiaryEntityIdColumnTextField + '\'' +
                ", individualFormChooser='" + individualFormChooser + '\'' +
                ", registrationModeList=" + registrationModeList +
                '}';
    }
}
