package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;

public class WorldRenderer {
  private SpriteBatch batch;
  private GameController gc;
  private BitmapFont font32;
  
  public WorldRenderer(SpriteBatch batch, GameController gc) {
    this.batch = batch;
    this.gc = gc;
    this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");
  }
  
  public void render() {
    Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    batch.begin();
    gc.getMap().render(batch);
    gc.getTanksController().render(batch);
    gc.getProjectilesController().render(batch);
    font32.draw(batch, "Dune Game 2020", 0, 680, 1280, 1, false);
    batch.end();
  }
}
