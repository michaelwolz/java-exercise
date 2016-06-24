import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class DBLPHandler extends DefaultHandler {
	private String magazineTitle = null;
	private boolean inTag = false;
	private String data = "";
	private Map<String, Magazine> magazineList = new HashMap<String, Magazine>(); //Hashmap mit den Magazinnamen als Key und einem Magazine Objekt als Values
	
	@Override
	public void startDocument() throws SAXException
	{
		System.out.println("#####################################################################");
		System.out.println("Die XML Datei wird analyisert... Bitte haben Sie einen Moment Geduld!");
		System.out.println("#####################################################################\n\n");
	}

	@Override
	public void endDocument() throws SAXException
	{
		//Ausgeben des Gesamtergebnisses
		this.magazineList.entrySet()
			.stream()
			.forEach(e -> 
					e.getValue().generateOutput(e.getKey())
					);
	}

	@Override
	public void startElement(String nsURI, String localName, String qName, Attributes attrs) throws SAXException
	{	
		if (qName.equals("article")) {
			this.magazineTitle = attrs.getValue("key").split("/", 3)[1];
			this.addOrUpdateMagazineList(this.magazineTitle);
		} else if (magazineTitle != null && (qName.equals("title") || 
				qName.equals("author") || qName.equals("year"))) {
			this.inTag = true;
		} 
	}

	@Override
	public void endElement(String nsURI, String localName, String qName)
	throws SAXException
	{
		if (qName.equals("article")) 
			this.magazineTitle = null;
		else if (qName.equals("title")) 
			this.addArticleTitleToMagazine(this.magazineTitle, this.data);
		else if (qName.equals("author")) 
			this.addAuthor(this.magazineTitle, this.data);
		else if (qName.equals("year")) 
			this.addYear(this.magazineTitle, this.data);
		
		this.inTag = false;
		this.data = "";
	}

	@Override
	public void characters(char[] ch, int start, int length)
	throws SAXException
	{
		if (this.inTag) //Dies ist notwendig, da characters nicht garanitiert, dass eine Zeile auch als eine einzige eingelesen wird und es hier vorallem Probleme mit den HTML Entities gab
			this.data += new String(ch, start, length);
	}
	
	public void addOrUpdateMagazineList(String magazineTitle) {
		Magazine tmp = this.magazineList.get(magazineTitle); 
		if (tmp == null) { //Wenn es das Magazin noch nicht gibt: lege es an. Sonst einfach den count erhöhen
			this.magazineList.put(magazineTitle, new Magazine());
		} else {
			tmp.incrCount();
		}
	}
	
	public void addAuthor(String magazineTitle, String author) {
		Magazine tmp = this.magazineList.get(magazineTitle);
		
		if (tmp != null) 
			tmp.addOrUpdateAuthor(author);
	}
	
	public void addArticleTitleToMagazine(String magazineTitle, String articleTitle) {
		Magazine tmp = this.magazineList.get(magazineTitle);
		
		if (tmp != null) 
			tmp.addStringToWordlist(articleTitle);
	}
	
	public void addYear(String magazineTitle, String year) {
		Magazine tmp = this.magazineList.get(magazineTitle);
		if (tmp != null)
		try {
			int yearAsInt = Integer.parseInt(year);
			tmp.addYear(yearAsInt);
			
		} catch(NumberFormatException e) {
			System.err.println("Error while parsing Year to Int");
		}
	}
}