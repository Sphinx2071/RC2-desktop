package edu.uw.cse.ifrcdemo.setup.model.health;

import edu.uw.cse.ifrcdemo.setup.model.config.Configurable;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;

import java.util.List;

public class HealthConfig extends Configurable {

    public HealthConfig() {
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
