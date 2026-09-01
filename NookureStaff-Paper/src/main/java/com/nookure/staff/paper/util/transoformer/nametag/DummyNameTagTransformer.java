package com.nookure.staff.paper.util.transoformer.nametag;

import com.google.inject.Singleton;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.util.transformer.NameTagTransformer;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DummyNameTagTransformer implements NameTagTransformer {
    @Override
    public void setPrefix(@NotNull PlayerWrapper player, @NotNull String prefix) {}

    @Override
    public void removePrefix(@NotNull PlayerWrapper player) {}

    @Override
    public void setSuffix(@NotNull PlayerWrapper player, @NotNull String suffix) {}

    @Override
    public void removeSuffix(@NotNull PlayerWrapper player) {}
}
