package com.dune.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dune.game.screens.GameScreen;

public class DuneGame extends Game {

//  private static class Circle {
//    private Vector2 position = new Vector2();
//    private Texture texture;
//
//    public Circle() {
//      setRandomPosition();
//      this.texture = new Texture("smile.png");
//    }
//
//    public void update(Tank tank) {
//      if (isTouched(tank)) {
//        setRandomPosition();
//      }
//    }
//    
//    private boolean isTouched(Tank tank) {
//      return this.position.dst(tank.position) < 65.0f;
//    }
//    
//    private void setRandomPosition() {
//      position.x = MathUtils.random(40.0f, 1241.0f);
//      position.y = MathUtils.random(40.0f, 681.0f);
//    }
//
//    public void render(SpriteBatch batch) {
//      batch.draw(texture, position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, 0, 0, 0, 80, 80, false, false);
//    }
//    
//    public void dispose() {
//      texture.dispose();
//    }
//  }

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