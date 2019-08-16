package com.szy.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 〈一句话功能简述〉<br>
 * 〈RabbitMQ will send each message to the next consumer, in sequence. On average every consumer will get the same number of messages. This way of distributing messages is called round-robin〉
 *  1. rabbitMQ 默认会轮询的给消费者发送消息
 * @author sunzhengyu
 * @create 2019/8/16
 * @since 1.0.0
 */
public class Worker {

    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("steamedfish.site");
        factory.setPort(5672);
        factory.setUsername("szy");
        factory.setPassword("szy123");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DeliverCallback callback = (consumerTag, deliver) -> {
            String message = new String(deliver.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");

            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME, autoAck, callback, consumerTag -> {
        });
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}