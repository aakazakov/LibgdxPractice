package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldRenderer {
  private SpriteBatch batch;
  private GameController gameController;
  
  public WorldRenderer(SpriteBatch batch, GameController gameController) {
    this.batch = batch;
    this.gameController = gameController;
  }
  
  public void render() {
    Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    batch.begin();
    gameController.getMap().render(batch);
    gameController.getTank().render(batch);
    gameController.getBullet().render(batch);
    batch.end();
  }
}
