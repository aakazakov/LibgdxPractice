package com.dune.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dune.game.core.*;

public class GameScreen extends AbstractScreen {
  private GameController gameController;
  private WorldRenderer worldRenderer;
  
  public GameScreen(SpriteBatch batch) {
    super(batch);
    this.batch = batch;
  }

  @Override
  public void show() {
    this.gameController = new GameController();
    this.worldRenderer = new WorldRenderer(batch, gameController);
  }

  @Override
  public void render(float deltaTime) {
    gameController.update(deltaTime);
    worldRenderer.render();
  }

  @Override
  public void dispose() { }
}
