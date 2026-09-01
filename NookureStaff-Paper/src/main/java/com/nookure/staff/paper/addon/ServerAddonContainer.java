package com.nookure.staff.paper.addon;

import com.nookure.staff.api.addons.AddonContainer;
import com.nookure.staff.api.addons.AddonDescription;
import com.nookure.staff.api.addons.AddonStatus;
import com.nookure.staff.api.addons.annotations.Addon;

public class ServerAddonContainer implements AddonContainer {
    private final AddonDescription description;
    private Object instance;
    private AddonStatus status = AddonStatus.LOADED;

    public ServerAddonContainer(AddonDescription description) {
        this.description = description;
        this.instance = description.getMain();
    }

    @Override
    public AddonDescription getDescription() {
        return description;
    }

    @Override
    public Object getInstance() {
        return instance;
    }

    @Override
    public void setInstance(Object instance) {
        this.instance = instance;
    }

    @Override
    public AddonStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(AddonStatus status) {
        this.status = status;
    }

    @Override
    public Addon getAddon() {
        return description.getAddon();
    }
}
