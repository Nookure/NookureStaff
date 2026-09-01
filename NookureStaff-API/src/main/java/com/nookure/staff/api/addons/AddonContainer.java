package com.nookure.staff.api.addons;

import com.nookure.staff.api.addons.annotations.Addon;

public interface AddonContainer {
    AddonDescription getDescription();

    Object getInstance();

    void setInstance(Object instance);

    AddonStatus getStatus();

    void setStatus(AddonStatus status);

    Addon getAddon();
}
