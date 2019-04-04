import java.io.FileWriter;
import java.util.*;

/**
 * Die Klasse stellt eine Relation auf einer Menge dar.
 * @author Sebastian Schlesinger
 * @see Element
 * @version 0.1
 *
 */
public class Relation implements Cloneable {

	/** 
	 * Das Attribut trägt die Liste von Elementen der Menge - gleichzeitig wird durch die Abbildung
	 * eine Reihenfolge definiert. Wir erhalten also eine Abbildung lfd.Nr.->Elemente
	 */
	private TreeMap elements;
	/**
	 * Das Attribut beschreibt die Relation in Form einer Adjazenzliste. Die Listen, die den Nummern
	 * zugeordnet werden sind vom Typ HashSet. 
	 */
	private TreeMap entries;
	
	
	/**
	 * Der parameterlose Konstruktor initialisiert lediglich die Attribute.
	 */
	public Relation() {
		elements=new TreeMap();
		entries=new TreeMap();
		
	}
	/**
	 * Der Konstruktor belegt die Attribute mit den übergebenen Parametern.
	 * @param m Dies ist die TreeMap von Elementen der Menge, auf denen die Relation definiert ist.
	 * @param n Dies ist die TreeMap, die die Relation als Adjazenzliste definiert.
	 */
	public Relation(TreeMap m, TreeMap n) {
		elements=m;
		entries=n;
		
		
	}
	/**
	 * Die methode setzt die Liste der Elemente der Menge, auf denen die Relation definiert ist.
	 * @param m Dies ist die TreeMap von Elementen der Menge.
	 */
	public void set_elements(TreeMap m) {
		elements=m;
	}
	/**
	 * Die Methode gibt die TreeMap von Elementen der Menge, auf denen die Relation definiert ist, zurück.
	 * @return Dies ist die TreeMap von Elementen der Menge, auf denen die Relation definiert ist
	 */
	public TreeMap get_elements() {
		return elements;
	}
	/**
	 * Die Metode setzt die Adjazenzliste der Relation.
	 * @param m Dies ist die Adjazenzliste der Relation vom Typ TreeMap.
	 */
	public void set_entries(TreeMap m) {
		entries=m;
	}
	/**
	 * Die Methode gibt die Adjazenzliste der Relation zurück.
	 * @return Dies ist die Adjazenzliste der Relation vom Typ TreeMap.
	 */
	public TreeMap get_entries() {
		return entries;
	}
	
	/**
	 * Die Methode gibt die Anzahl der Elemente der Menge zurück, auf denen die Relation definiert ist.
	 * @return Die Anzahl der Elemente der Grundmenge, über die die Relation definiert ist.
	 */
	public int size() {
		return elements.size();
	}
	
	
	/**
	 * Die Methode löscht ein Element aus der Grundmenge und der Adjazenzliste der Relation.
	 * @param x Der Parameter identifiziert das zu löschende Element.
	 */
	public void delete (Integer x) {
		//Zunächst die Zeile löschen...
		entries.remove(x);
		//Nun die Spalte, d.h. für alle Schlüssel jeweils in der Liste das Element löschen...
		Iterator it=entries.keySet().iterator();
		while(it.hasNext()) {
			((HashSet)entries.get((Integer)it.next())).remove(x);
		}
		//Nun noch das Element der Grundmenge entfernen...
		elements.remove(x);
	
	}
	

	/**
	 * Die Methode setzt den Eintrag (x,y) in der Matrix (bzw. Relation).
	 * @param x Abszisse des Eintrags
	 * @param y Ordinate des Eintrags
	 */
	public void set_entry(Integer x, Integer y) {
		((HashSet)entries.get(x)).add(y);
	}
	
