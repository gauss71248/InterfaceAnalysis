/**
 * Die Klasse stellt eine Kante, i.e. eine Verbindung zweier Knoten dar
 * @author Sebastian Schlesinger
 * @version 0.1
 *
 */
public class edge  implements Cloneable{

	private Element origin;
	private Element target;
	
	public edge(Element e1, Element e2) {
		origin=e1;
		target=e2;
	}
	
	public void set_origin(Element e) {
		origin=e;
	}
	
	public Element get_origin() {
		return origin;
	}
	
	public void set_target(Element e) {
		target=e;
	}
	
	public Element get_target() {
		return target;
	}
	
	public boolean equals(edge e) {
		return e.get_origin().equals(this.origin) && e.get_target().equals(this.target);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new edge((Element)origin.clone(), (Element)target.clone());
	}
	
	public String toString() {
		return "Kante von " + origin.toString() + " nach " + target.toString();
	}
}
