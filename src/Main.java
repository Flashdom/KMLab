import java.util.List;

public class Main {

    private static double[] lambda = new double[5];
    private static int N = 0;
    private static int c1;
    private static int c2;
    private static double[] A = new double[10000];
    private static double[] C = new double[10000];
    private static double t1 = 10000000;
    private static double ta = 10000000;
    private static double t2 = 10000000;
    private static double tw = 10000000;
    private static double t;
    private static double Q;
    private static double P1;
    private static double P2;
    private static double tl;
    private static double[] w = new double[10000];
    private static double W;
    private static int z = 100;
    private static double[] timeOfSettingOrder = new double[10000];
    private static double[] timeOfGettingOrder = new double[10000];
    private static double[] timeOfCheckingOut = new double[10000];
    private static double[] timeOfPrepareFood = new double[10000];
    private static double[] timeOfEatMeal = new double[10000];
    private static SystemState systemState = new SystemState();

    public static void main(String[] args) {

        lambda[0] = 0.2;
        lambda[1] = 0.38;
        lambda[2] = 0.38;
        lambda[3] = 0.1;
        lambda[4] = 0.08;
        tl = 0;
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
        calculateStatistics();
        for (int i = 0; i < A.length; i++)
            if (A[i] != 0)
                System.out.println("A[" + i + "] = " + A[i]);
        for (int i = 0; i < timeOfSettingOrder.length; i++) {
            if (timeOfSettingOrder[i] != 0)
                System.out.println("Оставление заказа[" + i + "] = " + timeOfSettingOrder[i]);
        }
        for (int i = 0; i < timeOfGettingOrder.length; i++) {
            if (timeOfGettingOrder[i] != 0)
                System.out.println("Получение заказа[" + i + "] = " + timeOfGettingOrder[i]);
        }
        for (int i = 0; i < timeOfCheckingOut.length; i++)
            if (timeOfCheckingOut[i] != 0)
                System.out.println("Получение чека[" + i + "] = " + timeOfCheckingOut[i]);

        for (int i = 0; i < timeOfPrepareFood.length; i++)
            if (timeOfPrepareFood[i] != 0)
                System.out.println("Готовка[" + i + "] = " + timeOfPrepareFood[i]);

        for (int i = 0; i < timeOfEatMeal.length; i++)
            if (timeOfEatMeal[i] != 0)
                System.out.println("Еда[" + i + "] = " + timeOfEatMeal[i]);

        System.out.println("Успех");
        System.out.println("W:" + W);
        System.out.println("Q:" + Q);
        System.out.println("P1:" + P1);
        System.out.println("P2:" + P2);


    }


    private static double getMin() {
        double tmp = t1;
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
        rememberStat();
        ta = createTimeClientArrival(t);
        A[N] = t;
        if (systemState.getFirstDeviceState() != 0 && systemState.getSecondDeviceState() == 0) {
            systemState.setClientsCount(systemState.getClientsCount() + 1);
            systemState.setSecondDeviceState(1);
            systemState.setClientAtSecondDevice(N);
            double tmp = getTimes(2);
            t2 = (tmp + t);
            timeOfSettingOrder[N] = tmp;
            System.out.println("Клиент №" + N + " прибыл и встал на обслуживание на устройство №2 и первое - занято " + t);

        } else {
            if (systemState.getFirstDeviceState() == 0 && systemState.getSecondDeviceState() == 0) {
                systemState.setFirstDeviceState(1);
                systemState.setClientAtFirstDevice(N);
                systemState.setClientsCount(systemState.getClientsCount() + 1);
                double tmp = getTimes(1);
                t1 = (tmp + t);
                timeOfSettingOrder[N] = tmp;
                System.out.println("Клиент №" + N + " прибыл и встал на обслуживание на устройство №1 и второе  - свободно " + t);
            } else {

                if (systemState.getFirstDeviceState() == 0 && systemState.getSecondDeviceState() != 0) {
                    systemState.setClientsCount(systemState.getClientsCount() + 1);
                    systemState.setFirstDeviceState(1);
                    systemState.setClientAtFirstDevice(N);
                    double tmp = getTimes(1);
                    t1 = (tmp + t);
                    timeOfSettingOrder[N] = tmp;
                    System.out.println("Клиент №" + N + " прибыл и встал на обслуживание на устройство №1 и второе - занято " + t);
                } else {


                    if (systemState.getFirstDeviceState() != 0 && systemState.getSecondDeviceState() != 0) {
                        Client client = new Client();
                        client.setNumber(N);
                        client.setServiceType(1);
                        client.setEmptyTime(0);
                        systemState.setClientQueue(client);
                        w[N] = t;
                        System.out.println("Клиент №" + N + " прибыл и встал в очередь, пока оба устройства заняты " + t);

                    }
                }
            }
        }
    }

