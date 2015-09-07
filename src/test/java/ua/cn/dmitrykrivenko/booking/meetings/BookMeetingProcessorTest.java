package ua.cn.dmitrykrivenko.booking.meetings;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Dmitry Krivenko <dmitrykrivenko at gmail.com>
 */
public class BookMeetingProcessorTest {

	private final BookMeetingProcessor bookMeetingProcessor = new BookMeetingProcessor();

	@Test
	public void shouldNotBookBookingWithOutOfWorkingHoursRange() throws BookMeetingException {
		String bookingRequest = "0900 1730\n"
			+ "2011-03-17 10:17:06 EMP001\n"
			+ "2011-03-21 09:00 2\n"
			+ "2011-03-16 12:34:56 EMP002\n"
			+ "2011-03-21 08:00 2\n";
		List<String> bookingRequests = Arrays.asList(bookingRequest.split("\n"));
		String meetings = bookMeetingProcessor.bookMeetings(bookingRequests);

		String expectedMeetings = "2011-03-21\n"
			+ "09:00 11:00 EMP001\n";

		Assert.assertEquals(expectedMeetings, meetings);
	}

	@Test
	public void shouldNotBook2ndMeetingWithTheSameMeetingTimeAndLaterSubmissionTime() throws BookMeetingException {
		String bookingRequest = "0900 1730\n"
			+ "2011-03-17 10:17:06 EMP001\n"
			+ "2011-03-21 09:00 2\n"
			+ "2011-03-18 12:34:56 EMP002\n"
			+ "2011-03-21 09:00 2";
		List<String> bookingRequests = Arrays.asList(bookingRequest.split("\n"));
		String meetings = bookMeetingProcessor.bookMeetings(bookingRequests);

		String expectedMeetings = "2011-03-21\n"
			+ "09:00 11:00 EMP001\n";

		Assert.assertEquals(expectedMeetings, meetings);
	}

	@Test
	public void shouldReplaceMeetingWithTheSameMeetingTimeByMeetingWithEarlierSubmissionTime() throws BookMeetingException {
		String bookingRequest = "0900 1730\n"
			+ "2011-03-17 10:17:06 EMP001\n"
			+ "2011-03-21 09:00 2\n"
			+ "2011-03-16 12:34:56 EMP002\n"
			+ "2011-03-21 09:00 2";
		List<String> bookingRequests = Arrays.asList(bookingRequest.split("\n"));
		String meetings = bookMeetingProcessor.bookMeetings(bookingRequests);

		String expectedMeetings = "2011-03-21\n"
			+ "09:00 11:00 EMP002\n";

		Assert.assertEquals(expectedMeetings, meetings);
	}

	@Test
	public void shouldReplaceMeetingInCaseOfOverlapByMeetingWithEarlierSubmissionTime() throws BookMeetingException {
		String bookingRequest = "0900 1730\n"
			+ "2011-03-17 10:17:06 EMP001\n"
			+ "2011-03-21 09:00 2\n"
			+ "2011-03-16 12:34:56 EMP002\n"
			+ "2011-03-21 09:00 2\n"
			+ "2011-03-16 09:28:23 EMP003\n"
			+ "2011-03-21 10:00 2\n"
			+ "2011-03-17 10:17:06 EMP004\n"
			+ "2011-03-22 16:00 1";
		List<String> bookingRequests = Arrays.asList(bookingRequest.split("\n"));
		String meetings = bookMeetingProcessor.bookMeetings(bookingRequests);

		String expectedMeetings = "2011-03-21\n"
			+ "10:00 12:00 EMP003\n"
			+ "2011-03-22\n"
			+ "16:00 17:00 EMP004\n";

		Assert.assertEquals(expectedMeetings, meetings);
	}

	@Test
	public void shouldGroupMeetingsChronologicallyByDay() throws BookMeetingException {
		String bookingRequest = "0900 1730\n"
			+ "2011-03-17 10:17:06 EMP001\n"
			+ "2011-03-21 09:00 2\n"
			+ "2011-03-16 12:34:56 EMP002\n"
			+ "2011-03-21 09:00 2\n"
			+ "2011-03-16 09:28:23 EMP003\n"
			+ "2011-03-22 14:00 2\n"
			+ "2011-03-17 10:17:06 EMP004\n"
			+ "2011-03-22 16:00 1\n"
			+ "2011-03-15 17:29:12 EMP005\n"
			+ "2011-03-21 16:00 3";
		List<String> bookingRequests = Arrays.asList(bookingRequest.split("\n"));
		String meetings = bookMeetingProcessor.bookMeetings(bookingRequests);

		String expectedMeetings = "2011-03-21\n"
			+ "09:00 11:00 EMP002\n"
			+ "2011-03-22\n"
			+ "14:00 16:00 EMP003\n"
			+ "16:00 17:00 EMP004\n";

		Assert.assertEquals(expectedMeetings, meetings);
	}

}
