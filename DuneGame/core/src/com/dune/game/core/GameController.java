package com.dune.game.core;

import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.*;
import com.dune.game.core.Tank.Owner;

public class GameController {
  private BattleMap map;
  private ProjectilesController projectilesController;
  private TanksController tanksController;
  private Set<Tank> selectedUnits;
  private Vector2 tmp;
  private Vector2 selectionStart;

  public GameController() {
    Assets.getInstance().loadAssets();
    this.map = new BattleMap();
    this.projectilesController = new ProjectilesController(this);
    this.tanksController = new TanksController(this);
    this.selectedUnits = new HashSet<>();
    this.tmp = new Vector2();
    this.selectionStart = new Vector2();
    for (int i = 0; i < 5; i++) {
      tanksController.setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Owner.PLAYER);
    }
    prepareInput();
  }
  
  private void prepareInput() {
    InputProcessor ip = new InputAdapter() {
      @Override
      public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
          selectionStart.set(screenX, 720 - screenY);
        }
        return true;
      }
      
      @Override
      public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
          tmp.set(Gdx.input.getX(), 720 - Gdx.input.getY());
          if (tmp.x < selectionStart.x) {
            float t = tmp.x;
            tmp.x = selectionStart.x;
            selectionStart.x = t;
          }
          if (tmp.y > selectionStart.y) {
            float t = tmp.y;
            tmp.y = selectionStart.y;
            selectionStart.y = t;
          }
          selectedUnits.clear();
          if (Math.abs(tmp.x - selectionStart.x) > 20 && Math.abs(tmp.y - selectionStart.y) > 20) {
            for (int i = 0; i < tanksController.activeSize(); i++) {
              Tank t = tanksController.getActiveList().get(i);
              if (t.getPosition().x > selectionStart.x
                  && t.getPosition().x < tmp.x
                  && t.getPosition().y > tmp.y
                  && t.getPosition().y < selectionStart.y) {
                selectedUnits.add(t);
              }
            }
          } else {
            for (int i = 0; i < tanksController.activeSize(); i++) {
              Tank t = tanksController.getActiveList().get(i);
              if (t.getPosition().dst(tmp) < 30.0f) {
                selectedUnits.add(t);
              }
            }
          }
        }
        return true;
      }
    };
    Gdx.input.setInputProcessor(ip);
  }
  
  public void update(float dt) {
    projectilesController.update(dt);
    map.update(dt);
    tanksController.update(dt);
//    checkSelection();
    checkCollisions();
  }
  
//  private void checkSelection() {
//    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//      selectedUnits.clear();
//      tmp.set(Gdx.input.getX(), 720 - Gdx.input.getY());
//      Tank t;
//      for (int i = 0; i < tanksController.activeSize(); i++) {
//        t = tanksController.getActiveList().get(i);
//        if (t.getPosition().dst(tmp) < 30.0f) {
//          selectedUnits.add(t);
//        }
//      }
//    }
//  }
  
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
