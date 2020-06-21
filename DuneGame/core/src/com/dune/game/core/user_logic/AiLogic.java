package com.dune.game.core.user_logic;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.BattleMap;
import com.dune.game.core.GameController;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.BattleTank;
import com.dune.game.core.units.Harvester;
import com.dune.game.core.units.types.Owner;
import com.dune.game.core.units.types.UnitType;

public class AiLogic extends BaseLogic {
  private class ResourceFinder {
    
    public void go() {
      Vector2 resource = getFirstAvailableResource();
      
      Harvester nearestHarvester = finedNearestHarvester(resource);
      
      Vector2 nearestResource = finedNearestResource(nearestHarvester);
      
      
      Harvester nearestHarvester1 = finedNearestHarvester(nearestResource);
      Vector2 nearestResource1 = finedNearestResource(nearestHarvester1);
      
      //==== logs =====
      
      System.out.println("resource: " + (int)resource.x / BattleMap.CELL_SIZE + " " + (int)resource.y / BattleMap.CELL_SIZE);
      System.out.println("nearestHarvester: " + nearestHarvester.getCellX() + " " + nearestHarvester.getCellY());
      System.out.println("nearestResource: " + (int)nearestResource.x / BattleMap.CELL_SIZE + " " + (int)nearestResource.y / BattleMap.CELL_SIZE);
      
      System.out.println("nearestHarvester1: " + nearestHarvester1.getCellX() + " " + nearestHarvester1.getCellY());
      System.out.println("nearestResource1: " + (int)nearestResource1.x / BattleMap.CELL_SIZE + " " + (int)nearestResource1.y / BattleMap.CELL_SIZE);
      System.out.println(nearestHarvester1 == nearestHarvester);
      
      // ==============
    }
    
    private Vector2 getFirstAvailableResource() {
      Vector2 resource = new Vector2();
      for (int i = 0; i < BattleMap.COLUMNS_COUNT; i++) {
        for (int j = 0; j < BattleMap.ROWS_COUNT; j++) {
          if (gc.getMap().getResourceCount(i, j) > 0 && !isResourceOccupied(i, j)) {
            resource.set(i * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2,
                j * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2);
            return resource;
          }
        }
      }
      return resource;
    }
    
    private boolean isResourceOccupied(int cellX, int cellY) {
      List<AbstractUnit> units = gc.getUnitsController().getUnits();
      for (int i = 0; i < units.size(); i++) {
        AbstractUnit au = units.get(i);
        if (au.getCellX() == cellX && au.getCellY() == cellY) {
          return true;
        }
      }
      return false;
    }
    
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
      Vector2 nearestResource = new Vector2();
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
