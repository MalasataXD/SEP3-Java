package Server.Receiver.Interfaces;

import com.rabbitmq.client.Connection;

public interface IDispatcher extends Runnable
{
    // < Fields
    static ISkeleton skeleton = null;
    static String Exchange = null;
    static String QUEUE = null;
    Connection connection = null;

    // < Methods
    Connection getConnection(); // * Get the connection
    void startOperation(String operation); // * Ex. CREATE, READ, UPDATE, DELETE (Inform skeleton what queue to open)
}
