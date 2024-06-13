package edu.uw.cse.ifrcdemo.setup.model.relief;

import edu.uw.cse.ifrcdemo.setup.model.config.Configurable;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.AuthorizationType;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;

import java.util.List;

public class ReliefConfig extends Configurable {
    public AuthorizationType authorizationType;
    public List<AuthorizationType> authorizationTypeList;

    public ReliefConfig() {
    }

    public AuthorizationType getAuthorizationType() {
        return authorizationType;
    }

    public void setAuthorizationType(AuthorizationType authorizationType) {
        this.authorizationType = authorizationType;
    }

    public List<AuthorizationType> getAuthorizationTypeList() {
        return authorizationTypeList;
    }

    public void setAuthorizationTypeList(List<AuthorizationType> authorizationTypeList) {
        this.authorizationTypeList = authorizationTypeList;
    }

    @Override
    public String toString() {
        return "ReliefConfig{" +
                "authorizationType=" + authorizationType +
                ", authorizationTypeList=" + authorizationTypeList +
                ", module=" + module +
                ", registrationMode=" + registrationMode +
                ", beneficiaryEntityFormChooser='" + beneficiaryEntityFormChooser + '\'' +
                ", beneficiaryEntityIdColumnTextField='" + beneficiaryEntityIdColumnTextField + '\'' +
                ", individualFormChooser='" + individualFormChooser + '\'' +
                ", registrationModeList=" + registrationModeList +
                '}';
    }
}
