package com.cy.rabbitmq.ack;

/**
 * @ClassName : Consumer
 * @Description : ack 和 重回队列 演示
 * @Author : cy
 * @Date : 2019-03-12 15:46
 * @Version : V1.0
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消费者
 */
@SuppressWarnings("all")
public class Consumer {

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

        String exchangeName = "ack_exchange";
        String routingKey = "ack.test";
        String queueName = "ack_queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 设置点 1 : 设置为 非自动接收就是使用 [ 手工 ACK ]
        channel.basicConsume(queueName, false, new MyConsumer_ack(channel));

    }
}
