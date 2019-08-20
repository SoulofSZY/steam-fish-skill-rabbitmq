package com.szy.rabbitmq.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author sunzhengyu
 * @create 2019/8/20
 * @since 1.0.0
 */
public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void recv(String... args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("www.steamedfish.site");
        factory.setPort(5672);
        factory.setUsername("szy");
        factory.setPassword("szy123");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        for (String bindingKey : args) {
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback callback = (consumerTag, deliver) -> {
            String msg = new String(deliver.getBody(), "utf-8");
            System.out.println(" [x] Received '" + deliver.getEnvelope().getRoutingKey() + "':'" + msg + "'");
        };

        channel.basicConsume(queueName, true, callback, consumerTag -> {
        });

    }

    public static void main(String[] args) throws Exception {
        recv("error");
    }
}