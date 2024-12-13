package com.cuzz.serverplugin.processor;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import com.cuzz.common.message.PlayerMsgMessage;
import lombok.Setter;
import net.afyer.afybroker.server.Broker;
import net.afyer.afybroker.server.BrokerServer;
import net.afyer.afybroker.server.aware.BrokerServerAware;
import net.afyer.afybroker.server.proxy.BrokerClientItem;
import net.afyer.afybroker.server.proxy.BrokerClientManager;
import net.afyer.afybroker.server.proxy.BrokerPlayer;

import java.util.Collection;
import java.util.List;

/**
 * @author Nipuru
 * @since 2023/1/23 9:44
 */
public class PlayerMsgBrokerProcessor extends SyncUserProcessor<PlayerMsgMessage> implements BrokerServerAware {

    @Setter
    BrokerServer brokerServer;
    static {
        System.out.println("******************** PlayerMsgBrokerProcessor");
    }
    @Override
    public Object handleRequest(BizContext bizCtx, PlayerMsgMessage request) throws Exception {
        System.out.println("get request!!!!!!!!!!!!!!!!!");
        String sender = request.getSender();
        String receiver = request.getReceiver();

        if (receiver.equalsIgnoreCase("@ALL")){

            List<BrokerClientItem> list = Broker.getServer().getClientManager().list();
            for (BrokerClientItem client : list) {
                client.oneway(request);
            }

            return false;
        }

        BrokerPlayer senderPlayer = brokerServer.getPlayer(sender);
        BrokerPlayer receiverPlayer = brokerServer.getPlayer(receiver);

        //发送者或接受者不存在则返回false
        if (senderPlayer == null || receiverPlayer == null) {
            return false;
        }


        BrokerClientItem senderBukkit = senderPlayer.getServer();
        BrokerClientItem receiverBukkit = receiverPlayer.getServer();

        String address = senderBukkit.getAddress();
        System.out.println(" debug address "+address);
        //发送者或接受所在的bukkit代理不存在则返回false
        if (senderBukkit == null || receiverBukkit == null) {
            return false;
        }

        //如果发送者和接受者在同一个服务器则往这个服务器发消息
        if (senderBukkit == receiverBukkit) {
            senderBukkit.oneway(request);
            return true;
        }

        //分别往发送者和接受者服务器发消息
        senderBukkit.oneway(request);
        receiverBukkit.oneway(request);
        return true;
    }

    @Override
    public String interest() {
        return PlayerMsgMessage.class.getName();
    }
}
