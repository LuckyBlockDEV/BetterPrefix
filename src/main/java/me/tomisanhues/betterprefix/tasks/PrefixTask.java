package me.tomisanhues.betterprefix.tasks;

import me.tomisanhues.betterprefix.BetterPrefix;
import me.tomisanhues.betterprefix.utils.Configuration;
import me.tomisanhues.betterprefix.utils.NametagChanger;
import me.tomisanhues.betterprefix.utils.Utils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PrefixTask extends BukkitRunnable {

    BetterPrefix betterprefix;

    public PrefixTask(BetterPrefix betterprefix) {
        this.betterprefix = betterprefix;
    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            return;
        }

        for (Player player: Bukkit.getOnlinePlayers()) {

            if (!Configuration.getChatEnabled()) {
                player.setDisplayName(Utils.setPlaceholders(player, Configuration.getChatFormat()));
            }

            if (Configuration.getTabEnabled()) {
                player.setPlayerListName(Utils.setPlaceholders(player, Configuration.getTabFormat()));
                player.setPlayerListOrder(rankWeight(player));
            }

            if (Configuration.getNametagEnabled()) {
                NametagChanger.changePlayerName(player, Utils.setPlaceholders(player, Configuration.getNametagFormatPrefix()), Utils.setPlaceholders(player, Configuration.getNametagFormatSuffix()), NametagChanger.TeamAction.UPDATE);
            }
        }
    }
    /**
     * The client sorts the tab list by this order first (highest on top), so players are listed by the
     * highest LuckPerms group weight they have; players with the same weight are sorted by name.
     */
    private static int rankWeight(Player player) {
        LuckPerms api = BetterPrefix.getApi();
        if (api == null) {
            return 0;
        }
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        return user.getInheritedGroups(user.getQueryOptions()).stream()
                .mapToInt(group -> group.getWeight().orElse(0))
                .max().orElse(0);
    }
}
