package com.dune.game.core;

public class Weapon {
  private float period;
  private float time;
  private float angle;
  private int power;

  public Weapon(float period, int power) {
    this.period = period;
    this.power = power;
  }

  public void setAngle(float angle) {
    this.angle = angle;
  }

  public float getAngle() {
    return angle;
  }

  public float getUsageTimePercentage() {
    return time / period;
  }

  public int use(float dt) {
    time += dt;
    if (time > period) {
      reset();
      return power;
    }
    return -1;
  }

  public void reset() {
    time = 0.0f;
  }
}
