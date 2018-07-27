import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame{

    /**
     * 2.1
     */
    private static final long serialVersionUID = 1L;

    Wall w1 = new Wall(100, 200, 20, 150, this), w2 = new Wall(300, 100, 300, 20, this);

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    Tank myTank = new Tank(50,50, true, Tank.Direction.STOP, this);

    List<Explode> explodes = new ArrayList<Explode>();
    List<Missile> missiles = new ArrayList<Missile>();
    List<Tank> tanks = new ArrayList<Tank>();

    Image offScreenImage = null;//屏幕背后，初始等于空


    public void paint(Graphics g) {//因为被重画，所以自动会被调用
        g.drawString("missiles count:"+ missiles.size(), 10, 50);
        g.drawString("explodes count:"+ explodes.size(), 10, 70);
        g.drawString("tanks    count:"+ tanks.size(), 10, 90);

        for(int i=0; i<missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.hitTanks(tanks);
            m.hitTank(myTank);
            m.hitWall(w1);
            m.hitWall(w2);//missile can not go through the wall;
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
            t.collideWithTank(tanks);
            t.draw(g);
        }
        myTank.draw(g);
        w1.draw(g);
        w2.draw(g);

    }
    //类名首字母要大写

    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.WHITE);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);//观察者
    }//背景没有重刷，之前的圆也会出现

    public void lauchFrame() {

        for(int i = 0; i < 10; i++) {
            tanks.add(new Tank(100 + 80 * (i + 1), 100 + 60 * (i + 1), false, Tank.Direction.D, this));
        }

        this.setLocation(400,300);//距离屏幕的左上角点的位置，往右数400，往下数300
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("TankWar");
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                System.exit(0);//正常退出程序，非0表示非正常退出
            }
        });
        this.setResizable(false);
        this.setBackground(Color.WHITE);

        this.addKeyListener(new KeyMonitor());

        setVisible(true);

        new Thread(new PaintThread()).start();
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.lauchFrame();
    }

    private class PaintThread implements Runnable{


        public void run() {
            while(true) {
                repaint();//外部包装类的repaint，是父类的，会内部调用paint
                //先调用update，再调用paint
                try {
                    Thread.sleep(100);
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


