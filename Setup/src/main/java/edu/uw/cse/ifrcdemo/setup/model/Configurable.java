package edu.uw.cse.ifrcdemo.setup.model;

import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.Module;

public interface Configurable {
    void setModule(Module module);

    Module getModule();

}
