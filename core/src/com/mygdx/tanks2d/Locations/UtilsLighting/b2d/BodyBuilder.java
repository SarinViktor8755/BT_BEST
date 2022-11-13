package com.mygdx.tanks2d.Locations.UtilsLighting.b2d;//package com.mygdx.tanks2d.Locations.UtilsLighting.b2d;
//
//import com.badlogic.gdx.physics.box2d.*;
//
//public class BodyBuilder {
//
//    public static Body createBox(World world, float x, float y, int width, int height, boolean isStatic, boolean fixedRotation) {
//        Body pBody;
//        BodyDef def = new BodyDef();
//
//        if(isStatic)
//            def.type = BodyDef.BodyType.StaticBody;
//        else
//            def.type = BodyDef.BodyType.DynamicBody;
//
//        def.position.set(x , y);
//        def.fixedRotation = fixedRotation;
//        pBody = world.createBody(def);
//        pBody.setUserData("wall");
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(width, height);
//
//        FixtureDef fd = new FixtureDef();
//        fd.shape = shape;
//        fd.density = 1.0f;
//
//        fd.filter.groupIndex = 0;
//        pBody.createFixture(fd);
//        shape.dispose();
//        return pBody;
//    }
//
//    public static Body createBox(final World world, float x, float y, float w, float h,
//                                 boolean isStatic, boolean canRotate, short cBits, short mBits, short gIndex) {
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.fixedRotation = canRotate;
//        bodyDef.position.set(x , y );
//
//        if(isStatic) {
//            bodyDef.type = BodyDef.BodyType.StaticBody;
//        } else {
//            bodyDef.type = BodyDef.BodyType.DynamicBody;
//        }
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(w, h);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = 1.0f;
//        fixtureDef.filter.categoryBits = cBits; // Is a
//        fixtureDef.filter.maskBits = mBits; // Collides with
//        fixtureDef.filter.groupIndex = gIndex;
//
//        return world.createBody(bodyDef).createFixture(fixtureDef).getBody();
//    }
//
//    public static Body createCircle(final World world, float x, float y, float r,
//                                 boolean isStatic, boolean canRotate, short cBits, short mBits, short gIndex) {
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.fixedRotation = canRotate;
//        bodyDef.position.set(x , y );
//
//        if(isStatic) {
//            bodyDef.type = BodyDef.BodyType.StaticBody;
//        } else {
//            bodyDef.type = BodyDef.BodyType.DynamicBody;
//        }
//
//        CircleShape shape = new CircleShape();
//
//        shape.setRadius(r);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = 1.0f;
//        fixtureDef.filter.categoryBits = cBits; // Is a
//        fixtureDef.filter.maskBits = mBits; // Collides with
//        fixtureDef.filter.groupIndex = gIndex;
//
//        return world.createBody(bodyDef).createFixture(fixtureDef).getBody();
//    }
//
//    public static Body createCircle(final World world, float x, float y, float r) {
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.fixedRotation = false;
//        bodyDef.position.set(x , y);
//        bodyDef.type = BodyDef.BodyType.StaticBody;
//        CircleShape shape = new CircleShape();
//        shape.setRadius(r);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = 1.0f;
//        fixtureDef.filter.categoryBits = 1; // Is a
//        fixtureDef.filter.maskBits = 1; // Collides with
//        fixtureDef.filter.groupIndex = 1;
//
//        return world.createBody(bodyDef).createFixture(fixtureDef).getBody();
//    }
//
//}
