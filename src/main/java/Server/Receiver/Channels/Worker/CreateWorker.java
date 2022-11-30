package Server.Receiver.Channels.Worker;


import Database.Dto.WorkerDTO;
import Database.Implementation.WorkerDao;
import Server.Receiver.Implementations.MessageHeaders.MessageHeader;
import Server.Receiver.Interfaces.IQueue;
import Server.Receiver.MQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class CreateWorker implements IQueue {

    private String Queue;
    private String Exchane;
    private Connection connection;
    private Channel channel;

    public CreateWorker(String queue, String exchane) {
        MQConfig mqConfig = MQConfig.getInstance();

        this.Queue = queue;
        Exchane = exchane;

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
    public void run() {
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

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                    System.out.println(" [CreateWorker] Created new worker " + message);

                    ObjectMapper mapper = new ObjectMapper();
                    MessageHeader messageHeader = mapper.readValue(message,MessageHeader.class);

                    System.out.println("test1 " + messageHeader.payload);

                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    String Payload = ow.writeValueAsString(messageHeader.payload);
                    System.out.println("Json: " + Payload);

                    Object object = mapper.readValue(Payload, WorkerDTO.class);
                    WorkerDTO dto = (WorkerDTO) object;

                    System.out.println("test2");
                    WorkerDao workerDao = WorkerDao.getInstance();
                    workerDao.CreateWorker(dto);

                    System.out.println("test3");
                };
                channel.basicConsume(Queue, true, deliverCallback, consumerTag -> {
                });
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
