package io.github.stgallen.simpleannounce.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class BroadcastCommand {

  @Inject
  private ProxyServer proxyServer;

  public String getName(CommandSource source) {
    if (source instanceof Player)
      return ((Player)source).getUsername();
    return "?";
  }

  public int execute(CommandContext<CommandSource> ctx) {
    Component deserialized = Component.text()
            .append(Component.text()
                    .color(NamedTextColor.DARK_GRAY)
                    .append(Component.text("["))
                    .build()
            )
      .append(Component.text()
        .color(NamedTextColor.GREEN)
        .append(Component.text("SHOUT"))
        .build()
      )
            .append(Component.text()
                    .color(NamedTextColor.DARK_GRAY)
                    .append(Component.text("] "))
                    .build()
            )
            .append(Component.text()
                    .color(NamedTextColor.WHITE)
                    .append(Component.text(getName(ctx.getSource())))
                    .build()
            )
            .append(Component.text()
                    .color(NamedTextColor.WHITE)
                    .append(Component.text(": "))
                    .build()
            )
      .append(LegacyComponentSerializer.legacyAmpersand().deserialize("&a" + ctx.getArgument("message", String.class)))
      .build();
    proxyServer.getAllPlayers().forEach(player -> player.sendMessage(deserialized));
    return 1;
  }
}
