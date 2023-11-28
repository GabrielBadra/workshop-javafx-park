package model.exceptions;

public class StringEqualsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public StringEqualsException(String msg) {
		super(msg);
	}
}
