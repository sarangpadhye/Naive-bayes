package nb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class NaiveBayes {
	// static ArrayList<DataRep> attributeList = new ArrayList<DataRep>();
	static ArrayList<String[]> completeDataset = new ArrayList();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<String[]> testSet = new ArrayList();
		NaiveBayes NB = new NaiveBayes();
		ArrayList<DataRep> attributeList = new ArrayList<DataRep>();

		// 0 indicates the Complete Data Set
		completeDataset = NB.LoadData(0);
		attributeList = setAttributeValues(attributeList);

		attributeList = calculateConditionalProb(attributeList);
		// 1 indicates the Test Data Set
		testSet = NB.LoadData(1);

		predictNewData(testSet, attributeList);

	}

	public static ArrayList<String> getActualTestValues(
			ArrayList<String[]> testSet) {

		ArrayList<String> ActualValues = new ArrayList<String>();

		for (String[] s : testSet) {

			ActualValues.add(s[s.length - 1]);
		}

		return ActualValues;
	}

	private static void predictNewData(ArrayList<String[]> testSet,
			ArrayList<DataRep> attributeList) {
		// TODO Auto-generated method stub
		ArrayList<String> ActualValues = new ArrayList<String>();
		ArrayList<String> savePredictedValues = new ArrayList<String>();
		ActualValues = getActualTestValues(testSet);
		System.out.println("Prediction has started ...../");

		for (String[] s : testSet) {
			savePredictedValues.add(classify(s, attributeList));
		}

		for (int i = 0; i < savePredictedValues.size(); i++) {
			System.out.println("Actual=> " + ActualValues.get(i) + "\t"
					+ "Predicted=> " + savePredictedValues.get(i));
		}

		int[][] c = CalculateConfusionMatrix(savePredictedValues, ActualValues);
		CalculateAccuracy(savePredictedValues, c, ActualValues);
	}

	public static int[][] CalculateConfusionMatrix(
			ArrayList<String> PredictedValues, ArrayList<String> actualValues) {

		int size = 0;
		int sizeofmat = 0;

		sizeofmat = (getsizeoflabels());

		int[][] C = new int[sizeofmat][sizeofmat];
		ArrayList<Integer> CovertedPredictedValues = new ArrayList<Integer>();
		ArrayList<Integer> CovertedActualValues = new ArrayList<Integer>();

		for (int i = 0; i < actualValues.size(); i++) {
			CovertedPredictedValues
					.add(Integer.parseInt(PredictedValues.get(i)));
			CovertedActualValues.add(Integer.parseInt(actualValues.get(i)));
		}

		for (int i = 0; i < actualValues.size(); i++) {
			int count = 0;
			int row = CovertedActualValues.get(i);
			int col = CovertedPredictedValues.get(i);
			if (row == col) {

				count = C[row][col];
				if (count == 0) {
					count = 1;
					C[row][col] = count;
				} else {
					count = count + 1;
					C[row][col] = count;
				}

			} else {
				count = C[row][col];
				if (count == 0) {
					count = 1;
					C[row][col] = count;
				} else {
					count = count + 1;
					C[row][col] = count;
				}

			}
		}

		System.out.println("------Confusion Matrix------");
		for (int i = 0; i < sizeofmat; i++) {
			for (int j = 0; j < sizeofmat; j++) {

				System.out.print("|" + C[i][j] + "|");

			}

			System.out.println();
		}
		return C;
	}

	public static int getsizeoflabels() {
		int size = 0;
		ArrayList<String> values = new ArrayList<String>();
		HashMap<String, Integer> countlabels = new HashMap<String, Integer>();

		for (String[] s : completeDataset) {
			for (int i = 0; i < s.length; i++) {

				values.add(s[s.length - 1]);
			}
		}

		int count = 0;

		for (int i = 0; i < values.size(); i++) {
			if (countlabels.get(values.get(i)) != null) {
				count = countlabels.get(values.get(i));
				count++;
				countlabels.put(values.get(i), count);
			} else {
				countlabels.put(values.get(i), 1);
			}
		}

		size = countlabels.keySet().size();

		return size;
	}

	// Function to Calculate the Accuracy
	public static double CalculateAccuracy(ArrayList<String> predictedValues,
			int[][] C, ArrayList<String> actualValues) {
		double accuracy = 0.0;
		double numerator = 0.0;
		double denominator = 0.0;
		double numerator1 = 0.0;
		double denominator1 = 0.0;
		double psumofcounts = 0.0;
		double nsumofcounts = 0.0;
		double misclassification = 0.0;
		int size = C.length;
		// ArrayList<String> actualValues = getActualTestValues();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i == j) {
					int count = C[i][j];
					psumofcounts = psumofcounts + count;
				} else {
					int count = C[i][j];
					nsumofcounts = nsumofcounts + count;
				}
			}
		}

		numerator = psumofcounts;
		denominator = actualValues.size();
		accuracy = (numerator / denominator) * 100.0;
		System.out.println("-----Evaluation-----");
		System.out.println("accuracy =" + accuracy + "%");
		numerator1 = nsumofcounts;
		denominator1 = actualValues.size();
		misclassification = (numerator1 / denominator1) * 100.0;
		System.out.println("misclassification =" + misclassification + "%");

		return accuracy;

	}

	private static String classify(String[] Record,
			ArrayList<DataRep> attributeList) {
		// TODO Auto-generated method stub
		String Result = "";
		String getFeatureVal = "";
		String One = "11";
		String Zero = "00";
		Integer posDenom = 0;
		Integer negDenom = 0;
		Double CondProdPos = 0.0;
		Double CondProdNeg = 0.0;
		HashMap<String, Integer> Map = new HashMap<String, Integer>();
		int length = Record.length - 1;
		ArrayList<Double> CondProbNeg = new ArrayList<Double>();
		ArrayList<Double> CondProbPos = new ArrayList<Double>();

		for (int i = 0; i < attributeList.size(); i++) {
			HashMap<String, Integer> temp = new HashMap<String, Integer>();
			// Get the Weight values for both the classes
			if (attributeList.get(i).getFeatureName() == "Target") {
				temp = attributeList.get(i).getMap();
				posDenom = temp.get(One);
				negDenom = temp.get(Zero);
				ArrayList<Double> Priors = attributeList.get(i).getPriors();
				CondProdPos = Priors.get(1);
				CondProdNeg = Priors.get(0);

				CondProbNeg.add(CondProdNeg);
				CondProbPos.add(CondProdPos);
				break;
			}
		}

		// Start Reading the test values
		for (int i = 0; i < length - 1; i++) {

			HashMap<String, Integer> temp = new HashMap<String, Integer>();
			String FeatureName = attributeList.get(i).getFeatureName();

			getFeatureVal = Record[i];

			temp = attributeList.get(i).getMap();

			int factor = applyLaplaceCorrection(attributeList.get(i)
					.getAllValues());
			if (getFeatureVal.equals("0")) {
				double prob = 0.0;
				if (!temp.containsKey("01"))
					prob = (double) 1.0 / (posDenom + factor);
				else {
					int num1 = temp.get("01");
					prob = (double) (num1 + 1) / (posDenom + factor);
				}
				CondProbPos.add(prob);
				CondProdPos = CondProdPos * prob;

				if (!temp.containsKey("00"))
					prob = (double) 1.0 / (negDenom + factor);
				else {
					int num1 = temp.get("00");
					prob = (double) (num1 + 1) / (negDenom + factor);
				}

				CondProbNeg.add(prob);
				CondProdNeg = CondProdNeg * prob;
			} else if (getFeatureVal.contains("1")) {
				double prob = 0.0;
				if (!temp.containsKey("11"))
					prob = (double) 1.0 / (posDenom + factor);
				else {

					int num1 = temp.get("11");
					prob = (double) (num1 + 1) / (posDenom + factor);
				}
				CondProbPos.add(prob);
				CondProdPos = CondProdPos * prob;

				if (!temp.containsKey("10"))
					prob = (double) 1.0 / (negDenom + factor);
				else {
					int num1 = temp.get("10");
					prob = (double) (num1 + 1) / (negDenom + factor);
				}

				CondProbNeg.add(prob);
				CondProdNeg = CondProdNeg * prob;
			} else if (getFeatureVal.contains("2")) {
				double prob = 0.0;
				if (!temp.containsKey("21"))
					prob = (double) 1.0 / (posDenom + factor);
				else {

					int num1 = temp.get("21");
					prob = (double) (num1 + 1) / (posDenom + factor);
				}
				CondProbPos.add(prob);
				CondProdPos = CondProdPos * prob;

				if (!temp.containsKey("20"))
					prob = (double) 1.0 / (negDenom + factor);
				else {
					int num1 = temp.get("20");
					prob = (double) (num1 + 1) / (negDenom + factor);
				}

				CondProbNeg.add(prob);
				CondProdNeg = CondProdNeg * prob;

			} else if (getFeatureVal.contains("4")) {
				double prob = 0.0;
				if (!temp.containsKey("41"))
					prob = (double) 1.0 / (posDenom + factor);
				else {

					int num1 = temp.get("41");
					prob = (double) (num1 + 1) / (posDenom + factor);
				}
				CondProbPos.add(prob);
				CondProdPos = CondProdPos * prob;

				if (!temp.containsKey("40"))
					prob = (double) 1.0 / (negDenom + factor);
				else {
					int num1 = temp.get("40");
					prob = (double) (num1 + 1) / (negDenom + factor);
				}

				CondProbNeg.add(prob);
				CondProdNeg = CondProdNeg * prob;

			} else if (getFeatureVal.contains("6")) {
				double prob = 0.0;
				if (!temp.containsKey("61"))
					prob = (double) 1.0 / (posDenom + factor);
				else {

					int num1 = temp.get("61");
					prob = (double) (num1 + 1) / (posDenom + factor);
				}
				CondProbPos.add(prob);
				CondProdPos = CondProdPos * prob;

				if (!temp.containsKey("60"))
					prob = (double) 1.0 / (negDenom + factor);
				else {
					int num1 = temp.get("60");
					prob = (double) (num1 + 1) / (negDenom + factor);
				}

				CondProbNeg.add(prob);
				CondProdNeg = CondProdNeg * prob;

			} else if (getFeatureVal.contains("8")) {
				double prob = 0.0;
				if (!temp.containsKey("81"))
					prob = (double) 1.0 / (posDenom + factor);
				else {

					int num1 = temp.get("81");
					prob = (double) (num1 + 1) / (posDenom + factor);
				}
				CondProbPos.add(prob);
				CondProdPos = CondProdPos * prob;

				if (!temp.containsKey("80"))
					prob = (double) 1.0 / (negDenom + factor);
				else {
					int num1 = temp.get("80");
					prob = (double) (num1 + 1) / (negDenom + factor);
				}

				CondProbNeg.add(prob);
				CondProdNeg = CondProdNeg * prob;
			}

		}

		if (CondProdNeg > CondProdPos)
			return "0";
		else
			return "1";

	}

	public static ArrayList<DataRep> calculateConditionalProb(
			ArrayList<DataRep> attributeList) {

		HashMap<String, Integer> countMap = new HashMap();
		for (int i = 0, j = attributeList.size() - 1; i < attributeList.size(); i++) {
			ArrayList<String> feature = attributeList.get(i).getAllValues();
			ArrayList<String> targetList = attributeList.get(j).getAllValues();
			countMap = getCount(feature, targetList);

			attributeList.get(i).setMap(countMap);

		}
		return attributeList;
	}

	private static HashMap<String, Integer> getCount(ArrayList<String> feature,
			ArrayList<String> targetList) {
		HashMap<String, Integer> outerMap = new HashMap<String, Integer>();

		int count = 0;
		ArrayList<String> temp = new ArrayList();
		for (int i = 0; i < feature.size(); i++) {
			// HashMap<String, Integer> innerMap = new HashMap();
			temp.add(feature.get(i) + targetList.get(i));

		}

		for (int i = 0; i < temp.size(); i++) {
			if (!outerMap.containsKey(temp.get(i))) {
				outerMap.put(temp.get(i), 1);
			} else {
				outerMap.put(temp.get(i), outerMap.get(temp.get(i)) + 1);
			}
		}

		return outerMap;

	}

	public static void printAttributeValues(ArrayList<DataRep> attributeList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < attributeList.size(); i++) {
			System.out.println(attributeList.get(i).getFeatureName());
			// System.out.println(attributeList.get(i).getAllValues());
			// System.out.println(attributeList.get(i).getPossibleValues());
			System.out.println(attributeList.get(i).getPriors());
			System.out.println(attributeList.get(i).getMap());
		}
	}

	public static ArrayList<DataRep> setAttributeValues(
			ArrayList<DataRep> attributeList) {
		// TODO Auto-generated method stub
		int length = completeDataset.get(0).length;

		for (int i = 0; i < length; i++) {
			DataRep DR = new DataRep();
			ArrayList<String> featureValues = new ArrayList();
			for (String[] s : completeDataset) {
				featureValues.add(s[i]);
			}
			if (i == length - 1) {
				DR.setFeatureName("Target");
				// DR.setPriors(calculatePriors(featureValues));
			} else
				DR.setFeatureName("F" + (i + 1));

			DR.setPossibleValues(getPossibleValues(featureValues, 0, 0));
			DR.setAllValues(featureValues);
			DR.setPriors(calculatePriors(featureValues));

			attributeList.add(DR);
		}
		return attributeList;
	}

	// Please specify the test data set and the training set
	public ArrayList<String[]> LoadData(int i) {
		ArrayList<String[]> CompleteDataset = new ArrayList();
		BufferedReader br = null;
		String line = "";
		String datapath = "";
		if (i == 0)
			datapath = "F:/ML-Course/zoo-train.csv";
		else
			datapath = "F:/ML-Course/zoo-test.csv";

		String split = ",";
		try {
			br = new BufferedReader(new FileReader(datapath));
			while ((line = br.readLine()) != null) {
				CompleteDataset.add(line.split(split));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return CompleteDataset;

	}

	public static ArrayList<Double> calculatePriors(
			ArrayList<String> featureValues) {
		// ArrayList<String> getTargetData = new ArrayList();
		// getTargetData = getTargetDistribution(completeDataset);
		HashMap<String, Integer> mapPriors = new HashMap<String, Integer>();

		ArrayList<Double> priors = new ArrayList();

		int count = 0;
		mapPriors = getPossibleValues(featureValues, count);

		Iterator it = mapPriors.entrySet().iterator();
		ArrayList<String> test = new ArrayList();
		while (it.hasNext()) {
			Map.Entry pairs = (Entry) it.next();
			test.add((String) pairs.getKey());
			int numerator = (int) pairs.getValue();
			int denominator = featureValues.size()
					+ applyLaplaceCorrection(featureValues);

			double prior = (double) (numerator + 1) / denominator;

			priors.add(prior);

		}

		return priors;

	}

	public static int applyLaplaceCorrection(ArrayList<String> getTargetData) {
		// TODO Auto-generated method stub
		int factor = 0;

		factor = getPossibleValues(getTargetData);

		return factor;
	}

	public static HashMap<String, Integer> getPossibleValues(
			ArrayList<String> getTargetData, int count) {
		HashMap<String, Integer> mapPriors = new HashMap();

		for (int i = 0; i < getTargetData.size(); i++) {
			if (!mapPriors.containsKey(getTargetData.get(i))) {

				mapPriors.put(getTargetData.get(i), 1);
			} else {
				count = mapPriors.get(getTargetData.get(i));
				count = count + 1;
				mapPriors.put(getTargetData.get(i), count);
			}
		}

		return mapPriors;

	}

	public static int getPossibleValues(ArrayList<String> getTargetData) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> mapIt = new HashMap();

		int count = 0;
		for (int i = 0; i < getTargetData.size(); i++) {
			if (!mapIt.containsKey(getTargetData.get(i))) {

				mapIt.put(getTargetData.get(i), 1);
			} else {
				count = mapIt.get(getTargetData.get(i));
				count = count + 1;
				mapIt.put(getTargetData.get(i), count);
			}
		}

		return mapIt.size();
	}

	public static ArrayList<String> getPossibleValues(ArrayList<String> temp,
			int i, int j) {
		int count = 0;
		HashMap<String, Integer> mapIt = new HashMap();
		ArrayList<String> pv = new ArrayList();
		for (int i1 = 0; i1 < temp.size(); i1++) {
			if (!mapIt.containsKey(temp.get(i1))) {

				mapIt.put(temp.get(i1), 1);
			} else {
				count = mapIt.get(temp.get(i1));
				count = count + 1;
				mapIt.put(temp.get(i1), count);
			}
		}

		Iterator it = mapIt.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pairs = (Entry) it.next();

			pv.add((String) pairs.getKey());
		}
		return pv;
	}

	public ArrayList<String> getTargetDistribution(
			ArrayList<String[]> completeDataset) {
		ArrayList<String> priorsOnly = new ArrayList();
		for (String[] s : completeDataset) {
			int length = s.length - 1;
			priorsOnly.add(s[length]);

		}

		return priorsOnly;

	}
}
