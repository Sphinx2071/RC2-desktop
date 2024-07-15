package edu.uw.cse.ifrcdemo.setup.model.healthConfig;

import edu.uw.cse.ifrcdemo.setup.model.AbsConfig;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;

public class HealthConfig extends AbsConfig {
    private HealthConfig(){

    }

    public static HealthConfig createConfig(){
        HealthConfig healthConfig = new HealthConfig();
        healthConfig.module = Module.HEALTH;
        return healthConfig;
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
