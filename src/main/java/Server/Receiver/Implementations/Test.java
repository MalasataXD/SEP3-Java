package Server.Receiver.Implementations;

import Server.Receiver.Implementations.MessageHeaders.MessageHeader;

public class Test {
    public static void main(String[] args) {

        Sender sender = Sender.getInstance();
        sender.send(new MessageHeader("localClient1","TEST","oof"));



    }
}
