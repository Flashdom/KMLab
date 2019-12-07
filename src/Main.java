import java.util.List;

public class Main {

    private static double[] lambda = new double[5];
    private static int N = 0;
    private static int c1;
    private static int c2;
    private static int[] A = new int[10000];
    private static int[] C = new int[10000];
    private static int t1 = 10000000;
    private static int ta = 10000000;
    private static int t2 = 10000000;
    private static int tw = 10000000;
    private static int t;
    private static SystemState systemState = new SystemState();

    public static void main(String[] args) {
        int z = 360;
        lambda[0] = 10;
        lambda[1] = 10;
        lambda[2]= 10;
        lambda[3] = 10;
        lambda[4] = 10;
        ta = createTimeClientArrival(t);
        while (t < z) {
            if (getMin() == ta)
                newClient();
            if (getMin() == t1)
                finishDevice1();
            if (getMin() == t2)
                finishDevice2();
            if (getMin() == tw)
                finishWaiting();
        }
        System.out.println("Успех");

    }


    private static int getMin() {
        int tmp = t1;
        if (tmp > ta)
            tmp = ta;
        if (tmp > t2)
            tmp = t2;
        if (tmp > tw)
            tmp = tw;
        return tmp;

    }


    private static void newClient() {

        t = ta;
        N++;
        ta = createTimeClientArrival(t);
        A[N] = t;
        if (systemState.getFirstDeviceState() != 0 && systemState.getSecondDeviceState() == 0) {
            systemState.setClientsCount(systemState.getClientsCount() + 1);
            systemState.setSecondDeviceState(1);
            systemState.setClientAtSecondDevice(N);
            System.out.println("Клиент №" + N + " прибыл и встал на обслуживание на устройство №2 и первое - занято "  + t);

        }
        else {
            if (systemState.getFirstDeviceState() == 0 && systemState.getSecondDeviceState() == 0) {
                systemState.setFirstDeviceState(1);
                systemState.setClientAtFirstDevice(N);
                systemState.setClientsCount(systemState.getClientsCount() + 1);
                t1 = (int) (getTimes(1) + t);
                System.out.println("Клиент №" + N + " прибыл и встал на обслуживание на устройство №1 и второе  - свободно " + t);
            }
            else {

                if (systemState.getFirstDeviceState() == 0 && systemState.getSecondDeviceState() != 0) {
                    systemState.setClientsCount(systemState.getClientsCount() + 1);
                    systemState.setFirstDeviceState(1);
                    systemState.setClientAtFirstDevice(N);
                    t1 = (int) (t + getTimes(1));
                    System.out.println("Клиент №" + N + " прибыл и встал на обслуживание на устройство №1 и второе - занято " + t);
                }
                else {


                    if (systemState.getFirstDeviceState() != 0 && systemState.getSecondDeviceState() != 0) {
                        Client client = new Client();
                        client.setNumber(N);
                        client.setServiceType(1);
                        client.setEmptyTime(0);
                        systemState.setClientQueue(client);
                        System.out.println("Клиент №" + N + " прибыл и встал в очередь, пока оба устройства заняты " + t);

                    }
                }
            }
        }
    }

    private static void finishWaiting() {
        t = tw;
        tw = 1000000000;
        List<Client> clients = systemState.getClients();
        for (int i = 0; i < clients.size(); i++) {

            if (t == clients.get(i).getEmptyTime()) {
                if (systemState.getFirstDeviceState() == 0) {
                    systemState.setFirstDeviceState(clients.get(i).getServiceType());
                    systemState.setClientAtFirstDevice(clients.get(i).getNumber());
                    System.out.println("Клиент №" + clients.get(i).getNumber() + " прекратил ожидание и стал обслуживаться первым устройством " + t);
                    clients.remove(i);
                    i--;
                    t1 = (int) (t + getTimes(1));
                } else {
                    if (systemState.getSecondDeviceState() == 0) {
                        systemState.setSecondDeviceState(clients.get(i).getServiceType());
                        systemState.setClientAtSecondDevice(clients.get(i).getNumber());
                        System.out.println("Клиент №" + clients.get(i).getNumber() + " прекратил ожидание и стал обслуживаться вторым устройством "  + t);
                        clients.remove(i);
                        i--;
                        t2 = (int) (t + getTimes(2));

                    } else {
                        System.out.println("Клиент №" + clients.get(i).getNumber() + " прекратил ожидание и попал в очередь, пока оба устройства - заняты "  + t);
                        systemState.setClientQueue(clients.get(i));
                        clients.remove(i);
                        i--;

                    }

                }
            } else {
                if (tw > clients.get(i).getEmptyTime()) {
                    tw = clients.get(i).getEmptyTime();
                }

            }
        }
        systemState.clearListClients();
        for (Client o : clients)
            systemState.setClients(o);

    }


