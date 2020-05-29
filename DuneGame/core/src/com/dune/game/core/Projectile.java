package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class Projectile extends GameObject implements Poolable {
  private TextureRegion texture;
  private float angle;
  private Vector2 velocity;
  private float speed;
  private boolean active;
  
  
  public Projectile(GameController gc) {
    super(gc);
    this.velocity = new Vector2();
    this.speed = 320.0f;
  }
  
  public void setup(Vector2 startPosition, float angle, TextureRegion texture) {
    this.texture = texture;
    this.angle = angle;
    this.velocity.set(speed * MathUtils.cosDeg(angle), speed * MathUtils.sinDeg(angle));
    this.position.set(startPosition);
    this.active = true;
  }

  public void update(float dt) {
    position.mulAdd(velocity, dt);
    if (position.x < 0 || position.x > 1280 || position.y < 0  || position.y > 720) {
      deactivate();
    }
  }
  
  public void deactivate() {
    active = false;
  }
  
  public void render(SpriteBatch batch) {
    batch.draw(texture, position.x - 8, position.y - 8);
  }
  
  @Override
  public boolean isActive() {
    return active;
  }
}
