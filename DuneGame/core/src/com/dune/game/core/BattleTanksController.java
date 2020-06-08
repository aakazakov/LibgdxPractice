package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dune.game.core.units.BattleTank;
import com.dune.game.core.units.Owner;

public class BattleTanksController extends ObjectPool<BattleTank> {
  private GameController gc;


  public BattleTanksController(GameController gc) {
    this.gc = gc;
  }

  public void setup(float x, float y, Owner ownerType) {
    BattleTank t = activateObject();
    t.setup(ownerType, x, y);
  }

  @Override
  protected BattleTank newObject() {
    return new BattleTank(gc);
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