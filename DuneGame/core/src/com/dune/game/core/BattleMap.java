package com.dune.game.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class BattleMap {
  private TextureRegion grassTexture;
  
  private Texture resourceTexture;
  private boolean[][] resource;
  
  public BattleMap() {
    this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
    
    this.resourceTexture = new Texture("smile.png");
    this.resource = setResource();
  }
  
  public void render(SpriteBatch batch) {
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        batch.draw(grassTexture, i * 80, j * 80);
        if (resource[i][j]) {
          batch.draw(resourceTexture, i * 80, j * 80);
        }
      }
    }
  }
  
  private boolean[][] setResource() {
    boolean[][] result = new boolean[16][9];
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        if (getRandomNumber() == 10) result[i][j] = true;
      }
    }
    return result;
  }
  
  private int getRandomNumber() {
    return MathUtils.random(10);
  }
  
  public void deleteResourceIfItFound(Vector2 gameObjPosition) {
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        if (resource[i][j] && gameObjPosition.dst(i * 80.0f + 40.0f, j * 80.0f + 40.0f) < 80.0f) {
          resource[i][j] = false;
        } 
      }
    }
  }
  
}
