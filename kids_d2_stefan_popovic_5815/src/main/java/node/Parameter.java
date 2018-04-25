package node;

// TODO add documentation and change name to NodeParameter
public class Parameter {
	
	public static final Class<?>[] VALID_TYPES = new Class<?>[] {
		String.class,
		Integer.class,
		Double.class,
		Boolean.class,
		Enum.class,
	};
	
	private Object value;
	
	private Class<?> type;
	
	public Parameter(Class<?> type) {
		checkIsTypeValid(type);
		this.type = type;
	}
	
	private void checkIsTypeValid(Class<?> type) {
		if(type.isEnum()) {
			return;
		}
		
		for(Class<?> clazz : VALID_TYPES) {
			if(type.isAssignableFrom(clazz)) {
				return;
			}
		}
		
		throw new RuntimeException("Node parameters cannot be " + type.getName());
	}
	
	public void setValue(Object value) {
		if(!type.isInstance(value)) {
			throw new RuntimeException(
				"Parameter requires " + type.getName() + " but " + value.getClass().getName() + " given"
			);
		}
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public Class<?> getType() {
		return type;
	}
	
}
