package com.cy.rabbitmq.qos;

import com.rabbitmq.client.*;
import com.rabbitmq.client.Consumer;

import java.io.IOException;

/**
 * @ClassName : MyConsumer_qos
 * @Description : 消费端限流 演示
 * @Author : cy
 * @Date : 2019-03-12 14:41
 * @Version : V1.0
 */
public class MyConsumer_qos extends DefaultConsumer {

    //定义成员变量 channel,因为下边要做 basicAck
    private Channel channel;

    public MyConsumer_qos(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("========== handle delivery ==========");
        System.out.println("consumerTag : " + consumerTag);
        System.out.println("envelope : " + envelope);
        System.out.println("properties : " + properties);
        System.out.println("body : " + new String(body));

        /**
         *  QOS重要步骤 : 主动的给broker 一个ack
         *       void basicAck(long deliveryTag, boolean multiple)
         *                deliveryTag
         *                multiple : 是否批量接收
         *  如果不做此操作,就不会主动的 broker 应答,就会认为这条信息还没消费完,就不会发送下一条数据
         *
         */
        channel.basicAck(envelope.getDeliveryTag(), false);

    }
}
