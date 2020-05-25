package com.dune.game.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
  private static final Assets instance = new Assets();
  
  private AssetManager assetManager;
  private TextureAtlas textureAtlas;
  
  private Assets() {
    assetManager = new AssetManager();
  }
  
  public void loadAssets() {
    assetManager.load("game.pack", TextureAtlas.class);
    assetManager.finishLoading();
    textureAtlas = assetManager.get("game.pack");
  }
  
  public void clear() {
    assetManager.clear();
  }
  
  public static Assets getInstance() {
    return instance;
  }
  
  public AssetManager getAssetManager() {
    return assetManager;
  }

  public TextureAtlas getAtlas() {
    return textureAtlas;
  }

}
