import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class Main {
	private final static String DIR = "./Datasets/TestDataLike/";

	private static SubsequenceRegister register = new SubsequenceRegister();

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

		files.forEach((file) -> register.push(SubsequenceRegister.getSubsequencesFromFile(file)));

		while(register.size() > 1) {
			register.push(SubsequenceRegister.mergeSubsequenceMaps(register.pop(), register.pop()));
		}

		Optional<Subsequence> subsequence = register
			.pop()
			.values()
			.stream()
			.max((subsequenceA, subsequenceB) -> Integer.compare(subsequenceA.getOccurences(), subsequenceB.getOccurences()));

		if(subsequence.isPresent()) {
			System.out.println(subsequence.get());
		}
	}
}
