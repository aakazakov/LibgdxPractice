package com.dune.game.core.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dune.game.core.Building;
import com.dune.game.core.GameController;
import com.dune.game.core.user_logic.BaseLogic;
import com.dune.game.core.utils.ObjectPool;

public class BuildingsController extends ObjectPool<Building> {
  private GameController gc;

  public BuildingsController(GameController gc) {
      this.gc = gc;
  }
  
  @Override
  protected Building newObject() {
    return new Building(gc);
  }

  public void setup(int cellX, int cellY, BaseLogic ownerLogic) {
    Building b = activateObject();
    b.setup(ownerLogic, cellX, cellY);
  }
  
  public void render(SpriteBatch batch) {
      for (int i = 0; i < activeList.size(); i++) {
          activeList.get(i).render(batch);
      }
  }

  public void update(float dt) {
      for (int i = 0; i < activeList.size(); i++) {
          activeList.get(i).update(dt);
      }
      checkPool();
  }
}