	/**
	 * Die Methode löscht den Eintrag (x,y) aus der Matrix (bzw. Relation).
	 * @param x Abszisse des Eintrags
	 * @param y Ordinate des Eintrags
	 */
	public void delete_entry(Integer x, Integer y) {
		HashSet set = (HashSet)entries.get(x);
		
		set.remove(y);
		entries.put(x, set);
	}
	/**
	 * Die Methode löscht eine Liste von Kanten aus der Relation.
	 * @param l die Liste der zu löschenden Kanten
	 */
	public void delete_entries(HashSet l) {
		Iterator it=l.iterator();
		while(it.hasNext()) {
			edge akt = (edge)it.next();
			this.delete_entry(this.getIndex(akt.get_origin()), this.getIndex(akt.get_target()));
		}
	}
	/**
	 * Die Methode prüft, ob (x,y) in der Relation ist, d.h. (x,y) in der Matrix existiert.
	 * @param x Abszisse des zu prüfenden Elementes
	 * @param y Ordinate des zu prüfenden Elementes
	 * @return true genau dann, wenn (x,y) in der Relation ist.
	 */
	public boolean isRelated(Integer x, Integer y) {
		if(!entries.containsKey(x)) {
			return false;
		}
		return ((HashSet)entries.get(x)).contains(y);
	}
	/**
	 * Die Methode prüft für jedes Element der Liste, ob es mit dem übergebenen Element in Relation steht.
	 * Sobald sie eines findet, wird dieses zurückgegeben.
	 * @param e das Urpsrungselement
	 * @param s die Liste der Elemente, mit denen das Ursprungselement potenziell in Relation steht
	 * @return das erste Element, das mit dem Ursprungselement in Relation steht
	 */
	public Element choose_Element_connection_exists_with(Element e, HashSet s) {
		Iterator it = s.iterator();
		Integer index_e = this.getIndex(e);
		
		while(it.hasNext()) {
			Element akt = (Element)it.next();
			Integer index_akt = this.getIndex(akt);
			if(this.isRelated(index_e, index_akt)) {
				return akt;
			}
		}
		return null;
	}
	
	/**
	 * Die Methode führt die übergebene Relation nach dieser aus.<br>
	 * Voraussetzung (wird nicht überprüft): Die beteiligten Relationen sind über derselben Menge definiert.
	 * 
	 * @param m die zu multiplizierende Matrix 
	 */
	public Relation mult(Relation m) {
		
		//entries_new wird das Produkt. In dieser Matrix existiert ein Eintrag (x,y) genau dann, wenn
		//in der ersten Matrix (x,z) und in der zweiten Matrix (z,y) existiert.
		TreeMap entries_new=new TreeMap();
		//itKeys trägt die x-Koordinaten der ersten Matrix...
		Iterator itKeys=entries.keySet().iterator();
		while(itKeys.hasNext()) {
			Integer aktKey=(Integer)itKeys.next();
			//new_ordinates ist der Träger der y-Koordinaten, die x zugeordnet werden
			HashSet new_ordinates=new HashSet();
			//abszissen trägt die potentiellen z für (z,y)
			Iterator abszissen=((HashSet)entries.get(aktKey)).iterator();
			while(abszissen.hasNext()) {
				//aktz ist der aktuell zu prüfende z-Wert
				Integer aktz=(Integer)abszissen.next();
				//In ordinaten sind die zu z gehörigen y-Koordinaten.
				Iterator ordinaten=((HashSet)m.get_entries().get(aktz)).iterator();
				while(ordinaten.hasNext()) {
					//y wird in die zu x gehörigen Liste eingefügt...
					new_ordinates.add((Integer)ordinaten.next());
				}
			}
			entries_new.put(aktKey, new_ordinates);
			
		}
		return new Relation(elements, entries_new);
		
	}
	
