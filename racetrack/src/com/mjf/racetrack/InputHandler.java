package com.mjf.racetrack;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {
    enum Actions {
        LEFT, RIGHT, ACCEL, BRAKE, PAUSE,
    }

    static Map<Actions, Boolean> inputs = new HashMap<InputHandler.Actions, Boolean>();
    static {
        inputs.put(Actions.LEFT, false);
        inputs.put(Actions.RIGHT, false);
        inputs.put(Actions.ACCEL, false);
        inputs.put(Actions.BRAKE, false);
        inputs.put(Actions.PAUSE, false);
    }

    private void set(int keycode, boolean isPressed) {
        switch (keycode) {
        case Keys.DPAD_LEFT:
            inputs.put(Actions.LEFT, isPressed);
            break;
        case Keys.DPAD_RIGHT:
            inputs.put(Actions.RIGHT, isPressed);
            break;
        case Keys.Z:
            inputs.put(Actions.ACCEL, isPressed);
            break;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        set(keycode, true);
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        // Special cases
        if (keycode == Keys.D) {
            Racetrack.toggleDebug();
        }

        // Default behavior
        set(keycode, false);
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    };

}
