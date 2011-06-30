package com.nachiketjoshi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Juggler {
	private final String _name;
	private final int _handEye;
	private final int _endurance;
	private final int _pizzazz;
	private final List<Circuit> _preferredCircuits;
	private final Map<Circuit, Integer> _circuitScores;

	public Juggler(String name_, int handEye_, int endurance_, int pizzazz_, List<Circuit> preferredCircuits_) {
		_name = name_;
		_handEye = handEye_;
		_endurance = endurance_;
		_pizzazz = pizzazz_;
		_preferredCircuits = preferredCircuits_;
		_circuitScores = new HashMap<Circuit, Integer>();
	}

	public int getCircuitScore(Circuit c_) {
		if (_circuitScores.containsKey(c_)) {
			// Return previously calculated value
			return _circuitScores.get(c_);
		}
		final int score = (c_.getHandEye() * _handEye) + (c_.getEndurance() * _endurance) + (c_.getPizzazz() * _pizzazz);
		// Store newly calculated value
		_circuitScores.put(c_, score);
		return score;
	}

	public String getName() {
		return _name;
	}

	public List<Circuit> getPreferredCircuits() {
		return _preferredCircuits;
	}

	public String toString() {
		return _name + " (H:" + _handEye + " E:" + _endurance + " P:"
				+ _pizzazz + ")";
	}
}
