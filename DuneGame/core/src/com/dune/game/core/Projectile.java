package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class Projectile extends GameObject implements Poolable {
  private TextureRegion texture;
  private Vector2 velocity;
  private float speed;
  private float angle;
  private boolean active;
  
  
  public Projectile(GameController gc) {
    super(gc);
    this.position = new Vector2();
    this.speed = 320.0f;
  }
  
  public void setup(Vector2 startPosition, float angle, TextureRegion texture) {
    this.texture = texture;
    this.position.set(startPosition);
    this.angle = angle;
    this.velocity.set(speed * MathUtils.cosDeg(angle), speed * MathUtils.sinDeg(angle));
    this.active = true;
  }

  @Override
  public boolean isActive() {
    return active;
  }
  
  public void update(float dt) {
    position.mulAdd(velocity, dt);
    if (position.x < 0 || position.x > 720 || position.y < 0  || position.y > 1280) {
      deactivate();
    }
  }
  
  public void deactivate() {
    active = false;
  }
  
  public void render(SpriteBatch batch) {
    batch.draw(texture, position.x - 8, position.y - 8);
  }
  
}
