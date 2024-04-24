import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor1 {
	ReentrantLock lock = new ReentrantLock();
	private SubsequenceRegister register;

	public Monitor1(SubsequenceRegister register) {
		this.register = register;
	}

	public void push(HashMap<String, Subsequence> subsequences) {
		lock.lock();
		try {
			register.push(subsequences);
		} finally {
			lock.unlock();
		}
	}

	public HashMap<String, Subsequence> pop() {
		return register.pop();
	}

	public int size() {
		return register.size();
	}
}
