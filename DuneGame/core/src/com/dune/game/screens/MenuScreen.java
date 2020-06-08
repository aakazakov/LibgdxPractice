package com.dune.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dune.game.core.Assets;

public class MenuScreen extends AbstractScreen {
  
  Stage stage;
  
  public MenuScreen(SpriteBatch batch) {
    super(batch);
  }

  @Override
  public void show() { 
    stage = new Stage(ScreenManager.getInstance().getViewport(), ScreenManager.getInstance().getBatch());
    Gdx.input.setInputProcessor(stage);
    Skin skin = new Skin();
    skin.addRegions(Assets.getInstance().getAtlas());
    BitmapFont font14 = Assets.getInstance().getAssetManager().get("fonts/font14.ttf");
    TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(skin.getDrawable("simpleButton"), null,
        null, font14);

    final TextButton playBtn = new TextButton("PLAY", textButtonStyle);
    playBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
      }
    });
    
    final TextButton exitBtn= new TextButton("EXIT", textButtonStyle);
    exitBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.app.exit();
      }
    });
    Group menuGroup = new Group();
    
    playBtn.setPosition(0.0f, 100.0f);
    exitBtn.setPosition(0.0f, 0.0f);
    menuGroup.addActor(playBtn);
    menuGroup.addActor(exitBtn);
    menuGroup.setPosition(ScreenManager.WORLD_WIDTH / 2 - 160,
        ScreenManager.WORLD_HEIGHT / 2);

    Label.LabelStyle labelStyle = new Label.LabelStyle(font14, Color.WHITE);
    skin.add("simpleLabel", labelStyle);
    stage.addActor(menuGroup);
    skin.dispose();
  }

  @Override
  public void render(float delta) {
    update(delta);
    Gdx.gl.glClearColor(0, 0, 0.4f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.draw();
  }

  public void update(float dt) {
    stage.act(dt);
  }

  @Override
  public void dispose() {
  }
}
