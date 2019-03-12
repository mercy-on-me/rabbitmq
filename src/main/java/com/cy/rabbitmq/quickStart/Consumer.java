package com.cy.rabbitmq.quickStart;

/**
 * @ClassName : Consumer
 * @Description : 消息消费者
 * @Author : cy
 * @Date : 2019-03-10 15:52
 * @Version : V1.0
 */

import com.rabbitmq.client.*;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * 消费者
 */
@SuppressWarnings("all")
public class Consumer {

    public static void main(String[] args) throws Exception {

        //1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.211.55.10");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");  //   "/"是RabbitMQ 默认的一个虚拟主机

        //2. 通过连接工厂创建一个连接
        Connection connection = connectionFactory.newConnection();

        //3. 通过连接创建一个 channel
        Channel channel = connection.createChannel();


        /**
         * 4. 通过 channel声明一个队列  queueDeclare
         *      第一个参数queue : 队列名称
         *
         *      第二个参数durable : 是否持久化,如果 true,代表持久化,服务即使重启,队列也不会消失
         *
         *      第三个参数exclusive : 是否独占,如果 true,则这个队列只有这一个 channel 可以连接,也就是说只有一个连接可以连接,其他的都不可以连接.
         *          比如顺序消费,比如说一共10条有序的消息,需要发给一个消费者去顺序的执行,但是一个集群模式,肯定是负载均衡的,这个集群有3个
         *          节点: A,B,C .A,B ,C 分别消费3条,但是如果不是独占的,就无法保证它们消费的数据有按顺序的,所有就用独占方式.
         *
         *      第四个参数autoDelete : 自动删除,队列如果与其他相关的Exchange(交换机)没有一个绑定关系,脱离了Exchange,那这个队列就会自动删除.
         *
         *      第五个参数arguments : 扩展参数
         */
        String queueName = "amqp-default";  //队列名称
        channel.queueDeclare(queueName,false,false,false,null);

        /**
         * 5. 创建 consumer 消费者对象
         *  JMS规定生产者要创建Procuder,消费者要创建一个 Consumer,通过 procuder和 consumer 来进行生产和消费
         *  RabbitMQ 规定生产者可以不用创建 procuder,可以直接通过 channel 来发送消息.
         *  但是 RabbitMQ 的消费者就必须创建一个 consumer 来消费消息 --- QueueingConsumer
         *
         *  参数 : 标识这个消费者要建立在哪个连接之上消费消息,就是一个 [ channel ]
         */
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        /**
         * 6. 设置 channel
         *      queue : 队列名称,指定要消费哪个队列的消息,就是监听哪个队列
         *
         *      autoAck : 是否自动接收. ack : 假设 broker 有一条信息发送到了 consumer 端,consumer会马上回送给 broker一条 ack 消息,告诉 broker
         *          这条信息我收到了. 如果 ack 为 true,则会自动签收.
         *          RabbitMQ 有两种签收方式 : 自动签收和手动签收.真正的工作环境中,基本上 [ 不会使用自动签收 ].因为如果要做消费端限流,一定要设置为 false.
         *
         *      callback : 具体的消费者对象
         */
        channel.basicConsume(queueName,true,queueingConsumer);

        /**
         * 7. 获取消息
         *      Delivery : rabbitmq 消息的对象,把消息,channel,producer 进行了 Java 级别的封装
         *          delivery = QueueingConsumer.nextDelivery(),这个对象里包含了消息的信息.比如Body, Exchange:是哪个 Exchange 过来的,routing key等
         *              envlope = delivery.getEnvelope() :
         *              deliveryTag = envlope.getDeliveryTag() : 做消息唯一性处理的时候会用到 deliveryTag. 进行 [手工签收] 的时候也会用到deliveryTag
         *
         *  nextDelivery() : 获取下一条消息
         *      无参 : 只要没有消息过来,就一直阻塞
         *      有参 : 参数为超时时间,消费者启动之后,会阻塞 超时时间 的一段时间,超时后如果还没有消息就会放行.
         */
        while (true){
            Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());   //获取消息内容
            Envelope envelope = delivery.getEnvelope();     //获取 envlope, 这个对象可以获取 routingKey,exchange,deliveryTag 等信息,其中 [ deliveryTag ] 等信息可以做消息唯一性处理等
            long deliveryTag = envelope.getDeliveryTag();
            String routingKey = envelope.getRoutingKey();
            String exchange = envelope.getExchange();
            System.out.println("消费端获取到消息 : " + msg);
            System.out.println("Exchange 为 : " + exchange);
            System.out.println("routingKey 为 : " + routingKey);
        }

    }


}