    private static void finishWaiting() {
        t = tw;
        rememberStat();
        tw = 1000000000;
        List<Client> clients = systemState.getClients();
        for (int i = 0; i < clients.size(); i++) {

            if (t == clients.get(i).getEmptyTime()) {
                if (systemState.getFirstDeviceState() == 0) {
                    systemState.setFirstDeviceState(clients.get(i).getServiceType());
                    systemState.setClientAtFirstDevice(clients.get(i).getNumber());
                    System.out.println("Клиент №" + clients.get(i).getNumber() + " прекратил ожидание и стал обслуживаться первым устройством " + t);
                    double tmp = getTimes(1);
                    t1 = (t + tmp);
                    if (clients.get(i).getServiceType() == 1)
                        timeOfSettingOrder[clients.get(i).getNumber()] = tmp;
                    if (clients.get(i).getServiceType() == 2)
                        timeOfGettingOrder[clients.get(i).getNumber()] = tmp;
                    if (clients.get(i).getServiceType() == 3)
                        timeOfCheckingOut[clients.get(i).getNumber()] = tmp;

                    clients.remove(i);
                    i--;
                } else {
                    if (systemState.getSecondDeviceState() == 0) {
                        systemState.setSecondDeviceState(clients.get(i).getServiceType());
                        systemState.setClientAtSecondDevice(clients.get(i).getNumber());
                        System.out.println("Клиент №" + clients.get(i).getNumber() + " прекратил ожидание и стал обслуживаться вторым устройством " + t);
                        double tmp = getTimes(2);
                        if (clients.get(i).getServiceType() == 1)
                            timeOfSettingOrder[clients.get(i).getNumber()] = tmp;
                        if (clients.get(i).getServiceType() == 2)
                            timeOfGettingOrder[clients.get(i).getNumber()] = tmp;
                        if (clients.get(i).getServiceType() == 3)
                            timeOfCheckingOut[clients.get(i).getNumber()] = tmp;
                        t2 = (t + tmp);
                        clients.remove(i);
                        i--;

                    } else {
                        System.out.println("Клиент №" + clients.get(i).getNumber() + " прекратил ожидание и попал в очередь, пока оба устройства - заняты " + t);
                        w[clients.get(i).getNumber()] = w[clients.get(i).getNumber()] -t;
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


    }


    private static void finishDevice2() {
        t = t2;
        double tw1;
        double tw2;
        rememberStat();
        Client anotherClient;
        switch (systemState.getSecondDeviceState()) {
            case 1: {
                double tm = getTimes(3);
                Client client = new Client();
                tw1 = (tm + t);
                timeOfPrepareFood[systemState.getClientAtSecondDevice()] = tm;
                client.setNumber(systemState.getClientAtSecondDevice());
                client.setEmptyTime(tw1);
                client.setServiceType(2);
                System.out.println("Клиент №" + client.getNumber() + " оставил свой заказ " + t);
                systemState.setClients(client);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtSecondDevice(anotherClient.getNumber());
                systemState.setSecondDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() != 0)
                    System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться вторым устройством " + t);

                if (tw1 < tw) {
                    tw = tw1;

                }
                if (anotherClient.getNumber() != 0) {
                    w[anotherClient.getNumber()] = w[anotherClient.getNumber()] + t;
                    double tmp = getTimes(2);
                    if (anotherClient.getServiceType() == 1)
                        timeOfSettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 2)
                        timeOfGettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 3)
                        timeOfCheckingOut[anotherClient.getNumber()] = tmp;
                    t2 = (tmp + t);
                } else {

                    t2 = 10000000;

                }
                break;
            }
            case 2: {
                double tm = getTimes(4);
                Client client = new Client();
                tw2 = (t + tm);
                timeOfEatMeal[systemState.getClientAtSecondDevice()] = tm;
                client.setNumber(systemState.getClientAtSecondDevice());
                client.setEmptyTime(tw2);
                client.setServiceType(3);
                systemState.setClients(client);
                System.out.println("Клиент №" + client.getNumber() + " получил свой заказ " + t);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtSecondDevice(anotherClient.getNumber());
                systemState.setSecondDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() != 0)
                    System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться вторым устройством " + t);

                if (tw2 < tw) {
                    tw = tw2;


                }
                if (anotherClient.getNumber() != 0) {
                    w[anotherClient.getNumber()] = w[anotherClient.getNumber()] + t;
                    double tmp = getTimes(2);
                    if (anotherClient.getServiceType() == 1)
                        timeOfSettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 2)
                        timeOfGettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 3)
                        timeOfCheckingOut[anotherClient.getNumber()] = tmp;
                    t2 = (tmp + t);
                } else {

                    t2 = 10000000;

                }

                break;
            }
            case 3: {
                c2 += 1;
                C[systemState.getClientAtSecondDevice()] = t;
                System.out.println("Клиент №" + systemState.getClientAtSecondDevice() + " вышел из системы от второго устройства " + t);
                systemState.setClientsCount(systemState.getClientsCount() - 1);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtSecondDevice(anotherClient.getNumber());
                systemState.setSecondDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() != 0)
                    System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться вторым устройством " + t);
                if (anotherClient.getNumber() != 0) {
                    w[anotherClient.getNumber()] = w[anotherClient.getNumber()] + t;
                    double tmp = getTimes(2);
                    if (anotherClient.getServiceType() == 1)
                        timeOfSettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 2)
                        timeOfGettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 3)
                        timeOfCheckingOut[anotherClient.getNumber()] = tmp;
                    t2 = (tmp + t);
                } else {

                    t2 = 10000000;

                }

                break;
            }


        }


    }

    private static void finishDevice1() {
        t = t1;
        double tw1;
        rememberStat();
        double tw2;
        Client anotherClient;
        switch (systemState.getFirstDeviceState()) {

            case 1: {
                double tm = getTimes(3);
                Client client = new Client();
                tw1 = (tm + t);
                timeOfPrepareFood[systemState.getClientAtFirstDevice()] = tm;
                client.setNumber(systemState.getClientAtFirstDevice());
                client.setEmptyTime(tw1);
                client.setServiceType(2);
                systemState.setClients(client);
                System.out.println("Клиент №" + client.getNumber() + " оставил свой заказ " + t);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtFirstDevice(anotherClient.getNumber());
                systemState.setFirstDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() != 0)
                    System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться первым устройством " + t);
                if (tw1 < tw) {
                    tw = tw1;
                }
                if (anotherClient.getNumber() != 0) {
                    w[anotherClient.getNumber()] = w[anotherClient.getNumber()] + t;
                    double tmp = getTimes(1);
                    if (anotherClient.getServiceType() == 1)
                        timeOfSettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 2)
                        timeOfGettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 3)
                        timeOfCheckingOut[anotherClient.getNumber()] = tmp;
                    t1 = (tmp + t);
                } else {

                    t1 = 10000000;

                }
                break;
            }
            case 2: {
                double tm = getTimes(4);
                tw2 = (t + tm);
                Client client = new Client();
                timeOfEatMeal[systemState.getClientAtFirstDevice()] = tm;
                client.setNumber(systemState.getClientAtFirstDevice());
                client.setEmptyTime(tw2);
                client.setServiceType(3);
                systemState.setClients(client);
                System.out.println("Клиент №" + client.getNumber() + " получил свой заказ " + t);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtFirstDevice(anotherClient.getNumber());
                systemState.setFirstDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() != 0)
                    System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться первым устройством " + t);

                if (tw2 < tw) {
                    tw = tw2;


                }
                if (anotherClient.getNumber() != 0) {
                    w[anotherClient.getNumber()] = w[anotherClient.getNumber()] + t;
                    double tmp = getTimes(1);
                    if (anotherClient.getServiceType() == 1)
                        timeOfSettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 2)
                        timeOfGettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 3)
                        timeOfCheckingOut[anotherClient.getNumber()] = tmp;
                    t1 = (tmp + t);
                } else {

                    t1 = 10000000;

                }

                break;
            }
            case 3: {
                c1 += 1;
                C[systemState.getClientAtFirstDevice()] = t;
                System.out.println("Клиент №" + systemState.getClientAtFirstDevice() + " вышел из системы от первого устройства " + t);
                systemState.setClientsCount(systemState.getClientsCount() - 1);
                anotherClient = systemState.getClientFromQueue();
                systemState.setClientAtFirstDevice(anotherClient.getNumber());
                systemState.setFirstDeviceState(anotherClient.getServiceType());
                if (anotherClient.getNumber() != 0)
                    System.out.println("Клиент №" + anotherClient.getNumber() + " стал обслуживаться первым устройством " + t);
                if (anotherClient.getNumber() != 0) {
                    w[anotherClient.getNumber()] = w[anotherClient.getNumber()] + t;
                    double tmp = getTimes(1);
                    if (anotherClient.getServiceType() == 1)
                        timeOfSettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 2)
                        timeOfGettingOrder[anotherClient.getNumber()] = tmp;
                    if (anotherClient.getServiceType() == 3)
                        timeOfCheckingOut[anotherClient.getNumber()] = tmp;
                    t1 = (tmp + t);
                } else {

                    t1 = 10000000;

                }

                break;
            }


        }


    }


    private static double createTimeClientArrival(double minute) {
        double t;
        double tmp2;
        do {
            t = minute;
            double tmp = Math.random();
            t = (t - (1 / (lambda[0] * Math.log(tmp))));
            tmp2 = Math.random();
        }
        while (tmp2 > getLambdaOtT(t) / lambda[0]);
        return t + 1;

    }

    private static double getLambdaOtT(double t) {

        if (t < 120 && t > 0)
            return 0.3;
        else
            return 0.2;

    }


    private static double getTimes(int b) {
        double a = Math.random();
        double rez;
        rez = (-1 / lambda[b]) * Math.log(a);
        return rez + 1;

    }

    private static double calculateQ() {
        return Q / t;
    }

    private static double calculateP1() {
        return P1 / t;

    }

    private static double calculateP2() {

        return P2 / t;
    }

    private static void calculateStatistics() {
        t = z;
        Q = calculateQ();
        P1 = calculateP1();
        P2 = calculateP2();
        W = calculateW();
    }

    private static void rememberStat()
    {
        Q = Q + systemState.getClientsCount() * (t-tl);
        if (systemState.getFirstDeviceState() != 0 )
        {
            P1 = P1 + (t-tl);

        }
        if (systemState.getSecondDeviceState() != 0 )
        {
            P2 = P2 + (t-tl);

        }
        tl=t;

    }

    private static double calculateW() {
        double tmp = 0;
        for (int i = 0; i < w.length; i++) {
            if (w[i] == 0)
                break;
            tmp = tmp + w[i];


        }
        return (1 / N) * tmp;

    }


}
