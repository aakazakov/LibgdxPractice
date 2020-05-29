package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

public class ProjectilesController extends ObjectPool<Projectile> {
  
  private GameController gc;
  private TextureRegion projectileTexture;
  
  public ProjectilesController(GameController gc) {
    this.gc = gc;
    this.projectileTexture = Assets.getInstance().getAtlas().findRegion("bullet");
  }

  @Override
  public Projectile newObject() {
    return new Projectile(gc);
  }
  
  public void setup(Vector2 startPosition, float angle) {
    Projectile p = getActiveElement();
    p.setup(startPosition, angle, projectileTexture);
  }
}
