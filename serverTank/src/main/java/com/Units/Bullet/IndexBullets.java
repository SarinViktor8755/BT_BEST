package main.java.com.Units.Bullet;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.tanks2d.ClientNetWork.Heading_type;

import main.java.com.GameServer;
import main.java.com.MainGame;
import main.java.com.MatchOrganization.IndexMath;
import main.java.com.Units.ListPlayer.Player;

public class IndexBullets {

    private final Array<Bullet> activeBullets = new Array<>();
    private final BulletPool bp = new BulletPool();
    private GameServer gameServer;


    private long previousStepTime = 0;
    private static int BULLET_SPEED = 400;


    public IndexBullets(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void addBullet(Vector2 pos, Vector2 vel, int nom, int nomAuthor) { // координаты, навправление, номер снаряда
        vel.clamp(BULLET_SPEED,BULLET_SPEED);
        // получи пулю из нашего бассейна
        Bullet b = bp.obtain();
        /// стреляйте пулей с того места, на которое мы нажимаем, в направлении прямо вверх
        b.fireBullet(pos.x, pos.y, vel.x, vel.y, nom,nomAuthor);
        // добавьте в наш массив маркеры, чтобы мы могли получить к ним доступ в нашем методе визуализации
        activeBullets.add(b);
//        System.out.println("activeBullets.size " + activeBullets.size);
       // System.out.println("++ bullet " + );


        /// ОТПРАВКА О ТОМ ЧТО УМЕР СНАРЯД
       // gameServer.outMassegeCollection.tellAboutShootBot((int)pos.x,(int)pos.y,(int)vel.angleDeg(),nom,nomAuthor);
    }

    private boolean checkingGoingAbroad(float x, float y) {
        // System.out.println(!gameServer.getMainGame().getMapSpace().isPointInCollision(x, y));
        if (gameServer.getMainGame().getMapSpace().isPointInCollision(x, y)) return true;
        if (!gameServer.getMainGame().getMapSpace().isPointWithinMmap(x, y)) return true;
        return false;
    }

    public boolean iSNullActiveBullets() {
        return activeBullets.size < 1;
    }

    public void updateBulets(float dt) {
        Bullet bullet;

        for (int i = 0; i < activeBullets.size; i++) {
            bullet = activeBullets.get(i);
            bullet.update(dt);

            ////////////
           // System.out.println(bullet.getAuthor_bullet());

            /// попадание по игкроку(np)
            int np = gameServer.getLp().projectile_collide_with_players(bullet.getAuthor_bullet(),bullet.position.x, bullet.position.y);

            if(np!=-1){


                delBullet(bullet); // /тут ели попали в игрок 1. минусуем хп 2. уничтожаем патрон 3. рассылаем игрока
                Player player = gameServer.getLp().getPlayerForId(np);
                gameServer.getLp().getPlayerForId(bullet.getAuthor_bullet()).getCommand();
                if(gameServer.getLp().getPlayerForId(bullet.getAuthor_bullet()).getCommand() == player.getCommand()) {
                //    System.out.println("frend");
                    continue;
                }
                //System.out.println("vrag");
                int  hp = player.minusHP(MathUtils.random(18,27));
                gameServer.send_PARAMETERS_PLAYER(player); // рассылка всем
                if(hp < 1){     // если игрок умер тогда присваиваем очки
                    IndexMath.add_score_team(player.getCommand());

                    /// отправить сообщение

                }

                if(bullet.getTimeLife() > Heading_type.SHOT_LIFETIME)  delBullet(bullet,false); // конец жизни пули )))
                gameServer.send_PARAMETERS_MATH();
            }
          //  System.out.println("______________________"+ np);
            if(gameServer.getMainGame().getMapSpace().isPointInCollision(bullet.position.x,bullet.position.y)) delBullet(bullet);

            ////////////

          //  System.out.println("  " + activeBullets.get(i));
            if (checkingGoingAbroad(bullet.position.x, bullet.position.y)) {
                freeBullet(i,bullet);
              //  System.out.println("вышел за границу карты x: " + bullet.position.x +"  y: " + bullet.position.y);
            }


        }

        /// System.out.println(activeBullets.size);
    }

    public synchronized void freeBullet(int index, Bullet bullet){
        /// System.out.println("!!!!!!!!!!!!!!!!!!!BOOOOOOOOOOOOOOOOOOOM !!!!!!!!!!!!!!!!!!!  " + bullet.position);
      /////////////  gameServer.outMassegeCollection.tellShellRupture(bullet.position,bullet.getNom());
        bp.free(bullet);
        activeBullets.removeIndex(index);

    }

    public void delBullet(Bullet bullet){
        gameServer.sendSHELL_RUPTURE(bullet.position.x, bullet.position.y,bullet.getNom(),bullet.getAuthor_bullet());
        bullet.position.set(-20_000,-20_000);
    }

    public void delBullet(Bullet bullet, boolean life){
        //gameServer.sendSHELL_RUPTURE(bullet.position.x, bullet.position.y,bullet.getNom(),bullet.getAuthor_bullet());
        bullet.position.set(-20_000,-20_000);
    }


}