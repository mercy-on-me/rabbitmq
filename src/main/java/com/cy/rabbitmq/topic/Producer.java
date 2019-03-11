package com.cy.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @ClassName : Producer
 * @Description : TODO
 * @Author : cy
 * @Date : 2019-03-11 16:21
 * @Version : V1.0
 */
@SuppressWarnings("all")
public class Producer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.211.55.10");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "topic_exchange";
        String routingKEy = "topic_routingKey.test";
        String msg = "topic exchanege!";

        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName, routingKEy, null, msg.getBytes());
        }

    }
}
