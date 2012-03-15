package mole.finder;

import java.util.ArrayList;

/** 
 * 
 * @author mbessett
 *
 */

public class MoleFinderModel {
	// db access
	private DatabaseManager DBManager;
	// active entries - to store after a search
	private ArrayList<ConditionEntry> conditions;
	private ArrayList<ConditionTag> tags;
}
