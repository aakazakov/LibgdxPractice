package com.dune.game.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.dune.game.core.Weapon.Type;

public class Tank extends GameObject {
  private TextureRegion[] textures;
  private Vector2 destination;
  private float angle;
  private float speed;
  private float rotationSpeed;
  private float moveTimer;
  private float timePerFrame;
  private Weapon weapon;
  private int container;
  
  public Tank(GameController gc, float x, float y) {
    super(gc);
    this.position.set(x, y);
    this.destination = new Vector2(position);
    this.textures = Assets.getInstance().getAtlas().findRegion("tankanim").split(64, 64)[0];
    this.speed = 120.0f;
    this.timePerFrame = 0.08f;
    this.rotationSpeed = 90.0f;
    this.weapon = new Weapon(Type.HARVEST, 3.0f, 1);
  }
  
  public void update(float dt) {
    if (Gdx.input.justTouched()) {
      destination.set(Gdx.input.getX(), 720 - Gdx.input.getY());
    }
    if (position.dst(destination) > 3.0f) {
      float angleTo = tmp.set(destination).sub(position).angle();
      if (angle > angleTo) {
        if (Math.abs(angle - angleTo) <= 180.0f) {
          angle -= rotationSpeed * dt;
        } else {
          angle += rotationSpeed * dt;
        }
      } else {
        if (Math.abs(angle - angleTo) <= 180.0f) {
          angle += rotationSpeed * dt;
        } else {
          angle -= rotationSpeed * dt;
        }
      }
      if (angle < 0.0f) {
        angle += 360.0f;
      }
      if (angle > 360.0f) {
        angle -= 360.0f;
      }
      moveTimer += dt;
      tmp.set(speed, 0).rotate(angle);
      position.mulAdd(tmp, dt);
      if (position.dst(destination) < 120.0f && Math.abs(angleTo - angle) > 10) {
        position.mulAdd(tmp, -dt);
      }
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
      fire();
    }
    
    checkBounds();
  }
  
  public void updateWeapon(float dt) {
    if (weapon.getType() == Type.HARVEST) {
      if (gc.getMap().getResourceCount(this) > 0) {
        int result = weapon.use(dt);
        if (result > -1) {
          container += gc.getMap().harvestResource(this, result);
        }
      } else {
        weapon.reset();
      }
    }
  }
  
  public void fire() {
    tmp.set(position).add(32 * MathUtils.cosDeg(angle), 32 * MathUtils.sinDeg(angle));
    gc.getProjectilesController().setup(tmp, angle);
  }
  
  private void checkBounds() {
    if (position.x < 40.0f) position.x = 40.0f;
    if (position.x > 1240.0f) position.x = 1240.0f;
    if (position.y < 40.0f) position.y = 40.0f;
    if (position.y > 680.0f) position.y = 680.0f;
  }

  public void render(SpriteBatch batch) {
    batch.draw(textures[getCurrentFrameIndex()], position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle);
  }
  
  private int getCurrentFrameIndex() {
    return (int) (moveTimer / timePerFrame) % textures.length;
  }
  
}
