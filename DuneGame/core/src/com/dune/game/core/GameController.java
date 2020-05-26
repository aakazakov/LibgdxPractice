package com.dune.game.core;

public class GameController {
  private BattleMap map;
  private ProjectilesController projectilesController;
  private Tank tank;

  public GameController() {
    Assets.getInstance().loadAssets();
    this.map = new BattleMap();
    this.projectilesController = new ProjectilesController(this);
    this.tank = new Tank(this, 200, 200);
  }
  
  public void update(float dt) {
    projectilesController.update(dt);
    tank.update(dt);
    
    map.deleteResourceIfItFound(tank.getPosition());
  }
  
  public BattleMap getMap() { return map; }
  
  public Tank getTank() { return tank; }
  
  public ProjectilesController getProjectilesController() {
    return projectilesController;
  }
}
