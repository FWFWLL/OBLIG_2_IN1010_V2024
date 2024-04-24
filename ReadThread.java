import java.io.File;

public class ReadThread extends Thread {
	private Monitor2 monitor;
	private File file;

	public ReadThread(Monitor2 monitor, File file) {
		this.monitor = monitor;
		this.file = file;
	}

	@Override
	public void run() {
		monitor.push(SubsequenceRegister.getSubsequencesFromFile(file));
		System.out.println("ReadThread " + threadId() + " Finished...");
	}
}
