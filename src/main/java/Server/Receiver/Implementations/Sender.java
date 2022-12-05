package Server.Receiver.Implementations;

import Server.Receiver.Implementations.MessageHeaders.MessageHeader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Sender {

    private static Sender instance = new Sender();

    private Sender() {}

    public static Sender getInstance() {
        if (instance == null) {
            synchronized (Sender.class) {
                if (instance == null) {
                    instance = new Sender();
                }
            }
        }
        return instance;
    }

    public void send(MessageHeader payload) {
        String queueName = payload.queue;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(queueName, false, false, false, null);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String message = ow.writeValueAsString(payload);

            System.out.println(payload.queue);
            System.out.println("sended: " + payload.payload.toString());

            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
