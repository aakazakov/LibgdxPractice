package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class Projectile {
  private Vector2 position;
  private Vector2 velocity;
  private TextureRegion texture;
  
  public Projectile(TextureAtlas atlas) {
    this.texture = atlas.findRegion("bullet");
    this.position = new Vector2(-20.0f, 0.0f);
    this.velocity = new Vector2();
  }
  
  public void setLaunchPosition(Vector2 origin, float angle) {
    if (position.x > 0) return;
    position.set(1.0f, 0.0f).rotate(angle).scl(30).add(origin);
    setVelocity(angle);
  }
  
  public void setVelocity(float angle) {
    velocity.set(100.0f * MathUtils.cosDeg(angle), 100.0f * MathUtils.sinDeg(angle));
  }
  
  public void update(float dt) {
    setWaitPosition();
    position.mulAdd(velocity, dt);
  }
  
  private void setWaitPosition() {
    if (position.x < 0.0f ||
        position.x > 1280.0f ||
        position.y < 0.0f ||
        position.y > 720.0f) {
      position.set(-20.0f, 0.0f);
    }
  }
  
  public void render(SpriteBatch batch) {
    batch.draw(texture, position.x, position.y);
  } 
}
