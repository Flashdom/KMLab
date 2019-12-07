

public class Client {

    public int getNumber() {
        return number;
    }

    public int getServiceType() {
        return serviceType;
    }

    public int getEmptyTime() {
        return emptyTime;
    }

    private int number;
    private int serviceType;
    private int emptyTime;

    public void setNumber(int number) {
        this.number = number;
    }

    public void setEmptyTime(int emptyTime) {
        this.emptyTime = emptyTime;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }



}
