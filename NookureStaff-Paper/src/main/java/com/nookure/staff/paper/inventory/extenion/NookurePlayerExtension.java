package com.nookure.staff.paper.inventory.extenion;

import com.google.inject.Inject;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NookurePlayerExtension extends AbstractExtension implements Function {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Override
    public Map<String, Function> getFunctions() {
        return Map.of("nookurePlayer", this);
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        String key = (String) args.get("key");

        try {
            UUID uuid = UUID.fromString(key);
            Optional<PlayerWrapper> playerWrapper = playerWrapperManager.getPlayerWrapper(uuid);

            if (playerWrapper.isPresent()) {
                return playerWrapper.get();
            }
        } catch (IllegalArgumentException e) {
            Player p = Bukkit.getPlayer(key);

            if (p != null) {
                return playerWrapperManager.getPlayerWrapper(p);
            }
        }

        return null;
    }

    @Override
    public List<String> getArgumentNames() {
        return List.of("key");
    }
}
