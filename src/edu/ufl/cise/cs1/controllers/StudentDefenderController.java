package edu.ufl.cise.cs1.controllers;

import game.controllers.DefenderController;
import game.models.*;

import java.util.List;

public final class StudentDefenderController implements DefenderController {
	public void init(Game game) {
	}

	public void shutdown(Game game) {
	}

	public int[] update(Game game, long timeDue) {
		Attacker attacker = game.getAttacker();
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();

		//There will be 4 defenders: Chase, Ambush, Patrol, and Gaurd
		// Chase: Find direction to make euclidean distance to attacker smaller
		// Ambush: Find direction to make distance to attackers line of direction smaller
		// Patrol: Roam randomly until Attacker is close, then become chase/ambush
		// Gaurd: Chase until 2 Power Pills left, then "gaurd" the furthest power pill from the attacker
		// Since the defenders move at the same speed as the attacker, it is important that they all have different strategies in order to trap the attacker
		for (int i = 0; i < actions.length; i++) {
			Defender defender = enemies.get(i);
			List<Integer> possibleDirs = defender.getPossibleDirs();
			actions[i] = getChaseDirection(defender, attacker, possibleDirs);
			switch (i) {
				case (0):
					actions[i] = getChaseDirection(defender, attacker, possibleDirs);
					break;
				case(1):
					actions[i] = getAmbushDirection(defender, attacker, possibleDirs);
					break;
				case(2):
					actions[i] = getAmbushDirection(defender, attacker, possibleDirs);
//					actions[i] = getPatrolDirection(defender, attacker, possibleDirs);
					break;
				case(3):
					actions[i] = getChaseDirection(defender, attacker, possibleDirs);
//					actions[i] = getGaurdDirection(defender, attacker, possibleDirs);
			}
		}

		return actions;
	}

	public int getChaseDirection(Defender defender, Attacker attacker, List<Integer> possibleDirs) {
		Node location = attacker.getLocation();
		int direction = defender.getNextDir(location, true);
		return direction;
	}
//


	public int getAmbushDirection(Defender defender, Attacker attacker, List<Integer> possibleDirs)
	{
		List<Node> possibleLocations = attacker.getPossibleLocations(false);
		Node closestNode = defender.getTargetNode(possibleLocations, true);
		Node furthestNode = defender.getTargetNode(possibleLocations, false);
		System.out.println("Closet: " + closestNode);
		System.out.println("Furthest: " + furthestNode);
		int direction = defender.getNextDir(closestNode, true);
		return direction;

	}
}
//	public int getPatrolDirection(defender, attacker, possibleDirs) {
//
//	}
//	public int getGaurdDirection(defender, attacker, possibleDirs) {}
//}