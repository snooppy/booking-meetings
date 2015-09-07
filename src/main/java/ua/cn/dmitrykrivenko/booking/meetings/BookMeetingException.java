package ua.cn.dmitrykrivenko.booking.meetings;

/**
 *
 * @author Dmitry Krivenko <dmitrykrivenko at gmail.com>
 */
public class BookMeetingException extends Exception {

	private static final long serialVersionUID = -5256826605080717166L;

	public BookMeetingException() {
		super();
	}

	public BookMeetingException(String message) {
		super(message);
	}

	public BookMeetingException(Throwable cause) {
		super(cause);
	}

	public BookMeetingException(String message, Throwable cause) {
		super(message, cause);
	}

}
