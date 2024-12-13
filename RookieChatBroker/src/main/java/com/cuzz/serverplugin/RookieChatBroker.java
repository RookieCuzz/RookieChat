package com.cuzz.serverplugin;

import com.cuzz.serverplugin.processor.PlayerMsgBrokerProcessor;
import net.afyer.afybroker.server.plugin.Plugin;

public final class RookieChatBroker extends Plugin {

    @Override
    public void onEnable(){
        System.out.println("注册ing");
        getServer().registerUserProcessor(new PlayerMsgBrokerProcessor());

    }

}
