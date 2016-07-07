package hackerrank.reuse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Node<ID, T> {
	private List<Node<? extends ID, ? extends T>> edges = new LinkedList<>();
	private HashMap<Node<? extends ID, ? extends T>, Number> edgeCosts = new HashMap<>();
		
	private final ID id;
	private T value;
	
	
	public Node(ID id, T value) {
		this.id = id;
		this.value = value;		
	}
	
	public Node(ID id, T value, Class<? extends List<Node<? extends ID, ? extends T>>> edgesListClass)  {
		this(id, value);
		try {
			this.edges = edgesListClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException("Could not instantiate edgesListClass '" + edgesListClass + "': " + e.getMessage(), e);
		}
	}
	
	public ID getId() {
		return id;
	}
	
	public T getValue() {
		return value;
	}	
	
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
