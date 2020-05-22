package com.dune.game.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class Tank {
  private Vector2 position;
  private TextureRegion[] textures;
  private float angle;
  private float speed;
  
  private float moveTimer;
  private float timePerFrame;
  
  public Tank(TextureAtlas atlas, float x, float y) {
    this.position = new Vector2(x, y);
    this.textures = new TextureRegion(atlas.findRegion("tankanim")).split(64, 64)[0];
    this.speed = 140.0f;
    this.timePerFrame = 0.08f;
  }
  
  private int getCurrentFrameIndex() {
    return (int) (moveTimer / timePerFrame) % textures.length;
  }

  public void update(float dt) {
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      angle += 180.0f * dt;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      angle -= 180.0f * dt;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {      
      position.add(speed * MathUtils.cosDeg(angle) * dt, speed * MathUtils.sinDeg(angle) * dt);
      moveTimer += dt;      
    } else {
      if (getCurrentFrameIndex() != 0) {
        moveTimer += dt;
      }
    }
    checkBounds();
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
}
