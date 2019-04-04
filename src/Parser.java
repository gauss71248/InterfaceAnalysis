import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Diese Klasse bildet die Schnittstelle zur Außenwelt, i.e. es ermöglicht das Einlesen
 * der als Textdatei übergebenen Matrix, die die zu analysierende Relation beschreibt.
 * Es wird mit der öffentlichen Funktion eine Relation mit system_elementen generiert.
 * 
 * @author gauss
 * @version 0.1
 * @see system_element
 * @see Relation
 */
public class Parser {

	/**
	 * Die Methode liest die an dem als Parameter übergebenen Pfad als Textdatei hinterlegte Matrix
	 * ein und generiert daraus eine Relation. Die Matrix muss dabei über folgende Form verfügen: <br>
	 * - Die Spalten sind durch Kommata voneinander getrennt.<br>
	 * - Die Zeilen enden mit einem ].<br>
	 * - In der ersten Spalte stehen die ID der SinN.<br>
	 * - In der zweiten Spalte stehen die Kurzbezeichnungen der SinN.<br>
	 * - Ab der dritten Spalte steht entweder 0,1,2 oder 3. Die Zahlen ungleich 0 wurden in
	 * der Ablöseplanung definiert, die Zahl 0 steht für kein Eintrag in der Ursprungsmatrix.
	 * @param path Dies ist der Pfad, an dem die Textdatei, die die Relation definiert, zu suchen ist.
	 * @return Es wird eine Relation erzeugt, die durch die übergebene Matrix definiert wird.
	 */
	public static Relation parse(String path) {
		String [][] matrix=getMatrix(readInput(path));
        
		TreeMap elements = new TreeMap();
		HashSet edges = new HashSet();
		TreeMap adjazenzen = new TreeMap();
		
		for(int i=0;i<matrix.length;i++){
			
			
			elements.put(new Integer(i+1), new system_element(new String(matrix[i][0]),new String(matrix[i][1])));
			edges=new HashSet();
			
					
				
			for(int j=2;j<matrix.length+2;j++){
				
				if(Integer.valueOf(matrix[i][j]).equals(new Integer(1))){
					//j zählt von 2 (in Spalte 0 steht die ID Nr., in Spalte 1 steht die Bezeichnung
					//des Verfahrens.
					//wenn das aktuelle Verfahren mit einem inzidiert (also=1), dann trage das Verfahren
					//(Identifikation über Spaltenzahl minus 1) ein
					edges.add(new Integer(j-1));
				}
				if(Integer.valueOf(matrix[i][j]).equals(new Integer(3))){
					edges.add(new Integer(j-1));
					if(adjazenzen.containsKey(new Integer(j-1))) {
						HashSet temp=((HashSet)adjazenzen.get(new Integer(j-1)));
						temp.add(new Integer(i+1));
						adjazenzen.put(new Integer(j-1), temp);
					}else{
						HashSet temp=new HashSet();
						temp.add(new Integer(i+1));
						adjazenzen.put(new Integer(j-1), temp);
											
					}
				}
			}
			
			//Nach der inneren for-Schleife sind also alle Knoten bekannt, die mit unserem 
			//aktuellen Knoten i+1 inzidieren. Diese müssen nun nur noch zur Adjazenzenliste
			//hinzugefügt werden.
			//vorher noch abfragen, ob schon was drinsteht...
			if(adjazenzen.containsKey(new Integer(i+1))){
				HashSet temp=((HashSet)adjazenzen.get(new Integer(i+1)));
				temp.addAll(edges);
				adjazenzen.put(new Integer(i+1),temp);
			}else{
				adjazenzen.put(new Integer(i+1), edges);
			}
			
		}
		return new Relation(elements,adjazenzen);		
	}
	/**
	 * Die Methode liest eine Textdatei am angegebenen Pfad ein und gibt den vollständigen Inhalt als String zurück.
	 * @param path Dies ist der Pfad der einzulesenden Datei.
	 * @return Es wird der Inhalt der Datei als String zurückgegeben.
	 */
	public static String readInput(String path) {
		String input="";
		String row="";
		try{
			BufferedReader stream = new BufferedReader(new FileReader(new File(path)));
			while(row!=null){
				input=input+row;
				row=stream.readLine();
			}
		}catch(Exception e) {
			System.out.println("Problem beim Einlesen der Datei! Relation kann nicht initialisiert werden!");
		}
		return input;
	}
	/**
	 * Die Methode erhält den Inhalt der Matrix in der in der öffentlichen Methode definierten Form 
	 * und nutzt die Trennsymbole, um eine Matrix in Form eines Array von Arrays zu generieren. 
	 * @see #parse(String)
	 * @param s Dies ist der Inhalt der eingelesenen Matrix.
	 * @return Es wird ein Array von Arrays von Strings zurückgegeben, der die Matrix in nunmehr verwendbarer Weise repräsentiert.
	 */
	public static String[][] getMatrix(String s) {
        String[] rows=s.split("]");
		
		String [][] matrix=new String[rows.length][rows.length+2];
		for(int i=0;i<rows.length;i++){
			matrix[i]=rows[i].split(",");
			
		}
		return matrix;
	}
	
