package com.cy.rabbitmq.ack;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : Producer
 * @Description : ack 和 重回队列 演示
 * @Author : cy
 * @Date : 2019-03-12 15:42
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

        //通过连接工厂创建连接body
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "ack_exchange";
        String routingKey = "ack.test";

        for (int i = 0; i < 5; i++) {
            String msg = "ack test! " + i;
            Map<String,Object> heads = new HashMap<>();
            heads.put("num", i);

            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                    .deliveryMode(2) //常用的模式  1:非持久化投递,消息投送到服务器之后,如果没有被消费者消费的话,服务器重启后消息就不存在了
                    // 2:持久化投递,消息投送到服务器之后,服务器重启后,消息依然存在
                    .contentEncoding("UTF-8")
                    .expiration("5000")  //过期时间,毫秒,如果过了过期时间没有被消费的话, 会自动被移除
                    .headers(heads)     //如果现有的属性已经满足不了需求,可以使用此方法添加自定义属性
                    .build();   //完成构建

            channel.basicPublish(exchangeName,routingKey,basicProperties, msg.getBytes());

        }
    }
}
