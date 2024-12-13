package com.cuzz.rookiechatbukkit;

import com.cuzz.rookiechatbukkit.command.MsgCommand;
import com.cuzz.rookiechatbukkit.listener.PlayerChatListener;
import com.cuzz.rookiechatbukkit.processor.PlayerMsgBukkitProcessor;
import lombok.Getter;
import net.afyer.afybroker.client.Broker;
import net.afyer.afybroker.client.BrokerClientBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class RookieChatBukkit extends JavaPlugin {


    public static  RookieChatBukkit instance;

    @Getter
    private String serverPrefix = "[测试1服]";

    @Override
    public void onLoad(){
        Broker.buildAction(this::buildBroker);
    }

    private void buildBroker(BrokerClientBuilder builder){
        builder.registerUserProcessor(new PlayerMsgBukkitProcessor());
    }

    @Override
    public void onEnable(){
        instance= this;

        getServer().getPluginManager().registerEvents(new PlayerChatListener(),this);
        getCommand("msg").setExecutor(new MsgCommand(this)
        );
    }

}
