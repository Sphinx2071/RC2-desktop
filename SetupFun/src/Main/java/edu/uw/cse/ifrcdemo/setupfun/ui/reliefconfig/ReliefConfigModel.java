package edu.uw.cse.ifrcdemo.setupfun.ui.reliefconfig;

import edu.uw.cse.ifrcdemo.setupfun.ui.common.CloudEndpointAuthFormModel;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;

import javax.validation.constraints.NotEmpty;

public class ReliefConfigModel {
    private String  regMode;
    private String  beneficiaryEntityFormChooser;
    private String  beneficiaryEntityIdColumnTextField;
    private String  individualFormChooser;

    public String getRegMode() {
        return regMode;
    }

    public void setRegMode(String regMode) {
        this.regMode = regMode;
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

    @Override
    public String toString() {
        return "ReliefConfigModel{" +
                "regMode='" + regMode + '\'' +
                ", beneficiaryEntityFormChooser='" + beneficiaryEntityFormChooser + '\'' +
                ", beneficiaryEntityIdColumnTextField='" + beneficiaryEntityIdColumnTextField + '\'' +
                ", individualFormChooser='" + individualFormChooser + '\'' +
                '}';
    }
}
