package com.kob.backend.consumer;

/**
 * @author :王冰冰
 * @date : 2022/9/18
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    // 线程安全的map
    public final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>(); // 对所有实例可见，全局变量
    private User user;
    private Session session = null;
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private static RestTemplate restTemplate;
    private static final String addPlayerUrl = "http://127.0.0.1:3001/player/add";
    private static final String removePlayerUrl = "http://127.0.0.1:3001/player/remove";
    private Game game = null;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接, 身份的验证
        this.session = session;
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public static void startGame(Integer aId, Integer bId) {
        User userA = userMapper.selectById(aId);
        User userB = userMapper.selectById(bId);
        Game game = new Game(13, 14, 16, aId, bId);
        game.createMap();
        game.start();
        if (users.get(aId) != null) {
            users.get(aId).game = game;
        }
        if (users.get(bId) != null) {
            users.get(bId).game = game;
        }
        JSONObject respA = getResMessage(userB, getStartLocation(game));
        if (users.get(aId) != null) {
            users.get(aId).sendMessage(respA.toJSONString());
        }
        JSONObject respB = getResMessage(userA, getStartLocation(game));
        if (users.get(bId) != null) {
            users.get(bId).sendMessage(respB.toJSONString());
        }
    }

    private void move(int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        JSONObject data = JSON.parseObject(message);
        String event = data.getString("event");
        if (Objects.equals(event, "start-matching")) {
            startMatching();
        } else if (Objects.equals(event, "stop-matching")) {
            stopMatching();
        } else if (Objects.equals(event, "move")) {
            move(data.getInteger("direction"));
        }
    }

    private void startMatching() {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.set("user_id", this.user.getId().toString());
        data.set("rating", this.user.getRating().toString());
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    private static JSONObject getResMessage(User x, JSONObject game) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", "start-matching");
        jsonObject.put("opponent_username", x.getUserName());
        jsonObject.put("opponent_photo", x.getPhoto());
        jsonObject.put("game", game);
        return jsonObject;
    }

    private static JSONObject getStartLocation(Game game) {
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());
        return respGame;
    }


    private void stopMatching() {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.set("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}