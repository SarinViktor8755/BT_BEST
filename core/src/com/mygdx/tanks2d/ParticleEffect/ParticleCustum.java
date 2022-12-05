package com.mygdx.tanks2d.ParticleEffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.ParticleEffect.StereoSmoke.Falling_element;
import com.mygdx.tanks2d.ParticleEffect.StereoSmoke.Flying_stereo_elements_base;
import com.mygdx.tanks2d.ParticleEffect.StereoSmoke.PasricalDeathSmoke;
import com.mygdx.tanks2d.ParticleEffect.StereoSmoke.Point_of_fire;
import com.mygdx.tanks2d.ParticleEffect.StereoSmoke.Smoke_element;
import com.mygdx.tanks2d.Screens.GamePlayScreen;
import com.mygdx.tanks2d.Utils.VectorUtils;


import java.util.ArrayDeque;


public class ParticleCustum {
    ArrayDeque<ParticleSmoke> particleDeque;// дым
    ArrayDeque<PasricalExplosion> pasricalExplosions; // мелкие взрывы
    ArrayDeque<PasricalExplosionBigParameter> pasricalExplosionsBigParam; // большие взрывы
    ArrayDeque<Garbage> pasricalGarbage; // большие взрывы
    ArrayDeque<Shard> shardsArr;
    ArrayDeque<Explosion_Death> explosion_Death; // взрыв из тотал анигилейшен
    ArrayDeque<Explosion_Death> explosion_Death_little; // взрыв из тотал анигилейшен
    ArrayDeque<Falling_element> falling_elements; //падаюшие элементы
    ArrayDeque<Smoke_element> smoke_elements; // Дым в лбьемк
    ArrayDeque<PasricalDeathSmoke> pasricalDeathSmokes;

    ArrayDeque<Flying_stereo_elements_base> flying_stereo_elements_bases;

    ArrayDeque<Point_of_fire> point_of_fires;

    Vector2 temp_V;
    private SpriteBatch sb;

    private Texture t;
    private Texture f;
    private Texture iron;
    private Texture shardsTex;// осколки
    private TextureAtlas textureAtlasDeathExplosion; /// атлес текстур взрыва тотала
    Point_of_fire point_of_fire;

    Texture explosion_ring;

    GamePlayScreen gps;

    public ParticleCustum(GamePlayScreen gps, Texture t, Texture f, Texture iron, TextureAtlas de, Texture shards) {
        this.t = t;
        this.f = f;
        this.iron = iron;
        this.textureAtlasDeathExplosion = de;
        this.shardsTex = shards;
        this.sb = gps.getBatch();

        temp_V = new Vector2(0, 0);
        this.shardsArr = new ArrayDeque<>();
        this.particleDeque = new ArrayDeque<>();
        this.pasricalExplosions = new ArrayDeque<>();
        this.pasricalExplosionsBigParam = new ArrayDeque<>();
        //this.pasricalGarbage = new ArrayDeque<>();
        this.explosion_Death = new ArrayDeque<>(); ///  текстур взрыва тотала
        this.explosion_Death_little = new ArrayDeque<>(); ///  текстур взрыва тотала__little

        this.falling_elements = new ArrayDeque<>();
        this.smoke_elements = new ArrayDeque<>();
        this.pasricalDeathSmokes = new ArrayDeque<>();
        this.point_of_fires = new ArrayDeque<>();
        this.flying_stereo_elements_bases = new ArrayDeque<>();

        for (int i = 0; i < 2000; i++) {
            Shard ed = new Shard();
            this.shardsArr.add(ed);
        }

        for (int i = 0; i < 30; i++) {
            Point_of_fire ed = new Point_of_fire(this, t);
            this.point_of_fires.add(ed);
        }

        for (int i = 0; i < 120; i++) {
            Falling_element falling_element = new Falling_element();
            this.falling_elements.add(falling_element);
        }

        for (int i = 0; i < 120; i++) {
            Smoke_element smoke_element = new Smoke_element();
            this.smoke_elements.add(smoke_element);
        }

        for (int i = 0; i < 120; i++) {
            Falling_element falling_element = new Falling_element();
            this.falling_elements.add(falling_element);
        }

        for (int i = 0; i < 4; i++) {
            Explosion_Death ed = new Explosion_Death();
            this.explosion_Death.add(ed);
        }

        for (int i = 0; i < 25; i++) {
            Explosion_Death ed = new Explosion_Death();
            this.explosion_Death_little.add(ed);
        }

        for (int i = 0; i < 350; i++) {
            this.particleDeque.add(new ParticleSmoke());
        }

        for (int i = 0; i < 12; i++) {
            this.pasricalExplosions.add(new PasricalExplosion());
        }

        for (int i = 0; i < 20; i++) {
            this.pasricalExplosionsBigParam.add(new PasricalExplosionBigParameter());
        }

//        for (int i = 0; i < 800; i++) {
//            this.pasricalGarbage.add(new Garbage());
//        }

        for (int i = 0; i < 18; i++) {
            this.pasricalDeathSmokes.add(new PasricalDeathSmoke());
        }

        for (int i = 0; i < 800; i++) {
            this.flying_stereo_elements_bases.add(new Flying_stereo_elements_base(sb));
        }


        // point_of_fire = new Point_of_fire(15, 300, 300, this);


        this.gps = gps;
        t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        shards.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        //f.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        explosion_ring =  gps.getAMG().get("explosion_ring.png");

    }

