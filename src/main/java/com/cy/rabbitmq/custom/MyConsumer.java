package com.cy.rabbitmq.custom;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @ClassName : MyConsumer_qos
 * @Description : 自定义消费者 演示
 * @Author : cy
 * @Date : 2019-03-12 13:38
 * @Version : V1.0
 */

/**
 * MyConsumer类,在消费者中 basicConsue()中指定使用哪个消费者来消费指定的消息队列的消息
 */

public class MyConsumer extends DefaultConsumer {

    public MyConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.err.println("========== handle delivery start ==========");
        System.err.println("consumerTag " + consumerTag);
        System.err.println("envelope " + envelope);
        System.err.println("properties " + properties);
        System.err.println("body " + new String(body));
        System.err.println("========== handle delivery end ==========");
    }
}
