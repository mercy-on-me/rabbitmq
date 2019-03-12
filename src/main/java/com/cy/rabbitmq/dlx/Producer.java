package com.cy.rabbitmq.dlx;

/**
 * @ClassName : Producer
 * @Description : 死信队列演示
 * @Author : cy
 * @Date : 2019-03-11 23:59
 * @Version : V1.0
 */

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 生产者
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

        //指定消息投递模式 : 确认模式
        channel.confirmSelect();

        String exchangeName = "dlx_exchange_default";
        String routingKey = "routingKey.delx";

        String msg = "dlx test!";

        //设置过期时间,让发送的这条信息在到达指定时间后变为死信
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                .expiration("5000")
                .build();
        channel.basicPublish(exchangeName, routingKey, basicProperties, msg.getBytes());

    }
}
