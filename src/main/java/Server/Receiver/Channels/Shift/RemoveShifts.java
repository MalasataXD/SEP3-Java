package Server.Receiver.Channels.Shift;

import Database.Dto.ShiftDTO;
import Database.Implementation.ShiftDao;
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
import jakarta.persistence.criteria.CriteriaBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RemoveShifts implements IQueue {
    private String queue;
    private String exchange;
    private Connection connection;
    private Channel channel;

    private String URL;

    private String action;
    private boolean durable;
    private boolean exclusive;
    private boolean autoDelete;
    private Map<String,Object> map;

    public RemoveShifts(String queue, String exchange) {
        MQConfig mqConfig = MQConfig.getInstance();

        // ¤ Returns action, this handles.
        // ---------------------------------------------
        action = "RemoveShifts";
        // ---------------------------------------------

        this.queue = queue;
        this.exchange = exchange;

        //Get from config
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

    @Override public String getQueue() {
        return queue;
    }

    @Override public void run() {
        int count = 0;

        try {
            count = channel.queueDeclarePassive(queue).getMessageCount();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (count != 0) {
            count--;
            try
            {
                // ¤ RabbitMQ setup
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(URL);
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                // Declare the queue, so we can use it.
                channel.queueDeclare(queue, durable, exclusive, autoDelete, map);

                DeliverCallback deliverCallback = (consumerTag, delivery) ->
                {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                    // Get the json with the info, to make a messageheader
                    ObjectMapper mapper = new ObjectMapper();
                    MessageHeader messageHeader = mapper.readValue(message,MessageHeader.class);

                    // Convert Json to make Messageheader
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

                    // Get the payload of the messageheader
                    String Payload = ow.writeValueAsString(messageHeader.payload);
                    //---------------------------------------------
                    List<Integer> list = mapper.readValue(Payload, new TypeReference<List<Integer>>(){});

                    // Delete to Database using Dao.
                    ShiftDao shiftDao = ShiftDao.getInstance();
                    for (Integer item: list)
                    {
                        shiftDao.DeleteShift(item);
                    }

                    // Returns the answer
                    Sender sender = Sender.getInstance();
                    sender.send(new MessageHeader(messageHeader.getQueue(), action, true));
                    //---------------------------------------------
                };
                channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
                });
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
