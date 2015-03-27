package utils;

import org.springframework.dao.DataAccessException;

public class DataBaseIsFullException extends DataAccessException {
	
	public DataBaseIsFullException (String message) {
		super(message);
	}
}
