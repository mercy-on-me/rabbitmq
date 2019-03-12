package com.cy.rabbitmq.custom;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @ClassName : Customer
 * @Description : 自定义消费者 演示
 * @Author : cy
 * @Date : 2019-03-12 13:32
 * @Version : V1.0
 */

/**
 * 消费者
 */
@SuppressWarnings("all")
public class Customer {

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

        String exchangeName = "customConsumer_exchange";
        String routingKey = "customConsumer.#";
        String queueName = "customConsumer_queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        //使用自定义消费者
        channel.basicConsume(queueName, true, new MyConsumer(channel));

    }
}
