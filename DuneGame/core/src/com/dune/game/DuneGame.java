package com.dune.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;

public class DuneGame extends ApplicationAdapter {
  private static class Tank {
    private Vector2 position;
    private Texture texture;
    private float angle;
    private float speed;

    public Tank(float x, float y) {
      this.position = new Vector2(x, y);
      this.texture = new Texture("tank.png");
      this.speed = 200.0f;
    }

    public void update(float dt) {
      if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        angle += 180.0f * dt;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        angle -= 180.0f * dt;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        float stepX = speed * MathUtils.cosDeg(angle) * dt;
        float stepY = speed * MathUtils.sinDeg(angle) * dt;
        
        if (position.x + stepX > 40.0f && position.x + stepX < 1240.0f) {
          position.x += stepX;
        }
        if (position.y + stepY > 40.0f && position.y + stepY < 680.0f) {
          position.y += stepY;
        }
      }
    }

    public void render(SpriteBatch batch) {
      batch.draw(texture, position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle, 0, 0, 80, 80, false, false);
    }

    public void dispose() {
      texture.dispose();
    }
  }

  private static class Circle {
    private Vector2 position = new Vector2();
    private Texture texture;

    public Circle() {
      setRandomPosition();
      this.texture = new Texture("smile.png");
    }

    public void update(Tank tank) {
      if (isTouched(tank)) {
        setRandomPosition();
      }
    }
    
    private boolean isTouched(Tank tank) {
      return this.position.cpy().sub(tank.position).len() < 65.0f;
    }
    
    private void setRandomPosition() {
      position.x = MathUtils.random(40.0f, 1241.0f);
      position.y = MathUtils.random(40.0f, 681.0f);
    }

    public void render(SpriteBatch batch) {
      batch.draw(texture, position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, 0, 0, 0, 80, 80, false, false);
    }
    
    public void dispose() {
      texture.dispose();
    }
  }

  private SpriteBatch batch;
  private Tank tank;
  private Circle circle;

  @Override
  public void create() {
    batch = new SpriteBatch();
    tank = new Tank(200, 200);
    circle = new Circle();
  }

  @Override
  public void render() {
    float dt = Gdx.graphics.getDeltaTime();
    update(dt);
    Gdx.gl.glClearColor(0, 0.4f, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    batch.begin();
    tank.render(batch);
    circle.render(batch);
    batch.end();
  }

  public void update(float dt) {
    tank.update(dt);
    circle.update(tank);
  }

  @Override
  public void dispose() {
    batch.dispose();
    tank.dispose();
    circle.dispose();
  }

}