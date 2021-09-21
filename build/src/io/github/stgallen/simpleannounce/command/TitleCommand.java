package io.github.stgallen.simpleannounce.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;

public class TitleCommand {

  @Inject
  private ProxyServer proxyServer;

  public int execute(CommandContext<CommandSource> ctx) {
    proxyServer.getAllPlayers().forEach(player -> player.showTitle(
      Title.title(
        LegacyComponentSerializer.legacyAmpersand().deserialize(ctx.getArgument("message", String.class)),
        Component.empty())
      )
    );
    return 1;
  }
}