    public Texture getT() {
        return t;
    }

    public void render(SpriteBatch sb) {
        float dt = Gdx.graphics.getDeltaTime();
        rander_point_of_fires(dt);
        rander_particleDeque(); // кусочки мусора
        rander_explosion_Death_little(); // маленькие взрывы
//        rander_pasricalExplosions();
        //rander_pasricalExplosionsBigParam();
        // updatePoinsFire(dt); // обновление дыма
        rander_smoke_death(sb, Gdx.graphics.getDeltaTime());
        rander_explosion_Death(); // большой взрыв
//        rander_Falling_element(sb, dt);
        //  rander_smoke_element(sb, dt); // дым вылетающий от ранения
        rander_flying_stereo_elements_bases();  // дым вылетающий от ранения НОВЫЙ

//        if (MathUtils.randomBoolean(.3f)) {
//           // System.out.println("addFly");
//            add_flying_stereo_elements_bases(
//                    gps.getTank().getPosition().x, gps.getTank().getPosition().y,
//                    1,.5f, 0.5f, t, 1, 1, 1, .8f
//            );
//        }


    }

    public void randerGarbage(SpriteBatch spriteBatch) {

        for (Shard s : shardsArr) {  // частицы
            s.upDate();
            spriteBatch.setColor(0, 0, 0, 1);


            if (s.temperature > 0) {
                s.temperature -= Gdx.graphics.getDeltaTime();
                float a = MathUtils.random(0, s.temperature);
                spriteBatch.setColor(a, a, a, 1);
                spriteBatch.draw(shardsTex, s.getPos().x, s.getPos().y, shardsTex.getWidth() * 1.5f, shardsTex.getHeight() * 1.5f);
            } else
                // spriteBatch.draw(shardsTex,MathUtils.random(150,500),MathUtils.random(150,500));
                spriteBatch.draw(shardsTex, s.getPos().x, s.getPos().y);
        }

    }


    public void addAnimationDeath(float x, float y) {

        this.addPasricalDeath(x, y);
        for (int i = 0; i < 90; i++) {
            addShares(x, y);
        }

        gps.getAudioEngine().pleySoundKickExplosion(x, y, gps.getCameraGame().getCamera().position.x, gps.getCameraGame().getCamera().position.y);
        gps.getGameSpace().getRadspurens().addCrater(x, y, MathUtils.random(0, 360)); // кратор
        gps.pc.addPasricalDeath_little(x, y, 2.7f);

    }


