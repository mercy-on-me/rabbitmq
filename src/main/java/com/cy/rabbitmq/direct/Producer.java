package com.cy.rabbitmq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @ClassName : Producer
 * @Description : Exchange 类型 : direct 演示
 * @Author : cy
 * @Date : 2019-03-11 15:14
 * @Version : V1.0
 */

/**
 * 生产者
 */
@SuppressWarnings("all")
public class Producer {

    public static void main(String[] args) throws  Exception {

        // 1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.211.55.10");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 2. 创建连接
        Connection connection = connectionFactory.newConnection();

        // 3. 创建 channel
        Channel channel = connection.createChannel();

        // 4. 声明消息要发送到的 Exchange 的名称,routingKEy
        String exchangeName = "direct_exchange";    //要和消费者内声明 Exchange 时指定的名称完全相同
        String routingKey = "direct_routingKey";    //要和消费者内Exchange 和 Queue 绑定是指定的 routingKey完全相同

        // 5. 通过 channel 发送信息
        String msg = "direct exchangge,通过 routingKey 将exchange 和 Queue 绑定";

        // 6. 发送消息
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());
            System.out.println(" producer end msg : " + msg);
        }

//        // 7. 关闭连接
//        channel.close();
//        connection.close();
    }
}
