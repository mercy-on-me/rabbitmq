package com.cy.rabbitmq.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : producer
 * @Description : Message 说明
 * @Author : cy
 * @Date : 2019-03-11 18:42
 * @Version : V1.0
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

        String msg = "hello rabbitmq!";
        String routingKey = "amqp-default";

        Map<String,Object> heads = new HashMap<>(); //定义自定义属性
        heads.put("arg_1","111");
        heads.put("arg_2","222");

        //定义附加属性,在生产者发送消息的时候,作为方法的参数设置进去
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                .deliveryMode(2) //常用的模式  1:非持久化投递,消息投送到服务器之后,如果没有被消费者消费的话,服务器重启后消息就不存在了
                                            // 2:持久化投递,消息投送到服务器之后,服务器重启后,消息依然存在
                .contentEncoding("UTF-8")
                .expiration("5000")  //过期时间,毫秒,如果过了过期时间没有被消费的话, 会自动被移除
                .headers(heads)     //如果现有的属性已经满足不了需求,可以使用此方法添加自定义属性
                .build();

        for (int i = 0; i < 5; i++) {
            /**
             * 通过 channel 发送数据
             *      参数:
             *          s : 指定Exchange
             *          s1 : 指定routing key
             *          basicProperties : 消息的附加属性
             *          最后一个是消息的内容
             *
             */
            channel.basicPublish("",routingKey,basicProperties,msg.getBytes());
            System.out.println("生产者生产消息 : " + msg);
        }

        //一定要关闭连接,从小到大关闭
        channel.close();
        connection.close();
    }

}
