package worldTankWar2018;

import java.io.IOException;
import java.net.Socket;

public class NetClient {



    public void connect(String IP, int port) {
        try {
            Socket s = new Socket(IP, port);
            System.out.println("Connected to Server");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
