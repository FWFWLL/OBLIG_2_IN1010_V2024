import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
	private final static int MERGE_THREADS = 8;

	private static SubsequenceRegister registerInfected = new SubsequenceRegister();
	private static SubsequenceRegister registerHealthy = new SubsequenceRegister();
	private static Monitor2 monitorInfected = new Monitor2(registerInfected);
	private static Monitor2 monitorHealthy = new Monitor2(registerHealthy);

	public static void main(String[] args) {
		if(args.length < 1) {
			System.err.println("Usage: java Main [DATASET]");
		}

		String directory = args[0];

		// Get all files from the `metadata.csv` file
		LinkedList<File> filesInfected = new LinkedList<>();
		LinkedList<File> filesHealthy = new LinkedList<>();
		try(Scanner sc = new Scanner(new File(directory + "/metadata.csv"))) {
			for(String line = sc.nextLine(); line != null; line = sc.nextLine()) {
				String[] metainfo = line.trim().split(",");

				if(metainfo[1].equals("True")) {
					filesInfected.push(new File(directory + "/" + metainfo[0]));
				} else {
					filesHealthy.push(new File(directory + "/" + metainfo[0]));
				}
			}
		} catch(NoSuchElementException e) {
		} catch(FileNotFoundException e) {
			System.err.println("`metadata.csv` not found in `" + directory + "`");
			System.exit(1);
		}

		// 1. Create the thread
		// 2. Store the thread
		// 3. Run the thread
		LinkedList<ReadThread> readThreads = new LinkedList<>();
		filesInfected.forEach((file) -> {
			ReadThread thread = new ReadThread(monitorInfected, file);
			readThreads.push(thread);
			thread.start();
		});

		// Same as above
		filesHealthy.forEach((file) -> {
			ReadThread thread = new ReadThread(monitorHealthy, file);
			readThreads.push(thread);
			thread.start();
		});

		// Join the all ReadThreads
		readThreads.forEach((thread) -> {
			try {
				thread.join();
			} catch(InterruptedException e) {
				System.err.println("Thread was interrupted!");
			}
		});

		System.out.println("Finished reading files...");

		// Create 8 threads for merging per monitor
		LinkedList<MergeThread> mergeThreads = new LinkedList<>();
		for(int i = 0; i < MERGE_THREADS; i++) {
			MergeThread thread = new MergeThread(monitorInfected);
			mergeThreads.push(thread);
			thread.start();

			thread = new MergeThread(monitorHealthy);
			mergeThreads.push(thread);
			thread.start();
		}

		// Join all the MergeThreads
		mergeThreads.forEach((thread) -> {
			try {
				thread.join();
			} catch(InterruptedException e) {
				System.err.println("Thread was interrupted!");
			}
		});

		System.out.println("Finished merging subsequences...");

		var infected = registerInfected.pop();
		var healthy = registerHealthy.pop();
		for(Subsequence sequenceA : infected.values()) {
			Subsequence sequenceB = null;
			for(Subsequence _sequenceB : healthy.values()) {
				if(sequenceA.substring.equals(_sequenceB.substring)) {
					sequenceB = _sequenceB;
				}
			}

			if (sequenceB != null && sequenceA.getOccurences() >= sequenceB.getOccurences() + 7) {
				System.out.println(sequenceA + " ---- " + sequenceB);
			} else if (sequenceB == null && sequenceA.getOccurences() >= 7) {
				System.out.println(sequenceA);
			}
		}
	}
}
