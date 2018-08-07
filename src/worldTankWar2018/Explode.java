package worldTankWar2018;

import java.awt.*;


public class Explode {
    int x, y;
    private boolean live = true;

    private TankClient tc;

    private static Toolkit tk = Toolkit.getDefaultToolkit();
    //use tool package to get the image from the hard disk (String filename, URL url<most used>)
    //filename, absolute name, relative name,


    private static Image[] imgs = {
        tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
        tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
    };
    //worldTankWar2018.Explode.class -> reflection: Class(a description for one class has been edit completely;
    // /Users/shangyang/IdeaProjects/TankWar/src/images/0.gif
    // /Users/shangyang/IdeaProjects/TankWar/out/production/TankWar/images/0.gif


    int step = 0;

    private static boolean init;
    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if(!init){
            for(int i = 0; i < imgs.length; i++) {
                g.drawImage(imgs[i], -100, -100, null);
            }

            init = true;
        }

        if(!live) {
            tc.explodes.remove(this);
            return;
        }

        if(step == imgs.length) {
            live = false;
            step = 0;
            return;
        }

        //io problem; do when read; Synchronized io;

        g.drawImage(imgs[step], x, y, null);

        step++;
    }
}
