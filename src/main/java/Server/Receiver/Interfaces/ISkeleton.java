package Server.Receiver.Interfaces;

import java.util.ArrayList;

public interface ISkeleton
{
    // < Fields
    ArrayList<IQueue> Queues = null;

    // < Methods
    void openChannel(String toOpen); // * Start the listener (+ Give it the connection)
}
