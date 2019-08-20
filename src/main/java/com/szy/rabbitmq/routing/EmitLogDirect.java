package com.szy.rabbitmq.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 〈一句话功能简述〉<br>
 * 〈rabbitmq消息路由〉
 * 1. direct/topic 类型支持路由
 * 2. exchange和queue绑定指定binding key  消息推送指定routing key
 * 3. 一个exchange可以和一个queue指定多个binding key, 多个queue可以和exchange指定相同的binding key
 * 4. 实现 按需处理消息
 *
 * @author sunzhengyu
 * @create 2019/8/20
 * @since 1.0.0
 */
public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void send(String... args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("www.steamedfish.site");
        factory.setPort(5672);
        factory.setUsername("szy");
        factory.setPassword("szy123");

        try (Connection conn = factory.newConnection(); Channel channel = conn.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String severity = args[0];
            String msg = args[1];

            channel.basicPublish(EXCHANGE_NAME, severity, null, msg.getBytes());
            System.out.println("[x] Sent '" + msg + "'");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        send("error", "error: 这是一个错误日志");
        send("warn", "warning: 这是一个警告日志");
        send("info", "info: 这是一个普通日志");

    }
}