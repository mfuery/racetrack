package com.mjf.racetrack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class Racecar implements Disposable {
//    float MAX_VELOCITY = 1f;
//    float facing;
    Fixture fixture;
    Body body;
    Vector2 initialPosition;

    public Racecar(World world, Vector2 initialPosition) {
        this.initialPosition = initialPosition;
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DynamicBody;
        body = world.createBody(bd);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Racetrack.UNIT_DISTANCE * 2, Racetrack.UNIT_DISTANCE * 3, initialPosition, 0);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 0.5f;
        fd.friction = 0.4f;
        fd.restitution = 0.6f;
        fixture = body.createFixture(fd);
        shape.dispose();


    }

    public void update() {
        if (Racetrack.handleMouse) {
            float ang = body.getAngle();
            Vector2 pos = initialPosition;
            Gdx.app.log("", "body position: " + pos + ", angle: " + ang);
            Vector2 toTarget = new Vector2(Racetrack.mousePosition.x - pos.x, Racetrack.mousePosition.y - pos.y);
            float desiredAngle = (float) Math.atan2(toTarget.y, -toTarget.x);
            body.setTransform(pos, desiredAngle);
        }
    }

    @Override
    public void dispose() {
        //body.destroyFixture(fixture); // causes runtime error
        fixture = null;
        body = null;
    }

    public void force(float amt) {
        //body.applyLinearImpulse(new Vector2(0, amt), body.getWorldCenter(), true);
        body.applyForce(new Vector2(0, amt), body.getWorldCenter(), true);

    }

    public void torque(float amt) {
        body.applyTorque(amt, true);
    }

}
