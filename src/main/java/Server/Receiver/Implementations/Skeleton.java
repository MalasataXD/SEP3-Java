package Server.Receiver.Implementations;

import Server.Receiver.Interfaces.IQueue;
import Server.Receiver.Interfaces.ISkeleton;

import java.util.ArrayList;

public class Skeleton implements ISkeleton
{
    //Field attributes
    private ArrayList<IQueue> queues;

    public Skeleton(ArrayList<IQueue> queues) {
        this.queues = queues;
    }

    private IQueue findQueue(String queue) {
        for (IQueue iQueue : queues) {
            if (iQueue.GetQueue().equalsIgnoreCase(queue)) {
                return iQueue;
            }
        }
        return null;
    }

    @Override
    public void openChannel(String toOpen) {
        IQueue queueFound = findQueue(toOpen);

        if (queueFound != null) {
            Thread thread = new Thread(queueFound);
            thread.start();
        }
        else {
            System.out.println("Queue not found");
        }
    }
}
