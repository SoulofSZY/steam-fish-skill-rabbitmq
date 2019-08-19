package com.szy.rabbitmq.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author sunzhengyu
 * @create 2019/8/19
 * @since 1.0.0
 */
public class EmitLog {

    private static final String EXCHANGE_NAME = "app_logs";

    public static void emit(String... args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("www.steamedfish.site");
        factory.setPort(5672);
        factory.setUsername("szy");
        factory.setPassword("szy123");


        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = args.length < 1 ? "info: hello world!" : String.join(" ", args);

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

            System.out.println("[x] Sent '" + message + "'");

        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        emit();
    }
}