	public static Relation parse_directly(String toparse) {
		String [][] matrix=getMatrix(toparse);
        
		TreeMap elements = new TreeMap();
		HashSet edges = new HashSet();
		TreeMap adjazenzen = new TreeMap();
		
		for(int i=0;i<matrix.length;i++){
			
			
			elements.put(new Integer(i+1), new system_element(new String(matrix[i][0]),new String(matrix[i][1])));
			edges=new HashSet();
			
					
				
			for(int j=2;j<matrix.length+2;j++){
				
				if(Integer.valueOf(matrix[i][j]).equals(new Integer(1))){
					//j zählt von 2 (in Spalte 0 steht die ID Nr., in Spalte 1 steht die Bezeichnung
					//des Verfahrens.
					//wenn das aktuelle Verfahren mit einem inzidiert (also=1), dann trage das Verfahren
					//(Identifikation über Spaltenzahl minus 1) ein
					edges.add(new Integer(j-1));
				}
				if(Integer.valueOf(matrix[i][j]).equals(new Integer(3))){
					edges.add(new Integer(j-1));
					if(adjazenzen.containsKey(new Integer(j-1))) {
						HashSet temp=((HashSet)adjazenzen.get(new Integer(j-1)));
						temp.add(new Integer(i+1));
						adjazenzen.put(new Integer(j-1), temp);
					}else{
						HashSet temp=new HashSet();
						temp.add(new Integer(i+1));
						adjazenzen.put(new Integer(j-1), temp);
											
					}
				}
			}
			
			//Nach der inneren for-Schleife sind also alle Knoten bekannt, die mit unserem 
			//aktuellen Knoten i+1 inzidieren. Diese müssen nun nur noch zur Adjazenzenliste
			//hinzugefügt werden.
			//vorher noch abfragen, ob schon was drinsteht...
			if(adjazenzen.containsKey(new Integer(i+1))){
				HashSet temp=((HashSet)adjazenzen.get(new Integer(i+1)));
				temp.addAll(edges);
				adjazenzen.put(new Integer(i+1),temp);
			}else{
				adjazenzen.put(new Integer(i+1), edges);
			}
			
		}
		return new Relation(elements,adjazenzen);		
	}
	
	
	public static Relation parse_clustering(Relation rel, String path, int mode) {
		String[][] s=Parser.getMatrix(Parser.readInput(path));
		if(mode==0) {
			return rel.clustering(Parser.parseMinimalClusters(rel,s));
		}
		if(mode==1) {
			return rel.clustering(Parser.cleanUp(Parser.merge(Parser.parseClusterExtensions(rel,s),Parser.parseMinimalClusters(rel,s))));
		}else
			return null;
	}
	/*ab hier alles private*/
	private static TreeMap parseIDs(String[][] matrix) {
		TreeMap result = new TreeMap();
		for(int i=0;i<matrix.length;i++) {
			result.put(new Integer(i), matrix[i][0]);
		}
		return result;
	}
	/*Achtung! Noch fehlerhaft!*/
	public static HashSet parseClusterExtensions(Relation rel, String[][] matrix) {
		HashSet result = new HashSet();
		TreeMap ID = Parser.parseIDs(matrix);
		for(int i=0;i<matrix.length;i++) {
			String aktID = matrix[i][0];
			HashSet fusion = new HashSet();
			Element toadd = rel.getElementByID(aktID);
			if(toadd!=null) {
				
				fusion.add(toadd);
				for(int j=4;j<matrix[0].length;j++) {
					if(matrix[i][j].equals("x")) {
						fusion.add(rel.getElementByID((String)ID.get(new Integer(j-4))));
					}
				}
				result.add(fusion);
			}
		}
		return result;
	}
	
