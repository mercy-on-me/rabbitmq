package com.cy.rabbitmq.quickStart;

/**
 * @ClassName : Producer
 * @Description : 消息生产者
 * @Author : cy
 * @Date : 2019-03-10 15:51
 * @Version : V1.0
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者要做两件事: 生产消息  发送消息
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

        //通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();
        /**
         * 通过连接创建 channel, 通信信道
         *      JMS规定生产者要创建Procuder,消费者要创建一个 Consumer,通过 procuder和 consumer 来进行生产和消费
         *      RabbitMQ 规定生产者可以不用创建 procuder,可以直接通过 channel 来发送消息.
         *      但是 RabbitMQ 的消费者就必须创建一个 consumer 来消费消息
         */
        Channel channel = connection.createChannel();

        String msg = "hello rabbitmq!";
        //routing key
        String routingKey = "amqp-default";
        /**
         * 通过 channel 发送数据
         *      参数:
         *          s : 指定Exchange
         *          s1 : 指定routing key
         *          basicProperties : 消息的附加属性
         *          最后一个是消息的内容
         *
         */
        for (int i = 0; i < 5; i++) {
            channel.basicPublish("",routingKey,null,msg.getBytes());
            System.out.println("生产者生产消息 : " + msg);
        }

        //一定要关闭连接,从小到大关闭
        channel.close();
        connection.close();
    }

}
