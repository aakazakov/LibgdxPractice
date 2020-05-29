package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TanksController extends ObjectPool<Tank> {
  private GameController gc;
  
  public TanksController(GameController gc) {
    this.gc = gc;
  }

  @Override
  public Tank newObject() {
    return new Tank(gc);
  }
  
  public void setup(float x, float y, Tank.Owner ownerType) {
    Tank t = getActiveElement();
    t.setup(x, y, ownerType);
  }
  
  public void update(float dt) {
    for (int i = 0; i < activeList.size(); i++) {
      activeList.get(i).update(dt);
    }
    checkPool();
  }
  
  public void render(SpriteBatch batch) {
    for (int i = 0; i < activeList.size(); i++) {
      activeList.get(i).render(batch);
    }
  }
  
}
