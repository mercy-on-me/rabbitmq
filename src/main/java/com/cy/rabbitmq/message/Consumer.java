package com.cy.rabbitmq.message;

/**
 * @ClassName : Consumer
 * @Description : Message
 * @Author : cy
 * @Date : 2019-03-10 15:52
 * @Version : V1.0
 */

import com.rabbitmq.client.*;
import com.rabbitmq.client.QueueingConsumer.Delivery;

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
        channel.confirmSelect();



        String queueName = "amqp-default";  //队列名称
        channel.queueDeclare(queueName,true,false,false,null);

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        channel.basicConsume(queueName,true,queueingConsumer);

        while (true){
            Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());   //获取消息内容
            AMQP.BasicProperties properties = delivery.getProperties(); //获取附加属性
            System.out.println(properties.getContentEncoding());    //获取编码
            Map<String, Object> headers = properties.getHeaders();  //获取 headers
            System.out.println(headers.get("arg_1"));   //获取 headers 内的自动以属性
            System.out.println(headers.get("arg_2"));   //获取 headers 内的自动以属性
            System.out.println("=========");
        }

    }


}
