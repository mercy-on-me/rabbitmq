package com.cy.rabbitmq.confirm;

/**
 * @ClassName : Producer
 * @Description : RabbitMQ 投递消息机制 : [ confirm 消息确认机制 ] 演示
 * @Author : cy
 * @Date : 2019-03-11 23:59
 * @Version : V1.0
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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

        String exchangeName = "test_confirm_exchange";
        String routingKey = "routingKey.confirm";

        String msg = "confirm test!";
        channel.basicPublish(exchangeName, routingKey,null, msg.getBytes());


        //添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {

            //投递成功了执行此方法
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("=============  ack  =============");
            }

            //投递失败执行此方法
            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("============= nack  =============");
            }
        });

    }
}
