package main.java.com.Bots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;
import com.mygdx.tanks2d.ClientNetWork.Network;
import com.mygdx.tanks2d.Units.ListPlayers;

import main.java.com.GameServer;
import main.java.com.Units.ListPlayer.Player;

public class BehaviourBot { // поведение бота - вектор направлениея  - логика принятия решений




//
//    public static void moveBot(float dt, DBBot db_bot) { // просто перемещение бота
//        db_bot.getPosition().add(db_bot.getBody_rotation_angle().scl(dt * SPEED));
//    }
//
//    private static void rotation_body(float dt, DBBot db_bot) { // поворот тела
//        if (MathUtils.isEqual(db_bot.getBody_rotation_angle().angleDeg(), db_bot.getTarget_body_rotation_angle().angleDeg(), .5f))
//            return;
//        if ((db_bot.getBody_rotation_angle().angleDeg(db_bot.getTarget_body_rotation_angle()) > 180)) {
//            db_bot.getBody_rotation_angle().setAngleDeg(db_bot.getBody_rotation_angle().angleDeg() - dt * SPEED_ROTATION);
//        } else
//            db_bot.getBody_rotation_angle().setAngleDeg(db_bot.getBody_rotation_angle().angleDeg() + dt * SPEED_ROTATION);
//    }
//
//

//
//    public static void updatePlayerInPlayList(){
////        for (var entry : map.entrySet()) {
////            System.out.println(entry.getKey() + "/" + entry.getValue());
////        }
////
////
//    }


}
