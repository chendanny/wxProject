package org.stan.yxgz.cuitl;

public class ServiceError extends RuntimeException {

	private static final long serialVersionUID = -1885940518100822273L;

	public ServiceError(String message) {
		super(message);
	}
	
	private String fieldName;
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	

}
