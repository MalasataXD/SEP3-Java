package Server.Receiver.Channels.Shift;

import Database.Implementation.ShiftDao;
import Server.Receiver.Interfaces.IQueue;
import Server.Receiver.MQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RemoveShift implements IQueue {

    // < Fields
    private String Queue;
    private String Exchane;
    private Connection connection;
    private Channel channel;

    // < Constructor
    public RemoveShift(String queue, String exchange)
    {
        MQConfig mqConfig = MQConfig.getInstance();

        this.Queue = queue;
        Exchane = exchange;

        String URL = mqConfig.getURL();

        boolean durable = mqConfig.isDurable();
        boolean exclusive = mqConfig.isExclusive();
        boolean autoDelete = mqConfig.isAutoDelete();
        Map<String,Object> map = mqConfig.getMap();

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(URL);
            connection = factory.newConnection();
            channel = connection.createChannel();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public String GetQueue() {
        return Queue;
    }

    @Override
    public void run()
    {
        int count = 0;

        try {
            count = channel.queueDeclarePassive(Queue).getMessageCount();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (count != 0) {
            count--;
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.queueDeclare(Queue, false, false, false, null);
                //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                    System.out.println(" [RemoveShift] remove Shift");

                    int shiftId = Integer.parseInt(message);

                    ShiftDao shiftDao = ShiftDao.getInstance();
                    shiftDao.DeleteShift(shiftId);

                };
                channel.basicConsume(Queue, true, deliverCallback, consumerTag -> {
                });
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
