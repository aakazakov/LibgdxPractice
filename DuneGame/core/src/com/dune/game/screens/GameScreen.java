package com.dune.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends AbstractScreen {
  private SpriteBatch batch;
  private GameController gameController;
  private WorldRenderer worldRendere;
  
  public GameScreen(SpriteBatch batch) {
    this.batch = batch;
  }

  @Override
  public void show() {
    this.gameController = new GameController();
    this.worldRendere = new WorldRendere(batch, gameController);
  }

  @Override
  public void render(float delta) {
    gameController.update(delta);
    worldRendere.render();
  }

  @Override
  public void dispose() { }

}
