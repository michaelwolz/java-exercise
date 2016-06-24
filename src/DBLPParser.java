/* Java **********************************************************************
**
** Author: Michael Wolz
** Matrikelnummer: 1195270
**
** Author: Aaron Winziers
** Matrikelnummer: 1176638
**
******************************************************************************/

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class DBLPParser {

	public static void main(String[] args)
	{

		/**
			Einlesen der XML-Datei und initialisieren, sowie starten des SAX-Parsers
		**/

		System.setProperty("entityExpansionLimit", "2500000");
		
		DBLPHandler handler;
		
		if (args.length != 1) {
			System.err.println("usage: java DBLPParser <path/to/file.xml>");
			System.exit(1);
		}
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
			
		try {
			InputStream xmlInput= new FileInputStream(args[0]);

			SAXParser saxParser = factory.newSAXParser();
			handler = new DBLPHandler();
			
            saxParser.parse(xmlInput, handler);
            
		}  catch (IOException e) {
            System.out.println("Error reading URI: " + e.getMessage());
        } catch (SAXException e) {
            System.out.println("Error in parsing: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println("Error in XML parser configuration: "
                    + e.getMessage());
        }
	}
}
