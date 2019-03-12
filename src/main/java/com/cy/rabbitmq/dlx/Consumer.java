package com.cy.rabbitmq.dlx;

/**
 * @ClassName : Consumer
 * @Description :  死信队列演示
 * @Author : cy
 * @Date : 2019-03-11 23:59
 * @Version : V1.0
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者
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

        //指定死信队列的 Exchange 和 Queue,正常声明正常绑定,和正常的 Exchange 和 Queue 没有任何区别,只是作为死信队列使用
        String dlxExchangeName = "dlx_exchange";
        String dlxRoutingKey = "#";
        String dlxQueueName = "dlx_queue";
        channel.exchangeDeclare(dlxExchangeName, "topic", true, false, false, null);
        channel.queueDeclare(dlxQueueName, true, false, false, null);
        channel.queueBind(dlxQueueName, dlxExchangeName, dlxRoutingKey);



        //指定正常的 Exchange 和 Queue,正常的绑定,只是在 Queue 的扩展参数中指定 DLX
        String exchangeName = "dlx_exchange_default";
        String routingKey = "routingKey.#";
        String queueName = "dlx_queue_default";
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);

        //在正常的 Queue 的扩展参数中指定 死信交换器
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "dlx_exchange");    //dlx.change 是声明的作为DLX 的队列的名称
        channel.queueDeclare(queueName,false, false, false, arguments);
        channel.queueBind(queueName, exchangeName, routingKey);

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            Delivery delivery = queueingConsumer.nextDelivery();
            System.out.println(new String(delivery.getBody()));
        }
    }
}
