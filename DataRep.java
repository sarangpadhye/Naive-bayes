package nb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DataRep {

	private String featureName;
	private ArrayList<String> possibleValues;
	private double conditionalProbability;
	private ArrayList<String> allValues;
	private ArrayList<Double> priors;
	private double ConditionalProbabilityForZero;
	private double ConditionalProbabilityForOne;
	private HashMap<String, Integer> CountMap = new HashMap<String,Integer>();
	
	public DataRep() {
		// possibleValues = null;
		conditionalProbability = 0.0;
		allValues = null;
		priors = null;
		CountMap = null;
	}

	public void setPriors(ArrayList<Double> Priors) {
		this.priors = Priors;
	}

	public ArrayList<Double> getPriors() {
		return priors;
	}

	public void setAllValues(ArrayList<String> allvalues) {
		this.allValues = allvalues;
	}

	public ArrayList<String> getAllValues() {
		return allValues;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFeatureName() {
		return featureName;

	}

	public void setPossibleValues(ArrayList<String> possiblevalues) {

		this.possibleValues = possiblevalues;
	}

	public ArrayList<String> getPossibleValues() {
		return possibleValues;

	}

	public void setConditionalProbabilityForClassZero(
			double ConditionalProbabilityForZero) {
		this.ConditionalProbabilityForZero = ConditionalProbabilityForZero;
	}

	public double getProbabilityForClassZero() {
		return ConditionalProbabilityForZero;

	}

	public void setConditionalProbabilityForClassOne(
			double ConditionalProbabilityForOne) {
		this.ConditionalProbabilityForOne = ConditionalProbabilityForOne;
	}

	public double getProbabilityForClassOne() {
		return ConditionalProbabilityForOne;

	}
	
	public void setMap(HashMap<String,Integer> countMap2)
	{
		this.CountMap = countMap2;
	}
	
	public HashMap<String,Integer> getMap()
	{
		return CountMap;
		
	}

}
