package at.andiwand.library.util;


public class BinaryTupel<E> {
	
	private E elementA;
	private E elementB;
	
	
	public BinaryTupel(E elementA, E elementB) {
		this.elementA = elementA;
		this.elementB = elementB;
	}
	
	
	public String toString() {
		return "{" + elementA + ", " + elementB + "}";
	}
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof BinaryTupel<?>)) return false;
		BinaryTupel<?> tupel = (BinaryTupel<?>) obj;
		
		return (elementA.equals(tupel.elementA) && elementB
				.equals(tupel.elementB));
	}
	public int hashCode() {
		return elementA.hashCode() + 31 * elementB.hashCode();
	}
	
	public E getElementA() {
		return elementA;
	}
	public E getElementB() {
		return elementB;
	}
	
}