package dev.nara;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CoinFlipDistribution {

    private final int K;
    private final Map<Integer, Double> distribution;
    private final Map<Integer, List<List<String>>> combinationSets;

    public CoinFlipDistribution(int K) {
		if (K > 21) {
			System.err.println("Max K is 21! Switching to 21.");
			K = 21;
		}
        this.K = K;
        this.distribution = new HashMap<>();
        this.combinationSets = new HashMap<>();
        calculateDistribution();
    }

    /**
     * Mendapatkan distribusinya
     * */
    private void calculateDistribution() {
		final List<String> outcome = List.of("H", "T");
        final int jumlahSisi = outcome.size();
        final double probabilitasKejadian = 1.0 / jumlahSisi;

        for (int xCount = 0; xCount <= K; xCount++) {
            System.out.println("=== HEAD: " + xCount);
            long banyakPola = combine(K, xCount);
            System.out.println("n(X) = " + banyakPola);
            double probability = banyakPola * Math.pow(probabilitasKejadian, K);
            System.out.println("p(X) = " + probability);
            distribution.put(xCount, probability);

			// buat kombinasi untuk semua kemungkinan berjumlah x
            List<List<String>> combinations = generateCombinations(K, xCount);
            combinationSets.put(xCount, combinations);
        }
    }

    /**
     * Generate all possible combinations patterns of coin flips.
     */
    private List<List<String>> generateCombinations(int flips, int heads) {
        List<List<String>> result = new ArrayList<>();
        List<String> current = new ArrayList<>();

        generateCombinations(flips, heads, 0, current, result);
        return result;
    }

    /**
     * Recursive helper to generate each possible combination patterns given the current index to flips,
     * Setiap "depth" rekursif, {@param remainingHeads} dikurangi 1 hingga tidak tersisa.
     * */
    private void generateCombinations(int flips, int remainingHeads, int index, List<String> currCombination, List<List<String>> combinations) {
		// base case: index setara dengan jumlah flip
        if (index == flips) {
            if (remainingHeads == 0) {
                combinations.add(new ArrayList<>(currCombination));
            }
            return;
        }

        // Sisa kepala
        if (remainingHeads > 0) {
            currCombination.add("H");
            generateCombinations(flips, remainingHeads - 1, index + 1, currCombination, combinations);
            currCombination.remove(currCombination.size() - 1);
        }

        // Tambah Tail
        currCombination.add("T");
        generateCombinations(flips, remainingHeads, index + 1, currCombination, combinations);
        currCombination.remove(currCombination.size() - 1);
    }

    /**
     * Metode untuk melakukan proses kombinasi
     * */
    private long combine(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;

        k = Math.min(k, n - k);
        long result = 1;

        // perhitungan faktor
        for (int i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;
        }

        return result;
    }

    /**
     * Print combinations/patterns for each possible number of heads (X).
     */
    public void printCombinations() {
        for (int heads = 0; heads <= K; heads++) {
            System.out.println("\n--- Kembinasi dengan " + heads + " heads ---");
            List<List<String>> combinations = combinationSets.get(heads);
            for (List<String> combination : combinations) {
                System.out.println(combination);
            }
            System.out.println("Total kombinasi: " + combinations.size());
        }
    }

    public void plot() {
    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Distribusi pelemparan koin (K=" + K + ")");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        DistributionPanel panel = new DistributionPanel(distribution, K);
        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);

        frame.setVisible(true);
    });
}

	/* Test for class */
    public static void main(String[] args) {
        int K = 4;

        CoinFlipDistribution cfd = new CoinFlipDistribution(K);
        cfd.plot();
    }
}

class DistributionPanel extends JPanel {
    private final Map<Integer, Double> distribution;
    private final int K;

    public DistributionPanel(Map<Integer, Double> distribution, int K) {
        this.distribution = distribution;
        this.K = K;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int chartWidth = width - 2 * padding;
        int chartHeight = height - 2 * padding;

        g.drawLine(padding, height - padding, width - padding, height - padding);
        g.drawLine(padding, height - padding, padding, padding);

        int numTicks = 11; // From 0.0 to 1.0 in 0.1 increments
        for (int i = 0; i < numTicks; i++) {
            double value = i * 0.1;
            int y = height - padding - (int)(value * chartHeight);

            g.drawLine(padding - 5, y, padding, y);

            String label = String.format("%.1f", value);
            g.drawString(label, padding - 40, y + 5);
        }

        int barWidth = chartWidth / (K + 1);

        for (int heads = 0; heads <= K; heads++) {
            double probability = distribution.get(heads);
            int barHeight = (int) (probability * chartHeight);

            int x = padding + heads * barWidth;
            int y = height - padding - barHeight;

            g.setColor(new Color(70, 130, 180));
            g.fillRect(x, y, barWidth - 2, barHeight);

            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(heads), x + barWidth/2 - 5, height - padding + 15);

            String probText = String.format("%.4f", probability);
            g.drawString(probText, x + barWidth/2 - 15, y - 5);
        }

        g.drawString("Banyak Heads/Kepala (X)", width/2 - 50, height - 10);

        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(-Math.PI/2);
        g2d.drawString("Probability", -height/2 - 30, 20);

        g.setColor(Color.BLACK);
        g.drawString("Distribusi pelemparan " + K + " koin",
                    width/2 - 150, 20);
    }
}