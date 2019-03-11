package com.cy.rabbitmq.returnListener;

/**
 * @ClassName : Producer
 * @Description :  RabbitMQ 投递消息机制 : [ return机制 ] 演示
 * @Author : cy
 * @Date : 2019-03-12 01:05
 * @Version : V1.0
 */

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 生产者
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

        String exchangeName = "test_return_exchange";
        String routingKey = "routingKey.return";
        String routingKeyError = "routingKey_1.return";

        String msg = "return test!";
        //设置 mandatory 为 true,在 Exchange 不存在或者 routingKey 路由不到队列的情况下,会监听路由不到的信息
        channel.basicPublish(exchangeName, routingKeyError, true, null,msg.getBytes());

        // 添加 return listener,重写 handleReturn 方法
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange,String routingKey,
                                     AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("================= handle return ==================");
                System.out.println("replyCode : " + replyCode);
                System.out.println("replyText : " + replyText);
                System.out.println("exchange : " + exchange);
                System.out.println("routingKey : " + routingKey);
                System.out.println("properties : " + properties);
                System.out.println("body : " + new String(body));
            }
        });

    }
}
