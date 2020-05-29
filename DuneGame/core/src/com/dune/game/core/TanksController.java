package com.dune.game.core;

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
}
