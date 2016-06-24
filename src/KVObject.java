/*
	Key-Value Objekt, welches sich dann später über compareTo vergleichen lässt und somit im Stream über die sorted() Funktion sortiert werden kann
*/

public class KVObject implements Comparable<KVObject> {		
	private String name;
	private int	   count;

	KVObject(String name) {
		this.name = name;
		count = 1;
	}
	
	public void add() {
		this.count++;
	}
	
	@Override
	public int compareTo(KVObject o2) {
		if (this.count == o2.count)
			return this.name.compareTo(o2.name);
		return o2.count - this.count;
	}
	
	public String toString() {
		return name + ": " + count;
	}
}