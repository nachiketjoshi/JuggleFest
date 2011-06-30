package com.nachiketjoshi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class JuggleFest
{
	private static final boolean _debug = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Map<String, Circuit> circuitNameMap = new HashMap<String, Circuit>();
		final List<Juggler> jugglerList = new ArrayList<Juggler>();
		try {
			BufferedReader bReader = new BufferedReader (new InputStreamReader (new URL("http://www.yodle.com/downloads/puzzles/jugglefest.txt").openStream()));
			String nextLine = bReader.readLine();
			while (nextLine != null) {
				String tokens[] = nextLine.split(" +");
				String firstToken = tokens[0];
				if (firstToken.equals("C")) {
					circuitNameMap.put(tokens[1], new Circuit(tokens[1], parseAbilityValue(tokens[2]), parseAbilityValue(tokens[3]), parseAbilityValue(tokens[4])));
				} else if (firstToken.equals("J")) {
					jugglerList.add(new Juggler(tokens[1], parseAbilityValue(tokens[2]), parseAbilityValue(tokens[3]), parseAbilityValue(tokens[4]), 
							parseCircuitPrefs(circuitNameMap, tokens[5])));
				}
				nextLine = bReader.readLine();
			}
			bReader.close();

		} 
		catch (Exception e) {
			System.err.println("Unable to initialize jugglers and circuits!");
			e.printStackTrace();
			System.exit(-1);
		}	

		assignJugglersToCircuits(circuitNameMap, new ArrayList<Juggler>(jugglerList));		
		printCircuitsWithJugglerAssignments(circuitNameMap);		
	
		/* Map<String, Circuit> c1970 = new HashMap<String, Circuit>();
			c1970.put("C1970", circuitNameMap.get("C1970"));
			printCircuitsWithJugglerAssignments(c1970); */
		
		/* C1970 
		 * J2602 C1970:300 C1921:120 C1504:90 C22:180 C62:240 C1357:90 C1252:80 C1751:150 C332:130 C539:150, 
		 * J4761 C1970:300 C19:160 C1521:200 C824:220 C141:120 C811:120 C761:190 C1650:120 C1037:100 C371:100, 
		 * J7850 C1970:300 C945:120 C667:150 C1655:230 C1785:210 C1295:80 C223:60 C707:250 C728:170 C1794:160, 
		 * J4445 C1970:300 C158:200 C1758:190 C1676:130 C1727:160 C802:170 C738:140 C1998:160 C1789:110 C112:150, 
		 * J2594 C1970:300 C646:100 C1528:210 C887:180 C1799:190 C652:180 C1914:120 C1280:110 C1235:190 C1447:120, 
		 * J6510 C1970:300 C1558:170 C1599:100 C1348:140 C1288:50 C243:170 C289:100 C1067:150 C136:120 C1008:150 */
	}

	private static void printCircuitsWithJugglerAssignments(Map<String, Circuit> circuitNameMap_) {
		for (Circuit c : circuitNameMap_.values()) {			
			StringBuffer sb = new StringBuffer();
			sb.append(c.getName() + " ");
			Iterator<Juggler> finalAssignments = c.getAssignedJugglers().iterator();
			while (finalAssignments.hasNext())
			{
				Juggler j = finalAssignments.next();
				sb.append(j.getName() + " ");
				Iterator<Circuit> preferred = j.getPreferredCircuits().iterator();
				while (preferred.hasNext())
				{
					Circuit pc = preferred.next();
					sb.append(pc.getName() + ":" + j.getCircuitScore(pc));
					if (preferred.hasNext())
					{
						sb.append(" ");
					}
				}
				if (finalAssignments.hasNext())
				{
					sb.append(", ");
				}
			}
			System.out.println(sb.toString());
		}		
	}

	private static void assignJugglersToCircuits(Map<String, Circuit> circuitNameMap_, List<Juggler> unassignedJugglers_) {
		// maximum number of jugglers on a circuit
		final int maxJugglersInOneCircuit = unassignedJugglers_.size() / circuitNameMap_.size();
		// while there are still jugglers that have not been assigned to a circuit
		while (!unassignedJugglers_.isEmpty()) { 
			// get a random juggler from the list
			Juggler nextJuggler = unassignedJugglers_.get(new Random().nextInt(unassignedJugglers_.size())); 
			// get the juggler's preferred circuits
			final List<Circuit> circuitListByPreference = new ArrayList<Circuit>(nextJuggler.getPreferredCircuits()); 
			// add all the remaining circuits after the preferred ones
			circuitListByPreference.addAll(circuitNameMap_.values()); 
			for (Circuit c : circuitListByPreference) {
				// if preferred circuit is already full
				if (c.getAssignedJugglers().size() >= maxJugglersInOneCircuit) {
					final Juggler lowestJuggler = c.getJugglerWithLowestScore();
					/* juggler should switch to a circuit that they prefer more if they are a better fit for the circuit 
					   than one of the other jugglers assigned to it */
					if (nextJuggler.getCircuitScore(c) > lowestJuggler.getCircuitScore(c)) {
						c.getAssignedJugglers().remove(lowestJuggler);
						log("Removed " + lowestJuggler + " from " + c);
						unassignedJugglers_.add(lowestJuggler);
						c.getAssignedJugglers().add(nextJuggler);
						log("Added " + nextJuggler + " to " + c);
						unassignedJugglers_.remove(nextJuggler);
						break;
					}
					else {
						/* juggler isn't a better fir for the circuit than any of the other jugglers assigned to it
						   get the juggler's next preferred circuit */
					}
				} 
				else {
					c.getAssignedJugglers().add(nextJuggler);
					log("Added " + nextJuggler + " to " + c);
					unassignedJugglers_.remove(nextJuggler);
					break;
				}
			}
		// loop until all jugglers are assigned a circuit
		}
	}
	
	private static void log(String s)
	{
		if (_debug)
		{
			System.out.println(s);
		}
	}

	private static List<Circuit> parseCircuitPrefs(Map<String, Circuit> circuitNameMap_, String inputString_) {
		List<Circuit> preferredCircuits = new ArrayList<Circuit>();
		String tokens[] = inputString_.split(",");
		for (String t : tokens) {
			preferredCircuits.add(circuitNameMap_.get(t));
		}
		return preferredCircuits;
	}

	private static int parseAbilityValue(String inputString_) {
		String tokens[] = inputString_.split(":");
		return Integer.parseInt(tokens[1]);
	}
}