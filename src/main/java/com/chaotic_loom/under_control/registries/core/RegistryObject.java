package com.chaotic_loom.under_control.registries.core;

public abstract class RegistryObject {
    private ObjectIdentifier objectIdentifier;
    private boolean isPopulated = false;

    public ObjectIdentifier getObjectIdentifier() {
        if (objectIdentifier == null) {
            throw new RuntimeException("This RegistryObject is not populated yet");
        }

        return this.objectIdentifier;
    }

    public void setObjectIdentifier(ObjectIdentifier objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

    public String getIdentifierPath() {
        RegistryGroup<?> registryGroup = this.getObjectIdentifier().getRegistryGroup();
        return registryGroup.getRootDirectory() + "/" + this.getObjectIdentifier().getPath();
    }

    //Gets called when the game registers the item
    public void onPopulate() {
        this.isPopulated = true;
    }

    public boolean isPopulated() {
        return this.isPopulated;
    }
}