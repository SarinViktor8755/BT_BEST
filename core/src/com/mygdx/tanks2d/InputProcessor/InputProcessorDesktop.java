package com.mygdx.tanks2d.InputProcessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.tanks2d.Screens.GamePlayScreen;

import java.io.InputStream;

public class InputProcessorDesktop implements InputProcessor {
    GamePlayScreen gps;
    Vector2 velocity;



    public InputProcessorDesktop(GamePlayScreen gamePlayScreen) {
        this.gps = gamePlayScreen;
        this.velocity = new Vector2(0,0);
        Gdx.input.setInputProcessor(this);
        System.out.println("create_InputProcessor");
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
     //   System.out.println(">>>>>");
        System.out.println(keycode);
            return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println(keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
