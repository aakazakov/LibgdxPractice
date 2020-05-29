package com.dune.game.core;

public class Weapon {
  public enum Type {
    GROUND,
    AIR,
    HARVEST
  }
  
  private Type type;
  private float period;
  private float time;
  private int power;
  
  public Weapon(Type type, float period, int power) {
    this.type = type;
    this.period = period;
    this.power = power;
  }
  
  public void reset() {
    time = 0.0f;
  }
  
  public int use(float dt) {
    time += dt;
    if (time > period) {
      time = 0.0f;
      return power;
    }
    return -1;
  }
  
  public Type getType() {
    return type;
  }
  
  public float getUsageTimePersentage() {
    return time / period;
  }
  
}