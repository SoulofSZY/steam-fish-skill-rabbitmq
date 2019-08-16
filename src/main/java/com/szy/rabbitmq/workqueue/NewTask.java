package com.szy.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author sunzhengyu
 * @create 2019/8/16
 * @since 1.0.0
 */
public class NewTask {

    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("steamedfish.site");
        factory.setPort(5672);
        factory.setUsername("szy");
        factory.setPassword("szy123");

        try (Connection conn = factory.newConnection(); Channel channel = conn.createChannel();) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            String message = "hello...";
            IntStream.range(0, 100).forEach(i -> {
                String  sendMsg = message + i;
                try {
                    channel.basicPublish("", QUEUE_NAME, null, sendMsg.getBytes());
                    System.out.println(" [x] Sent '" + sendMsg + "'");
                }catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}