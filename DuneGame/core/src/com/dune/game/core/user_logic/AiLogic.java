package com.dune.game.core.user_logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.BattleMap;
import com.dune.game.core.GameController;
import com.dune.game.core.units.*;
import com.dune.game.core.units.types.Owner;
import com.dune.game.core.units.types.UnitType;

public class AiLogic extends BaseLogic {
  private class ResourceFinder {
    Map<Harvester, Vector2> harvestersAtWork;
    
    public ResourceFinder() {
      harvestersAtWork = new HashMap<>();
    }
    
    public void go() { // optimization, where r u ??!! ))))
      
      if (!tmpAiHarvesters.isEmpty()) {
        for (int i = 0; i < tmpAiHarvesters.size(); i++) {
          Harvester harvester = tmpAiHarvesters.get(i);
          Vector2 nearestResource = finedNearestResource(harvester);
          Harvester nearestHarvester = finedNearestHarvester(nearestResource);
          if (nearestHarvester == harvester && !harvestersAtWork.containsKey(nearestHarvester)) {
            harvestersAtWork.put(nearestHarvester, nearestResource);
            nearestHarvester.commandMoveTo(nearestResource);
          }
          if (harvestersAtWork.containsKey(nearestHarvester) &&
              gc.getMap().getResourceCount(harvestersAtWork.get(nearestHarvester)) == 0) {
            harvestersAtWork.remove(nearestHarvester);
          }
        }
      }
      
      
//      Harvester harvester = getFirstAvailableHarvester();
//      
//      if (harvester == null) return;
//      
//      Vector2 nearestResource = finedNearestResource(harvester);
//      
//      Harvester nearestHarvester = finedNearestHarvester(nearestResource);
//      
//      if (nearestHarvester == harvester && !harvestersAtWork.containsKey(nearestHarvester)) {
//        harvestersAtWork.put(nearestHarvester, nearestResource);
//        nearestHarvester.commandMoveTo(nearestResource);
//      }
//      
//      if (harvestersAtWork.containsKey(nearestHarvester) &&
//          gc.getMap().getResourceCount(harvestersAtWork.get(nearestHarvester)) == 0) {
//        harvestersAtWork.remove(nearestHarvester);
//      }
      
      //==== logs =====
      
//      System.out.println("harvester: " + harvester.getCellX() + " " + harvester.getCellY());
//      System.out.println("nearestResource: " + (int)nearestResource.x / BattleMap.CELL_SIZE + " " + (int)nearestResource.y / BattleMap.CELL_SIZE);
//      System.out.println("nearestHarvester: " + nearestHarvester.getCellX() + " " + nearestHarvester.getCellY());
//      System.out.println(harvestersAtWork.size());
      
      // ==============
    }
    
//    private Harvester getFirstAvailableHarvester() {
//      Harvester harvester = null;
//      for (int i = 0; i < tmpAiHarvesters.size(); i++) {
//        Harvester h = tmpAiHarvesters.get(i);
//        if (h.isActive()) {
//          harvester = h;
//          break;
//        }
//      }
//      return harvester;
//    }
       
    private Harvester finedNearestHarvester(Vector2 resource) {
      Harvester nearestHarvester = null;
      float minDst = 1000000.0f;
      for (int i = 0; i < tmpAiHarvesters.size(); i++) {
        Harvester h = tmpAiHarvesters.get(i);
        float dst = h.getPosition().dst(resource);
        if (minDst > dst) {
          minDst = dst;
          nearestHarvester = h; 
        }
      }
      return nearestHarvester;
    }
  
    private Vector2 finedNearestResource(Harvester h) {
      Vector2 nearestResource = new Vector2(); // TODO replace to tmp ???
      float minDst = 1000000.0f;
      for (int i = 0; i < BattleMap.COLUMNS_COUNT; i++) {
        for (int j = 0; j < BattleMap.ROWS_COUNT; j++) {
          if (gc.getMap().getResourceCount(i, j) > 0 && !isResourceOccupied(i, j)) {
            float x = i * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2;
            float y = j * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2;
            float dst = h.getPosition().dst(x, y);
            if (minDst > dst) {
              minDst = dst;
              nearestResource.set(x, y);
            }
          }
        }
      } 
      return nearestResource;
    }
    
    private boolean isResourceOccupied(int cellX, int cellY) {
      List<AbstractUnit> units = gc.getUnitsController().getUnits();
      for (int i = 0; i < units.size(); i++) {
        AbstractUnit au = units.get(i);
        if (au.getCellX() == cellX && au.getCellY() == cellY && !harvestersAtWork.containsKey(au)) {
          return true;
        }
      }
      return false;
    }
  }
  
  private ResourceFinder resourceFinder;
  
  private float timer;

  private List<BattleTank> tmpAiBattleTanks;
  private List<Harvester> tmpAiHarvesters;
  private List<Harvester> tmpPlayerHarvesters;
  private List<Harvester> tmpPlayerBattleTanks;

  public AiLogic(GameController gc) {
    this.gc = gc;
    this.money = 1000;
    this.unitsCount = 10;
    this.unitsMaxCount = 100;
    this.ownerType = Owner.AI;
    this.tmpAiBattleTanks = new ArrayList<>();
    this.tmpAiHarvesters = new ArrayList<>();
    this.tmpPlayerHarvesters = new ArrayList<>();
    this.tmpPlayerBattleTanks = new ArrayList<>();
    this.timer = 10000.0f;
    this.resourceFinder = new ResourceFinder();
  }

  public void update(float dt) {
    timer += dt;
    if (timer > 2.0f) {
      timer = 0.0f;
      gc.getUnitsController().collectTanks(tmpAiBattleTanks, gc.getUnitsController().getAiUnits(),
          UnitType.BATTLE_TANK);
      gc.getUnitsController().collectTanks(tmpAiHarvesters, gc.getUnitsController().getAiUnits(),
          UnitType.HARVESTER);
      gc.getUnitsController().collectTanks(tmpPlayerHarvesters, gc.getUnitsController().getPlayerUnits(),
          UnitType.HARVESTER);
      gc.getUnitsController().collectTanks(tmpPlayerBattleTanks, gc.getUnitsController().getPlayerUnits(),
          UnitType.BATTLE_TANK);
//      for (int i = 0; i < tmpAiBattleTanks.size(); i++) {
//        BattleTank aiBattleTank = tmpAiBattleTanks.get(i);
//        aiBattleTank.commandAttack(findNearestTarget(aiBattleTank, tmpPlayerBattleTanks));
//        System.out.println(tmpPlayerBattleTanks.size());
//      }
      
      resourceFinder.go();
    }
  }

  public <T extends AbstractUnit> T findNearestTarget(AbstractUnit currentTank, List<T> possibleTargetList) {
    T target = null;
    float minDist = 1000000.0f;
    for (int i = 0; i < possibleTargetList.size(); i++) {
      T possibleTarget = possibleTargetList.get(i);
      float currentDst = currentTank.getPosition().dst(possibleTarget.getPosition());
      if (currentDst < minDist) {
        target = possibleTarget;
        minDist = currentDst;
      }
    }
    return target;
  }
}
