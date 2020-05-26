package com.dune.game.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class BattleMap {
  private TextureRegion grassTexture;
  
  private Texture resource;
  private boolean[][] resourceMap;
  
  public BattleMap() {
    this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
    
    this.resource = new Texture("smile.png");
    this.resourceMap = getResourceMap();
  }
  
  public void render(SpriteBatch batch) {
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        batch.draw(grassTexture, i * 80, j * 80);
      }
    }
    
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        if (resourceMap[i][j]) {
          batch.draw(resource, i * 80, j * 80);
        }
      }
    }
  }
  
  private boolean[][] getResourceMap() {
    boolean[][] result = new boolean[16][9];
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        if (getRandom() == 10) result[i][j] = true;
      }
    }
    return result;
  }
  
  private int getRandom() {
    return MathUtils.random(10);
  }
  
  public void resourceFound(Vector2 gameObj) {
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        if (resourceMap[i][j] && gameObj.dst(40 + (i * 80.0f - 40.0f), 40 + (j * 80.0f - 40.0f)) < 90.0f) {
          resourceMap[i][j] = false;
        } 
      }
    }
  }
  
}
