package com.nookure.staff.api.util.transformer;

import com.nookure.staff.api.PlayerWrapper;
import org.jetbrains.annotations.NotNull;

public interface NameTagTransformer {
    void setPrefix(@NotNull PlayerWrapper player, @NotNull String prefix);

    void removePrefix(@NotNull PlayerWrapper player);

    void setSuffix(@NotNull PlayerWrapper player, @NotNull String suffix);

    void removeSuffix(@NotNull PlayerWrapper player);
}
