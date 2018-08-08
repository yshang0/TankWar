package worldTankWar2018;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TankServer {

    public static final int TCP_PORT = 8888;


    public static void main(String[] args)  {
        try {
            ServerSocket ss = new ServerSocket(TCP_PORT);
            while(true) {
                Socket s = ss.accept();//accept the
                System.out.println("A Client Connect! Addr: " + s.getInetAddress() + ":" + s.getPort());//create server
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
