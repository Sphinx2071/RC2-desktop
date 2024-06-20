package edu.uw.cse.ifrcdemo.setup.model.reliefConfig;

import edu.uw.cse.ifrcdemo.setup.model.Configurable;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.AuthorizationType;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;

import java.util.List;

public class ReliefConfig implements Configurable {
    public Module module;
    public AuthorizationType authorizationType;
    public RegistrationMode registrationMode;
    public String beneficiaryEntityFormChooser;
    public String beneficiaryEntityIdColumnTextField;
    public String individualFormChooser;
    public List<AuthorizationType> authorizationTypeList;
    public List<RegistrationMode> registrationModeList;

    private ReliefConfig() {
    }

    public static ReliefConfig createReliefConfig(){
        ReliefConfig reliefConfig = new ReliefConfig();
        reliefConfig.module = Module.RELIEF;
        return  reliefConfig;
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public void setModule(Module module) {
        this.module = module;
    }

    public AuthorizationType getAuthorizationType() {
        return authorizationType;
    }

    public void setAuthorizationType(AuthorizationType authorizationType) {
        this.authorizationType = authorizationType;
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
        return "ReliefConfig{" +
                "module=" + module +
                ", authorizationType=" + authorizationType +
                ", registrationMode=" + registrationMode +
                ", beneficiaryEntityFormChooser='" + beneficiaryEntityFormChooser + '\'' +
                ", beneficiaryEntityIdColumnTextField='" + beneficiaryEntityIdColumnTextField + '\'' +
                ", individualFormChooser='" + individualFormChooser + '\'' +
                ", authorizationTypeList=" + authorizationTypeList +
                ", registrationModeList=" + registrationModeList +
                '}';
    }
}