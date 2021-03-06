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
import io.github.stgallen.simpleannounce.command.AdminChannel;
import io.github.stgallen.simpleannounce.command.StaffChat;
import io.github.stgallen.simpleannounce.command.Shout;
import org.slf4j.Logger;

@Plugin(
  id = "staffchat",
  name = "StaffChat",
  version = "1.0",
  description = "staffchat",
  authors = {"STG_Allen"}
)
public class SimpleAnnounce {

  @Inject
  private Logger logger;

  @Inject
  private ProxyServer proxyServer;

  @Inject
  private StaffChat broadcastCommand;

  @Inject
  private Shout titleCommand;
  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    CommandManager manager = proxyServer.getCommandManager();
    LiteralCommandNode<CommandSource> broadcast = LiteralArgumentBuilder.<CommandSource>literal("staffchat")
      .requires(ctx -> ctx.hasPermission("staffchat.use"))
      .then(RequiredArgumentBuilder.<CommandSource, String>argument("message", StringArgumentType.greedyString())
        .executes(broadcastCommand::execute)
        .build())
      .build();
    manager.register(
      manager.metaBuilder("staffchat").aliases("sc").build(),
      new BrigadierCommand(broadcast)
    );

    LiteralCommandNode<CommandSource> shout = LiteralArgumentBuilder.<CommandSource>literal("shout")
            .requires(ctx -> ctx.hasPermission("shout.use"))
            .then(RequiredArgumentBuilder.<CommandSource, String>argument("message", StringArgumentType.greedyString())
                    .executes(titleCommand::execute)
                    .build())
            .build();
    manager.register(
            manager.metaBuilder("shout").aliases("s").build(),
            new BrigadierCommand(shout)
    );
    new AdminChannel(this, proxyServer);
  }
}
