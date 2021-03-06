package com.dune.game.core.user_logic;

import java.util.ArrayList;
import java.util.List;

import com.dune.game.core.GameController;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.BattleTank;
import com.dune.game.core.units.Harvester;
import com.dune.game.core.units.types.Owner;
import com.dune.game.core.units.types.UnitType;

public class AiLogic extends BaseLogic {
  private float timer;

  private List<BattleTank> tmpAiBattleTanks;
  private List<Harvester> tmpPlayerHarvesters;
  private List<Harvester> tmpPlayerBattleTanks;

  public AiLogic(GameController gc) {
    this.gc = gc;
    this.money = 1000;
    this.unitsCount = 10;
    this.unitsMaxCount = 100;
    this.ownerType = Owner.AI;
    this.tmpAiBattleTanks = new ArrayList<>();
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
      gc.getUnitsController().collectTanks(tmpPlayerHarvesters, gc.getUnitsController().getPlayerUnits(),
          UnitType.HARVESTER);
      gc.getUnitsController().collectTanks(tmpPlayerBattleTanks, gc.getUnitsController().getPlayerUnits(),
          UnitType.BATTLE_TANK);
      for (int i = 0; i < tmpAiBattleTanks.size(); i++) {
        BattleTank aiBattleTank = tmpAiBattleTanks.get(i);
        aiBattleTank.commandAttack(findNearestTarget(aiBattleTank, tmpPlayerBattleTanks));
        System.out.println(tmpPlayerBattleTanks.size());
      }
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
