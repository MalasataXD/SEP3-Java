package Server.Receiver.Channels.Shift;

import Database.Dto.SearchShiftParametersDto;
import Database.Dto.ShiftDTO;
import Database.Implementation.ShiftDao;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class GetShiftBySearchParameters implements IQueue {

    // # Fields

    // RabbitMQ Setup
    private String queue;
    private String exchange;
    private Connection connection;
    private Channel channel;
    private String URL;
    private boolean durable;
    private boolean exclusive;
    private boolean autoDelete;

    // Action
    private String action;
    private Map<String,Object> map;

    // ¤ Constructor
    public GetShiftBySearchParameters(String queue, String exchange) {
        MQConfig mqConfig = MQConfig.getInstance();

        // ¤ Tell it what queue to use
        // ---------------------------------------------
        action = "GetShiftBySearchParameters";
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

    // ¤ Returns action, this handles
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
                    //Convert to SearchShiftParametersDto from JSON
                    Object object = mapper.readValue(Payload, SearchShiftParametersDto.class);
                    SearchShiftParametersDto dto = (SearchShiftParametersDto) object;

                    // Get from database using Dao.
                    ShiftDao shiftDao = ShiftDao.getInstance();
                    ArrayList<ShiftDTO> shifts = shiftDao.getBySearchParameters(dto.date, dto.workerName);

                    //Return the answer
                    Sender sender = Sender.getInstance();
                    sender.send(new MessageHeader(messageHeader.getQueue(), action, shifts));
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
