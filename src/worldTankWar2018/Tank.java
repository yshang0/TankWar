package worldTankWar2018;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

//make it can fire in 8 directions
//show the life value;
//use the tag to represent the life value;
public class Tank {
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private boolean live = true;

    private int life = 100;

    private BloodBar bb = new BloodBar();

    public int getLife() {
        return life;
    }
    public void setLife(int life) {
        this.life = life;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    TankClient tc;

    private boolean good;

    private int x, y;//坐标属性

    private int oldX, oldY;

    private static Random r = new Random();//some tanks share one r

    private boolean bL=false, bU=false, bR=false, bD=false;

    private Direction dir = Direction.STOP;
    private Direction ptDir = Direction.D;

    private int step = r.nextInt(12) + 3;

    private static Toolkit tk = Toolkit.getDefaultToolkit();

    private static Image[] tankImages = null;

    static Map<String, Image> imgs = new HashMap<String, Image>();
//static code area;
    static {
        tankImages = new Image[] {
                tk.getImage(Explode.class.getClassLoader().getResource("images/tankL.gif")),
                tk.getImage(Explode.class.getClassLoader().getResource("images/tankLU.gif")),
                tk.getImage(Explode.class.getClassLoader().getResource("images/tanKU.gif")),
                tk.getImage(Explode.class.getClassLoader().getResource("images/tankRU.gif")),
                tk.getImage(Explode.class.getClassLoader().getResource("images/tankR.gif")),
                tk.getImage(Explode.class.getClassLoader().getResource("images/tankRD.gif")),
                tk.getImage(Explode.class.getClassLoader().getResource("images/tankD.gif")),
                tk.getImage(Explode.class.getClassLoader().getResource("images/tankLD.gif"))
        };
        imgs.put("L", tankImages[0]);
        imgs.put("LU", tankImages[1]);
        imgs.put("U", tankImages[2]);
        imgs.put("RU", tankImages[3]);
        imgs.put("R", tankImages[4]);
        imgs.put("RD", tankImages[5]);
        imgs.put("D", tankImages[6]);
        imgs.put("LD", tankImages[7]);

    }

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x,y, good);
        this.dir = dir;
        this.tc=tc;
    }

    public void draw(Graphics g) {
        if(!live) {
            if(!good) {
                tc.tanks.remove(this);
            }
            return;
        }



        if(good) {
            bb.draw(g);
        }

        switch(ptDir) {
            case L:
                g.drawImage(imgs.get("L"), x, y, null);
                break;
            case LU:
                g.drawImage(imgs.get("LU"), x, y, null);
                break;
            case U:
                g.drawImage(imgs.get("U"), x, y, null);
                break;
            case RU:
                g.drawImage(imgs.get("RU"), x, y, null);
                break;
            case R:
                g.drawImage(imgs.get("R"), x, y, null);
                break;
            case RD:
                g.drawImage(imgs.get("RD"), x, y, null);
                break;
            case LD:
                g.drawImage(imgs.get("LD"), x, y, null);
                break;
            case D:
                g.drawImage(imgs.get("D"), x, y, null);
                break;
            default:
                break;
        }
        move();
    }

    void move() {

        this.oldX = x;
        this.oldY = y;
        //record the location before move;

        switch(dir) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case STOP:
                break;
        }

        if(this.dir != Direction.STOP){
            this.ptDir = this.dir;
        }

        if(x<0) x=0;
        if(y<25) y=25;
        if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x=TankClient.GAME_WIDTH - Tank.WIDTH;
        if(y + Tank.HEIGHT> TankClient.GAME_HEIGHT) y=TankClient.GAME_HEIGHT - Tank.HEIGHT;

        if(!good) {
            Direction[] dirs = Direction.values();

            if(step == 0) {
                step = r.nextInt(12) + 3;
                int rn = r.nextInt(dirs.length);
                dir = dirs[rn];
            }
            step--;

            if(r.nextInt(40) > 38) this.fire();

        }



    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_B :
                if(!this.live) {
                    this.live = true;
                    this.life = 100;
                }
                break;
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }//改动可能不均匀
        //设置键盘抬起来的时候重新设置为false；
        locateDirection();
    }


    void locateDirection() {
        if(bL && !bU && !bR && !bD) dir = Direction.L;
        else if(!bL && bU && !bR && !bD) dir = Direction.U;
        else if(!bL && !bU && bR && !bD) dir = Direction.R;
        else if(!bL && !bU && !bR && bD) dir = Direction.D;
        else if(bL && bU && !bR && !bD) dir = Direction.LU;
        else if(!bL && !bU && bR && bD) dir = Direction.RD;
        else if(bL && !bU && !bR && bD) dir = Direction.LD;
        else if(!bL && bU && bR && !bD) dir = Direction.RU;
        else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;

    }
    //设定坦克具体的方向

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_1:
                superFire();
                break;
        }
        locateDirection();
    }
    public Missile fire() {
        if(!live) return null;

        int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
        Missile m = new Missile(x, y, good, ptDir, this.tc);
        tc.missiles.add(m);
        return m;
        //坦克现在的位置和方向
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean isGood() {
        return good;
    }

    public boolean collideWithWall(Wall w) {
        if(this.live && this.getRect().intersects(w.gerRect())) {
            this.stay();//this method will not make the tank stick to the wall;
            return true;
        }
        return false;
    }

    private void stay() {
        x = oldX;
        y = oldY;//if collide happened, it's direction should change into the last one;
    }

    public boolean collideWithTank(java.util.List<Tank> tanks) {
        for(int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            if(this != t) {
                if(this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
                    this.stay();
                    t.stay();//this two lines make the two tanks stop when they meet;
                    return true;
                }
            }
        }

        return false;
    }

    public Missile fire(Direction dir) {
        if(!live) return null;

        int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
        Missile m = new Missile(x, y, good, dir, this.tc);
        tc.missiles.add(m);
        return m;
    }

    private void superFire() {
        Direction[] dirs = Direction.values();
        for(int i = 0; i < 8; i++) {
            fire(dirs[i]);
        }
    }

    private class BloodBar {
        public void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.RED);
            g.drawRect(x - 3, y - 13, WIDTH - 10, 5);

            int w =(WIDTH - 10) * life / 100;

            g.fillRect(x - 3, y - 13, w, 5);
            g.setColor(c);
        }
    }



    public boolean eat(Blood b) {
        if(this.live && b.getlive() && this.getRect().intersects(b.getRect())) {
            this.life = 100;
            b.setlive(false);
            return true;
        }
        return false;
    }
 }
