import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class Magazine {
	private Map<String, KVObject> authors = new HashMap<String, KVObject>();
	private Map<String, KVObject> wordlist = new HashMap<String, KVObject>();
	private TreeSet<Integer> years = new TreeSet<Integer>();

	private int count = 1;

	public void incrCount() {
		this.count++;
	}

	public void addStringToWordlist(String articleTitle) {
		String[] words = articleTitle.split(" ");
		
		for (String word: words) {
			KVObject i;
			word = word.trim();
			word = word.replaceAll("[\\:\\=\\,\\;\\.]",""); //Remove unnecessary characters
			if (!word.isEmpty()) {
				i = this.wordlist.get(word);
				if (i != null) 
					i.add();
				else
					i = new KVObject(word); 
				this.wordlist.put(word, i);
			}
		}
	}

	public void addOrUpdateAuthor(String name) {
		KVObject i = this.authors.get(name);

		if (i != null) 
			i.add();
		else 
			i = new KVObject(name); 
		this.authors.put(name, i);
	}

	public void addYear(int year) {
		if (!this.years.contains(year))
			this.years.add(year);
	}

	public void printYearsInfo() {
		System.out.println("\nZuerst erschienen: " + this.years.first());
		System.out.println("Zuletzt erschienen: " + this.years.last());
		System.out.println("Zeitliche Lücken: ");
		if (this.years.size() < this.years.last() - this.years.first()) {
			Iterator<Integer> itr = this.years.iterator();
			int prev = itr.next();
			int next = 0;
			while(itr.hasNext()) {
				next = itr.next();
				if (prev != next - 1) 
					System.out.println("  - " + prev + " bis " + next);
				prev = next;
			}
		} else
			System.out.println("  - keine");
	}

	public void printTopAuthors() {
		System.out.println("\n*** Top Autoren (Autor: #Artikel) ***");
		this.authors.entrySet()
			.parallelStream()
			.sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
			.limit(20)
			.forEach(
					e -> System.out.println(e.getValue())
					);
	}

	public void printTopWordCloud() {		
		System.out.println("\n*** Wordcloud ***");		
		this.wordlist.entrySet()
			.parallelStream()
			.sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
			.limit(20)
			.forEach(
					e -> System.out.println(e.getValue())
					);
	}

	public void generateOutput(String name) {
		if (this.count >= 500) {
			System.out.println("#### Magazin: " + name  + " ####");
			System.out.println("\n*** Veröffentlichte Artikel: " + this.count); 
			this.printYearsInfo();
			this.printTopAuthors();
			this.printTopWordCloud();
			System.out.println("\n----------------------------- \n\n");
		}
	}
}