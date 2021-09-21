package io.github.stgallen.simpleannounce;

import com.google.inject.Inject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.stgallen.simpleannounce.command.BroadcastCommand;
import io.github.stgallen.simpleannounce.command.TitleCommand;
import org.slf4j.Logger;

@Plugin(
  id = "shout",
  name = "Shout",
  version = "1.0",
  description = "shout",
  authors = {"Brqdford"}
)
public class SimpleAnnounce {

  @Inject
  private Logger logger;

  @Inject
  private ProxyServer proxyServer;

  @Inject
  private BroadcastCommand broadcastCommand;

  @Inject
  private TitleCommand titleCommand;

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    LiteralCommandNode<CommandSource> broadcast = LiteralArgumentBuilder.<CommandSource>literal("shout")
      .requires(ctx -> ctx.hasPermission("shout.use"))
      .then(RequiredArgumentBuilder.<CommandSource, String>argument("message", StringArgumentType.greedyString())
        .executes(broadcastCommand::execute)
        .build())
      .build();

    LiteralCommandNode<CommandSource> title = LiteralArgumentBuilder.<CommandSource>literal("title")
      .requires(ctx -> ctx.hasPermission("title.use"))
      .then(RequiredArgumentBuilder.<CommandSource, String>argument("message", StringArgumentType.greedyString())
        .executes(titleCommand::execute)
        .build())
      .build();

    CommandManager manager = proxyServer.getCommandManager();
    manager.register(
      manager.metaBuilder("shout").aliases("s").build(),
      new BrigadierCommand(broadcast)
    );
    manager.register(
      manager.metaBuilder("title").aliases("alerttitle").build(),
      new BrigadierCommand(title)
    );
  }
}
