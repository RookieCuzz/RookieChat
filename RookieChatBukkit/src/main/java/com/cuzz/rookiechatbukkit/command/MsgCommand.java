package com.cuzz.rookiechatbukkit.command;

import com.alipay.remoting.exception.RemotingException;
import com.cuzz.common.message.PlayerMsgMessage;
import com.cuzz.rookiechatbukkit.RookieChatBukkit;
import net.afyer.afybroker.client.Broker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Nipuru
 * @since 2023/1/23 9:48
 */
public class MsgCommand implements CommandExecutor {
    private final RookieChatBukkit plugin;

    public MsgCommand(RookieChatBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家才能运行此指令");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("/msg <player> msg");
            return true;
        }

        String receiver = args[0];
        StringBuilder msg = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            msg.append(args[i]).append(" ");
        }

        //消息主体
        PlayerMsgMessage message = new PlayerMsgMessage()
                .setSender(sender.getName())
                .setReceiver(receiver)
                .setMsg(msg.toString());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                //请求结果
                boolean result = Broker.invokeSync(message);
                if (!result) {
                    sender.sendMessage("玩家 " + receiver + " 不存在");
                }
            } catch (RemotingException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
