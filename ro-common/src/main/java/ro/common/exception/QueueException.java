package ro.common.exception;

/**
 * Custom exception for kafka queue issues
 *
 */
public class QueueException extends Exception {

	public QueueException(String message) {
		super(message);
	}

	public QueueException(String message, Throwable e) {
		super(message, e);
	}

}
