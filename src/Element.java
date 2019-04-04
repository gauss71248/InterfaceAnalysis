
/**
 * Das Interface definiert den Typ von Elementen einer Menge, auf der die Relation "operiert".
 * @author Sebastian Schlesinger
 * @version 0.1
 *
 */
public interface Element extends Cloneable, Comparable{
	
	
	
	/**
	 * Die Methode gibt an, ob zwei Elemente gleich sind.
	 * @param e Das mit dieser Instanz zu vergleichende Element.
	 * @return true, falls die Elemente gleich sind, false sonst.
	 */
	public boolean equals(Element e);
	/**
	 * Die Methode vereint zwei Elemente.
	 * @param e Der mit dieser Instanz zu vereinigende Knoten.
	 */
	public Element union(Element e);
	
	/**
	 * Die Methode prüft, ob das übergebene Element in dieser Instanz enthalten ist. <br>
	 * Dies ist bei <code>system_element</code>en trivial - es enstpricht der Gleichheit; bei <code>set_element</code>en
	 * muss hingegen geprüft werden, ob das übergebene Element in der Liste der Elemente vorkommt.
	 * @param e das auf Enthaltensein zu überprüfende Element
	 * @return wahr genau dann, wenn das übergebene Element in der Instanz enthalten ist.
	 */
	public boolean contains(Element e);
	
	/**
	 * Die Methode gibt die Anzahl der im Element befindlichen <code>system_element</code>e zurück.
	 * @return die Anzahl der im Element befindlichen <code>system_element</code>e
	 */
	public int size();
	/**
	 * Die Methode berechnet die Anzahl der im Element gespeicherten Elemente - aber nur eine Ebene tiefer.
	 * @return die Anzahl der enthaltenen Elemente - eine Ebene tiefer
	 */
	public int one_step_size();
	/**
	 * Die Methode definiert die Ausgabe zur Darstellung des Elementes als Knoten des zur Relation korresprondierenden Graphen.
	 * @return Dies ist die Darstellung des Knoten des zur Relation korrespondierenden Graphen.
	 */
	public String toString();
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone();
	
	/*
	 * (non-Javadoc)
	 * @see Comparable#compareTo()
	 */
	public int compareTo(Object o);
	
	public boolean equalsID(String s);
}
