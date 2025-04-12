package dev.nara;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RandomVariableCalculator {

	private final ArrayList<InputData> inputData;
	private double mu;

	RandomVariableCalculator() {
		this.inputData = new ArrayList<>(List.of(
			new InputData(1, 0.16),
			new InputData(2, 0.22),
			new InputData(3, 0.28),
			new InputData(4, 0.20),
			new InputData(5, 0.14)
		));
	}

	private LinkedList<ExpectedValueRow> calculateExpectedValue(boolean showTable) {
		double sumX = 0;
		double sumPX = 0;
		double sumXPX = 0;

		LinkedList<ExpectedValueRow> expectedValues = new LinkedList<>();

		/* Calculating the expected values for each X */

		for (InputData data : inputData) {
			final double xPx = data.getX() * data.getPX();
			sumX += data.getX();
			sumPX += data.getPX();
			sumXPX += xPx;
			expectedValues.add(new ExpectedValueRow(data.getX(), data.getPX(), xPx));
		}

		if (showTable) {
			/* Showing the results in table */

			StringBuilder sb = new StringBuilder();
			// header
			sb.append("TABEL EXPECTED VALUES\n");
			sb.append("| x | P(x) | x•P(x) |\n");
			sb.append("|---|------|--------|\n");

			for (var row : expectedValues) {
				sb.append(String.format("| %d | %4.2f | %6.2f |\n", row.x(), row.PX(), row.XPX()));
			}
			sb.append("|---|------|--------|\n");
			sb.append(String.format("| T | %4.2f | %6.2f |\n", sumPX, sumXPX));

			System.out.println(sb);
		}

		mu = sumXPX;
		return expectedValues;
	}

	private void calculateVariance() {
		double sumPX = 0;
		double sumVarianceTerms = 0;

		LinkedList<VarianceTermRow> varianceTerms = new LinkedList<>();

		/* Calculating the deviation, squared deviation, and variance term for each X */

		// verify miu (mean)/expected value is initialized
		if (mu == 0) {
			calculateExpectedValue(false);
		}

		for (InputData data : inputData) {
			sumPX += data.getPX();
			final double deviation = data.getX() - mu;
			final double sqDeviation = Math.pow(deviation, 2);
			final double varianceTerm = sqDeviation * data.getPX();
			sumVarianceTerms += varianceTerm;
			varianceTerms.add(new VarianceTermRow(data.getX(), data.getPX(), deviation, sqDeviation, varianceTerm));
		}

		StringBuilder sb = new StringBuilder();
		// header
		sb.append("TABEL VARIANCE TERMS\n");
		sb.append("| x | P(x) | x - mu | (x - u)^2 | ((x - u)^2)•P(x) | \n");
		sb.append("|---|------|--------|-----------|------------------|\n");

		for (var row : varianceTerms) {
			sb.append(String.format("| %d | %4.2f | %6.2f | %9.4f | %16.6f |\n",
				row.x(), row.PX(), row.deviation(), row.sqDeviation(), row.varianceTerm()));
		}
		sb.append("|---|------|--------|-----------|------------------|\n");
		sb.append(String.format("| %1s | %4.2f | %6s | %9s | %16.6f |\n",
			"", sumPX, "", "", sumVarianceTerms));

		final double variance = Math.sqrt(sumVarianceTerms);
		sb.append(String.format("Variance: σ = sqrt(%.6f) = %.2f", sumVarianceTerms, variance));

		System.out.println(sb);
	}

	public void run() {
		calculateExpectedValue(true);
		calculateVariance();
	}
}

record ExpectedValueRow(int x, double PX, double XPX) {
};

record VarianceTermRow(int x, double PX, double deviation, double sqDeviation, double varianceTerm) {
};

class InputData {

	private int x;
	private double PX;

	InputData() {
		this.x = 0;
		this.PX = 0;
	}

	InputData(int x, double PX) {
		this.x = x;
		this.PX = PX;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getPX() {
		return PX;
	}

	public void setPX(double PX) {
		this.PX = PX;
	}
}
