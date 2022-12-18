package main.java.com.Units.SpaceMap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import main.java.com.GameServer;
import main.java.com.Units.SpaceMap.Figure.Ellipse;
import main.java.com.Units.SpaceMap.Figure.Figure;
import main.java.com.Units.SpaceMap.Figure.Rectangle;

public class IndexMap {
    // private TiledMap map;

    private int width_map; // ширина карты
    private int height_map; // высота карты

    private Vector2 rasp1, rasp2;

    private final Vector2 centr = new Vector2(width_map / 2f, height_map / 2);

//    final float SPEED = 90f;
//    final float SPEED_ROTATION = 180f;

    private String map_math;

    private ArrayList<Figure> allfigure; // набор колизий

    public IndexMap(String map_math) {
        allfigure = new ArrayList<>();
        this.map_math = map_math;
        //String map = readFile("index.json");
        System.out.println("GET_MAP " + map_math + "  " + map_math + ".json");

        JSONObject obj = new JSONObject(readFile(map_math + ".json"));
        // System.out.println(obj);
        //   String firstName = String.valueOf(obj.getInt("height"));
        // System.out.println(firstName);
        width_map = obj.getInt("width") * obj.getInt("tilewidth"); // ширина карты
        height_map = obj.getInt("height") * obj.getInt("tilewidth");  // высота карты
//        System.out.println(width_map);
        // JSONArray arr = obj.getJSONArray("layers").getJSONObject(1).getJSONArray("objects");
        // JSONObject arr = obj.getJSONArray("layers").get
//        System.out.println("!!!!!!!!!!!!!!!!!");
//        System.out.println(obj.getJSONArray("layers").getJSONObject(2).getJSONArray("objects"));
//        System.out.println("!!!!!!!!!!!!!!!!!");
        JSONArray arr = obj.getJSONArray("layers").getJSONObject(2).getJSONArray("objects");

        for (int i = 0; i < arr.length(); i++) {
            ceateObjectmap((JSONObject) arr.get(i));
        }
        //     System.out.println("rasp1 "+rasp1 +"  rasp2 "+  rasp2);
       // System.out.println("install_map : " + GameServer.getDate());
    }

    ////////////////
    public void ceateObjectmap(JSONObject obj) {
        if (obj.optBoolean("ellipse")) createEllipse(obj);
        else createRectangle(obj);

    }


    private void createEllipse(JSONObject obj) {
        Ellipse e = new Ellipse(new Vector2(obj.getInt("x"), obj.getInt("y")), obj.getInt("width"), obj.getInt("height"));
        this.allfigure.add(e);
        // System.out.println("createEllipse position " + e.getPositionCenter()+"  redius " + e.getRadius() + "  redius2 " + e.getRadius2());
        //  System.out.println(this.allfigure);
    }

    public String getMap_math() {
        return map_math;
    }

    private void createRectangle(JSONObject obj) {
       // System.out.println(obj.get("name"));
        if (obj.get("name").equals("resp_1")) {
            rasp1 = new Vector2(obj.getInt("x"), height_map - obj.getInt("y"));
            return;
        }
        if (obj.get("name").equals("resp_2")) {
            rasp2 = new Vector2(obj.getInt("x"), height_map - obj.getInt("y"));
            return;
        }
        Rectangle r = new Rectangle(obj.getInt("x"), height_map - obj.getInt("y"), obj.getInt("width"), obj.getInt("height"), this.height_map);


        this.allfigure.add(r);
        // System.out.println(" Rectangle: position" + r.getPosition() + "  WH" + r.getPositionWH());

    }
//////////////