	/**
	 * Die Methode prüft, ob die Mengen, auf denen die Relationen definiert sind, gleich sind (dieselben Elemente in derselben Reihenfolge).
	 * @param m Dies ist die Relation, mit der verglichen wird.
	 * @return true genau dann, wenn die Mengen in gleicher Reihenfolge mit identischen Elementen vorliegen.
	 */
	public boolean equalityOfSet(Relation m) {
		
		//Erstmal prüfen, ob die Matrizen gleich groß sind...
		if(m.size()!=size()) {
			return false;
		}
		//Die Elemente müssen gleich sein...
		Iterator it=m.get_entries().keySet().iterator();
		while(it.hasNext()) {
			Integer index=(Integer)it.next();
			Element akt_m = (Element)m.get_elements().get(index);
			Element akt=(Element)elements.get(index);
			if(!akt.equals(akt_m)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Die Methode prüft, ob die Relation mit der übergebenen übereinstimmt, also alle Werte identisch sind und in derselben Reihenfolge abgelegt wurden.
	 * @param m die zu vergleichende Matrix
	 * @return true genau dann, wenn die Matrizen gleich sind.
	 */
	public boolean equals(Relation m) {
		
		
		//Passen die Matrizen?
		if(!this.equalityOfSet(m)) {
			return false;
		}
		
		//Zuletzt die Prüfung, ob die Einträge gleich sind.
		//Dies erfolgt wie der Nachweis der Gleichheit von Mengen
		
		Iterator it=entries.keySet().iterator();
		while(it.hasNext()) {
			Integer akt=(Integer)it.next();
			Iterator innerit=((HashSet)entries.get(akt)).iterator();
			while(innerit.hasNext()) {
				Integer innerakt=(Integer)innerit.next();
				if(!(((HashSet)m.get_entries().get(akt)).contains(innerakt))) {
					return false;
				}
			}
		}
		
		it=m.get_entries().keySet().iterator();
		while(it.hasNext()) {
			Integer akt=(Integer)it.next();
			Iterator innerit=((HashSet)m.get_entries().get(akt)).iterator();
			while(innerit.hasNext()) {
				Integer innerakt=(Integer)innerit.next();
				if(!(((HashSet)entries.get(akt)).contains(innerakt))) {
					return false;
				}
			}
		}
		
		return true;
		
	}
	

	/**
	 * Die Methode vereinigt die Relation mit der übergebenen (sofern passend), d.h. ein Eintrag in der resultierenden Matrix
	 * existiert genau dann, wenn an der Stelle in mindestens einer der beiden Matrizen ein Eintrag war.
	 * @param m zu vereinigende Relation
	 * @return die Vereinigung der Relationen
	 */
	public Relation union(Relation m) {
		
		//Passen die Matrizen zusammen?
		if(!this.equalityOfSet(m)) {
			return null;
		}
		
			
		//Jetzt kommt die eigentliche Vereinigung...
		TreeMap new_entries=new TreeMap();
		Iterator it=m.get_entries().keySet().iterator();
		while(it.hasNext()) {
			Integer aktKey=(Integer)it.next();
			HashSet input=new HashSet((HashSet)entries.get(aktKey));
			input.addAll((HashSet)m.get_entries().get(aktKey));
			new_entries.put(aktKey, input);
		}
		
		return new Relation(elements,new_entries);
		
	}
	/*
	 * **********************************************************************
	 * Es folgen die Methoden zur Generierung der reflexiven und transitiven
	 * Hüllen sowie deren jeweilige Umkehr.
	 * **********************************************************************
	 */
	/**
	 * Die Methode bildet die reflexive Hülle der Relation.
	 * @return Dies ist die reflexive Hülle der Relation.
	 */
	public Relation reflexive_hull() {
		TreeMap result_entries=(TreeMap)entries.clone();
		Iterator it=result_entries.keySet().iterator();
		while(it.hasNext()) {
			Integer akt=(Integer)it.next();
			((HashSet)result_entries.get(akt)).add(akt);
		}
		return new Relation(elements,result_entries);
	}
	
	/**
	 * Die Methode berechnet die transitive Hülle der Relation.
	 * @return Dies ist die transitive Hülle der Relation.
	 */
	public Relation transitive_hull() {
		Relation first=(Relation)this.clone();
		Relation result=first;
		Relation next=first.mult(first);
		while(!result.equals(result.union(next))) {
			result=result.union(next);
			next=result.mult(first);
		}
		return result;
	}
	
	public Relation transitive_development(int n) {
		Relation first=(Relation)this.clone();
		Relation result=first;
		
		for(int i=0;i<n;i++) {
			Relation next=result.mult(first);
			result=result.union(next);
			
		}
		return result;
	}
	
	/**
	 * Die Methode entfernt alle Elemente (x,x) aus der Relation.
	 * @return Dies ist die resultierende, irreflexive Relation.
	 */
	public Relation reflexive_reduction() {
		TreeMap result_entries=(TreeMap)entries.clone();
		Iterator it=result_entries.keySet().iterator();
		while(it.hasNext()) {
			Integer akt=(Integer)it.next();
			((HashSet)result_entries.get(akt)).remove(akt);
		}
		return new Relation(elements, result_entries);
	}
	/**
	 * Die Methode bildet diejenige Relation, die entsteht, wenn alle Beziehungen <code>(x,y)</code> entfernt werden,
	 * sofern ein <code>z</code> mit <code>(x,z)</code> und <code>(z,y)</code> in Relation existiert.
	 * ACHTUNG! Zuvor wird die Relation irreflexiv gemacht, da dies für die Anwendung genügt und so die (umständliche) Prüfung entfallen kann,
	 * ob <code>z=x</code> oder <code>z=y</code>.<br>
	 * ACHTUNG! Die Methode ist zunächst noch mit Vorsicht zu genießen! Sie funktioniert nur partiell einwandfrei.
	 * @return Dies ist die irreflexive, transitive Reduktion der Relation.
	 */
	public Relation transitive_reduction() {
		//ist der Graph reflexiv, kommt es zu Problemen
		Relation result=this.reflexive_reduction();
		Iterator it=result.get_entries().keySet().iterator();
		while(it.hasNext()) {
			Integer aktNode = (Integer) it.next();
			Iterator adjacencies = ((HashSet)result.get_entries().get(aktNode)).iterator();
			while(adjacencies.hasNext()) {
				Integer target = (Integer) adjacencies.next();
				Iterator target_adjacencies = ((HashSet)result.get_entries().get(target)).iterator();
				while(target_adjacencies.hasNext()) {
					Integer akt_target_node = (Integer) target_adjacencies.next();
					if(result.isRelated(aktNode,akt_target_node)) {
						
						result.delete_entry(aktNode, akt_target_node);
						adjacencies=((HashSet)result.get_entries().get(aktNode)).iterator();
						//target_adjacencies = ((HashSet)result.get_entries().get(target)).iterator();
					}
				}
			}
		}
		return result;
	}
	
	/*
	 * *******************************************************************
	 * Es folgen Methoden zur Herstellung der Antisymmetrie der Relation.
	 * ******************************************************************* 
	 */
	/**
	 * Die Methode vereinigt zwei Elemente der Menge in der Relation, i.e. Relationen zwischen den beiden Elementen werden
	 * nicht mehr betrachtet, aber alle Referenzen auf andere Elemente und von anderen Elementen auf die beiden fraglichen Elemente.
	 * @param x Dies ist die Abszisse in der die Relation darstellenden Matrix.
	 * @param y Dies ist die Ordinate in der die Relation darstellenden Matrix.
	 * @return Dies ist die Adjazenzliste des neuen, vereinigten Elementes.
	 */
	private HashSet fuseByIndex(Integer x, Integer y) {
		/**
		 * Der <code>HashSet result</code> ist Träger des Ergebnisses, d.h. der Vereinigung der beteiligten Listen.
		 */
		HashSet result=new HashSet();
		HashSet set_x=(HashSet)((TreeMap)entries.clone()).get(x);
		HashSet set_y=(HashSet)((TreeMap)entries.clone()).get(y);
		result.addAll(set_x);
		result.addAll(set_y);
		return result;
	}
	
	/**
	 * Die Methode vereinigt zwei Elemente sowohl der Menge als auch in der Relation.
	 * @param x Dies ist der Index des ersten zu vereinigenden Elementes.
	 * @param y Dies ist der Index des zweiten zu vereinigenden Elementes.
	 * @return Es wird die Relation zurückgegeben, die identisch mit dieser ist - jedoch die beiden Elemente vereinigt in sich trägt.
	 */
	private Relation fuse(Integer x, Integer y){
		
		Relation result=(Relation)this.clone();
		Element new_element=((Element)result.get_elements().get(x)).union((Element)result.get_elements().get(y));
		TreeMap new_elements = result.get_elements();
		TreeMap matrix = result.get_entries();
		HashSet fused=result.fuseByIndex(x,y);
		//Nach den Vorbereitungen muss nun in matrix das x die Rolle des veschmelzten Knotens
		//übernehmen, y verschwindet...
		//Verweise auf y von x aus (die dann Selbstverweise wären) können entfallen
		fused.remove(y);
		
		matrix.put(x, fused);
		//Zeile y löschen
		matrix.remove(y);
			
		
		//Zeiger auf y umsetzen auf x...
		Iterator it=matrix.keySet().iterator();
		while(it.hasNext()) {
			Integer akt=(Integer)it.next();
			if(!akt.equals(x)) {
				HashSet aktSet=(HashSet)matrix.get(akt);
				//wenn Verweis auf y existiert, dann setze ihn auf x
				if(aktSet.remove(y)) {
					aktSet.add(x);
				}
			}
		}
		//Jetzt ist die Matrix bereinigt - die Elemente müssen noch bereinigt werden...
		
		new_elements.remove(y);
		new_elements.put(x, new_element);
		
		result.set_entries(matrix);
		result.set_elements(new_elements);
		return result;
		
	}
	
	/**
	 * Die Methode prüft für eine gegebene Matrixposition, ob ein Bruch der Antisymmetrie vorliegt,
	 * also ob in der Matrix an Position (x,y) und an Position (y,x) gleichzeitig ein Eintrag existiert
	 * (wobei x ungleich y).
	 * @param x die Abszisse der Matrix
	 * @param y die Ordinate der Matrix
	 * @return <code>true</code> genau dann, wenn eine Verletzung der Antisymmetrie vorliegt
	 */
	private boolean breach_antisymmetry(Integer x, Integer y){
		
		if(x.equals(y)==false) {
			if(entries.containsKey(x)){
				if(((HashSet)entries.get(x)).contains(y)) {
					if(entries.containsKey(y)) {
						if(((HashSet)entries.get(y)).contains(x)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Die Methode prüft, ob die Matrix antisymmetrisch ist.<br>
	 * Die Matrix ist antisymmetrisch, wenn für alle x,y der Matrix gilt: (x,y) und (y,x) in der 
	 * Relation hat zur Folge, dass x=y ist.
	 * @return <code>true</code> genau dann, wenn die Matrix antisymmetrisch ist
	 */
	public boolean is_antisymmetric() {
		Iterator it=entries.keySet().iterator();
		while(it.hasNext()) {
			Integer akt=(Integer)it.next();
			Iterator inner_it=((HashSet)entries.get(akt)).iterator();
			while(inner_it.hasNext()) {
				if(breach_antisymmetry(akt, (Integer)inner_it.next())) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Die Methode wird für den Transformationsalgorithmus verwendet.<br>
	 * Sie erzeugt eine <code>TreeMap</code>, die jedem Element der Grundmenge die Liste der Verletzungen
	 * der Antisymmetrie, an denen er beteiligt ist, zuordnet.
	 * @return Abbildung: Nummer -> Liste der Verletzungen der Antisymmetrie, an denen das Element beteiligt ist.
	 */
	private TreeMap list_breaches_antisymmetry() {
		
		/**
		 * Dies wird das Resultat.
		 */
		TreeMap breaches=new TreeMap();
		/**
		 * Dies ist die Liste der Brüche eines aktuellen Elementes.
		 */
		HashSet current_breaches;
		Iterator it=entries.keySet().iterator();
		while(it.hasNext()) {
			Integer aktID=(Integer)it.next();
			Iterator inner_it=((HashSet)entries.get(aktID)).iterator();
			current_breaches=new HashSet();
			while(inner_it.hasNext()) {
				Integer akttarget=(Integer)inner_it.next();
				if(breach_antisymmetry(aktID,akttarget)) {
					current_breaches.add(akttarget);
				}
			}
			
			breaches.put(aktID, current_breaches);
			
		}
		return breaches;
	}
	
	/**
	 * Die Methode transformiert die Matrix derart, dass eine antisymmetrische Relation erzeugt wird.<br>
	 * Hierfür werden die Knoten maximal zusammengefasst.
	 * @return Dies ist die Relation nach antisymmetrischer Reduktion.
	 */
	public Relation antisymmetric_reduction() {
		
		//Zuerst prüfen, ob sich die Arbeit lohnt...
		Relation result=(Relation)this.clone();
		
		
		while(!result.is_antisymmetric()) {
			/**
			 * Abbildung: Nummer -> Antisymmetriebrüche
			 */
			TreeMap breaches=result.list_breaches_antisymmetry();
			
		    
			Iterator keyIt=breaches.keySet().iterator();
			while(keyIt.hasNext()) {
				Integer aktKey=(Integer)keyIt.next();
				
				Iterator it=((HashSet)breaches.get(aktKey)).iterator();
				if(it.hasNext()) {
					Integer akt_breach=(Integer)it.next();
					result=result.fuse(aktKey,akt_breach);
					break;
				}
			}
		}
		
		return result;
	}
	
	/*
	 * ********************************************************
	 * Beginn der weitergehenden Analyse (Projekt NOSAM)
	 * *******************************************************
	 */
	/*
	 * Clustering von Knoten
	 */
	
	/**
	 * Die Methode konvertiert die Relation in eine Relation, die über <code>set_element</code>en definiert ist.
	 * Die Elemente sind dann einelementige Mengen von Elementen, die in der Ursprungsrelation waren.
	 * @return die äquivalente Relation, die auf <code>set_element</code>en definiert wurde.
	 * @see system_element
	 */
	public Relation convert() {
		HashSet akt_elem_set = new HashSet();
		TreeMap the_entries = (TreeMap)this.get_entries().clone();
		TreeMap elems = new TreeMap();
		Iterator it = this.get_elements().keySet().iterator();
		while(it.hasNext()){
			Integer akt = (Integer)it.next();
			Element akt_elem = (Element)this.get_elements().get(akt);
			akt_elem_set.add(akt_elem);
			set_element toput=new set_element(akt_elem_set);
			elems.put(akt, toput);
			akt_elem_set=new HashSet();
		}
		return new Relation(elems, the_entries);
	}
	
	/**
	 * Die Methode gibt den Index (also die ID der TreeMap) des Elementes zurück.
	 * @param e Das Element, dessen Index gesucht wird.
	 * @return der Index des gesuchten Elementes
	 */
	public Integer getIndex(Element e) {
		Iterator it = this.get_elements().keySet().iterator();
		while(it.hasNext()) {
			Integer akt = (Integer) it.next();
			
			if(((Element)this.get_elements().get(akt)).equals(e)) {
				return akt;
			}
		}
		return new Integer(0);
	}
	
	/**
	 * Die Methode gibt ein Element vom Typ <code>set_element</code> zurück, das das übergebene Element enthält.
	 * Voraussetzung ist, dass die Relation über <code>set_element</code>en definiert ist. 
	 * @param e das zu vergleichende Element
	 * @return das set_element, das das übergebene Element enthält
	 */
	public set_element get_set_element(Element e) {
		Iterator it = this.get_elements().values().iterator();
		while(it.hasNext()) {
			
			set_element akt = (set_element)it.next();
			if(akt.contains(e)) {
				return akt;
			}
		}
		return null;
	}
	/**
	 * Die Methode vereinigt zwei Elemente sowohl der Menge als auch in der Relation.
	 * @param e1 Dies ist das erste zu vereinigende Element.
	 * @param e2 Dies ist das zweite zu vereinigende Element.
	 * @return Es wird die Relation zurückgegeben, die identisch mit dieser ist - jedoch die beiden Elemente vereinigt in sich trägt.
	 */
	private Relation fuseElements(Element e1, Element e2) {
		Integer index1;
		Integer index2;
		
		if(e1.getClass().getName().equals("set_element")) {
			index1=this.getIndex(e1);
			index2=this.getIndex(e2);
		}else {
			set_element set_e1=this.get_set_element(e1);
			set_element set_e2=this.get_set_element(e2);
			index1=this.getIndex(set_e1);
			index2=this.getIndex(set_e2);
			
		}
		
		Relation result=this.fuse(index1, index2);
		return result;
	}
	/**
	 * Die Methode vereinigt alle Knoten, die in der übergebenen Liste spezifiziert werden.
	 * @param tofuse Dies ist die Liste der Knoten, die vereinigt in der resultierenden Relation vereinigt sein sollen.
	 * @return Die Relation, die alle Knoten, die in der Liste übergeben wurden, ist das Resultat. Sie ist über Elementen 
	 * vom Typ <code>set_element</code>  definiert.
	 * @see system_element
	 */
	private Relation fuseByList(HashSet tofuse) {
		Relation result=(Relation)this.clone();
		Iterator test = result.get_elements().values().iterator();
		
		if(test.next().getClass().getName().equals("system_element")) {
			result=result.convert();
		
		}
		Iterator it = tofuse.iterator();
		Element first = (Element) it.next();
		while(it.hasNext()) {
			Element akt = (Element)it.next();
			result = result.fuseElements(first, akt);
		}
		
		return result;
	}
	
	/**
	 * Die Methode clustert die Relation anhand einer Partition der Knotenmenge.
	 * @param list Dies ist die Partition der Knotenmenge, i.e. eine Liste von Listen von zu vereinigenden Knoten.
	 * @return Die geclusterte Relation wird zurückgegeben.
	 */
	public Relation clustering(HashSet list) {
		Relation result = (Relation)this.clone();
		Iterator list_it = list.iterator();
		while(list_it.hasNext()) {
			result = result.fuseByList((HashSet)list_it.next());
		}
		//weil bei der Clusterung der Knoten, auf den alle zusamenzufassenden Knoten abgebildet werden einen Selbstverweis erhält,
		//wird die Relation hinterher einfach wieder irreflexiv gemacht.
		result=result.reflexive_reduction();
		return result;
	}
	/**
	 * Die Methode prüft, ob es einen Weg in zwei Schritten vom Element from zum Element to gibt (jeweils übergeben als Index).
	 * @param from der Index des Ursprungselementes
	 * @param to der Index des Zielelementes
	 * @return wahr genau dann, wenn es einen Weg in zwei Schritten vom Element from zum Element to gibt.
	 */
	public boolean exists_third_party_reference(Integer from, Integer to) {
		Iterator candidates = ((HashSet)this.entries.get(from)).iterator();
	
		while(candidates.hasNext()) {
			if(this.isRelated((Integer)candidates.next(),to)) {
				return true;
			}
		}
		return false;
	}
	
	public Element getElementByID(String ID) {
		Iterator elems = this.elements.values().iterator();
		while(elems.hasNext()) {
			Element akt = (Element)elems.next();
			if(akt.equalsID(ID)) {
				return akt;
			}
					
		}
		System.out.println(ID+" ist in Relation nicht vorhanden!");
		return null;
	}
	/**
	 * Die Methode generiert eine Datei im angegebenen Pfad im <code>TGF</code>-Format (Trivial Graphics Format).
	 * Diese Datei deklariert alle Knoten (Nummer, Bezeichnung des Knotens), gefolgt von einem <code>#</code> und 
	 * der Liste der Kanten (jeweils unter Nennung der Nummern der inzidierenden Knoten).<br>
	 * 
	 * @param path Der Pfad der zu speichernden Datei im <code>TGF</code>-Format.
	 */
	
	public void generate_tgf(String path) {
		/**
		 * Der <code>String output</code> enthält am Ende die Zeichenkette, die in der Datei abgelegt werden soll.
		 */
		String output="";
		//Zunächst die Knoten definieren, also die Zeilennummern den IDNr. der Verfahren
		//zuordnen.
		Iterator it=elements.keySet().iterator();
		while(it.hasNext()) {
			Integer akt=(Integer)it.next();
			output=output+akt.toString()+" ";
			String value=((Element)elements.get(akt)).toString();
			output=output+value+" \n";
		}
		
		
		
		output=output+"# \n";
		//Trennsymbol ist #. Danach kommen die Kanten, d.h. durchlaufe die 
		//Adjazenzmatrix.
		
		it=entries.keySet().iterator();
		while (it.hasNext()){			
			Integer akt=(Integer)it.next();
			HashSet value=(HashSet)entries.get(akt);
			Iterator itList=value.iterator();
			while(itList.hasNext()){
				output=output+akt.toString()+" ";
				Integer aktNode=(Integer)itList.next();
				output=output+aktNode.toString()+" \n";
			}
			
		}
		//System.out.println(output);
		
		//Nun das Ganze in eine Datei schreiben.
		
		try{
			FileWriter stream=new FileWriter(path);
			stream.write(output);
			stream.close();
		}
		catch(Exception e){
			System.out.println("Schreiben der Datei nicht möglich!");
		}
		
	}
				
	/**
	 * Die Methode erzeugt eine neue, identische Relation.
	 */
	public Object clone() {
		
		return new Relation((TreeMap)elements.clone(), (TreeMap)entries.clone());
	}
}
