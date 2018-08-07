package worldTankWar2018;

import java.awt.*;

public class Blood {
    int x, y, w, h;
    TankClient tc;

    int step = 0;
    private boolean live = true;

    public boolean getlive() {
        return live;
    }

    public boolean setlive(boolean a) {
        live = a;
        return live;
    }

    private int[][] position= {
        {350, 300}, {355, 310}, {360, 320}, {370, 330}, {380, 340}, {390, 330}, {400, 350}
    };

    public Blood() {
        x = position[0][0];

        y = position[0][1];

        w = h = 15;
    }

    public void draw(Graphics g) {
        if(!live) {
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, w, h);
        g.setColor(c);

        move();
    }

    private void move() {
        step++;
        if(step == position.length) {
            step = 0;
        }

        x = position[step][0];
        y = position[step][1];
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, w, h);
    }


}
