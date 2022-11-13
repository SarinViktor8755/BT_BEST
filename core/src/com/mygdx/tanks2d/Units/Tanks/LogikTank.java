package com.mygdx.tanks2d.Units.Tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.tanks2d.Locations.GameSpace;

public class LogikTank { /// логика сервера

    final float SPEED = 90;
    final float SPEED_ROTATION = 180;

    private GameSpace gameSpace;

    public LogikTank(GameSpace gameSpace) {
        this.gameSpace = gameSpace;
    }

    public void moveTank(OpponentsTanks t) { // перемещение танка бота )))
        randomDirection(t);


        updateTank(t); //обновить танк
    }


    private void  randomDirection(OpponentsTanks t){ ///случайное направление
        float raz = Math.abs(t.getDirection().angleDeg() - t.getDirectionMovementControll().angleDeg());
        if (raz > 20)
            if ((t.getDirectionMovementControll().angleDeg(t.getDirection()) > 180))
                t.getDirection().setAngleDeg(t.getDirection().angleDeg() - Gdx.graphics.getDeltaTime() * SPEED_ROTATION);
            else
                t.getDirection().setAngleDeg(t.getDirection().angleDeg() + Gdx.graphics.getDeltaTime() * SPEED_ROTATION);
        if (MathUtils.randomBoolean(.004f))
            t.getDirectionMovementControll().rotateDeg(MathUtils.random(-360, 360));
    }


    private void updateTank(OpponentsTanks t){
        t.getPosition().add(
                t.getDirection().clamp(SPEED, SPEED).scl(Gdx.graphics.getDeltaTime())
        );
    }


    private boolean checkCosllisionsByDirection(OpponentsTanks t, float align){ // препятствие по направлению
      //  t.getPosition().cpy()



        return true;
    }

}
