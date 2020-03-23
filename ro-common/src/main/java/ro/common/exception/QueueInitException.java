package ro.common.exception;

/**
 * Custom exception for kafka queue init
 *
 */
public class QueueInitException extends RuntimeException {

	public QueueInitException(String message) {
		super(message);
	}

	public QueueInitException(String message, Throwable e) {
		super(message, e);
	}

}
