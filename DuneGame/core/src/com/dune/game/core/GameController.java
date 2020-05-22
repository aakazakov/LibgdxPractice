package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameController {
  private BattleMap map;
  private Tank tank;
  private Projectile bullet;

  public BattleMap getMap() { return map; }
  
  public Tank getTank() { return tank; }
  
  public Projectile getBullet() { return bullet; }
  
  public GameController() {
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("game.pack"));
    this.map = new BattleMap(atlas);
    this.tank = new Tank(atlas, 200, 200);
    this.bullet = new Projectile(atlas);
  }
  
  public void update(float delta) {
    tank.update(delta);
  }  
}
