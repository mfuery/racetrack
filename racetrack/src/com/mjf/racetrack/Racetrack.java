package com.mjf.racetrack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mjf.racetrack.screens.GameScreen;

public class Racetrack extends Game {
    public static int gameWidth = 800, gameHeight = 600;
    public static boolean debug = true;

    private final InputHandler inputHandler = new InputHandler();
    private BitmapFont font;
    private SpriteBatch batch;

	@Override
	public void create() {
	    Assets.load();
	    Gdx.input.setInputProcessor(inputHandler);
	    setScreen(new GameScreen(this));

        font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);
        font.setScale(1f);
        batch = new SpriteBatch();
	}

	@Override
	public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // Renders active screen
	    super.render();

        if (Racetrack.debug) {
            batch.begin();
            font.draw(batch, "fps:" + Gdx.graphics.getFramesPerSecond(), 2, 20);
            batch.end();
        }
	}

	@Override
	public void dispose() {
        Gdx.input.setInputProcessor(null);
        batch.dispose();
        font.dispose();
	}

	@Override
	public void resize(int width, int height) {
	    gameWidth = width;
	    gameHeight = height;
	}

    public static void toggleDebug() {
        debug = !debug;
    }
}
