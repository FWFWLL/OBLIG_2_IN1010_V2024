import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor2 {
	private ReentrantLock lock = new ReentrantLock();
	private SubsequenceRegister register;

	public Monitor2(SubsequenceRegister register) {
		this.register = register;
	}

	public void push(HashMap<String, Subsequence> subsequences) {
		register.push(subsequences);
	}

	public HashMap<String, Subsequence> pop() {
		return register.pop();
	}

	public void pushMerged(HashMap<String, Subsequence> mapA, HashMap<String, Subsequence> mapB) {
		register.push(SubsequenceRegister.mergeSubsequenceMaps(mapA, mapB));
	}

	public LinkedList<HashMap<String, Subsequence>> popPair() {
		while(size() < 2) {
			try {
				System.out.println("Thread " + Thread.currentThread().threadId() + " Waiting...");
				Thread.sleep(1000);

				if(size() >= 1) {
					break;
				}

				throw new InterruptedException();
			} catch(InterruptedException e) {
				System.out.println("Thread " + Thread.currentThread().threadId() + " Terminating...");
			}
		}

		LinkedList<HashMap<String, Subsequence>> subsequences = new LinkedList<>();
		lock.lock();
		try {
			if(size() < 2) {
				return null;
			}

			subsequences.push(pop());
			subsequences.push(pop());
		} finally {
			lock.unlock();
		}

		return subsequences;
	}

	public int size() {
		return register.size();
	}
}
