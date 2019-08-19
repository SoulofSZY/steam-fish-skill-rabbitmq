package com.szy.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 〈一句话功能简述〉<br>
 * 〈RabbitMQ will send each message to the next consumer, in sequence. On average every consumer will get the same number of messages. This way of distributing messages is called round-robin〉
 *  1. rabbitMQ 默认会轮询的给消费者发送消息，消息是预先分配，若有消费者挂掉，分配将略过该消费者的消息，其他消费者消费完预分配的消息之后 才会去消费挂掉的消费者的消息
 *  2. 消息持久化
 *
 *  防止消息丢失的方式
 *  1. 取消自动确认机制
 *  2. 申明队列持久化，指定消息持久化
 *
 *  去除消息预分配
 *  1. 指定prefetchCount 指定消息分配数量
 * @author sunzhengyu
 * @create 2019/8/16
 * @since 1.0.0
 */
public class WorkerManual {

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
            }finally {
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