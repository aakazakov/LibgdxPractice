package com.dune.game.core.user_logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dune.game.core.BattleMap;
import com.dune.game.core.GameController;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.BattleTank;
import com.dune.game.core.units.Harvester;
import com.dune.game.core.units.types.Owner;
import com.dune.game.core.units.types.UnitType;

public class AiLogic extends BaseLogic {
  private class ResourceFinder {
    Map<Harvester, int[]> harvestTable;
    
    public ResourceFinder() {
      harvestTable = new HashMap<>();
    }
    
    public void go() {
      harvestTable.clear();
      
      if (!tmpAiHarvesters.isEmpty()) {
        for (int i = 0; i < tmpAiHarvesters.size(); i++) {
          int[] resource = getFirstAvailableResource();     
          finedPair(resource);
        }
      }
      
      // FIXME hash map ??????
      
    }
    
    private void finedPair(int[] reasourceCell) {
      if (reasourceCell == null) return;
      
      Harvester nearestHarvester = findNearestHarvester(reasourceCell);
      
      if (nearestHarvester == null) return;
      
      int[] nearestResource = findNearestResource(nearestHarvester);
      
      if (Arrays.equals(reasourceCell, nearestResource)) {
        harvestTable.put(nearestHarvester, nearestResource);
      } else {
        finedPair(nearestResource);
      }
    }
    
    private int[] getFirstAvailableResource() {
      int[] resource = new int[2];
      for (int i = 0; i < BattleMap.COLUMNS_COUNT; i++) {
        for (int j = 0; j < BattleMap.ROWS_COUNT; j++) {
          resource[0] = i;
          resource[1] = j;
          if (!isResourceOccupied(resource) && !harvestTable.containsValue(resource)) return resource;
        }
      }
      return null;
    }
       
    private Harvester findNearestHarvester(int[] resource) {
      Harvester nearestHarvester = null;
      float minDst = 10000000.0f;
      if (tmpAiHarvesters.isEmpty()) return nearestHarvester;
      for (int i = 0; i < tmpAiHarvesters.size(); i++) {
        Harvester harvester = tmpAiHarvesters.get(i);
        if (harvestTable.containsKey(harvester)) continue;
        float x = resource[0] * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2;
        float y = resource[1] * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2;
        float dst = harvester.getPosition().dst(x, y);
        if (minDst > dst) {
          minDst = dst;
          nearestHarvester = harvester;
        }
      }
      return nearestHarvester;
    }
    
    private int[] findNearestResource(Harvester harvester) {
      int[] nearestResource = null;
      float minDst = 10000000.0f;
      for (int i = 0; i < BattleMap.COLUMNS_COUNT; i++) {
        for (int j = 0; j < BattleMap.ROWS_COUNT; j++) {
          int[] resource = new int[] {i, j};
          if (isResourceOccupied(resource) || harvestTable.containsValue(resource)) continue;
          float x = resource[0] * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2;
          float y = resource[1] * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2;
          float dst = harvester.getPosition().dst(x, y);
          if (minDst > dst) {
            nearestResource = resource;
            minDst = dst;
          }
        }
      }
      return nearestResource;
    }
    
    private boolean isResourceOccupied(int[] resourcePosition) {
      List<AbstractUnit> units = gc.getUnitsController().getUnits();
      for (int i = 0; i < units.size(); i++) {
        AbstractUnit au = units.get(i);
        if (au.getCellX() == resourcePosition[0] && au.getCellY() == resourcePosition[1]) {
          return true;
        }
      }
      return false;
    }
  }
  
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
