package com.brokergateway.WebSocket;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/webSocket-depth")
public class webSocket {
    private Session session;

    private static CopyOnWriteArraySet<webSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        System.out.println("webSocket has new connections and total connections:" + webSocketSet.size());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        System.out.println("webSocket disconnect,numbers:" + webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("get information: " + message);
    }

    public void sendMessage(JSONArray message) {
        System.out.println(message);
        for (webSocket webSocket1 : webSocketSet) {
            System.out.println("broadcast info ,message{}" + message);
            try {
                webSocket1.session.getBasicRemote().sendText(message.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
