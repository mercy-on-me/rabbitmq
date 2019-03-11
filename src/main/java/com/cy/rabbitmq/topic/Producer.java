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
        String routingKey_1 = "topic_routingKey.save";
        String routingKey_2 = "topic_routingKey.update";
        String routingKey_3 = "topic_routingKey.update.delete";

        String msg_1 = "topic exchanege! [ topic_routingKey.save ]";
        String msg_2 = "topic exchanege! [ topic_routingKey.update ]";
        String msg_3 = "topic exchanege! [ topic_routingKey.update.delete ]";

        channel.basicPublish(exchangeName, routingKey_1, null, msg_1.getBytes());
        System.out.println("producer send message : " + msg_1);

        channel.basicPublish(exchangeName, routingKey_2, null, msg_2.getBytes());
        System.out.println("producer send message : " + msg_2);

        channel.basicPublish(exchangeName, routingKey_3, null, msg_3.getBytes());
        System.out.println("producer send message : " + msg_3);

        channel.close();
        connection.close();

    }
}
