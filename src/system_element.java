
/**
 * 
 * Die Klasse ist die Standardvariante zur Modellierung von Relationen auf SinN.
 * @author Sebastian Schlesinger
 * @version 0.1
 *
 */
public class system_element implements Element {

	/**
	 * Das Attribut trägt die ID des SinN.
	 */
	private String system_ID;
	/**
	 * Das Attribut trägt die Kurzbezeichnung des SinN.
	 */
	private String system_name;
	
	/**
	 * Der parameterlose Konstruktor belegt ID und Kurzbezeichnung des SinN mit "ohne".
	 */
	public system_element() {
		system_ID="ohne";
		system_name="ohne";
	}
	/**
	 * Der Konstruktor belegt ID und Kurzbezeichnung des SinN.
	 * @param ID Dies ist die ID des SinN.
	 * @param name Dies ist die Kurzbezeichnung des SinN.
	 */
	public system_element(String ID, String name) {
		system_ID=ID;
		system_name=name;
	}
	
	/**
	 * Die Methode setzt die ID des SinN.
	 * @param s Dies ist die zu setzende ID des SinN.
	 */
	public void set_systemID(String s) {
		system_ID=s;
	}
	/**
	 * Die Methode gibt die ID des SinN zurück.
	 * @return Dies ist die ID des SinN.
	 */
	public String get_systemID() {
		return system_ID;
	}
	/**
	 * Die Methode setzt die Kurzbezeichnung des SinN.
	 * @param s Dies ist die zu setzende Kurzbezeichnung des SinN.
	 */
	public void set_system_name(String s) {
		system_name=s;
	}
	/**
	 * Die Methode gibt die Kurzbezeichnung des SinN zurück.
	 * @return Dies ist die Kurzbezeichnung des SinN.
	 */
	public String get_system_name() {
		return system_name;
	}
	/* (non-Javadoc)
	 * @see Element#equals(Element)
	 */
	public boolean equals(Element e) {
		if(e.getClass().getName().equals("system_element")) {
			return ((system_element)e).get_systemID().equals(system_ID) && ((system_element)e).get_system_name().equals(system_name);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see Element#contains(Element)
	 */
	public boolean contains(Element e) {
		return this.equals(e);
	}
	/* (non-Javadoc)
	 * @see Element#union(Element)
	 */
	public Element union(Element e) {
		if(this.equals(e)) {
			return this;
		}
		String new_ID=system_ID+"_"+((system_element)e).get_systemID();
		String new_system_name=system_name+"_"+((system_element)e).get_system_name();
		return new system_element(new_ID,new_system_name);
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new system_element(system_ID,system_name);
	}
	/*
	 * (non-Javadoc)
	 * @see Element#size()
	 */
	public int size() {
		return 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Element#one_step_size()
	 */
	public int one_step_size() {
		return 1;
	}
	/*
	 * (non-Javadoc)
	 * @see Element#toString()
	 */
	public String toString() {
		return system_ID+" "+system_name;
	}
	/*
	 * (non-Javadoc)
	 * @see Element#compareTo()
	 */
	public int compareTo(Object o) {
		if(!(o instanceof system_element)) {
			throw new ClassCastException("Objekt ist kein system_element!");
		}
		if(this.equals(o)) {
			return 0;
		}else
			return 1;
	}
	
	public boolean equalsID(String s) {
		return system_ID.equals(s);
		
	}

}
