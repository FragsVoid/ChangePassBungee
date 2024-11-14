package org.frags.changePassBungee;

import com.nickuc.login.api.nLoginAPI;
import com.nickuc.login.api.types.AccountData;
import com.nickuc.login.api.types.Identity;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Optional;

public class ChangePass extends Command {

    private final ChangePassBungee plugin;
    public ChangePass(String name, ChangePassBungee plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer))
            return;

        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if (args.length != 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("incorrect-syntax")));
            return;
        }
        
        Identity identity = Identity.ofKnownName(player.getName());

        Optional<AccountData> accountData = nLoginAPI.getApi().getAccount(identity);
        boolean compare = accountData.get().comparePassword(args[0]);
        if (compare) {
            plugin.getProxy().getScheduler().runAsync(plugin, () -> {
                nLoginAPI.getApi().changePassword(identity, args[1]);
            });
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("contraseña-cambiada")));
            return;
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("contraseña-erronea")));
    }
}
