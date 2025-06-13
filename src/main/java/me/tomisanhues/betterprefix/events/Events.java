package me.tomisanhues.betterprefix.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.tomisanhues.betterprefix.BetterPrefix;
import me.tomisanhues.betterprefix.utils.Configuration;
import me.tomisanhues.betterprefix.utils.NametagChanger;
import me.tomisanhues.betterprefix.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {

    public BetterPrefix betterprefix;

    public Events(BetterPrefix betterprefix) {
        this.betterprefix = betterprefix;
    }


    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (Configuration.getNametagEnabled()) {
            Player player = event.getPlayer();
            NametagChanger.changePlayerName(player, "", "", NametagChanger.TeamAction.DESTROY);
        }
    }

    @EventHandler
    public void aSyncPlayerChatEvent(AsyncChatEvent event) {
        if (Configuration.getChatEnabled()) {
            // Set the new message
            event.renderer(((source, sourceDisplayName, message, viewer) -> {
                // Convert format string to a Component
                String formatStr = Utils.setPlaceholders(source, Configuration.getChatFormat(), "");
                // Create a formatted component
                Component formatted = Component.text(formatStr)
                        .append(message);
                return formatted;
            }));
        }
    }
}
