package com.mjf.racetrack;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;

public class Racetrack extends Game implements InputProcessor {
    public static int gameWidth = 800, gameHeight = 600;
    public static final int WIDTH_METERS = 96, HEIGHT_METERS = 64;
    public static final float UNIT_DISTANCE = 0.5f;
    public static boolean debug = true;

    private BitmapFont font;
    private SpriteBatch batch;

    // Box2D
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    protected MouseJoint mouseJoint = null;
    protected Body hitBody = null;

    Racecar racecar;

    /**
     * ApplicationListener implementation
     */

	@Override
	public void create() {
	    Gdx.input.setInputProcessor(this);

        font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);
        font.setScale(1f);
        batch = new SpriteBatch();

        world = new World(new Vector2(0,0), true);
        debugRenderer = new Box2DDebugRenderer();

        // setup the camera. In Box2D we operate on a
        // meter scale, pixels won't do it. So we use
        // an orthographic camera with a viewport of
        // 48 meters in width and 32 meters in height.
        // We also position the camera so that it
        // looks at (0,16) (that's where the middle of the
        // screen will be located).
        camera = new OrthographicCamera(WIDTH_METERS, HEIGHT_METERS);
        camera.position.set(WIDTH_METERS / 2, HEIGHT_METERS / 2, 0);

        createWorld();
	}

    @Override
	public void render() {
        // update the world with a fixed time step
        world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
        //world.step(1/60f, 6, 2);

        // clear the screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera.update();

        // process input(?)
        processInput();
        racecar.update();

        // render
        if (Racetrack.debug) {
            batch.begin();
            font.draw(batch, "fps:" + Gdx.graphics.getFramesPerSecond()
                    + " Body angle " + Math.round(racecar.body.getAngle() * MathUtils.radiansToDegrees)
                    + " Mouse pos: " + mousePosition
                    + " Racecar pos: " + racecar.body.getPosition(), 2, 20);
            batch.end();
        }

        // render the world using the debug renderer
        debugRenderer.render(world, camera.combined);

	}

    @Override
	public void dispose() {
        Gdx.input.setInputProcessor(null);
        batch.dispose();
        font.dispose();

        world.dispose();
        world = null;

        debugRenderer.dispose();
        debugRenderer = null;

        mouseJoint = null;
        hitBody = null;

        racecar.dispose();
        racecar = null;
	}

	@Override
	public void resize(int width, int height) {
	    gameWidth = width;
	    gameHeight = height;
	}



	/**
	 * Racetrack methods
	 */

    public static void toggleDebug() {
        debug = !debug;
    }

    private void createWorld() {

        createEdge(WIDTH_METERS / 2, UNIT_DISTANCE, WIDTH_METERS / 2, UNIT_DISTANCE); // bottom
        createEdge(WIDTH_METERS / 2, UNIT_DISTANCE, WIDTH_METERS / 2, HEIGHT_METERS - UNIT_DISTANCE); // top
        createEdge(UNIT_DISTANCE, HEIGHT_METERS / 2 - (2 * UNIT_DISTANCE), UNIT_DISTANCE, HEIGHT_METERS / 2); // left
        createEdge(UNIT_DISTANCE, HEIGHT_METERS / 2 - (2 * UNIT_DISTANCE), WIDTH_METERS - UNIT_DISTANCE, HEIGHT_METERS / 2); // right

        racecar = new Racecar(world, new Vector2(Racetrack.WIDTH_METERS / 2, Racetrack.HEIGHT_METERS / 2));

    }

    private void createEdge(float halfWidth, float halfHeight, float x, float y) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.StaticBody;
        Body b = world.createBody(bd);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(halfWidth, halfHeight, new Vector2(x, y), 0);
        b.createFixture(shape, 0.0f);
        shape.dispose();

    }

    /** we instantiate this vector and the callback here so we don't irritate the GC **/
    Vector3 testPoint = new Vector3();
    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture (Fixture fixture) {
            // if the hit point is inside the fixture of the body
            // we report it
            if (fixture.testPoint(testPoint.x, testPoint.y)) {
                hitBody = fixture.getBody();
                return false;
            } else
                return true;
        }
    };


    /**
     * InputProcessor implementation
     */
    enum INPUT {
        FORWARD, BACKWARD, LEFT, RIGHT
    }

    public HashMap<INPUT, Boolean> inputs = new HashMap<INPUT, Boolean>();
    {
        for (INPUT i : INPUT.values()) {
            inputs.put(i, false);
        }
    }
    static boolean handleMouse = false;
    static Vector2 mousePosition;

    private void processInput() {
        float amt = 20;
        if (inputs.get(INPUT.FORWARD)) {
            racecar.force(amt);
        }
        if (inputs.get(INPUT.BACKWARD)) {
            racecar.force(-amt);
        }
        if (inputs.get(INPUT.LEFT)) {
            racecar.torque(amt);
        }
        if (inputs.get(INPUT.RIGHT)) {
            racecar.torque(-amt);
        }
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
        case Keys.W:
            inputs.put(INPUT.FORWARD, true);
            break;
        case Keys.S:
            inputs.put(INPUT.BACKWARD, true);
            break;
        case Keys.A:
            inputs.put(INPUT.LEFT, true);
            break;
        case Keys.D:
            inputs.put(INPUT.RIGHT, true);
            break;

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
        case Keys.W:
            inputs.put(INPUT.FORWARD, false);
            break;
        case Keys.S:
            inputs.put(INPUT.BACKWARD, false);
            break;
        case Keys.A:
            inputs.put(INPUT.LEFT, false);
            break;
        case Keys.D:
            inputs.put(INPUT.RIGHT, false);
            break;

        case Keys.X:
            toggleDebug();
            break;

        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mousePosition = new Vector2(screenX, screenY);
        handleMouse = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        handleMouse = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (handleMouse) {
            mousePosition = new Vector2(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }


}
