import java.util.*;

import y.base.EdgeList;


/**
 * Die Klasse beherbergt die Analysewerkzeuge zur Verfügung, um 
 * - Schnittstellen zu identifizieren, die die Komplexität in der transformierten Relation (=Striktordnung) (bei Wegfall) verringern und
 * - logSinN (=Knoten) zu identifizieren, die dann konfliktarm abgelöst werden können.
 * Sie dient weiterhin als Container für die Ursprungsrelation einerseits und andererseits für die generierte Striktordnung.
 * @author Sebastian Schlesiner
 * @version 0.1
 *
 */
public class Analyzer {

	/**
	 * Das Attribut beherbergt die Ursprungsrelation, die die Schnittstellen zwischen den logSinN modelliert.
	 */
	private Relation matrix;
	/**
	 * Das Attribut beherbergt die Striktordnung, generiert aus der übergebenen Relation und definiert über <code>set_element</code>en.
	 */
	private Relation order;
	/**
	 * Das Attribut beherbergt die transitive Hülle, die aus der gegebenen Relation generiert wurde.
	 */
	private Relation transitive_hull;
	/**
	 * Der Konstruktor belegt das Attribut <code>matrix</code> mit der Relation, die übergeben wurde, generiert anschließend
	 * die transitive Hülle der Relation, belegt damit das Attribut <code>transitive_hull</code> und generiert weiter eine Striktordnung und belegt damit dass Attribut <code>order</code>. Die Attribute <code>order</code> und <code>transitive_hull</code> werden dann <code>set_element</code>e
	 * enthalten.
	 * @param r Der Parameter beinhaltet die zu analysierende Relation (die die Schnittstellen zwischen logSinN modelliert).
	 */
	public Analyzer(Relation r) {
		this.matrix=r;
		
		Iterator test=r.get_elements().values().iterator();
		//order soll als Relation auf set_elementen vorliegen, daher ggf. vorher konvertieren...
		if(test.next().getClass().getName().equals("system_element")) {
			this.matrix=this.matrix.convert();
		}
	
		this.transitive_hull=((Relation)this.matrix.clone()).transitive_hull();
		
		this.transitive_hull.reflexive_reduction();
		this.order=(Relation)transitive_hull.clone();
		TreeMap testmap=(TreeMap)order.get_entries().clone();
		
	    this.order=order.antisymmetric_reduction();
	  
		
		this.order.reflexive_reduction();
		
		
	}
	/**
	 * Die Methode setzt das Attribut <code>matrix</code>, welches die zu analysierende Relation enthält.
	 * @param r die zu analysierende Relation
	 */
	public void set_relation(Relation r) {
		this.matrix=r;
	}
	/**
	 * Die Methode gibt die zu analysierende Relation zurück.
	 * @return die zu analysierende Relation
	 */
	public Relation get_relation() {
		return this.matrix;
	}
	/**
	 * Die Methode setzt die Striktordnung (die üblicherweise aus der Ursprungsrelation in <code>matrix</code> generiert wird).
	 * @param r die Striktordnung
	 */
	public void set_order(Relation r) {
		this.order=r;
	}
	/**
	 * Die Methode gibt die Striktordnung zurück.
	 * @return die Striktordnung
	 */
	public Relation get_order() {
		return this.order;
	}
	/**
	 * Die Methode setzt die transitive Hülle (die üblicherweise aus der Ursprungsrelation generiert wird).
	 * @param r die transitive Hülle
	 */
	public void set_transitive_hull(Relation r) {
		this.transitive_hull=r;
	}
	/**
	 * Die Methode gibt die transitive Hülle zurück.
	 * @return die transitive Hülle
	 */
	public Relation get_transitive_hull() {
		return this.transitive_hull;
	}
	/**
	 * Die Methode berechnet den auslaufenden Grad des Knotens bezogen auf seine starke Zusammenhangskomponente. 
	 * @param e das Element, dessen Grad berechnet werden soll
	 * @return Der auslaufende Grad, genauer die Anzahl der Knoten, mit denen der übergebene Knoten inzidiert und in dessen starker Zusammenhangskomponente liegen, wird zurückgegeben.
	 */
	public int outbound_degree_strong_connectivity_component(Element e) {
		set_element to_investigate = this.strong_connectivity_component(e);
		
		Iterator incidences = ((HashSet)matrix.get_entries().get(matrix.getIndex(e))).iterator();
		int result=0;
		while(incidences.hasNext()) {
			system_element akt;
			try{
				akt = ((set_element)matrix.get_elements().get((Integer)incidences.next())).to_system_element();
			}catch(Exception ex) {
				System.err.println("Typen in Matrix matchen nicht!");
				return 0;
			}
			
			if(to_investigate.contains(akt)) {
				result++;
			}
		}
		return result;
	}
	/**
	 * Die Methode berechnet den einlaufenden Grad des Knotens bezogen auf seine starke Zusammenhangskomponente. 
	 * @param e das Element, dessen Grad berechnet werden soll
	 * @return Der einlaufende Grad, genauer die Anzahl der Knoten, die mit dem übergebenen Knoten inzidieren und in dessen starker Zusammenhangskomponente liegen, wird zurückgegeben.
	 */
	public int incoming_degree_strong_connectivity_component(Element e) {
		set_element to_investigate = this.strong_connectivity_component(e);
		Iterator elems_to_investigate = to_investigate.getSet().iterator();
		int result=0;
		while(elems_to_investigate.hasNext()) {
			Element akt = (Element)elems_to_investigate.next();
			
			if(matrix.isRelated(matrix.getIndex(akt), matrix.getIndex(e))) {
				result++;
			}
		}
		return result;
	}
	/**
	 * Die Methode gibt die starke Zusammenhangskomponente zurück, in der sich das übergebene Element befindet.
	 * @param e das übergebene Element, dessen starke Zusammenhangskomponente gesucht ist
	 * @return die starke Zusammenhangskomponente, in der sich das übergebene Element befindet
	 */
	protected set_element strong_connectivity_component(Element e) {
		Iterator set_elems = order.get_elements().values().iterator();
		while(set_elems.hasNext()) {
			set_element akt = (set_element)set_elems.next();
			if(akt.contains(e)) {
				return akt;
			}
		}
		return null;
	}
	
