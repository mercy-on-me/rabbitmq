package com.cy.rabbitmq.ack;

import com.rabbitmq.client.*;
import com.rabbitmq.client.Consumer;

import java.io.IOException;

/**
 * @ClassName : MyConsumer_ack
 * @Description : TODO
 * @Author : cy
 * @Date : 2019-03-12 15:50
 * @Version : V1.0
 */
public class MyConsumer_ack extends DefaultConsumer {

    private Channel channel;

    public MyConsumer_ack(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

        System.out.println("============= consumer message =============");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int num = (Integer)properties.getHeaders().get("num");
        // 分别设置 ack 和 nack 的操作
        if( num == 0) {
            /**
             * 当消息内的属性 num 为 1 时,进行 nack,就是说把这条数据作为失败失败
             *   第二个参数 : 是否批量处理
             *  最后一个参数 : 是否重回队列
             *      true : 重回队列,会将失败的消息返回给broker,
             *      false : 不重回队列
             */
            channel.basicNack(envelope.getDeliveryTag(), false, true);
            System.out.println(new String(body));
            System.out.println(num);

        } else {
            //ack 操作
            channel.basicAck(envelope.getDeliveryTag(), false);
            System.out.println(new String(body));
        }


    }
}
