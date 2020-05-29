package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Updatable {
  void update(float dt);
  void render(SpriteBatch batch);
}
