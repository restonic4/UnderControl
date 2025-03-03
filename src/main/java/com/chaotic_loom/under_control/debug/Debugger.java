package com.chaotic_loom.under_control.debug;

import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.registries.core.RegistryObject;

public abstract class Debugger extends RegistryObject {
    private final ExecutionSide executionSide;

    public Debugger(ExecutionSide executionSide) {
        this.executionSide = executionSide;
    }

    public abstract void onInitialize();
    public abstract void onStart();
    public abstract void onStop();

    public ExecutionSide getExecutionSide() {
        return executionSide;
    }
}
