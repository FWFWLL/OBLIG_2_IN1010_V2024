import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

// Wrapper around our list of `Subsequenes`
public class SubsequenceRegister {
	private ConcurrentLinkedQueue<HashMap<String, Subsequence>> subsequencesMaps = new ConcurrentLinkedQueue<>();

	public SubsequenceRegister() {}

	public void push(HashMap<String, Subsequence> subsequences) {
		subsequencesMaps.add(subsequences);
	}

	public HashMap<String, Subsequence> pop() {
		return subsequencesMaps.remove();
	}

	public int size() {
		return subsequencesMaps.size();
	}

	public static HashMap<String, Subsequence> getSubsequencesFromFile(File file) {
		HashMap<String, Subsequence> temp = new HashMap<>();

		try(Scanner sc = new Scanner(file)) {
			for(String line = sc.nextLine(); line != null; line = sc.nextLine()) {
				if(line.length() < 3) {
					System.err.println("Invalid line found!");
					System.exit(1);
				}

				for(int i = 0; i < line.length() - 2; i++) {
					String substring = "" + line.substring(i, i + 3);
					temp.put(substring, new Subsequence(substring));
				}
			}
		} catch(NoSuchElementException e) {
		} catch(FileNotFoundException e) {
			System.err.println("`" + file.getName() + "` not found!");
		}

		return temp;
	}

	public static HashMap<String, Subsequence> mergeSubsequenceMaps(HashMap<String, Subsequence> mapA, HashMap<String, Subsequence> mapB) {
		mapB.forEach((key, value) -> mapA.merge(key, value, (oldVal, newVal) -> oldVal.add(newVal)));
		HashMap<String, Subsequence> temp = mapA;
		return temp;
	}
}
