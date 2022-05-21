package io.github.stgallen.simpleannounce.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.stgallen.simpleannounce.SimpleAnnounce;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;

public class AdminChannel {

    public ArrayList<Player> users;
    private ProxyServer server;

    public AdminChannel(SimpleAnnounce plugin, ProxyServer proxyServer) {
        this.server = proxyServer;
        users = new ArrayList<>();
        LiteralCommandNode<CommandSource> admin = LiteralArgumentBuilder.<CommandSource>literal("admin")
                .requires(ctx -> ctx.hasPermission("adminchat.use"))
                .executes(this::execute)
                .build();
        proxyServer.getCommandManager().register(
                proxyServer.getCommandManager().metaBuilder("admin").aliases("adminchat").build(),
                new BrigadierCommand(admin)
        );
        proxyServer.getEventManager().register(plugin, this);
    }

    public int execute(CommandContext<CommandSource> ctx) {
        if(ctx.getSource() instanceof Player) {
            Player player = (Player) ctx.getSource();
            if(users.contains(player)) {
                users.remove(player);
                player.sendMessage(Component.text("No longer using admin chat").color(NamedTextColor.AQUA));
            } else {
                users.add(player);
                player.sendMessage(Component.text("Now using admin chat").color(NamedTextColor.AQUA));
            }
        }
        return 1;
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        if(users.contains(event.getPlayer())) {
            Component deserialized = Component.text()
                    .append(Component.text()
                            .color(NamedTextColor.RED)
                            .append(Component.text("["))
                            .build()
                    )
                    .append(Component.text()
                            .color(NamedTextColor.DARK_RED)
                            .append(Component.text("Admin"))
                            .build()
                    )
                    .append(Component.text()
                            .color(NamedTextColor.RED)
                            .append(Component.text("] "))
                            .build()
                    )
                    .append(Component.text()
                            .color(NamedTextColor.DARK_RED)
                            .append(Component.text(event.getPlayer().getUsername()))
                            .build()
                    )
                    .append(Component.text()
                            .color(NamedTextColor.RED)
                            .append(Component.text(": "))
                            .build()
                    )
                    .append(LegacyComponentSerializer.legacyAmpersand().deserialize("&c" + event.getMessage()))
                    .build();
            server.getAllPlayers().stream().filter(target -> target.hasPermission("adminchat.use")).forEach(player -> player.sendMessage(deserialized));
            event.setResult(PlayerChatEvent.ChatResult.denied());
        }
    }
}
