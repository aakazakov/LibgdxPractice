package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class Projectile {
  private Vector2 position;
  private Vector2 velocity;
  private TextureRegion texture;
  
  public Projectile(TextureAtlas atlas) {
    this.texture = atlas.findRegion("bullet");
  }
  
  public void setVelocity(float angle) {
    velocity.set(100.0f * MathUtils.cosDeg(angle), 0.0f);
  }
  
  public void update(float dt) {
    position.mulAdd(velocity, dt);
  }
  
  public void render(SpriteBatch batch) {
    batch.draw(texture, 500.0f, 500.0f);
  }
}
