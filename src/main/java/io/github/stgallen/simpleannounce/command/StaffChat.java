package io.github.stgallen.simpleannounce.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class StaffChat {

    @Inject
  private ProxyServer proxyServer;


  public String getName(CommandSource source) {
    if (source instanceof Player)
      return ((Player)source).getUsername();
    return "Console";
  }


  public String getServerName(CommandSource source) {
      if (source instanceof Player)
          return ((Player)source).getCurrentServer().get().getServerInfo().getName();
      return "proxy";
  }

  public int execute(CommandContext<CommandSource> ctx) {
      if (ctx.getSource().hasPermission("staffchat.use")) {
          Component deserialized = Component.text()
                  .append(Component.text()
                          .color(NamedTextColor.RED)
                          .append(Component.text("["))
                          .build()
                  )
                  .append(Component.text()
                          .color(NamedTextColor.DARK_RED)
                          .append(Component.text("Staff"))
                          .build()
                  )
                  .append(Component.text()
                          .color(NamedTextColor.RED)
                          .append(Component.text("] "))
                          .build()
                  )
//                  .append(Component.text()
//                          .color(NamedTextColor.DARK_GRAY)
//                          .append(Component.text("["))
//                          .build()
//                  )
//                  .append(Component.text()
//                          .color(NamedTextColor.AQUA)
//                          .append(Component.text(getServerName(ctx.getSource())))
//                          .build()
//                  )
//                  .append(Component.text()
//                          .color(NamedTextColor.DARK_GRAY)
//                          .append(Component.text("] "))
//                          .build()
//                  )
                  .append(Component.text()
                          .color(NamedTextColor.DARK_RED)
                          .append(Component.text(getName(ctx.getSource())))
                          .build()
                  )
                  .append(Component.text()
                          .color(NamedTextColor.RED)
                          .append(Component.text(": "))
                          .build()
                  )
                  .append(LegacyComponentSerializer.legacyAmpersand().deserialize("&c" + ctx.getArgument("message", String.class)))
                  .build();
          proxyServer.getAllPlayers().stream().filter(target -> target.hasPermission("staffchat.use")).forEach(player -> player.sendMessage(deserialized));
      }else {
          Component deserialized = Component.text()
                  .append(Component.text()
                          .color(NamedTextColor.RED)
                          .append(Component.text("You do not have permission."))
                          .build()
                  )
                  .build();
      }
      return 1;
  }
}
