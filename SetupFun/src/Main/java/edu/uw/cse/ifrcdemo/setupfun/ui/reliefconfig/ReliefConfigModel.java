package edu.uw.cse.ifrcdemo.setupfun.ui.reliefconfig;

import edu.uw.cse.ifrcdemo.setupfun.ui.common.CloudEndpointAuthFormModel;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.AuthorizationType;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
import java.util.List;


public class ReliefConfigModel {

    private AuthorizationType authorizationType;
    private RegistrationMode  regMode;
    private String  beneficiaryEntityFormChooser;
    private String  beneficiaryEntityIdColumnTextField;
    private String  individualFormChooser;
    private List<AuthorizationType> authorizationTypeList;
    private List<RegistrationMode> registrationModeList;

    public ReliefConfigModel() {

    }

    public AuthorizationType getAuthorizationType() {
        return authorizationType;
    }

    public void setAuthorizationType(AuthorizationType authorizationType) {
        this.authorizationType = authorizationType;
    }

    public RegistrationMode getRegMode() {
        return regMode;
    }

    public void setRegMode(RegistrationMode regMode) {
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

    public List<AuthorizationType> getAuthorizationTypeList() {
        return authorizationTypeList;
    }

    public void setAuthorizationTypeList(List<AuthorizationType> authorizationTypeList) {
        this.authorizationTypeList = authorizationTypeList;
    }

    public List<RegistrationMode> getRegistrationModeList() {
        return registrationModeList;
    }

    public void setRegistrationModeList(List<RegistrationMode> registrationModeList) {
        this.registrationModeList = registrationModeList;
    }

    @Override
    public String toString() {
        return "ReliefConfigModel{" +
                "authorizationType='" + authorizationType + '\'' +
                ", regMode='" + regMode + '\'' +
                ", beneficiaryEntityFormChooser='" + beneficiaryEntityFormChooser + '\'' +
                ", beneficiaryEntityIdColumnTextField='" + beneficiaryEntityIdColumnTextField + '\'' +
                ", individualFormChooser='" + individualFormChooser + '\'' +
                ", authorizationTypeList=" + authorizationTypeList +
                ", registrationModeList=" + registrationModeList +
                '}';
    }
}
