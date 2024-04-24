public class Subsequence {
	public final String substring;

	private int occurences = 1;

	public Subsequence(String s) {
		this.substring = s;
	}

	public int getOccurences() {
		return occurences;
	}

	public Subsequence add(Subsequence rhs) {
		this.occurences += rhs.occurences;
		return this;
	}

	@Override
	public String toString() {
		return "(" + substring + ", " + occurences + ")";
	}
}
