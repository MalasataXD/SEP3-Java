package Server.Receiver.Implementations.MessageHeaders;

public class MessageHeader {

    public String queue;
    public Object payload;

    public String action;

    public MessageHeader() {
    }

    public MessageHeader(String queue, String action, Object payload)
    {
        this.queue = queue;
        this.action = action;
        this.payload = payload;
    }

    public String getQueue() {
        return queue;
    }

    public Object getPayload() {
        return payload;
    }

    public String getAction() {
        return action;
    }
}
