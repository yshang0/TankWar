import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {
    public static final int XSPEED = 10;
    public static final int YSPEED = 10;

    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;

    int x,y;
    Tank.Direction dir;

    private boolean live = true;

    private TankClient tc;

    public boolean isLive() {
        return live;
    }

    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile (int x, int y, Tank.Direction dir, TankClient tc) {
        this(x, y, dir);
        this.tc = tc;

    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillOval(x, y, 5, 5);
        g.setColor(c);

        move();
    }

    private void move() {
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
            default:
                break;
        }

        if(x<0 || y<0 || x>TankClient.GAME_WIDTH || y>TankClient.GAME_HEIGHT) {
            live = false;
            tc.missiles.remove(this);
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean hitTank(Tank t) {
        if(this.getRect().intersects(t.getRect())) {
            t.setLive(false);
            this.live = false;
            Explode e = new Explode(x, y, tc);
            tc.explodes.add(e); // nullpointerexception tc
            return true;
        }
        return false;
    }

    public boolean hitTanks(List<Tank> tanks) {
        for(int i = 0; i < tanks.size(); i++) {
            if(hitTank(tanks.get(i))) {
                return true;
            }
        }
        return false;
    }


}

