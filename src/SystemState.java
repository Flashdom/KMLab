import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayList;
import java.util.List;

public class SystemState {

    private int clientsCount;
    private int clientAtFirstDevice;
    private int clientAtSecondDevice;
    private int firstDeviceState;
    private int secondDeviceState;
    private List<Client> clients = new ArrayList<>();
    private ArrayQueue<Client> clientQueue = new ArrayQueue<>(1000);


    public Client getClientFromQueue() {
        Client client;
        if (clientQueue.size() > 0) {
            client = clientQueue.get(0);
            clientQueue.remove(0);
        } else {
            client = new Client();
            client.setServiceType(0);
            client.setNumber(0);
            client.setEmptyTime(0);
        }
        return client;
    }

    public int getClientsCount() {
        return clientsCount;
    }

    public void setClientsCount(int clientsCount) {
        this.clientsCount = clientsCount;
    }

    public int getClientAtFirstDevice() {
        return clientAtFirstDevice;
    }

    public void setClientAtFirstDevice(int clientAtFirstDevice) {
        this.clientAtFirstDevice = clientAtFirstDevice;
    }

    public int getClientAtSecondDevice() {
        return clientAtSecondDevice;
    }

    public void setClientAtSecondDevice(int clientAtSecondDevice) {
        this.clientAtSecondDevice = clientAtSecondDevice;
    }

    public int getFirstDeviceState() {
        return firstDeviceState;
    }

    public void setFirstDeviceState(int firstDeviceState) {
        this.firstDeviceState = firstDeviceState;
    }

    public int getSecondDeviceState() {
        return secondDeviceState;
    }

    public void setSecondDeviceState(int secondDeviceState) {
        this.secondDeviceState = secondDeviceState;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(Client client) {
        this.clients.add(client);
    }

    public void clearListClients()
    {
        this.clients.clear();

    }
    public void setClientQueue(Client client) {
        this.clientQueue.add(client);
    }

}
