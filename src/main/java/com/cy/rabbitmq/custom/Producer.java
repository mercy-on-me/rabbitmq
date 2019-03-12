package com.cy.rabbitmq.custom;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * @ClassName : Producer
 * @Description : 自定义消费者
 * @Author : cy
 * @Date : 2019-03-12 13:33
 * @Version : V1.0
 */

/**
 * 生产者
 */
@SuppressWarnings("all")
public class Producer {

    public static void main(String[] args) throws Exception {

        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //配置工厂 IP 端口 虚拟主机
        connectionFactory.setHost("10.211.55.10");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        channel.confirmSelect();

        String exchangeName = "customConsumer_exchange";
        String routingKey = "customConsumer.test";
        String msg = "custom Consumer test!";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
            System.out.println("producer send message : " + msg);
        }

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("============ ack ============");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("============ no ack ============");
            }
        });
    }

}
