package com.dune.game.core;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool<T extends Poolable> {
  protected List<T> activeList;
  protected List<T> freeList;
  
  public ObjectPool() {
    activeList = new ArrayList<>();
    freeList = new ArrayList<>();
  }
  
  public T getActiveElement() {
    if (freeList.isEmpty()) freeList.add(newObject());
    T tempObject = freeList.remove(freeList.size() - 1);
    activeList.add(tempObject);
    return tempObject;
  }
  
  public abstract T newObject();
  
  public void free(int index) {
    freeList.add(activeList.remove(index));
  }
  
  public void checkPool() {
    for (int i = activeList.size() - 1; i >= 0; i--) {
      if (activeList.get(i).isActive()) continue;
      free(i);
    }
  }
  
  public List<T> getActiveList() {
    return activeList;
  }
  
  public List<T> getFreeList() {
    return freeList;
  }
  
}
