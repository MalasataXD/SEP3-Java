package Server;


import Server.Receiver.Channels.Shift.*;
import Server.Receiver.Channels.TestChannel;
import Server.Receiver.Channels.Worker.*;
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

        //GetById
        arrayList.add(new GetWorkerById("GetWorkerById",""));
        arrayList.add(new GetShiftById("GetShiftById",""));

        //GetByFullName
        arrayList.add(new GetWorkerByFullName("GetWorkerByFullName", ""));

        //SearchParameters
        arrayList.add(new GetWorkerBySearchParameters("GetWorkerBySearchParameters", ""));
        arrayList.add(new GetShiftBySearchParameters("GetShiftBySearchParameters", ""));

        //bulk
        arrayList.add(new CreateShifts("CreateShifts", ""));
        arrayList.add(new EditShifts("EditShifts", ""));
        arrayList.add(new RemoveShifts("RemoveShifts", ""));

        //Test
        arrayList.add(new TestChannel("Test", ""));




        IDispatcher dispatcher = new Dispatcher("Dispatcher", new Skeleton(arrayList));
        Thread thread = new Thread(dispatcher);
        thread.start();

    }
}
