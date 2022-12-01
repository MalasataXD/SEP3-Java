package Server;


import Server.Receiver.Channels.Shift.CreateShift;
import Server.Receiver.Channels.Shift.EditShift;
import Server.Receiver.Channels.Shift.GetShiftById;
import Server.Receiver.Channels.Shift.RemoveShift;
import Server.Receiver.Channels.TestChannel;
import Server.Receiver.Channels.Worker.CreateWorker;
import Server.Receiver.Channels.Worker.EditWorker;
import Server.Receiver.Channels.Worker.GetWorkerById;
import Server.Receiver.Channels.Worker.RemoveWorker;
import Server.Receiver.Implementations.Dispatcher;
import Server.Receiver.Implementations.Skeleton;
import Server.Receiver.Interfaces.IDispatcher;
import Server.Receiver.Interfaces.IQueue;

import java.util.ArrayList;

public class RunServer {
    public static void main(String[] args) {

        ArrayList<IQueue> arrayList = new ArrayList<>();
        //Create
        arrayList.add(new CreateWorker("CreateWorker", ""));
        arrayList.add(new CreateShift("CreateShift", ""));

        //Edit
        arrayList.add(new EditWorker("EditWorker", ""));
        arrayList.add(new EditShift("EditShift", ""));

        //Remove
        arrayList.add(new RemoveWorker("RemoveWorker",""));
        arrayList.add(new RemoveShift("RemoveShift", ""));

        //getById
        arrayList.add(new GetWorkerById("GetWorkerById",""));
        arrayList.add(new GetShiftById("GetShiftById",""));

        //Test
        arrayList.add(new TestChannel("Test", ""));

        IDispatcher dispatcher = new Dispatcher("Dispatcher", new Skeleton(arrayList));
        Thread thread = new Thread(dispatcher);
        thread.start();

    }
}
