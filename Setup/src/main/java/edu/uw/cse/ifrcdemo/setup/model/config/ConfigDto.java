package edu.uw.cse.ifrcdemo.setup.model.config;

import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;

import java.util.List;

public class ConfigDto {
    private Module module;
    public List<Module> moduleList;

    public void setModule(Module module){
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<Module> moduleList) {
        this.moduleList = moduleList;
    }

    public ConfigDto() {
    }
}
