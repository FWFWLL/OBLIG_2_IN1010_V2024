import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class Main {
	private final static String DIR = "./Datasets/TestDataLike/";

	private static SubsequenceRegister register = new SubsequenceRegister();
	private static Monitor1 monitor = new Monitor1(register);

	public static void main(String[] args) {
		LinkedList<File> files = new LinkedList<>();
		try(Scanner sc = new Scanner(new File(DIR + "metadata.csv"))) {
			for(String line = sc.nextLine(); line != null; line = sc.nextLine()) {
				String filename = line.trim().split(",")[0];
				files.push(new File(DIR + filename));
			}
		} catch(NoSuchElementException e) {
		} catch(FileNotFoundException e) {
			System.err.println("`metadata.csv` not found in `" + DIR + "`");
			System.exit(1);
		}

		// 1. Create the thread
		// 2. Store the thread
		// 3. Run the thread
		LinkedList<ReadThread> readThreads = new LinkedList<>();
		files.forEach((file) -> {
			ReadThread thread = new ReadThread(monitor, file);
			readThreads.push(thread);
			thread.start();
		});

		// Join the threads
		readThreads.forEach((thread) -> {
			try {
				thread.join();
			} catch(InterruptedException e) {
				System.err.println("Thread was interrupted!");
			}
		});

		while(monitor.size() > 1) {
			monitor.push(SubsequenceRegister.mergeSubsequenceMaps(monitor.pop(), monitor.pop()));
		}

		Optional<Subsequence> subsequence = monitor
			.pop()
			.values()
			.stream()
			.max((subsequenceA, subsequenceB) -> Integer.compare(subsequenceA.getOccurences(), subsequenceB.getOccurences()));

		if(subsequence.isPresent()) {
			System.out.println(subsequence.get());
		}
	}
}
