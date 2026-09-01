package com.nookure.staff.api.addons;

import com.nookure.staff.api.addons.annotations.Addon;

public class AddonDescriptionBuilder {
    private Addon addon;
    private Object main;

    public AddonDescriptionBuilder setAddon(Addon addon) {
        this.addon = addon;
        return this;
    }

    public AddonDescriptionBuilder setMain(Object main) {
        this.main = main;
        return this;
    }

    public AddonDescription build() {
        return new AddonDescription(addon, main);
    }
}
