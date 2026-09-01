package com.nookure.staff.paper.factory;

import com.nookure.staff.api.config.bukkit.partials.CustomItemPartial;
import com.nookure.staff.paper.item.CustomCommandItem;
import org.jetbrains.annotations.NotNull;

public interface CustomCommandItemFactory {
    @NotNull CustomCommandItem create(@NotNull final CustomItemPartial customItemPartial);
}
