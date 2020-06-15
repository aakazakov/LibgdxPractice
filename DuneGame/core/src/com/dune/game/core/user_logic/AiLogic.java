package com.dune.game.core.user_logic;

import java.util.ArrayList;
import java.util.List;

import com.dune.game.core.BattleMap;
import com.dune.game.core.Building;
import com.dune.game.core.GameController;
import com.dune.game.core.units.*;
import com.dune.game.core.units.types.Owner;
import com.dune.game.core.units.types.UnitType;

public class AiLogic extends BaseLogic {
  private Building stock;
  private float timer;
  private List<BattleTank> tmpAiBattleTanks;
  private List<Harvester> tmpAiHarvesters;
  private List<BattleTank> tmpPlayerBattleTanks;
  private List<Harvester> tmpPlayerHarvesters;
  private List<AbstractUnit> tmpAllPlayerUnits;

  public AiLogic(GameController gc) {
    this.gc = gc;
    this.money = 1000;
    this.unitsCount = 10;
    this.unitsMaxCount = 100;
    this.ownerType = Owner.AI;
    this.tmpAiBattleTanks = new ArrayList<>();
    this.tmpAiHarvesters = new ArrayList<>();
    this.tmpPlayerBattleTanks = new ArrayList<>();
    this.tmpPlayerHarvesters = new ArrayList<>();
    this.tmpAllPlayerUnits = new ArrayList<>();
    this.timer = 10000.0f;
  }

  public void update(float dt) {
    timer += dt;
    if (timer > 2.0f) {
      timer = 0.0f;
      gc.getUnitsController().collectTanks(tmpAiBattleTanks, gc.getUnitsController().getAiUnits(),
          UnitType.BATTLE_TANK);
      gc.getUnitsController().collectTanks(tmpAiHarvesters, gc.getUnitsController().getAiUnits(), UnitType.HARVESTER);
      gc.getUnitsController().collectTanks(tmpPlayerBattleTanks, gc.getUnitsController().getPlayerUnits(),
          UnitType.BATTLE_TANK);
      gc.getUnitsController().collectTanks(tmpPlayerHarvesters, gc.getUnitsController().getPlayerUnits(),
          UnitType.HARVESTER);

      tmpAllPlayerUnits.addAll(tmpPlayerBattleTanks);
      tmpAllPlayerUnits.addAll(tmpPlayerHarvesters);

      for (int j = 0; j < gc.getBuildingsController().getActiveList().size(); j++) {
        if (gc.getBuildingsController().getActiveList().get(j).getOwnerLogic() == this) {
          stock = gc.getBuildingsController().getActiveList().get(j);
          break;
        }
      }

      attackThePlayer();
      
      collectResources();

    }
  }

  private void collectResources() {
    for (int i = 0; i < BattleMap.COLUMNS_COUNT; i++) {
      int leftX = 0;
      int rightX = 0;
      int leftY = 0;
      int rightY = 0;
      for (int j = 0; j < tmpAiHarvesters.size(); j++) {
        Harvester h = tmpAiHarvesters.get(j);
        if (!h.isUsed()) {
          int posX = h.getCellX();
          int posY = h.getCellY();
          if (posX - i >= 0) {
            leftX = posX - i;
          }
          if (posY - i >= 0) {
            leftY = posY - i;
          }
          if (posX + i < BattleMap.COLUMNS_COUNT) {
            rightX = posX + i;
          }
          if (posY + i < BattleMap.ROWS_COUNT) {
            rightY = posY + i;
          }
          for (int x = leftX; x <= rightX; x++) {
            for (int y = leftY; y <= rightY; y++) {
              if (gc.getMap().getResourceCount(x, y) > 0) {
                h.setUsed(true);
                h.commandMoveTo(x * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2,
                    y * BattleMap.CELL_SIZE + BattleMap.CELL_SIZE / 2);
                break;
              }
            }
            if (h.isUsed()) {
              break;
            }
          }
        }
      }
    }
    for (int i = 0; i < tmpAiHarvesters.size(); i++) {
      Harvester h = tmpAiHarvesters.get(i);
      if (h.isUsed()) {
        if (gc.getMap().getResourceCount(h.getCellX(), h.getCellY()) <= 0) {
          h.setUsed(false);
        }
      }
      if (h.isContainerLoaded()) {
        h.commandMoveTo(stock.getPosition());
      }
    }
  }

  private void attackThePlayer() {
    for (int i = 0; i < tmpAiBattleTanks.size(); i++) {
      BattleTank aiBattleTank = tmpAiBattleTanks.get(i);
      aiBattleTank.commandAttack(findNearestTarget(aiBattleTank, tmpAllPlayerUnits));
    }
  }

  private <T extends AbstractUnit> T findNearestTarget(AbstractUnit currentTank, List<T> possibleTargetList) {
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
