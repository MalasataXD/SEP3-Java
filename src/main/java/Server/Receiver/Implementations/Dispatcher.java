package Server.Receiver.Implementations;


import Server.Receiver.Interfaces.IDispatcher;
import Server.Receiver.MQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Dispatcher implements IDispatcher
{

    private String URL;
    private String QUEUE;
    private Connection connection;
    private Channel channel;
    private Skeleton skeleton;

    public Dispatcher(String QUEUE, Skeleton skeleton) {
        MQConfig mqConfig = MQConfig.getInstance();

        this.URL = mqConfig.getURL();
        this.QUEUE = QUEUE;
        this.skeleton = skeleton;

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(mqConfig.getURL());
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(
                    QUEUE,
                    mqConfig.isDurable(),
                    mqConfig.isExclusive(),
                    mqConfig.isAutoDelete(),
                    mqConfig.getMap()
            );

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection()
    {
        return connection;
    }

    @Override
    public void startOperation(String operation)
    {
        skeleton.openChannel(operation);
    }

    @Override
    public void run()
    {
        while (true) {
            int count = 0;
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                count = channel.queueDeclarePassive(QUEUE).getMessageCount();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }

            if (count != 0) {
                System.out.println("hello");
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("localhost");
                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();
                    channel.queueDeclare(QUEUE, false, false, false, null);
                    //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                        System.out.println(" [Dispatcher] Received '" + message + "'");
                        startOperation(message);

                    };
                    channel.basicConsume(QUEUE, true, deliverCallback, consumerTag -> {
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
