package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.dune.game.core.units.Owner;
import com.dune.game.screens.utils.Assets;

public class BattleMap {
  private class Cell {
    private int cellX;
    private int cellY;
    private int resource;
    private float resourceRegenerationRate;
    private float resourceRegenerationTime;

    public Cell(int cellX, int cellY) {
      this.cellX = cellX;
      this.cellY = cellY;
      if (MathUtils.random() < 0.1f) {
        resource = MathUtils.random(1, 3);
      }
      resourceRegenerationRate = MathUtils.random(5.0f) - 4.5f;
      if (resourceRegenerationRate < 0.0f) {
        resourceRegenerationRate = 0.0f;
      } else {
        resourceRegenerationRate *= 20.0f;
        resourceRegenerationRate += 10.0f;
      }
    }

    private void update(float dt) {
      if (resourceRegenerationRate > 0.01f) {
        resourceRegenerationTime += dt;
        if (resourceRegenerationTime > resourceRegenerationRate) {
          resourceRegenerationTime = 0.0f;
          resource++;
          if (resource > 5) {
            resource = 5;
          }
        }
      }
    }

    private void render(SpriteBatch batch) {
      if (resource > 0) {
        float scale = 0.5f + resource * 0.2f;
        batch.draw(resourceTexture, cellX * CELL_SIZE, cellY * CELL_SIZE, CELL_SIZE / 2,
            CELL_SIZE / 2, CELL_SIZE, CELL_SIZE, scale, scale, 0.0f);
      } else {
        if (resourceRegenerationRate > 0.01f) {
          batch.draw(resourceTexture, cellX * CELL_SIZE, cellY * CELL_SIZE, CELL_SIZE / 2,
              CELL_SIZE / 2, CELL_SIZE, CELL_SIZE, 0.1f, 0.1f, 0.0f);
        }
      }
    }
  }
  
  private class ResourceStorage {
    private int cellX;
    private int cellY;
    private Owner ownerType;
    
    public ResourceStorage(int cellX, int cellY, Owner ownerType) {
      this.cellX = cellX;
      this.cellY = cellY;
      this.ownerType = ownerType;
    }
    
    public int getX() {
      return cellX;
    }
    
    public int getY() {
      return cellY;
    }
    
    public Owner getOwnerType() {
      return ownerType;
    }

    private void render(SpriteBatch batch) {
      batch.setColor(0.0f, 0.0f, 0.0f, 0.7f);
      batch.draw(grassTexture, cellX * CELL_SIZE, cellY * CELL_SIZE);
    }
  }
  
  public static final int COLUMNS_COUNT = 24;
  public static final int ROWS_COUNT = 16;
  public static final int CELL_SIZE = 60;
  public static final int MAP_WIDTH_PX = COLUMNS_COUNT * CELL_SIZE;
  public static final int MAP_HEIGHT_PX = ROWS_COUNT * CELL_SIZE;

  private TextureRegion grassTexture;
  private TextureRegion resourceTexture;
  private Cell[][] cells;
  
  private ResourceStorage resourceStorage;
  
  public boolean isResourseStorage(Vector2 point, Owner ownerType) {
    return (int) (point.x / CELL_SIZE)  == resourceStorage.getX()
        && (int) (point.y / CELL_SIZE) == resourceStorage.getY()
        && ownerType == resourceStorage.getOwnerType();
  }

  public BattleMap() {
    this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
    this.resourceTexture = Assets.getInstance().getAtlas().findRegion("resource");
    this.cells = new Cell[COLUMNS_COUNT][ROWS_COUNT];
    for (int i = 0; i < COLUMNS_COUNT; i++) {
      for (int j = 0; j < ROWS_COUNT; j++) {
        cells[i][j] = new Cell(i, j);
      }
    }
    
    resourceStorage = new ResourceStorage(1, ROWS_COUNT / 2, Owner.PLAYER);
  }

  public int getResourceCount(Vector2 point) {
    int cx = (int) (point.x / CELL_SIZE);
    int cy = (int) (point.y / CELL_SIZE);
    return cells[cx][cy].resource;
  }

  public int harvestResource(Vector2 point, int power) {
    int value = 0;
    int cx = (int) (point.x / CELL_SIZE);
    int cy = (int) (point.y / CELL_SIZE);
    if (cells[cx][cy].resource >= power) {
      value = power;
      cells[cx][cy].resource -= power;
    } else {
      value = cells[cx][cy].resource;
      cells[cx][cy].resource = 0;
    }
    return value;
  }

  public void update(float dt) {
    for (int i = 0; i < COLUMNS_COUNT; i++) {
      for (int j = 0; j < ROWS_COUNT; j++) {
        cells[i][j].update(dt);
      }
    }
  }

  public void render(SpriteBatch batch) {
    for (int i = 0; i < COLUMNS_COUNT; i++) {
      for (int j = 0; j < ROWS_COUNT; j++) {
        batch.draw(grassTexture, i * CELL_SIZE, j * CELL_SIZE);
        cells[i][j].render(batch);
      }
    }
    
    resourceStorage.render(batch);
  }
}
