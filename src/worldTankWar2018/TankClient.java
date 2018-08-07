package worldTankWar2018;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

public class TankClient extends Frame{
    //add tank.properties file;
    //singleton;

    private static final long serialVersionUID = 1L;

    Wall w1 = new Wall(100, 70, 20, 100, this);
    Wall w2 = new Wall(300, 70, 20, 100, this);
    Wall w3 = new Wall(500, 70, 20, 100, this);
    Wall w4 = new Wall(700, 70, 20, 100, this);
    Wall w5 = new Wall(200, 270, 20, 100, this);
    Wall w6 = new Wall(400, 270, 20, 100, this);
    Wall w7 = new Wall(600, 270, 20, 100, this);
    Wall w8 = new Wall(100, 470, 20, 100, this);
    Wall w9 = new Wall(300, 470, 20, 100, this);
    Wall w10 = new Wall(500, 470, 20, 100, this);
    Wall w11 = new Wall(700, 470, 20, 100, this);

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    Tank myTank = new Tank(50,50, true, Direction.STOP, this);

    List<Explode> explodes = new ArrayList<Explode>();
    List<Missile> missiles = new ArrayList<Missile>();
    List<Tank> tanks = new ArrayList<Tank>();

    Image offScreenImage = null;//屏幕背后，初始等于空

    Blood b = new Blood();


    public void paint(Graphics g) {//因为被重画，所以自动会被调用
        g.drawString("missiles count:"+ missiles.size(), 10, 50);
        g.drawString("explodes count:"+ explodes.size(), 10, 70);
        g.drawString("tanks    count:"+ tanks.size(), 10, 90);
        g.drawString("tanks life:" + myTank.getLife(), 10, 110);

        if(tanks.size() <= 0) {

            for(int i = 0; i < Integer.parseInt(PropertyMgr.getProperty("reProduceTankCount")); i++) {
                tanks.add(new Tank(100 + 80 * (i + 1), 100 + 60 * (i + 1), false, Direction.D, this));
            }
        } // when the enemy is all dead, add more enemy;

        for(int i=0; i<missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.hitTanks(tanks);
            m.hitTank(myTank);
            m.hitWall(w1);
            m.hitWall(w2);
            m.hitWall(w3);
            m.hitWall(w4);
            m.hitWall(w5);
            m.hitWall(w6);
            m.hitWall(w7);
            m.hitWall(w8);
            m.hitWall(w9);
            m.hitWall(w10);
            m.hitWall(w11);
            //missile can not go through the wall;
            m.draw(g);
            //if(!m.isLive()) missiles.remove(m);
            //else m.draw(g);
        }

        for(int i = 0; i < explodes.size(); i++) {
            Explode e = explodes.get(i);
            e.draw(g);
        }
        for(int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            t.collideWithWall(w1);
            t.collideWithWall(w2);
            t.collideWithWall(w3);
            t.collideWithWall(w4);
            t.collideWithWall(w5);
            t.collideWithWall(w6);
            t.collideWithWall(w7);
            t.collideWithWall(w8);
            t.collideWithWall(w9);
            t.collideWithWall(w10);
            t.collideWithWall(w11);
            t.collideWithTank(tanks);

            t.draw(g);
        }
        myTank.collideWithWall(w1);
        myTank.collideWithWall(w2);
        myTank.collideWithWall(w3);
        myTank.collideWithWall(w4);
        myTank.collideWithWall(w5);
        myTank.collideWithWall(w6);
        myTank.collideWithWall(w7);
        myTank.collideWithWall(w8);
        myTank.collideWithWall(w9);
        myTank.collideWithWall(w10);
        myTank.collideWithWall(w11);
        myTank.draw(g);
        myTank.eat(b);
        w1.draw(g);
        w2.draw(g);
        w3.draw(g);
        w4.draw(g);
        w5.draw(g);
        w6.draw(g);
        w7.draw(g);
        w8.draw(g);
        w9.draw(g);
        w10.draw(g);
        w11.draw(g);
        b.draw(g);

    }
    //类名首字母要大写

    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);//观察者
    }//背景没有重刷，之前的圆也会出现

    public void lauchFrame() throws IOException {

        int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
        for(int i = 0; i < initTankCount; i++) {
            tanks.add(new Tank(100 + 40 * (i + 1), 100 + 30 * (i + 1), false, Direction.D, this));
        }

        //this.setLocation(400,300);//距离屏幕的左上角点的位置，往右数400，往下数300
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("TankWar");
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                System.exit(0);//正常退出程序，非0表示非正常退出
            }
        });
        this.setResizable(false);
        this.setBackground(Color.BLACK);

        this.addKeyListener(new KeyMonitor());

        setVisible(true);

        new Thread(new PaintThread()).start();
    }

    public static void main(String[] args) throws IOException {
        TankClient tc = new TankClient();
        tc.lauchFrame();
    }

    private class PaintThread implements Runnable{


        public void run() {
            while(true) {
                repaint();//外部包装类的repaint，是父类的，会内部调用paint
                //先调用update，再调用paint
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class KeyMonitor extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }//继承
        //真正改变位置在此处
    }
}


