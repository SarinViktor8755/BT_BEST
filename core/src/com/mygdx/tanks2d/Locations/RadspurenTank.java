package com.mygdx.tanks2d.Locations;

public class RadspurenTank {
    float xp,yp,aligbDeg,timer;
    boolean life;


    public RadspurenTank(float xp, float yp, float aligbDeg, boolean life) {
        this.xp = xp;
        this.yp = yp;
        this.aligbDeg = aligbDeg;
        this.timer = 0;
        this.life = life;
    }

    @Override
    public String toString() {
        return "Radspuren{" +
                "xp=" + xp +
                ", yp=" + yp +
                ", aligbDeg=" + aligbDeg +
                ", timer=" + timer +
                ", life=" + life +
                '}';
    }
}
