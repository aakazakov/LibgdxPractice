package com.dune.game.core;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.Tank.Owner;

public class GameController {
  private BattleMap map;
  private ProjectilesController projectilesController;
  private TanksController tanksController;
  private Set<Tank> selectedUnits;
  private Vector2 tmp;

  public GameController() {
    Assets.getInstance().loadAssets();
    this.map = new BattleMap();
    this.projectilesController = new ProjectilesController(this);
    this.tanksController = new TanksController(this);
    this.selectedUnits = new HashSet<>();
    this.tmp = new Vector2();
    tanksController.setup(200, 200, Owner.PLAYER);
    tanksController.setup(200, 400, Owner.PLAYER);
  }
  
  public void update(float dt) {
    projectilesController.update(dt);
    map.update(dt);
    tanksController.update(dt);
    checkSelection();
    checkCollisions();
  }
  
  private void checkSelection() {
    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
      selectedUnits.clear();
      tmp.set(Gdx.input.getX(), 720 - Gdx.input.getY());
      Tank t;
      for (int i = 0; i < tanksController.activeSize(); i++) {
        t = tanksController.getActiveList().get(i);
        if (t.getPosition().dst(tmp) < 30.0f) {
          selectedUnits.add(t);
        }
      }
    }
  }
  
  public boolean isTankSelected(Tank t) {
    return selectedUnits.contains(t);
  }
  
  private void checkCollisions() {
    for (int i = 0; i < tanksController.activeSize() - 1; i++) {
      Tank t1 = tanksController.getActiveList().get(i);
      for (int j = i + 1; j < tanksController.activeSize(); j++) {
        Tank t2 = tanksController.getActiveList().get(j);
        float dst = t1.getPosition().dst(t2.getPosition());
        if (dst >= 30 + 30) continue;
        tmp.set(t2.getPosition()).sub(t1.getPosition()).nor().scl((60 - dst) / 2);
        t2.moveBy(tmp);
        tmp.scl(-1);
        t1.moveBy(tmp);
      }
    }
  }
  
  public BattleMap getMap() {
    return map;
  }
  
  public TanksController getTanksController() {
    return tanksController;
  }

  public ProjectilesController getProjectilesController() {
    return projectilesController;
  }
}