    public String readFile(String name) {

        try {
            FileInputStream fis = null;
            fis = new FileInputStream(name);
            byte[] buffer = new byte[10];
            StringBuilder sb = new StringBuilder();
            while (fis.read(buffer) != -1) {
                sb.append(new String(buffer));
                buffer = new byte[10];
            }
            fis.close();
            String content = sb.toString();
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "{}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }

    }


    public boolean isPointWithinMmap(Vector2 p) {
        return isPointWithinMmap(p.x, p.y);
    }

    public boolean isPointWithinMmap(float x, float y) {
        //System.out.println(" width_map" + width_map + "  height_map" + height_map);
        if (x > width_map - 7) return false;
        if (x < 7) return false;
        if (y > height_map - 7) return false;
        if (y < 7) return false;
        return true;
    }

    public void returnToSpace(Vector2 pos) { // возвращение игрока в карту
        if (pos.x < 0 - 7) pos.x = 0;
        if (pos.x > width_map + 7) pos.x = width_map;
        if (pos.y < 0 - 7) pos.y = 0;
        if (pos.y > height_map + 7) pos.y = height_map;
    }

    public Vector2 getCentrMap() {
        return centr;
    }

    public boolean isPointInCollision(float x, float y) { // кализия для бууулета
        for (int i = 0; i < allfigure.size(); i++) {
            if (allfigure.get(i) instanceof Rectangle) {
                Rectangle r = (Rectangle) allfigure.get(i);
                /// true если касается
                //System.out.println("111");
                if (r.isPointCollision((int) x, (int) y)) return true;
            }

            if (allfigure.get(i) instanceof Ellipse) {
//                Ellipse e = (Ellipse) allfigure.get(i);
//                if (e.isPointCollision((int) x, (int) y)) return true;
            }

        }
        return false;
    }


    public void resolving_conflict_with_objects(Vector2 pos, float dt) { /// решение коллизии с обьектами
        for (int i = 0; i < allfigure.size(); i++) {
            if (allfigure.get(i) instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) allfigure.get(i);
                //    Vector2 resolving = rectangle.get_vector2_from_center(pos);
                if (!rectangle.isPointCollision(pos.x, pos.y)) continue;

                float min;

                float x1 = Math.abs(pos.x - rectangle.getPositionWH().x);
                float x2 = Math.abs(pos.x - rectangle.getPosition().x);

                float y1 = Math.abs(pos.y - rectangle.getPositionWH().y);
                float y2 = Math.abs(pos.y - rectangle.getPosition().y);

                if ((x1 < x2) && (x1 < y1) && (x1 < y2)) {
                    pos.x = rectangle.getPositionWH().x + 15;
                    return;
                }
                if ((x2 < x1) && (x2 < y1) && (x2 < y2)) {
                    pos.x = rectangle.getPosition().x - 15;
                    return;
                }

                if ((y1 < x1) && (y1 < x2) && (y1 < y2)) {
                    pos.y = rectangle.getPositionWH().y + 15;
                    return;
                }
                if ((y2 < x1) && (y2 < y1) && (y2 < y1)) {
                    pos.y = rectangle.getPosition().y - 15;
                    return;
                }

            }

            if (allfigure.get(i) instanceof Ellipse) {
                Ellipse e = (Ellipse) allfigure.get(i);
                // if (!e.isPointCollision(pos.x, pos.y)) continue;
//                System.out.println("Ellipse");
//                pos.set(500,500);


            }
        }
    }

    public Vector2 getRasp1() {
        return rasp1;
    }

    public Vector2 getRasp2() {
        return rasp2;
    }

    public int getWidth_map() {
        return width_map;
    }

    public int getHeight_map() {
        return height_map;
    }

    ////////////////////////////////////////
//    public void collisinRectangleTrue(float dt) {
//        Vector2 c = gsp.getGameSpace().getMainCollision().isCollisionsRectangle(getPosition());
//        if (c != null) position.add(c.scl(SPEED * dt));
//    }

    //
//    public void collisinCircleTrue() {
//        Vector2 c = isCircleCircle(getPosition());
//        if (c != null) position.add(c.scl(SPEED * dt));
//    }
//
//    private void collisinOtherTanksTrue() {
//        Vector2 ct = gsp.getTanksOther().isCollisionsTanks(position);
//        if (ct != null) {  // танки другие
//            position.add(ct.scl(2 * SPEED * dt));
//        }
//    }
//    /////////////////////////////////////
//    public Vector2 isCollisionsRectangle(Vector2 pos) {
//        for (BoxCollision b : box) {
//            if (!b.isCollisionTank(pos)) {
//                tempVector.set(pos.cpy().sub(b.center));
//                if (Math.abs(tempVector.x) >= Math.abs(tempVector.y)) {
//                    if (tempVector.x > 0) return new Vector2(1, 0);
//                    else return new Vector2(-1, 0);
//                } else {
//                    if (tempVector.y > 0) return new Vector2(0, 1);
//                    else return new Vector2(0, -1);
//                }
//            }
//        }
//        return null;
//    }
//
//
//    public Vector2 isCircleCircle(Vector2 pos) {
//        for (CircleCollision c : circle) {
//            if (!c.isCollisionCircle(pos)) {
//                return tempVector.set(pos.cpy().sub(c.circule).nor());
//            }
//        }
//
//        return null;
//    }
    //////////////////////////////////////
    public boolean in_dimensions_terrain(float x, float y) {
        if (x > width_map) return false;
        if (x < 0) return false;
        if (y > height_map) return false;
        if (y < 0) return false;
        return true;
    }
}
