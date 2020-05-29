package com.dune.game.core;

import com.dune.game.core.Tank.Owner;

public class GameController {
  private BattleMap map;
  private ProjectilesController projectilesController;
  private TanksController tanksController;

  public GameController() {
    Assets.getInstance().loadAssets();
    this.map = new BattleMap();
    this.projectilesController = new ProjectilesController(this);
    this.tanksController = new TanksController(this);
    tanksController.setup(200, 200, Owner.PLAYER);
    tanksController.setup(200, 400, Owner.PLAYER);
  }
  
  public void update(float dt) {
    projectilesController.update(dt);
    map.update(dt);
    tanksController.update(dt);
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
