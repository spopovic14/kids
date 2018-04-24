package model;

/**
 * Represents a single parameter used by a Node. The type of a parameter can only
 * belong to VALID_TYPES.
 * @author stefan
 *
 * @param <T>
 */
public class NodeParameter <T> {
	
	/**
	 * Only these values will be accepted as node parameters
	 */
	public static final Class<?>[] VALID_TYPES = new Class<?>[] {
		String.class,
		Integer.class,
		Double.class,
		Boolean.class,
		Enum.class,
	}; 
	
	/**
	 * Value of the parameter
	 */
	private T value;
	
	/**
	 * Constructs a NodeParameter with the given value. A RuntimeException will be
	 * thrown if the parameter type isn't allowed
	 * @param value
	 */
	public NodeParameter(T value) {
		checkIsTypeValid(value);
		this.value = value;
	}
	
	/**
	 * Sets the value of this Parameter. A RuntimeException will be thrown if the
	 * parameter type isn't allowed
	 * @param value
	 */
	public void setValue(T value) {
		checkIsTypeValid(value);
		this.value = value;
	}
	
	/**
	 * Standard value getter
	 * @return
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * Checks if the type is valid and throws a RuntmeException if it isn't
	 * @param value
	 */
	private void checkIsTypeValid(T value) {
		for(Class<?> type : VALID_TYPES) {
			if(type.isInstance(value)) {
				return;
			}
		}
		
		throw new RuntimeException("Node parameters cannot be " + value.getClass().getName());
	}
	
}
