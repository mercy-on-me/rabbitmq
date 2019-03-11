package com.cy.rabbitmq.topic;

import com.rabbitmq.client.*;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * @ClassName : Consumer
 * @Description : TODO
 * @Author : cy
 * @Date : 2019-03-11 16:24
 * @Version : V1.0
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

        String exchangeName = "topic_exchange";
        String exchangeType = "topic";
        //String routingKey = "topic_routingKey.#"; //可以匹配到多个
        String routingKey = "topic_routingKey.*";   //只能匹配到一个
        String queueName = "topic_queueName";

        channel.exchangeDeclare(exchangeName,exchangeType, true, false, false, null);
        channel.queueDeclare(queueName, false, false, false, null);

        channel.queueBind(queueName, exchangeName, routingKey);
        //当时如果前面运行过 .# ,然后再次运行 * 的话,Queue 就会有两个绑定键,一个 # 和一个 * ,所以要进行解绑,要么在管控台解绑,要么使用以下代码进行解绑
        //channel.queueUnbind(queueName, exchangeName, routingKey_1); //解绑,将使用 routingKey_1 为路由键的 Exchange 和 Queue 解绑.

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName,true,queueingConsumer);

        while (true) {
            Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            Envelope envelope = delivery.getEnvelope();
            String exchange = envelope.getExchange();
            String routingKey_2 = envelope.getRoutingKey();
            System.out.println("exchange : " + exchange);
            System.out.println("routingKey : " + routingKey_2);
            System.out.println("msg : " + msg);
            System.out.println("======================================");
        }


    }

}
