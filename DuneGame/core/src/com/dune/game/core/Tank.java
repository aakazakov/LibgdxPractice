package com.dune.game.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.dune.game.core.Weapon.Type;

public class Tank extends GameObject implements Poolable, Updatable {
  public enum Owner { PLAYER, AI }
  
  private Owner ownerType;
  private Weapon weapon;
  private TextureRegion[] textures;
  private TextureRegion progressbarTexture;
  private Vector2 destination;
  private float angle;
  private float speed;
  private float timePerFrame;
  private float rotationSpeed;
  private float moveTimer;
  private int container;
  private int hp;
  private boolean controlled;
  private BitmapFont font16;
  
  public Tank(GameController gc) {
    super(gc);
    this.progressbarTexture = Assets.getInstance().getAtlas().findRegion("progressbar");
    this.font16 = Assets.getInstance().getAssetManager().get("fonts/font16.ttf");
    this.timePerFrame = 0.08f;
    this.rotationSpeed = 90.0f;
  }
  
  public void setup(float x, float y, Owner ownerType) {
    this.textures = Assets.getInstance().getAtlas().findRegion("tankanim").split(64, 64)[0];
    this.position.set(x, y);
    this.speed = 120.0f;
    this.ownerType = ownerType;
    this.hp = 100;
    this.weapon = new Weapon(Type.HARVEST, 3.0f, 1);
    this.destination = new Vector2(position);
  }
  
  @Override
  public void update(float dt) {
    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
        && position.dst(Gdx.input.getX(), 720 - Gdx.input.getY()) < 40) {
        controlled = !controlled;
    }
    if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
        && controlled) {
        destination.set(Gdx.input.getX(), 720 - Gdx.input.getY());
        controlled = false;
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
    updateWeapon(dt);
    checkBounds();
  }
  
  public void updateWeapon(float dt) {
    if (weapon.getType() == Type.HARVEST) {
      if (gc.getMap().getResourceCount(this) > 0 && container < 50) {
        int result = weapon.use(dt);
        if (result > -1) {
          container += gc.getMap().harvestResource(this, result);
        }
      } else {
        weapon.reset();
      }
    }
  }
  
  private void checkBounds() {
    if (position.x < 40.0f) position.x = 40.0f;
    if (position.x > 1240.0f) position.x = 1240.0f;
    if (position.y < 40.0f) position.y = 40.0f;
    if (position.y > 680.0f) position.y = 680.0f;
  }
  
  @Override
  public void render(SpriteBatch batch) {
    batch.draw(textures[getCurrentFrameIndex()], position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle);
    if (weapon.getType() == Weapon.Type.HARVEST && weapon.getUsageTimePercentage() > 0.0f) {
      batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
      batch.draw(progressbarTexture, position.x - 32, position.y + 30, 64, 12);
      batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
      batch.draw(progressbarTexture, position.x - 30, position.y + 32, 60 * weapon.getUsageTimePercentage(), 8);
      batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    font16.draw(batch, Integer.toString(container), position.x - 32, position.y + 30);
  }
  
  private int getCurrentFrameIndex() {
    return (int) (moveTimer / timePerFrame) % textures.length;
  }

  @Override
  public boolean isActive() {
    return hp > 0;
  }
}
