package com.cuzz.rookiechatbukkit.listener;

import com.alipay.remoting.exception.RemotingException;
import com.cuzz.common.message.PlayerMsgMessage;
import com.cuzz.rookiechatbukkit.RookieChatBukkit;
import net.afyer.afybroker.client.Broker;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class PlayerChatListener implements Listener {

    @EventHandler
    public void playerChatEvent(AsyncPlayerChatEvent event){
        event.setCancelled(true);
        PlayerMsgMessage playerMsgMessage = new PlayerMsgMessage();
        playerMsgMessage.setMsg(event.getMessage());
        playerMsgMessage.setSender(event.getPlayer().getName());
        playerMsgMessage.setReceiver("@ALL");
        playerMsgMessage.setServer(RookieChatBukkit.instance.getServerPrefix());
        System.out.println("say hi");
        Bukkit.getScheduler().runTaskAsynchronously(RookieChatBukkit.instance, () -> {
            try {
                //请求结果
                boolean result = Broker.invokeSync(playerMsgMessage);
                System.out.println(" result is" + result);
            } catch (RemotingException | InterruptedException e) {
                e.printStackTrace();
            }
        });


    }


}
