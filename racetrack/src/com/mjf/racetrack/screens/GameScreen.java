package com.mjf.racetrack.screens;

import com.badlogic.gdx.Screen;
import com.mjf.racetrack.Level;
import com.mjf.racetrack.Racetrack;

public class GameScreen implements Screen {

    private final Racetrack game;
    private Level level;

    public GameScreen(Racetrack game) {
        this.game = game;
        level = new Level();
    }

    @Override
    public void render(float delta) {
        level.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        level.dispose();
    }
}
