package com.szy.rabbitmq.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import javax.xml.crypto.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author sunzhengyu
 * @create 2019/8/19
 * @since 1.0.0
 */
public class ReceiveLogs {

    private static final String EXCHANGE_NAME = "app_logs";

    public static void recive() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("www.steamedfish.site");
        factory.setPort(5672);
        factory.setUsername("szy");
        factory.setPassword("szy123");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 1. 声明创建 exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 2. 声明创建 queue 由rabbit服务端创建随机对列名
        String queueName = channel.queueDeclare().getQueue();
        // 3. 绑定队列到指定exchange
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, deliver) -> {
            String msg = new String(deliver.getBody(), "utf-8");
            System.out.println(" [x] Received '" + msg + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

    }

    public static void main(String[] args) throws Exception {
        recive();
    }
}