package com.cy.rabbitmq.direct;

/**
 * @ClassName : Consumer
 * @Description : Exchange 类型 : direct 演示
 * @Author : cy
 * @Date : 2019-03-11 15:20
 * @Version : V1.0
 */

import com.rabbitmq.client.*;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * 消费者
 */
@SuppressWarnings("all")
public class Consumer {

    public static void main(String[] args) throws Exception{

        // 1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.211.55.10");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 2. 创建连接
        Connection connection = connectionFactory.newConnection();

        // 3. 创建 channel
        Channel channel = connection.createChannel();


        String exchangeName = "direct_exchange";
        String routingkEy = "direct_routingKey";
        String queueName = "direct_queue";
        String exchanggeType = "direct";

        // 4. 声明一个Exchange
        // 参数列表 : ExchangeName,ExchangeType,是否持久化,是否独占,是否自动删除,扩展参数
        channel.exchangeDeclare(exchangeName,exchanggeType,true,false,false,null);

        // 5. 声明一个 Queue
        // 参数列表 : queueName,是否持久化,是否独占,是否自动删除,扩展参数
        channel.queueDeclare(queueName,false,false,false,null);

        // 6. 将 Exchange 和 Queue 通过 routingKEy 绑定关系
        channel.queueBind(queueName,exchangeName,routingkEy);

        // 7. 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        // 8.设置 channel
        // 参数列表 : 要监听的队列, 是否自动接收, 具体的消费者
        channel.basicConsume(queueName,true,queueingConsumer);

        // 9. 接收信息
        while (true) {
            Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            Envelope envelope = delivery.getEnvelope();
            String exchange = envelope.getExchange();
            String routingKey = envelope.getRoutingKey();
            System.out.println("exchange : " + exchange);
            System.out.println("routingKey : " + routingKey);
            System.out.println("msg : " + msg);
            System.out.println("======================================");
        }

    }
}
