

public class Client {

    public int getNumber() {
        return number;
    }

    public int getServiceType() {
        return serviceType;
    }

    public double getEmptyTime() {
        return emptyTime;
    }

    private int number;
    private int serviceType;
    private double emptyTime;

    public void setNumber(int number) {
        this.number = number;
    }

    public void setEmptyTime(double emptyTime) {
        this.emptyTime = emptyTime;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }



}
