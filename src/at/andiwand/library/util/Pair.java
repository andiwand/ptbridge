package at.andiwand.library.util;


public class Pair<E> {
	
	private E elementA;
	private E elementB;
	
	
	public Pair(E elementA, E elementB) {
		this.elementA = elementA;
		this.elementB = elementB;
	}
	
	
	public String toString() {
		return "{" + elementA + "; " + elementB + "}";
	}
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof Pair<?>)) return false;
		Pair<?> pair = (Pair<?>) obj;
		
		return (elementA.equals(pair.elementA) && elementB.equals(pair.elementB))
				|| (elementA.equals(pair.elementB) && elementB.equals(pair.elementA));
	}
	public int hashCode() {
		return elementA.hashCode() ^ elementB.hashCode();
	}
	
	public E getElementA() {
		return elementA;
	}
	public E getElementB() {
		return elementB;
	}
	
}