    private static void finishDevice2() {
        t = t2;
        int tw1;
        int tw2;
        Client anotherClient;
        switch (systemState.getSecondDeviceState()) {
            case 1: {
                Client client = new Client();
                tw1 = (int) (getTimes(3) + t);
                client.setNumber(systemState.getClientAtSecondDevice());
                client.setEmptyTime(tw1);
                client.setServiceType(2);
                System.out.println("Клиент №" + client.getNumber() + " оставил свой заказ "  + t);
                systemState.setClients(client);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtSecondDevice(anotherClient.getNumber());
                systemState.setSecondDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() !=0)
                System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться вторым устройством "  + t);

                if (tw1 < tw) {
                    tw = tw1;

                }
                if (anotherClient.getNumber() != 0) {
                    t2 = (int) (getTimes(2) + t);
                } else {

                    t2 = 10000000;

                }
                break;
            }
            case 2: {
                Client client = new Client();
                tw2 = (int) (t + getTimes(4));
                client.setNumber(systemState.getClientAtSecondDevice());
                client.setEmptyTime(tw2);
                client.setServiceType(3);
                systemState.setClients(client);
                System.out.println("Клиент №" + client.getNumber() + " получил свой заказ "  + t);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtSecondDevice(anotherClient.getNumber());
                systemState.setSecondDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() !=0)
                System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться вторым устройством "  + t);

                if (tw2 < tw) {
                    tw = tw2;


                }
                if (anotherClient.getNumber() != 0) {
                    t2 = (int) (getTimes(2) + t);
                } else {

                    t2 = 10000000;

                }

                break;
            }
            case 3: {
                c2 += 1;
                C[systemState.getClientAtSecondDevice()] = t;
                System.out.println("Клиент №" + systemState.getClientAtSecondDevice() + " вышел из системы от второго устройства "  + t);
                systemState.setClientsCount(systemState.getClientsCount() - 1);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtSecondDevice(anotherClient.getNumber());
                systemState.setSecondDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() !=0)
                System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться вторым устройством "  + t);
                if (anotherClient.getNumber() != 0) {
                    t2 = (int) (getTimes(2) + t);
                } else {

                    t2 = 10000000;

                }

                break;
            }


        }


    }

    private static void finishDevice1() {
        t = t1;
        int tw1;
        int tw2;
        Client anotherClient;
        switch (systemState.getFirstDeviceState()) {

            case 1: {
                Client client = new Client();
                tw1 = (int) (getTimes(3) + t);
                client.setNumber(systemState.getClientAtFirstDevice());
                client.setEmptyTime(tw1);
                client.setServiceType(2);
                systemState.setClients(client);
                System.out.println("Клиент №" + client.getNumber() + " оставил свой заказ "  + t);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtFirstDevice(anotherClient.getNumber());
                systemState.setFirstDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() !=0)
                System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться первым устройством "  + t);
                if (tw1 < tw) {
                    tw = tw1;

                }
                if (anotherClient.getNumber() != 0) {
                    t1 = (int) (getTimes(1) + t);
                } else {

                    t1 = 10000000;

                }
                break;
            }
            case 2: {
                tw2 = (int) (t + getTimes(4));

                Client client = new Client();
                client.setNumber(systemState.getClientAtFirstDevice());
                client.setEmptyTime(tw2);
                client.setServiceType(3);
                systemState.setClients(client);
                System.out.println("Клиент №" + client.getNumber() + " получил свой заказ " + t);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtFirstDevice(anotherClient.getNumber());
                systemState.setFirstDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() !=0)
                System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться первым устройством "  + t);

                if (tw2 < tw) {
                    tw = tw2;


                }
                if (anotherClient.getNumber() != 0) {
                    t1 = (int) (getTimes(1) + t);
                } else {

                    t1 = 10000000;

                }

                break;
            }
            case 3: {
                c1 += 1;
                C[systemState.getClientAtFirstDevice()] = t;
                System.out.println("Клиент №" + systemState.getClientAtFirstDevice() + " вышел из системы от первого устройства "  + t);
                systemState.setClientsCount(systemState.getClientsCount() - 1);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtFirstDevice(anotherClient.getNumber());
                systemState.setFirstDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() !=0)
                System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться первым устройством "  + t);
                if (anotherClient.getNumber() != 0) {
                    t1 = (int) (getTimes(1) + t);
                } else {

                    t1 = 10000000;

                }

                break;
            }


        }


    }


    private static int createTimeClientArrival(int minute) {
        int t;
        double tmp2;
       // do {
            t = minute;
            double tmp = Math.random();
            t = ((int) (t - (1 / (lambda[0] * Math.log(tmp)))));
            tmp2 = Math.random();
    //    }
     //   while (tmp2 <= getLambdaOtT(t) / t);
        return t + 1;

    }

    private static double getLambdaOtT(int t) {

        return t;
    }


    private static double getTimes(int b) {
        double a = Math.random();
        double rez;
        rez = (-1 / lambda[b]) * Math.log(a);
        return rez +1 ;

    }

}
