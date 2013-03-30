package com.mjf.racetrack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Level {
    private static final float CAMERA_WIDTH = 60f;
    private static final float CAMERA_HEIGHT = 40f;
    private static final float RUNNING_FRAME_DURATION = 0.06f;
    private static final float FONT_SCALE = 1f;

    private SpriteBatch batch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private BitmapFont font;

    public Level() {
        Texture.setEnforcePotImages(false);
        map = new TmxMapLoader().load("data/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);
        batch = new SpriteBatch();

        // create an orthographic camera, shows us 30x20 units of the world
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        camera.update();

        font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);
        font.setScale(FONT_SCALE);
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        map.dispose();
    }

    public void render(float delta) {

        camera.update();

        // set the tile map rendere view based on what the
        // camera sees and render the map
        renderer.setView(camera);
        renderer.render();

    }
}