	/**
	 * Die Methode gibt den Index der starken Zusammenhangskomponente zurück, in der sich das übergebene Element befindet.
	 * @param e das übergebene Element, dessen starke Zusammenhangskomponente gesucht ist
	 * @return der Index der starken Zusammenhangskomponente, in der sich das übergebene Element befindet
	 */
	protected Integer getIndex_strong_connectivity_component(Element e) {
		return new Integer(0);
	}
	/**
	 * Die Methode erzeugt eine Liste der echten starken Zusammenhangskomponenten (also mit Elementzahl >1)
	 * @return die Liste der echten starken Zusammenhangskomponenten
	 */
	protected HashSet list_true_strong_connectivity_components() {
		HashSet result = new HashSet();
		Iterator elems = this.order.get_elements().values().iterator();
		while(elems.hasNext()) {
			Element akt = (Element)elems.next();
			if(akt.one_step_size()>1) {
				result.add(akt);
			}
		}
		return result;
	}
	/**
	 * Die Methode generiert eine TreeMap (also Schlüssel in aufsteigender Reihenfolge), die Grade Listen von Knoten
	 * zuordnet. Die zugeordneten Listen enhtalten die Knoten gleichen ausgehenden Grades. Es werden nur die Knoten des übergebenen
	 * Elementes betrachtet.
	 * @param e das zu analysierende set_element
	 * @return die Abbildung auslaufender Grad -> Menge der Knoten mit diesem Grad
	 */
	protected TreeMap get_outbound_degree_map_strong_connectivity_component(Element e) {
		Iterator to_analyse = this.order.get_set_element(e).getSet().iterator();
		TreeMap result = new TreeMap();
		while(to_analyse.hasNext()) {
			Element akt = (Element) to_analyse.next();
			Integer akt_degree = new Integer(this.outbound_degree_strong_connectivity_component(akt));
			HashSet to_expand = new HashSet();
			if(result.containsKey(akt_degree)) {
				to_expand = (HashSet)result.get(akt_degree);
			}
			to_expand.add(akt);
			result.put(akt_degree, to_expand);
		}
		return result;
		
	}
	/**
	 * Die Methode generiert eine TreeMap (also Schlüssel in aufsteigender Reihenfolge), die Grade Listen von Knoten
	 * zuordnet. Die zugeordneten Listen enhtalten die Knoten gleichen einlaufenden Grades. Es werden nur die Knoten des übergebenen
	 * Elementes betrachtet.
	 * @param e das zu analysierende set_element
	 * @return die Abbildung auslaufender Grad -> Menge der Knoten mit diesem Grad
	 */
	protected TreeMap get_incoming_degree_map_strong_connectivity_component(Element e) {
		Iterator to_analyse = this.order.get_set_element(e).getSet().iterator();
		TreeMap result = new TreeMap();
		while(to_analyse.hasNext()) {
			Element akt = (Element) to_analyse.next();
			Integer akt_degree = new Integer(this.incoming_degree_strong_connectivity_component(akt));
			HashSet to_expand = new HashSet();
			if(result.containsKey(akt_degree)) {
				to_expand = (HashSet)result.get(akt_degree);
			}
			to_expand.add(akt);
			result.put(akt_degree, to_expand);
		}
		return result;
	}
	/**
	 * Die Methode löscht alle durch die übergebene Kante im Zuge der Generierung der transitiven Hülle
	 * der Relation zum Ursprungsknoten hinzugefügten Kanten. Es werden also alle Verweise auf Knoten gelöscht, die
	 * vom Zielknoten in der transitiven Hülle ausgehen und in der Ursprungsrelation nicht vom Ursprungsknoten ausgegangen
	 * waren. Die Ordnung wird nicht betrachtet.
	 * @param e die zu löschende Kante
	 */
	protected void partial_transitive_reduction(edge e) {
		
		Integer from_trans = this.transitive_hull.getIndex(e.get_origin());
		Integer to_trans = this.transitive_hull.getIndex(e.get_target());
		//erstmal die fragliche Kante löschen...
		
		this.transitive_hull.delete_entry(from_trans, to_trans);
		this.matrix.delete_entry(from_trans, to_trans);
		HashSet not_to_delete = (HashSet)matrix.get_entries().get(from_trans);
		//Idee: Lösche alle Zeiger aus to_trans aus from_trans, wenn der Zeiger nicht schon
		//in matrix existiert hat (Indizes sind in beiden Matrizen matrix und transitive_hull identisch).
		//Prüfe weiter, ob evtl. Verweise von from_trans zu z und von z zu einem weiteren z' existieren - wenn ja, dann lösche den Eintrag nicht.
		Iterator candidates = ((HashSet)this.transitive_hull.get_entries().get(to_trans)).iterator();
		while(candidates.hasNext()) {
			Integer akt_cand=(Integer)candidates.next();
			
			if(!not_to_delete.contains(akt_cand) && !this.transitive_hull.exists_third_party_reference(from_trans, akt_cand)) {
				transitive_hull.delete_entry(from_trans, akt_cand);
			}
		}
		
				
	}
	/**
	 * Die Methode versucht, zwischen zwei gegebenen Mengen von Elementen eine Verbindung zu identifizieren (in der Ursprungsmatrix) - die Verbindung muss in der Ursprungsmatrix existieren. Existiert eine,
	 * so wird diese zurückgegeben.
	 * @param from_elems die Menge von Elementen, von der aus die Verbindung hergestellt werden soll
	 * @param to_elems die Menge von Elementen, zu denen die Verbindung hergestellt werden soll
	 * @return eine Kante, die die Verbindung realisiert
	 */
	protected edge identify_connection(HashSet from_elems, HashSet to_elems) {
		Iterator from = from_elems.iterator();
		while(from.hasNext()) {
			Element akt = (Element) from.next();
			
			Element connector = matrix.choose_Element_connection_exists_with(akt, to_elems);
			if(connector!=null) {
				return new edge(akt,connector);
			}
		}
		return null;
	}
	
	
	/**
	 * Die Methode führt das Kanteneliminationsverfahren durch und gibt die Liste von (direkt) eliminierten Kanten zurück.
	 * @return die Liste von direkt eliminierten Kanten (ohne die aus der Berechung der Auswirkungen auf die transitive Hülle)
	 */
	public HashSet edge_elimination_algorithm() {
		HashSet result = new HashSet();
		Iterator components = this.list_true_strong_connectivity_components().iterator();
		
		System.out.println("Größe bei "+this.list_true_strong_connectivity_components().size() + " bzw. bei "+((set_element)components.next()).size());
		components = this.list_true_strong_connectivity_components().iterator();
		while(components.hasNext()) {
			Element to_investigate = (Element)components.next();
			TreeMap incoming = this.get_incoming_degree_map_strong_connectivity_component(to_investigate);
			
			
			TreeMap outgoing = this.get_outbound_degree_map_strong_connectivity_component(to_investigate);
			
			Iterator outgoing_it = outgoing.keySet().iterator();
			
			while(outgoing_it.hasNext()) {
				HashSet outgoing_min = (HashSet)outgoing.get((Integer) outgoing_it.next());
				
				HashSet incoming_max = (HashSet)incoming.get((Integer)incoming.lastKey());
			
				edge candidate = this.identify_connection(outgoing_min,incoming_max);
			
				if(candidate!=null) {
					result.add(candidate);
					System.out.println(result);
					//Variante 1: ohne partial transitive_reduction
					Relation newMatrix = (Relation)matrix.clone();
					newMatrix.delete_entry(matrix.getIndex(candidate.get_origin()), matrix.getIndex(candidate.get_target()));
					Analyzer new_analyser = new Analyzer(newMatrix);
				
					result.addAll(new_analyser.edge_elimination_algorithm());
					break;
				}else {
				
					if(incoming.size()>0) {
						incoming.remove(incoming.lastKey());
					}else{
						incoming = this.get_incoming_degree_map_strong_connectivity_component(to_investigate);
						outgoing.remove(outgoing.firstKey());
					}
				}
				
			}
			
			//components=this.list_true_strong_connectivity_components().iterator();	
			
		}
		
		return result;
	}
	
	
}
