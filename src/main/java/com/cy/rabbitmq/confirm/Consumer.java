package com.cy.rabbitmq.confirm;

/**
 * @ClassName : Consumer
 * @Description :  RabbitMQ 投递消息机制 : [ confirm 消息确认机制 ] 演示
 * @Author : cy
 * @Date : 2019-03-11 23:59
 * @Version : V1.0
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * 生产者
 */
@SuppressWarnings("all")
public class Consumer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.211.55.10");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "routingKey.#";
        String queueName = "test_connfitm_queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName,false, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            Delivery delivery = queueingConsumer.nextDelivery();
            System.out.println(new String(delivery.getBody()));
        }

    }

}
