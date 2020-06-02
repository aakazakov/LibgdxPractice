package com.dune.game.core;

public class Weapon {
  public enum Type {
    GROUND(0), HARVEST(1), AIR(2);
    
    private int imageIndex;
    
    private Type(int imageIndex) {
      this.imageIndex = imageIndex;
    }
    
    public int getImageIndex() {
      return imageIndex;
    }
  }
  
  private Type type;
  private float period;
  private float time;
  private int power;
  private float angle;
  
  public Weapon(Type type, float period, int power) {
    this.type = type;
    this.period = period;
    this.power = power;
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
  
  public Type getType() {
    return type;
  }
  
  public float getUsageTimePercentage() {
    return time / period;
  }
  
  public void setAngle(float angle) {
    this.angle = angle;
  }

  public float getAngle() {
    return angle;
  }
}
