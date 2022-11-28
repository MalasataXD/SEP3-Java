package Server.Receiver;

import java.util.Map;

public class MQConfig
{
    // < Singleton field
    private static MQConfig instance = new MQConfig();

    // < Field attributes
    private String URL = "localhost";

    private String Exchane = "";
    private boolean durable = false;
    private boolean exclusive = false;
    private boolean autoDelete = false;
    private Map<String,Object> map = null;

    //Private constructor
    private MQConfig() {}

    //Static get method for singleton access
    public static MQConfig getInstance() {
        if (instance == null) {
            synchronized (MQConfig.class) {
                if (instance == null) {
                    instance = new MQConfig();
                }
            }
        }
        return instance;
    }

    public String getURL() {
        return URL;
    }

    public boolean isDurable() {
        return durable;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public Map<String, Object> getMap() {
        return map;
    }
}