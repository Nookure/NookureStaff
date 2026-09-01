package com.nookure.staff.paper.command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.nookure.staff.api.command.Command;
import com.nookure.staff.api.command.CommandManager;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.common.CommandConfig;
import com.nookure.staff.api.config.common.CommandPartial;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PaperCommandManager extends CommandManager {
    private static final CommandMap commandMap;

    static {
        try {
            commandMap = (CommandMap) Bukkit.getServer()
                    .getClass()
                    .getDeclaredMethod("getCommandMap")
                    .invoke(Bukkit.getServer());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get command map");
        }
    }

    @Inject
    private Injector injector;

    @Inject
    private ConfigurationContainer<CommandConfig> commandConfig;

    @Override
    public void registerCommand(@NotNull Command command) {
        TemplateCommand templateCommand = new TemplateCommand(command);
        injector.injectMembers(templateCommand);
        CommandPartial commandPartial;

        if (!commandConfig
                .get()
                .getCommands()
                .containsKey(command.getCommandData().name())) {
            commandPartial = new CommandPartial(
                    command.getCommandData().name(),
                    List.of(command.getCommandData().aliases()),
                    command.getCommandData().permission(),
                    command.getCommandData().description(),
                    command.getCommandData().usage(),
                    true);

            commandConfig.get().getCommands().put(command.getCommandData().name(), commandPartial);
            commandConfig.save().join();
        } else {
            commandPartial = commandConfig
                    .get()
                    .getCommands()
                    .get(command.getCommandData().name());
        }

        templateCommand.setName(commandPartial.name());

        if (commandPartial.aliases() != null && !commandPartial.aliases().isEmpty())
            templateCommand.setAliases(commandPartial.aliases());
        if (commandPartial.permission() != null && !commandPartial.permission().isEmpty())
            templateCommand.setPermission(commandPartial.permission());
        if (commandPartial.description() != null
                && !commandPartial.description().isEmpty())
            templateCommand.setDescription(commandPartial.description());
        if (commandPartial.usage() != null && !commandPartial.usage().isEmpty())
            templateCommand.setUsage(commandPartial.usage());

        commandMap.register("nkstaff", templateCommand);
        command.prepare();
    }

    @Override
    public void unregisterCommand(@NotNull Command command) {
        org.bukkit.command.Command cmd =
                commandMap.getCommand(command.getCommandData().name());

        if (cmd != null) {
            cmd.unregister(commandMap);
        }
    }
}
