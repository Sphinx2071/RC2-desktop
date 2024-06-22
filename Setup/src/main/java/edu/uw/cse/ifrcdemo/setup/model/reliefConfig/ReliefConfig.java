package edu.uw.cse.ifrcdemo.setup.model.reliefConfig;

import edu.uw.cse.ifrcdemo.setup.model.AbsConfig;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.AuthorizationType;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;
import java.util.List;

public class ReliefConfig extends AbsConfig {
    public AuthorizationType authorizationType;
    public List<AuthorizationType> authorizationTypeList;

    private ReliefConfig() {
    }

    public static ReliefConfig createConfig(){
        ReliefConfig reliefConfig = new ReliefConfig();
        reliefConfig.module = Module.RELIEF;
        return  reliefConfig;
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