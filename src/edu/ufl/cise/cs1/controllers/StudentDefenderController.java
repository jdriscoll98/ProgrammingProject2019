package edu.ufl.cise.cs1.controllers;

import game.controllers.DefenderController;
import game.models.*;

import java.util.ArrayList;
import java.util.List;

public final class StudentDefenderController implements DefenderController {
	private List<Node> powerPillsToAttack = new ArrayList<>();
	public void init(Game game) {
	}

	public void shutdown(Game game) {
	}

	public int[] update(Game game, long timeDue) {

		//There will be 4 defenders: Chase, Ambush, Patrol, and Gaurd
		// Smart: Chases until attacker gets close to a pill then flees
		// Chase: Chases after attacker unless vulnerable
		// Bait: Smart Defender, but gets close to attacker when hes waiting to eat pill
		// Gaurd: Chase until 2 Power Pills left, then "gaurd" the furthest power pill from the attacker
		// Since the defenders move at the same speed as the attacker, it is important that they all have different strategies in order to trap the attacker
		Attacker attacker = game.getAttacker();
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();
		actions[0] = getSmartDirection(game, enemies.get(0), attacker, enemies.get(1).getPossibleDirs());
		actions[1] = getGaurdDirection(game, enemies.get(1), attacker, enemies.get(1).getPossibleDirs());
		actions[2] = getChaseDirection(game, enemies.get(2), attacker, enemies.get(2).getPossibleDirs());
		actions[3] = getBaitDirection(game, enemies.get(3), attacker, enemies.get(3).getPossibleDirs());


		return actions;
	}
	public int getGaurdDirection(Game game, Defender defender, Attacker attacker, List<Integer> possibleDirs){
		List<Node> powerPillList = game.getPowerPillList();
		List<Node> powerPillsLeft = new ArrayList<>();
		for (Node node: powerPillList) {
			if (node != null) {
				powerPillsLeft.add(node);
			}
		}
		if (powerPillsLeft != null) {
			if (powerPillsLeft.size() == 2) {
				Node furthestPowerPill = attacker.getTargetNode(powerPillList, false);
				int direction = defender.getNextDir(furthestPowerPill, true);
				return direction;
			}}
		return getChaseDirection(game, defender, attacker, possibleDirs);
	}

	public int getChaseDirection(Game game, Defender defender, Attacker attacker, List<Integer> possibleDirs) {
		Node location = attacker.getLocation();
		int direction = defender.getNextDir(location, true);
		if (defender.isVulnerable()) {
			return -direction;
		}
		return direction;
	}
	public int getSmartDirection(Game game, Defender defender, Attacker attacker, List<Integer> possibleDirs) {
		Node attackerLocation = attacker.getLocation();
		List<Node> powerPillList = game.getPowerPillList();
		int max_distance = 50074;
		for (Node node: powerPillList) {
			if (node != null) {
				if (attackerLocation.getPathDistance(node) < max_distance) {
					max_distance = attackerLocation.getPathDistance(node);
				}
			}
		}
		if (max_distance < 25 || defender.isVulnerable()) {
			return defender.getNextDir(attackerLocation, false);
		}
		return defender.getNextDir(attackerLocation, true);
	}
	public int getBaitDirection(Game game, Defender defender, Attacker attacker, List<Integer> possibleDirs) {
		Node attackerLocation = attacker.getLocation();
		List<Node> powerPillList = game.getPowerPillList();
		int max_distance = 50074;
		for (Node node: powerPillList) {
			if (node != null) {
				if (attackerLocation.getPathDistance(node) < max_distance) {
					max_distance = attackerLocation.getPathDistance(node);
				}
			}
		}
		if (max_distance < 25 && max_distance > 5) {
			return defender.getNextDir(attackerLocation, false);
		}
		return defender.getNextDir(attackerLocation, true);
	}
}
