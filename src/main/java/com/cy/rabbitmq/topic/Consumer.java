package com.cy.rabbitmq.topic;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
    }

}
