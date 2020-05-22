package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.*;

public class BattleMap {
  TextureRegion grassTexture;
  
  public BattleMap(TextureAtlas atlas) {
    this.grassTexture = atlas.findRegion("grass");
  }
  
  public void render(SpriteBatch batch) {
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        batch.draw(grassTexture, i * 80, j * 80);
      }
    }
  }
  
}
