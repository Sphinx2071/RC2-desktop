package edu.uw.cse.ifrcdemo.setup.ui.relief;

import edu.uw.cse.ifrcdemo.setup.ui.AbsConfig;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.AuthorizationType;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;
import java.util.List;

public class ReliefConfigFormModel extends AbsConfig {
    public AuthorizationType authorizationType;
    public List<AuthorizationType> authorizationTypeList;

    private ReliefConfigFormModel() {
    }

    public static ReliefConfigFormModel createConfig(){
        ReliefConfigFormModel reliefConfigFormModel = new ReliefConfigFormModel();
        reliefConfigFormModel.module = Module.RELIEF;
        return reliefConfigFormModel;
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
        return "ReliefConfigFormModel{" +
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