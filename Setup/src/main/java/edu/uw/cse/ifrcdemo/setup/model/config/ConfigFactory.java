package edu.uw.cse.ifrcdemo.setup.model.config;

import edu.uw.cse.ifrcdemo.setup.model.health.HealthConfig;
import edu.uw.cse.ifrcdemo.setup.model.relief.ReliefConfig;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;

public class ConfigFactory {
    public static Configurable createConfig(ConfigDto configDto) {
        if ((Module.RELIEF).equals(configDto.getModule())) {
            ReliefConfig reliefConfig = new ReliefConfig();
            reliefConfig.setModule(Module.RELIEF);
            return reliefConfig;
        } else if ((Module.HEALTH).equals(configDto.getModule())) {
            HealthConfig healthConfig = new HealthConfig();
            healthConfig.setModule(Module.HEALTH);
            return healthConfig;
        } else
            throw new IllegalArgumentException("Invalid module.");
    }
}
