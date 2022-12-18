package main.java.com.Units.Bullet;

import com.badlogic.gdx.utils.Pool;

public class BulletPool extends Pool<Bullet> {

    // конструктор с начальным количеством объектов и максимальным количеством объектов
// max-это максимальное количество объектов, содержащихся в пуле, а не
// максимальное количество объектов, которые могут быть созданы пулом
    public BulletPool(int init, int max){
        super(init,max);
    }

    // создайте пул с 16 начальными объектами по умолчанию и без макс.
    public BulletPool(){
        super();
    }

    // метод создания одного объекта
    @Override
    protected Bullet newObject() {
           System.out.println("Creating new bullet");
        return new Bullet();
    }

}