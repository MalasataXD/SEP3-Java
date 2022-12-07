package Server.Receiver.Channels.Shift;

import Database.Dto.ShiftDTO;
import Database.Implementation.ShiftDao;
import Database.Implementation.WorkerDao;
import Server.Receiver.Implementations.MessageHeaders.MessageHeader;
import Server.Receiver.Implementations.Sender;
import Server.Receiver.Interfaces.IQueue;
import Server.Receiver.MQConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class CreateShifts implements IQueue {
    // < Fields
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

    public CreateShifts(String queue, String exchane) {
        MQConfig mqConfig = MQConfig.getInstance();

        // ---------------------------------------------
        action = "CreateShifts";
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
                    System.out.println("JSON: " + Payload);
                    //---------------------------------------------
                    //string => object
                    List<ShiftDTO> list = mapper.readValue(Payload, new TypeReference<List<ShiftDTO>>(){});


                    //skriv til dao/DB
                    ShiftDao shiftDao = ShiftDao.getInstance();
                    for (ShiftDTO item: list) {
                        shiftDao.CreateShift(item);
                    }
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
