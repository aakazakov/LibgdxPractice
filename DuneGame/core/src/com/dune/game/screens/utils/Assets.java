package com.dune.game.screens.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.dune.game.screens.ScreenManager;

public class Assets {
  private static final Assets instance = new Assets();

  private AssetManager assetManager;
  private TextureAtlas textureAtlas;

  private Assets() {
    assetManager = new AssetManager();
  }

  public static Assets getInstance() {
    return instance;
  }

  public void loadAssets(ScreenManager.ScreenType type) {
    switch (type) {
    case MENU:
      assetManager.load("images/game.pack", TextureAtlas.class);
      createStandardFont(14);
      createStandardFont(24);
      createStandardFont(72);
      break;
    case GAME:
      assetManager.load("images/game.pack", TextureAtlas.class);
      createStandardFont(14);
      createStandardFont(32);
      break;
    }
  }

  public void createStandardFont(int size) {
    FileHandleResolver resolver = new InternalFileHandleResolver();
    assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
    assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
    fontParameter.fontFileName = "fonts/Roboto-Medium.ttf";
    fontParameter.fontParameters.size = size;
    fontParameter.fontParameters.color = Color.WHITE;
    fontParameter.fontParameters.borderWidth = 1;
    fontParameter.fontParameters.borderColor = Color.BLACK;
    fontParameter.fontParameters.shadowOffsetX = 1;
    fontParameter.fontParameters.shadowOffsetY = 1;
    fontParameter.fontParameters.shadowColor = Color.BLACK;
    assetManager.load("fonts/font" + size + ".ttf", BitmapFont.class, fontParameter);
  }

  public void makeLinks() {
    textureAtlas = assetManager.get("images/game.pack", TextureAtlas.class);
  }

  public void clear() {
    assetManager.clear();
  }

  public AssetManager getAssetManager() {
    return assetManager;
  }

  public TextureAtlas getAtlas() {
    return textureAtlas;
  }
}
