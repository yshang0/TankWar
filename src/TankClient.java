import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
//解决炮弹不消亡的问题
//解决坦克出界的问题
//每当抬起Ctrl键就往容器中加入新的炮弹
//逐一画出每一发炮弹
public class TankClient extends Frame{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    Tank myTank = new Tank(50,50, true, this);
    Tank enemyTank = new Tank(100,100,false, this);
    List<Missile> missiles = new ArrayList<Missile>();

    Image offScreenImage = null;//屏幕背后，初始等于空


    public void paint(Graphics g) {//因为被重画，所以自动会被调用
        g.drawString("missiles count:"+missiles.size(), 10, 50);

        for(int i=0; i<missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.hitTank(enemyTank);
            m.draw(g);
            //if(!m.isLive()) missiles.remove(m);
            //else m.draw(g);
        }
        myTank.draw(g);//不要改变原来的前景色
        enemyTank.draw(g);
    }
    //类名首字母要大写

    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.GREEN);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);//观察者
    }//背景没有重刷，之前的圆也会出现

    public void lauchFrame() {
        this.setLocation(400,300);//距离屏幕的左上角点的位置，往右数400，往下数300
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("TankWar");
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                System.exit(0);//正常退出程序，非0表示非正常退出
            }
        });
        this.setResizable(false);
        this.setBackground(Color.GREEN);

        this.addKeyListener(new KeyMonitor());

        setVisible(true);

        new Thread(new PaintThread()).start();
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.lauchFrame();//还不能关闭窗口
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



