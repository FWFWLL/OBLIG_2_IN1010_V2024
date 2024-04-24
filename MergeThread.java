import java.util.HashMap;
import java.util.LinkedList;

public class MergeThread extends Thread {
	private Monitor2 monitor;

	public MergeThread(Monitor2 monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {
		while(monitor.size() > 1) {
			LinkedList<HashMap<String, Subsequence>> pair = monitor.popPair();

			if(pair != null) {
				monitor.pushMerged(pair.pop(), pair.pop());
				System.out.println("MergeThread " + threadId() + " Finished...");
			}
		}
	}
}