    public void rander_explosion_Death() {
        for (Explosion_Death ed : explosion_Death) {
            if (!ed.isLife()) continue;
            ed.update();
            /////////////////
            TextureAtlas.AtlasRegion tex = textureAtlasDeathExplosion.findRegion(ed.getNameTextureRegion());
            float xw = MathUtils.map(100, 0, 100, 0, tex.getRegionWidth());
            float yw = MathUtils.map(100, 0, 100, 0, tex.getRegionHeight());
            /////////////////
            //    System.out.println("getTime_life"+ed.getTime_life());
            sb.setColor(1,1,1,Interpolation.pow2InInverse.apply(ed.getTime_life() -0.95f));
            sb.draw(
                    explosion_ring,
                    ed.getPosition().x - (tex.getRegionWidth() / 2 / ed.getKefm() * ed.getTime_life()) , ed.getPosition().y - (tex.getRegionHeight() / 2 / ed.getKefm() * ed.getTime_life()),
                    xw / ed.getKefm() * ed.getTime_life(), yw / ed.getKefm() * ed.getTime_life()
            );

            sb.setColor(1,1,1,1);
            sb.draw(
                    tex,
                    ed.getPosition().x - (tex.getRegionWidth() / 2), ed.getPosition().y - (tex.getRegionHeight() / 2),
                    xw, yw
            );



        }
    }

    public void rander_flying_stereo_elements_bases() {
        for (Flying_stereo_elements_base fse : flying_stereo_elements_bases) {
            fse.rander(Gdx.graphics.getDeltaTime(), gps.getCameraGame().getCamera());
        }
    }

    public void add_flying_stereo_elements_bases(float x, float y, float h, float scale, float speed, Texture tex, float r, float g, float b, float a) {
        Flying_stereo_elements_base fe = this.flying_stereo_elements_bases.pollLast();
        fe.add(x, y, h, scale, speed, tex, r, g, b, a);
        this.flying_stereo_elements_bases.offerFirst(fe);
    }

    public void addParticalsSmokeStereo(float x, float y, float hp) {/// дым горения Уже обновил
        float black = MathUtils.map(50, 0, 0, 1, hp) + MathUtils.random(-.35f, +.35f);
        if (!checkViseble(x, y)) return;
        float delta = 12;
        add_flying_stereo_elements_bases(
                x + MathUtils.random(-delta, delta), y + MathUtils.random(-delta, delta),
                0, MathUtils.random(.3f, 1),
                2, t, 1 - black, 1 - black, 1 - black, 1
        );

    }


    public void addParticalsSmokeStereo(float x, float y, float random, boolean a) {/// дым умершего
        Smoke_element smoke_element = this.smoke_elements.pollLast();
        smoke_element.add(x + MathUtils.random(0, 160), y + MathUtils.random(-random, random),
                0f, 1, t,
                1, 0, MathUtils.random(.5f, 1), 1
        );
        //      smoke_element.add(gps.getTank().getPosition().x + 16 + MathUtils.random(-16, 16), gps.getTank().getPosition().y + 16 + MathUtils.random(-16, 16), MathUtils.random(2, 8), MathUtils.random(35, 40), t);
        smoke_elements.offerFirst(smoke_element);
    }


    private void rander_explosion_Death_little() { // Рендер смерти
        for (Explosion_Death ed : explosion_Death_little) {
            if (!ed.isLife()) continue;
            ed.update();

            /////////////////
            //Texture explosion_ring =  gsp.getAMG().get("trb2.png");
            TextureAtlas.AtlasRegion tex = textureAtlasDeathExplosion.findRegion(ed.getNameTextureRegion());
            float xw = MathUtils.map(100, 0, 100, 0, tex.getRegionWidth());
            float yw = MathUtils.map(100, 0, 100, 0, tex.getRegionHeight());
            /////////////////
            sb.draw(
                    tex,
                    ed.getPosition().x - (tex.getRegionWidth() / 2 / ed.getKefm() * ed.getTime_life()), ed.getPosition().y - (tex.getRegionHeight() / 2 / ed.getKefm() * ed.getTime_life()),
                    xw / ed.getKefm() * ed.getTime_life(), yw / ed.getKefm() * ed.getTime_life()
            );




        }
    }

