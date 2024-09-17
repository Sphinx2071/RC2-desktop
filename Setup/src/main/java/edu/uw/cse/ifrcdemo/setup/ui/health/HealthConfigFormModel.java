package edu.uw.cse.ifrcdemo.setup.ui.health;

import edu.uw.cse.ifrcdemo.setup.ui.AbsConfig;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;

public class HealthConfigFormModel extends AbsConfig {
    private HealthConfigFormModel(){

    }

    public static HealthConfigFormModel createConfig(){
        HealthConfigFormModel healthConfigFormModel = new HealthConfigFormModel();
        healthConfigFormModel.module = Module.HEALTH;
        return healthConfigFormModel;
    }

    @Override
    public String toString() {
        return "HealthConfigFormModel{" +
                "module=" + module +
                ", registrationMode=" + registrationMode +
                ", beneficiaryEntityFormChooser='" + beneficiaryEntityFormChooser + '\'' +
                ", beneficiaryEntityIdColumnTextField='" + beneficiaryEntityIdColumnTextField + '\'' +
                ", individualFormChooser='" + individualFormChooser + '\'' +
                ", registrationModeList=" + registrationModeList +
                '}';
    }
}
