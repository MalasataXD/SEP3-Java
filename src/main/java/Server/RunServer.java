package Server;


import Server.Receiver.Channels.Shift.CreateShift;
import Server.Receiver.Channels.Shift.EditShift;
import Server.Receiver.Channels.Shift.RemoveShift;
import Server.Receiver.Channels.Worker.CreateWorker;
import Server.Receiver.Implementations.Dispatcher;
import Server.Receiver.Implementations.Skeleton;
import Server.Receiver.Interfaces.IDispatcher;
import Server.Receiver.Interfaces.IQueue;

import java.util.ArrayList;

public class RunServer {
    public static void main(String[] args) {

        ArrayList<IQueue> arrayList = new ArrayList<>();
        arrayList.add(new CreateWorker("CreateWorker", ""));
        arrayList.add(new CreateShift("CreateShift", ""));
        arrayList.add(new EditShift("EditShift", ""));
        arrayList.add(new RemoveShift("RemoveShift", ""));

        IDispatcher dispatcher = new Dispatcher("Dispatcher", new Skeleton(arrayList));
        Thread thread = new Thread(dispatcher);
        thread.start();

    }
}
