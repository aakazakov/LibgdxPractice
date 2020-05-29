package com.dune.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dune.game.screens.GameScreen;

public class DuneGame extends Game {
  private SpriteBatch batch;
  private GameScreen gameScreen;

  @Override
  public void create() {
    batch = new SpriteBatch();
    this.gameScreen = new GameScreen(batch);
    this.setScreen(gameScreen);
  }

  @Override
  public void render() {
    gameScreen.render(Gdx.graphics.getDeltaTime());
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}
