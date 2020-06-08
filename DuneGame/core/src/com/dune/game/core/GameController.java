package com.dune.game.core;

import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dune.game.core.gui.GuiPlayerInfo;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.screens.ScreenManager;

public class GameController {
  private BattleMap map;
  private PlayerLogic playerLogic;
  private ProjectilesController projectilesController;
  private UnitsController unitsController;
  private ParticleController particleController;
  private Vector2 tmp;
  private Vector2 selectionStart;
  private Vector2 mouse;
  private GuiPlayerInfo guiPlayerInfo;
  private Stage stage;
  private Collider collider;
  private List<AbstractUnit> selectedUnits;

  public GameController() {
    this.mouse = new Vector2();
    this.tmp = new Vector2();
    this.playerLogic = new PlayerLogic(this);
    this.collider = new Collider(this);
    this.selectionStart = new Vector2();
    this.selectedUnits = new ArrayList<>();
    this.map = new BattleMap();
    this.projectilesController = new ProjectilesController(this);
    this.unitsController = new UnitsController(this);
    this.particleController = new ParticleController();
    createGuiAndPrepareGameInput();
  }

  public void update(float dt) {
    mouse.set(Gdx.input.getX(), Gdx.input.getY());
    ScreenManager.getInstance().getViewport().unproject(mouse);
    unitsController.update(dt);
    playerLogic.update(dt);
    projectilesController.update(dt);
    particleController.update(dt);
    map.update(dt);
    collider.checkCollisions();
    guiPlayerInfo.update(dt);
    stage.act(dt);
  }

  public boolean isUnitSelected(AbstractUnit abstractUnit) {
    return selectedUnits.contains(abstractUnit);
  }

  public InputProcessor prepareInput() {
    return new InputAdapter() {
      @Override
      public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
          selectionStart.set(mouse);
        }
        return true;
      }

      @Override
      public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
          tmp.set(mouse);

          if (tmp.x < selectionStart.x) {
            float t = tmp.x;
            tmp.x = selectionStart.x;
            selectionStart.x = t;
          }
          if (tmp.y > selectionStart.y) {
            float t = tmp.y;
            tmp.y = selectionStart.y;
            selectionStart.y = t;
          }

          selectedUnits.clear();
          if (Math.abs(tmp.x - selectionStart.x) > 20 && Math.abs(tmp.y - selectionStart.y) > 20) {
            for (int i = 0; i < unitsController.getPlayerUnits().size(); i++) {
              AbstractUnit t = unitsController.getPlayerUnits().get(i);
              if (t.getPosition().x > selectionStart.x && t.getPosition().x < tmp.x && t.getPosition().y > tmp.y
                  && t.getPosition().y < selectionStart.y) {
                selectedUnits.add(t);
              }
            }
          } else {
            for (int i = 0; i < unitsController.getUnits().size(); i++) {
              AbstractUnit t = unitsController.getUnits().get(i);
              if (t.getPosition().dst(tmp) < 30.0f) {
                selectedUnits.add(t);
              }
            }
          }
        }
        return true;
      }
    };
  }

  public void createGuiAndPrepareGameInput() {
    stage = new Stage(ScreenManager.getInstance().getViewport(), ScreenManager.getInstance().getBatch());
    Gdx.input.setInputProcessor(new InputMultiplexer(stage, prepareInput()));
    Skin skin = new Skin();
    skin.addRegions(Assets.getInstance().getAtlas());
    BitmapFont font14 = Assets.getInstance().getAssetManager().get("fonts/font14.ttf");
    TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(skin.getDrawable("smButton"), null,
        null, font14);
    final TextButton menuBtn = new TextButton("Menu", textButtonStyle);
    menuBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
      }
    });

    final TextButton testBtn = new TextButton("Test", textButtonStyle);
    testBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        System.out.println("Test");
        ;
      }
    });
    Group menuGroup = new Group();
    menuBtn.setPosition(0, 0);
    testBtn.setPosition(130, 0);
    menuGroup.addActor(menuBtn);
    menuGroup.addActor(testBtn);
    menuGroup.setPosition(900, 680);

    Label.LabelStyle labelStyle = new Label.LabelStyle(font14, Color.WHITE);
    skin.add("simpleLabel", labelStyle);

    guiPlayerInfo = new GuiPlayerInfo(playerLogic, skin);
    guiPlayerInfo.setPosition(0, 700);
    stage.addActor(guiPlayerInfo);
    stage.addActor(menuGroup);
    skin.dispose();
  }

  public UnitsController getUnitsController() {
    return unitsController;
  }

  public List<AbstractUnit> getSelectedUnits() {
    return selectedUnits;
  }

  public Vector2 getMouse() {
    return mouse;
  }

  public ProjectilesController getProjectilesController() {
    return projectilesController;
  }

  public BattleMap getMap() {
    return map;
  }

  public ParticleController getParticleController() {
    return particleController;
  }

  public Stage getStage() {
    return stage;
  }
}