	public static HashSet parseMinimalClusters(Relation rel, String[][] matrix) {
		HashSet partition=new HashSet();
		//ACHTUNG! Annahme: nur 12 Cluster (identifiziert mit Zahlen von 1 bis 12) werden gebildet
		for(int i=1;i<13;i++) {
			HashSet cluster = new HashSet();
			for(int j=0;j<matrix.length;j++) {
				if((new Integer(i)).toString().equals(matrix[j][3])) {
					Element toadd = rel.getElementByID(matrix[j][0]);
					if(toadd!=null) {
						cluster.add(toadd);
					}
				}
			}
			partition.add(cluster);
		}
		return partition;
	
	}
	
	public static HashSet merge(HashSet set1, HashSet set2) {
		HashSet result = new HashSet();
		Iterator it1=set1.iterator();
		boolean stopit = false;
		while(it1.hasNext()) {
			HashSet aktSet1=(HashSet)it1.next();
			Iterator setIt = aktSet1.iterator();
			while(setIt.hasNext()) {
				Element candidate = (Element)setIt.next();
				Iterator it2 = set2.iterator();
				while(it2.hasNext()) {
					HashSet aktSet2 = (HashSet)it2.next();
					if(aktSet2.contains(candidate)) {
						aktSet1.addAll(aktSet2);
						
						stopit=true;
						break;
					}
				}
				if(stopit) {
					break;
				}
			}
			result.add(aktSet1);
			stopit=false;
		}
		return result;
	}
	
	public static HashSet cleanUp(HashSet s) {
		Object[] s_array = s.toArray();
		
		for(int i=0;i<s_array.length;i++) {
			HashSet setOfInterest = (HashSet)s_array[i];
			Iterator candidates = setOfInterest.iterator();
			while(candidates.hasNext()) {
				Element candidate = (Element)candidates.next();
				for(int j=i+1;j<s_array.length;j++) {
					HashSet toCheck = (HashSet)s_array[j];
					if(toCheck.contains(candidate)) {
						setOfInterest.addAll(toCheck);
						s.remove(toCheck);
						if(Parser.mutuallyDisjoint(s)) {
							return s;
						}else
							return Parser.cleanUp(s);
					}
				}

			}
			


		}
		return s;
		
	}
	
	
	
	private static HashSet breachesDisjointness(HashSet set) {
		HashSet result = new HashSet();
		Object[] set_array = set.toArray();
		for(int i=0;i<set_array.length;i++) {
			for(int j=i+1;j<set_array.length;j++) {
				result.addAll(Parser.intersect((HashSet)set_array[i], (HashSet)set_array[j]));
			}
		}
		return result;
	}
	
	private static boolean mutuallyDisjoint(HashSet s)  {
		Object[] set_array = s.toArray();
		for(int i=0;i<set_array.length;i++) {
			for(int j=i+1;j<set_array.length;j++) {
				if((Parser.intersect((HashSet)set_array[i], (HashSet)set_array[j])).size()>0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static HashSet intersect(HashSet set1, HashSet set2) {
		HashSet result = new HashSet();
		Iterator it = set1.iterator();
		while(it.hasNext()) {
			Element akt = (Element)it.next();
			if(set2.contains(akt)) {
				result.add(akt);
			}
		}
		return result;
	}
}
