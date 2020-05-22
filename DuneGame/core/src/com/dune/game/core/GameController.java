package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameController {
  private BattleMap map;
  private Tank tank;

  public BattleMap getMap() { return map; }
  
  public Tank getTank() { return tank; }
  
  public GameController() {
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("game.pack"));
    this.map = new BattleMap(atlas);
    this.tank = new Tank(atlas, 200, 200);
  }
  
  public void update(float delta) {
    tank.update(delta);
  }  
}
