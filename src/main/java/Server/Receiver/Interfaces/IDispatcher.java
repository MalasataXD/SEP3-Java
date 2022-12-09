package Server.Receiver.Interfaces;

import com.rabbitmq.client.Connection;

public interface IDispatcher extends Runnable
{
    // < Methods
    Connection getConnection(); // * Get the connection
    void startOperation(String operation); // * Ex. CREATE, READ, UPDATE, DELETE (Inform skeleton what queue to open)
}
