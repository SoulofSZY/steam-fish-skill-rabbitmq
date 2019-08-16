package com.szy.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author sunzhengyu
 * @create 2019/8/16
 * @since 1.0.0
 */
public class WorkerNew {

    private static final String QUEUE_NAME = "task_durable";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("steamedfish.site");
        factory.setPort(5672);
        factory.setUsername("szy");
        factory.setPassword("szy123");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        DeliverCallback callback = (consumerTag, deliver) -> {
            String message = new String(deliver.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");

            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(" [x] Done");
                channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
            }
        };

        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, callback, consumerTag -> {
        });
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}