    public void addParticalsSmoke(int quantity, float x, float y, int hp) {
        // System.out.println(hp);
        if (VectorUtils.getLen2(gps.getTank().getPosition(), x, y) > 90_000) return;
        float black = MathUtils.map(50, 0, 0, 1, hp) + MathUtils.random(-.35f, +.35f);
        //  System.out.println(black);
        if (!checkViseble(x, y)) return;
        for (int i = 0; i < quantity; i++) {
            ParticleSmoke a = this.particleDeque.pollLast();
            a.setParameters(MathUtils.random(.5f, 2),
                    x + MathUtils.random(-7, 7),
                    y + MathUtils.random(-7, 7),
                    MathUtils.random(0, 360), MathUtils.random(-20, 20),
                    MathUtils.random(-.2f, .7f),
                    MathUtils.random(-.2f, .4f),
                    MathUtils.random(-10, 10), MathUtils.random(-10, 10),
                    1 - black, 1 - black, 1 - black, 1
            );
            this.particleDeque.offerFirst(a);
        }
    }


    public void add_Point_of_fire(float x, float y) {
        Point_of_fire pf = this.point_of_fires.pollLast();
        pf.setParametors(MathUtils.random(7, 12), x, y);
        this.point_of_fires.offerFirst(pf);
    }


//    public void addParticalsSmokeOne(float x, float y) {
//        if (VectorUtils.getLen2(gps.getTank().getPosition(), x, y) > 90_000) return;
//        ParticleSmoke a;
//        a = this.particleDeque.pollLast();
//        a.setParameters(1.0f,
//                x, y,
//                MathUtils.random(0, 360), MathUtils.random(-5, 5),
//                .5f,
//                1.2f,
//                -.4f, -.4f,
//                MathUtils.random(.7f, 1), .4f, .4f,
//                .2f
//        );
//        this.particleDeque.offerFirst(a);
//    }

    public void addParticalsSmokeOneBullet(float x, float y) {
        if (!checkViseble(x, y)) return;
        ParticleSmoke a;
        a = this.particleDeque.pollLast();
        a.setParameters(.4f,
                x, y,
                MathUtils.random(0, 360), MathUtils.random(-5, 5),
                .3f,
                00.1f,
                -.4f, -.4f,
                .4f, .4f, MathUtils.random(.7f, 1),
                1f
        );
        this.particleDeque.offerFirst(a);
    }


    public void addPasricalDeath(float x, float y) {
        if (!checkViseble(x, y)) return;
        Explosion_Death a = this.explosion_Death.pollLast();
        a.setParameters(x, y);
        //System.out.println("Vzriv smerti");
        // addDeathSmoke(x, y);
        this.explosion_Death.offerFirst(a);
        add_Point_of_fire(x, y);

    }

    public void addPasricalDeath_little(float x, float y, float kefM) {
        if (!checkViseble(x, y)) return;
        Explosion_Death a = this.explosion_Death_little.pollLast();
        a.setParameters(x, y, kefM);
        this.explosion_Death_little.offerFirst(a);
    }

//    public void addGarbage(float x, float y) {
//        Garbage g = this.pasricalGarbage.pollLast();
//        g.setParametors(x, y);
//        this.pasricalGarbage.offerFirst(g);
//    }


    public void addShares(float x, float y) {
        temp_V.set(1, 1).setLength2(MathUtils.random(150, 230));
        temp_V.setAngleDeg(MathUtils.random(0, 360));
        addShares(x, y, temp_V.x, temp_V.y);
        // System.out.println("!");

    }

    public void addShares(float x, float y, float vx, float vy) {
        Shard g = this.shardsArr.pollLast();
        g.setParametors(x, y);
        g.setParametors(x, y, vx, vy);
        this.shardsArr.offerFirst(g);
    }


    public void addPasricalExplosionDeath(float x, float y) {
        if (!checkViseble(x, y)) return;
        PasricalExplosionBigParameter f;
        f = this.pasricalExplosionsBigParam.pollLast();
        f.setParameters(x, y);
        this.pasricalExplosionsBigParam.offerFirst(f);

        for (int i = 0; i < 2; i++) {
            f = this.pasricalExplosionsBigParam.pollLast();
            f.setParameters(x + MathUtils.random(-40, +40), y + MathUtils.random(-40, +40));
            this.pasricalExplosionsBigParam.offerFirst(f);
        }


    }

    private boolean checkViseble(float x, float y) {
        if (!MathUtils.isEqual(gps.getCameraGame().getCameraPosition().x, x, 500)) return false;
        if (!MathUtils.isEqual(gps.getCameraGame().getCameraPosition().y, y, 350)) return false;
        return true;
    }


