import java.util.*;

/**
 * Die Klasse ist als Element einer Menge eine Menge von Elementen - die Relation, die sich dieses Elementes bedient,
 * ist also über einer Menge von Mengen definiert.
 * @author gauss
 * @version 0.1
 *
 */
public class set_element implements Element {

	/**
	 * Das Attribut ist die Menge von Elementen.
	 */
	private HashSet set;
	
	/**
	 * Der parameterlose Konstruktor initialisiert lediglich die Menge.
	 */
	public set_element() {
		set=new HashSet();
	}
	/**
	 * Der Konstruktor belegt die Menge mit dem übergebenen Parameter.
	 * @param l Dies ist die zu belegende Menge.
	 */
	public set_element(HashSet l) {
		set=l;
	}
	/**
	 * Der Konstruktor erzeugt ein einelementiges Mengenelement mit dem übergebenen Parameter.
	 * @param e Dies ist das Element, aus dem die einelementige Menge erzeugt wird.
	 */
	public set_element (Element e) {
		set=new HashSet();
		set.add(e);
	}
	/**
	 * Die Methode setzt die Menge.
	 * @param l Dies ist die zu setzende Menge.
	 */
	public void setSet(HashSet l) {
		set=l;
	}
	/**
	 * Die Methode gibt die Menge zurück.
	 * @return Dies ist die zurückgegebene Menge.
	 */
	public HashSet getSet() {
		return set;
	}
	/*
	 * @see Element#equals(Element)
	 */
	public boolean equals(Element e) {
		set_element set_e;
		try{
			set_e=(set_element)e;
		}catch(ClassCastException ex) {
		
			if(this.size()==1) {
				Iterator it = set.iterator();
				try{
					return e.equals((system_element)it.next());
				}catch(ClassCastException ex2) {
					return false;
				}
			}else
				return false;
		}
		Iterator it=this.set.iterator();
		while(it.hasNext()) {
			if(!set_e.contains((Element)it.next())) {
				return false;
			}
		}
		it=set_e.getSet().iterator();
		while(it.hasNext()) {
			if(!this.contains((Element)it.next())) {
				return false;
			}
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see Element#contains(Element)
	 */
	public boolean contains(Element e) {
		if(this.equals(e)) {
			return true;
		}
		Iterator it=set.iterator();
		while(it.hasNext()) {
			Element akt=(Element)it.next();
			
		    if(akt.equals(e)) {
		    	return true;
		    }
			if(akt.contains(e)) {
				return true;
			}
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see Element#union(Element)
	 */
	public Element union(Element e) {
		if(this.equals(e)) {
			return this;
		}
		HashSet result=(HashSet)this.set.clone();
		try{
		
			result.addAll(((set_element)e).getSet());
		}catch(ClassCastException c) {
		
		}
		
		return new set_element(result);
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new set_element(set);
	}
	/*
	 * (non-Javadoc)
	 * @see Element#size()
	 */
	public int size() {
		Iterator it = set.iterator();
		int result = 0;
		while(it.hasNext()) {
			Element akt = (Element) it.next();
			result+=akt.size();
		}
		return result;
	}
	/*
	 * (non-Javadoc)
	 * @see Element#one_step_size()
	 */
	public int one_step_size() {
		return set.size();
	}
	/**
	 * Die Methode transformiert das <code>set_element</code> in ein <code>system_element</code>, sofern in ihm nur eines enthalten ist.
	 * @return das <code>system_element</code>, welches im Element enthalten war.
	 * @throws Transforming_Exception
	 */
	public system_element to_system_element() throws Transforming_Exception{
		if(this.size()>1) {
			throw new Transforming_Exception("system_element "+this.toString()+" enthält mehr als ein Element!");
		}else {
			Iterator it = this.set.iterator();
			try{
				return (system_element)((system_element)it.next()).clone();
			}catch(ClassCastException e) {
				throw new Transforming_Exception("Enthaltenes Element ist nicht vom Typ system_element!");
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * @see Element#toString()
	 */
	public String toString() {
		Iterator it = set.iterator();
		String result="";
		while(it.hasNext()) {
			result+=((Element)it.next()).toString()+"_";
		}
		return result;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see Element#compareTo()
	 */
	public int compareTo(Object o) {
		if(!(o instanceof set_element)) {
			throw new ClassCastException("Objekt ist kein system_element!");
		}
		if(this.equals(o)) {
			return 0;
		}else
			return 1;
	}
	
	
	public boolean equalsID(String s) {
		Iterator it = this.set.iterator();
		while(it.hasNext()) { 
			if(((Element)it.next()).equalsID(s)) {
				return true;
			}
		}
		return false;
	}
		

}
