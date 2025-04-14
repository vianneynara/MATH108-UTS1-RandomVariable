package dev.nara;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		RandomVariableCalculator soal1 = new RandomVariableCalculator();
		System.out.println("/// SOAL 1: ///");
		System.out.println();
		soal1.run();

		Scanner sc = new Scanner(System.in);
		System.out.println();
		System.out.println("/// Soal 2 ///\n");
		System.out.print("Masukkan jumlah pelemparan koin: ");
		int K = sc.nextInt();
		System.out.println("Membuka plot...");

        final long startTime = System.currentTimeMillis();
		CoinFlipDistribution cfd = new CoinFlipDistribution(K);
        final long endTime = System.currentTimeMillis();

		cfd.plot();

        final long sTime = System.currentTimeMillis();
		cfd.printCombinations();
        final long eTime = System.currentTimeMillis();

        System.out.println("\nCalculation time taken: " + (endTime-startTime) + " ms");
        System.out.println("Printing time taken: " + (eTime-sTime) + " ms");
	}
}