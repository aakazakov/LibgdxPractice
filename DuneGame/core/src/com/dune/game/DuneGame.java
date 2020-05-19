package com.dune.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class DuneGame extends ApplicationAdapter {
  private static class Tank {
    private Vector2 position;
    private Texture texture;
    private float angle;
    private float speed;

    public Tank(float x, float y) {
      this.position = new Vector2(x, y);
      this.texture = new Texture("tank1.png");
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
        position.x += speed * MathUtils.cosDeg(angle) * dt;
        position.y += speed * MathUtils.sinDeg(angle) * dt;
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
    private Random random = new Random();

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
      float deltaX = Math.abs(tank.position.x - this.position.x);
      float deltaY = Math.abs(tank.position.y - this.position.y);
      return deltaX < 65 && deltaY < 65;
    }
    
    private void setRandomPosition() {
      float x = (float) random.nextInt(1241);
      float y = (float) random.nextInt(681);
      position.x = x > 40f ? x : 40f;
      position.y = y > 40f ? y : 40f;
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
  }

}