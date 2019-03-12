package com.cy.rabbitmq.qos;

/**
 * @ClassName : Consumer
 * @Description : 消费者限流 演示
 * @Author : cy
 * @Date : 2019-03-12 14:38
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

        String exchangeName = "qos_exchange";
        String routingKey = "qos.#";
        String queueName = "qos_queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        /**
         * QOS重要步骤 : 设置 qos
         *  prefetchSize : 消息限制的大小,一般设置为 0
         *  prefetchCount : 生产者一次最多给消费者发送多少条消息,一般设置为 1 条,就是说在生产者一次只能消费者发送1条消息,在消费者确认后,在发送下一条
         *  global : 限流策略的级别, 两种级别 : channel,consumer,一般在 consumer 级别使用
         *            true : channel 级别
         *            false : consumer 级别
         */
        channel.basicQos(0, 1, false);

        //QOS重要步骤 : 指定消费者 consumer 要监听的队列 queueName, 并设置为非自动签收
        channel.basicConsume(queueName, false, new MyConsumer_qos(channel));



    }

}