    public void rander_Falling_element(SpriteBatch spriteBatch, float deltaTimme) {
        for (Falling_element falling_element : falling_elements) {
            falling_element.rander(deltaTimme, gps.getCameraGame().getCamera(), spriteBatch);

        }
    }

    private void rander_point_of_fires(float dt) {
        for (Point_of_fire pf : point_of_fires) {
            pf.update(dt);
        }
    }

    private void rander_particleDeque() {
        for (ParticleSmoke u : particleDeque) {
            if (!u.isLife()) continue;
//            k++;

            // sb.setColor(1, 0, 1, 1);

            u.update();
            u.setAlpha(MathUtils.clamp(u.getTime_life(), -1, .7f));
            sb.setColor(u.getColor());
            sb.draw(t,
                    u.getPosition().x - t.getWidth() / 2, u.getPosition().y - t.getHeight() / 2,
                    t.getWidth() / 2, t.getHeight() / 2,
                    t.getWidth(), t.getHeight(),
                    u.getScale(), u.getScale(),
                    u.getRotation(),
                    0, 0,
                    t.getWidth(), t.getHeight(),
                    false, false);
        }
        sb.setColor(1, 1, 1, 1);

    }

    public void addPasricalExplosion(float timeLife, float x, float y) {
        if (!checkViseble(x, y)) return;
        PasricalExplosion a = this.pasricalExplosions.pollLast();
        a.setParameters(timeLife, x, y);
        this.pasricalExplosions.offerFirst(a);
    }

    private void rander_pasricalExplosions() {  // взрывы вроде мелкие
        for (PasricalExplosion u : pasricalExplosions) {
            //  System.out.println("Fire 1  " + pasricalExplosions.size());
            if (!u.isLife()) continue;
            u.update();
            //     System.out.println("Fire 2");
            sb.setColor(1, 1, 1, MathUtils.clamp(u.getTime_life(), -1, 1) * 10);
            sb.draw(f,
                    u.getPosition().x - f.getWidth() * u.getScale() / 2, u.getPosition().y - f.getWidth() * u.getScale() / 2,
                    0, 0,
                    f.getWidth(), f.getHeight(),
                    u.getScale(), u.getScale(),
                    0,
                    0, 0,
                    f.getWidth(), f.getHeight(),
                    false, false);

        }
    }

    private void rander_pasricalExplosionsBigParam() {
        for (PasricalExplosionBigParameter fd : pasricalExplosionsBigParam) {  // смерть большие
            if (!fd.isLife()) continue;
            fd.update(this);
            sb.setColor(1, 1, 1, fd.getAlpha());
            //System.out.println(fd.getAlpha());
            sb.draw(f,
                    fd.getPosition().x - t.getWidth() / 2, fd.getPosition().y - t.getHeight() / 2,
                    f.getWidth() / 2, f.getHeight() / 2,
                    f.getWidth(), f.getHeight(),
                    fd.getScale(), fd.getScale(),
                    fd.getRot(),
                    0, 0,
                    f.getWidth(), f.getHeight(),
                    false, false);
        }
    }


    public void rander_smoke_element(SpriteBatch spriteBatch, float deltaTimme) {

        for (Smoke_element smoke_element : smoke_elements) {
            smoke_element.rander(deltaTimme, gps.getCameraGame().getCamera(), spriteBatch);

        }
    }

    public void rander_smoke_death(SpriteBatch spriteBatch, float deltaTimme) {
        for (PasricalDeathSmoke par : pasricalDeathSmokes) {
            par.rander(deltaTimme, gps.getCameraGame().getCamera(), spriteBatch);

        }
    }


    public void generatorSmoke(float hp, float x, float y) { // генератор Дыма для танка
        float dhp = hp;
        if (hp > 95) return;
        dhp = MathUtils.map(0, 90, 1, 0.0f, dhp);
        dhp = Interpolation.fade.apply(dhp);
        //System.out.println(dhp);
        if (MathUtils.randomBoolean(dhp / 3f))
            addParticalsSmokeStereo(x, y, hp);
    }


}
