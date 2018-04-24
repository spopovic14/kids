package node;

public class Parameter<T> {
	
	public static final Class<?>[] VALID_TYPES = new Class<?>[] {
		String.class,
		Integer.class,
		Double.class,
		Boolean.class,
		Enum.class,
	};
	
	private T value;
	
	private Class<T> type;
	
	public Parameter(Class<T> type) {
		checkIsTypeValid(type);
		this.type = type;
	}
	
	private void checkIsTypeValid(Class<T> type) {
		for(Class<?> clazz : VALID_TYPES) {
			if(type.isAssignableFrom(clazz)) {
				return;
			}
		}
		
		throw new RuntimeException("Node parameters cannot be " + type.getName());
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public Class<T> getType() {
		return type;
	}
	
}
