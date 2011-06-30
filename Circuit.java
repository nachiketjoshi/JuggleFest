package com.nachiketjoshi;

import java.util.ArrayList;
import java.util.List;

import com.nachiketjoshi.Juggler;

public class Circuit {
	private final String _name;
	private final int _handEye;
	private final int _endurance;
	private final int _pizzazz;
	private final List<Juggler> _assignedJugglers;

	public Circuit(String name_, int handEye_, int endurance_, int pizzazz_) {
		_name = name_;
		_handEye = handEye_;
		_endurance = endurance_;
		_pizzazz = pizzazz_;
		_assignedJugglers = new ArrayList<Juggler>();
	}

	public Juggler getJugglerWithLowestScore() {
		Juggler lowestJuggler = null;
		for (Juggler j : _assignedJugglers) {
			if (lowestJuggler == null
					|| (j.getCircuitScore(this) < lowestJuggler.getCircuitScore(this))) {
				lowestJuggler = j;
			}
		}
		return lowestJuggler;
	}

	public String getName() {
		return _name;
	}

	public int getHandEye() {
		return _handEye;
	}

	public int getEndurance() {
		return _endurance;
	}

	public int getPizzazz() {
		return _pizzazz;
	}

	public List<Juggler> getAssignedJugglers() {
		return _assignedJugglers;
	}

	public String toString() {
		return _name + " (H:" + _handEye + " E:" + _endurance + " P:"
				+ _pizzazz + " assigned = " + _assignedJugglers + ")";
	}
}