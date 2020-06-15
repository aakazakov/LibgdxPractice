package com.dune.game.core.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dune.game.core.GameController;
import com.dune.game.core.units.Harvester;
import com.dune.game.core.user_logic.BaseLogic;
import com.dune.game.core.utils.ObjectPool;

public class HarvestersController extends ObjectPool<Harvester> {
  private GameController gc;

  @Override
  protected Harvester newObject() {
    return new Harvester(gc);
  }

  public HarvestersController(GameController gc) {
    this.gc = gc;
  }

  public void setup(float x, float y, BaseLogic baseLogic) {
    Harvester t = activateObject();
    t.setup(baseLogic, x, y);
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
