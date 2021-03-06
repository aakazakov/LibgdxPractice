package com.dune.game.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.dune.game.core.interfaces.Poolable;

public abstract class ObjectPool<T extends Poolable> {
  protected List<T> activeList;
  protected List<T> freeList;
  
  public ObjectPool() {
    activeList = new ArrayList<>();
    freeList = new ArrayList<>();
  }
  
  public T activateObject() {
    if (freeList.isEmpty()) freeList.add(newObject());
    T tempObject = freeList.remove(freeList.size() - 1);
    activeList.add(tempObject);
    return tempObject;
  }
  
  protected abstract T newObject();
  
  public void free(int index) {
    freeList.add(activeList.remove(index));
  }
  
  public void checkPool() {
    for (int i = activeList.size() - 1; i >= 0; i--) {
      if (activeList.get(i).isActive()) continue;
      free(i);
    }
  }
  
  public int activeSize() {
    return activeList.size();
  }
  
  public List<T> getActiveList() {
    return activeList;
  }
}
