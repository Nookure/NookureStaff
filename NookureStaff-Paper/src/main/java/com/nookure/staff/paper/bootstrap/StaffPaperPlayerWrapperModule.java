package com.nookure.staff.paper.bootstrap;

import static java.util.Objects.requireNonNull;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.annotation.staff.StaffChatAsDefaultBool;
import com.nookure.staff.api.annotation.staff.StaffModeBool;
import com.nookure.staff.api.database.model.StaffStateModel;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import com.nookure.staff.paper.data.ServerStaffModeData;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class StaffPaperPlayerWrapperModule extends AbstractModule {
    private final StaffPaperPlayerWrapper staffPaperPlayerWrapper;
    private final Player player;
    private final AtomicReference<ServerStaffModeData> serverStaffModeData;
    private final AtomicReference<StaffStateModel> staffStateModel;
    private final AtomicBoolean staffModeEnabled;
    private final AtomicBoolean staffChatEnabled;

    public StaffPaperPlayerWrapperModule(
            @NotNull final StaffPaperPlayerWrapper staffPaperPlayerWrapper,
            @NotNull final AtomicReference<ServerStaffModeData> serverStaffModeData,
            @NotNull final AtomicReference<StaffStateModel> staffStateModel,
            @NotNull final AtomicBoolean staffModeEnabled,
            @NotNull final AtomicBoolean staffChatEnabled) {
        requireNonNull(staffPaperPlayerWrapper, "StaffPaperPlayerWrapper cannot be null");

        this.staffPaperPlayerWrapper = staffPaperPlayerWrapper;
        this.player = staffPaperPlayerWrapper.getPlayer();
        this.serverStaffModeData = serverStaffModeData;
        this.staffStateModel = staffStateModel;
        this.staffModeEnabled = staffModeEnabled;
        this.staffChatEnabled = staffChatEnabled;
    }

    @Override
    protected void configure() {
        bind(StaffPaperPlayerWrapper.class).toInstance(staffPaperPlayerWrapper);
        bind(StaffPlayerWrapper.class).toInstance(staffPaperPlayerWrapper);
        bind(Player.class).toInstance(player);

        bind(AtomicBoolean.class).annotatedWith(StaffChatAsDefaultBool.class).toInstance(staffChatEnabled);

        bind(AtomicBoolean.class).annotatedWith(StaffModeBool.class).toInstance(staffModeEnabled);

        bind(new TypeLiteral<AtomicReference<StaffStateModel>>() {}).toInstance(staffStateModel);

        bind(new TypeLiteral<AtomicReference<ServerStaffModeData>>() {}).toInstance(serverStaffModeData);
    }
}
