package Server.Receiver.Channels.Worker;

import Database.Dto.WorkerDTO;
import Database.Implementation.WorkerDao;
import Server.Receiver.Implementations.MessageHeaders.MessageHeader;
import Server.Receiver.Implementations.Sender;
import Server.Receiver.Interfaces.IQueue;
import Server.Receiver.MQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class GetWorkerById implements IQueue {
    private String Queue;
    private String Exchane;
    private Connection connection;
    private Channel channel;

    private String URL;

    private String action;
    private boolean durable;
    private boolean exclusive;
    private boolean autoDelete;
    private Map<String,Object> map;

    public GetWorkerById(String queue, String exchane) {
        MQConfig mqConfig = MQConfig.getInstance();

        // ---------------------------------------------
        action = "GetWorkerById";
        // ---------------------------------------------

        this.Queue = queue;
        Exchane = exchane;

        //from config
        URL = mqConfig.getURL();
        durable = mqConfig.isDurable();
        exclusive = mqConfig.isExclusive();
        autoDelete = mqConfig.isAutoDelete();
        map = mqConfig.getMap();

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
                factory.setHost(URL);
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.queueDeclare(Queue, durable, exclusive, autoDelete, map);

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                    // Json med info til at lave messageHeader => obj af messageHeader
                    ObjectMapper mapper = new ObjectMapper();
                    MessageHeader messageHeader = mapper.readValue(message,MessageHeader.class);

                    // payload er i "Test" => Json
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

                    // Json => string
                    String Payload = ow.writeValueAsString(messageHeader.payload);

                    //string => object
                    Object object = mapper.readValue(Payload, WorkerDTO.class);

                    // TODO: 01-12-2022
                    //--------------------------------------------- der skal laves en getByIdInDao
                    //cast til det object der skal bruges
                    int workerId = (int) object;

                    //skriv til dao/DB
                    WorkerDao workerDao = WorkerDao.getInstance();
                    WorkerDTO workerDTO = workerDao.GetWorker(workerId);

                    Sender sender = Sender.getInstance();
                    sender.send(new MessageHeader(messageHeader.getQueue(), action, workerDTO));
                    //---------------------------------------------
                };
                channel.basicConsume(Queue, true, deliverCallback, consumerTag -> {
                });
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
