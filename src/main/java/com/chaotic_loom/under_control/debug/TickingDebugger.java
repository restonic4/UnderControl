package com.chaotic_loom.under_control.debug;

import com.chaotic_loom.under_control.core.annotations.ExecutionSide;

public abstract class TickingDebugger extends Debugger {
    private boolean enabled = false;

    public TickingDebugger(ExecutionSide executionSide) {
        super(executionSide);
    }

    public abstract void onTick();

    public void internalTick() {
        if (this.enabled) {
            this.onTick();
        }
    }

    public void enable() {
        this.enabled = true;
        onStart();
    }

    public void disable() {
        this.enabled = false;
        onStop();
    }
}
