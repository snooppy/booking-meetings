package ua.cn.dmitrykrivenko.booking.meetings;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Dmitry Krivenko <dmitrykrivenko at gmail.com>
 */
public class BookMeetingProcessor {

	private static final String WORK_HOURS_TEMPLATE = "(?<workStartHH>\\d{2})(?<workStartMM>\\d{2}) (?<workEndHH>\\d{2})(?<workEndMM>\\d{2})";
	private static final Pattern workHoursPattern = Pattern.compile(WORK_HOURS_TEMPLATE);

	private static final String REQUEST_SUBMISSION_TEMPLATE = "(?<submissiontime>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) (?<employeeid>[\\S]+)";
	private static final Pattern requestSubmissionPattern = Pattern.compile(REQUEST_SUBMISSION_TEMPLATE);

	private static final String MEETING_TEMPLATE = "(?<starttime>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}) (?<duration>[0-9]+)";
	private static final Pattern meetingPattern = Pattern.compile(MEETING_TEMPLATE);

	public String bookMeetings(List<String> bookings) throws BookMeetingException {
		if (bookings.isEmpty()) {
			throw new BookMeetingException("File with booking requests is empty");
		}
		Matcher workHoursMatcher = workHoursPattern.matcher(bookings.get(0));
		if (!workHoursMatcher.matches()) {
			throw new BookMeetingException("Working hours don't match template [HHmm HHmm]");
		}
		int workStartHH = Integer.parseInt(workHoursMatcher.group("workStartHH"));
		int workStartMM = Integer.parseInt(workHoursMatcher.group("workStartMM"));
		int workEndHH = Integer.parseInt(workHoursMatcher.group("workEndHH"));
		int workEndMM = Integer.parseInt(workHoursMatcher.group("workEndMM"));

		LocalTime workStart = LocalTime.of(workStartHH, workStartMM);
		LocalTime workEnd = LocalTime.of(workEndHH, workEndMM);

		MeetingCalendar bookingCalendar = new MeetingCalendar(workStart, workEnd);

		int i = 1;
		while (i < bookings.size() - 1) {
			Matcher requestSubmissionMatcher = requestSubmissionPattern.matcher(bookings.get(i));
			if (!requestSubmissionMatcher.matches()) {
				throw new BookMeetingException("Request submission" + "[" + bookings.get(i) + "] doesn't match template [yyyy-MM-dd HH:mm:ss employeeId]");
			}
			Matcher meetingMatcher = meetingPattern.matcher(bookings.get(i + 1));
			if (!meetingMatcher.matches()) {
				throw new BookMeetingException("Meeting" + "[" + bookings.get(i + 1) + "] doesn't match template [yyyy-MM-dd HH:mm:ss duration]");
			}

			String submissionTimeStr = requestSubmissionMatcher.group("submissiontime");
			LocalDateTime submissionTime = LocalDateTime.parse(submissionTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			String employeeId = requestSubmissionMatcher.group("employeeid");

			String meetingStartTimeStr = meetingMatcher.group("starttime");
			LocalDateTime meetingStartTime = LocalDateTime.parse(meetingStartTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

			int duration = Integer.parseInt(meetingMatcher.group("duration"));

			BookingRequest bookingRequest = new BookingRequest(submissionTime, employeeId, meetingStartTime, duration);
			bookingCalendar.bookMeeting(bookingRequest);

			i += 2;
		}
		StringBuilder meetings = new StringBuilder();
		bookingCalendar.getMeetings().entrySet().stream().forEach((entrySet) -> {
			LocalDate key = entrySet.getKey();
			Set<Meeting> value = entrySet.getValue();
			meetings.append(key).append(System.lineSeparator());
			value.stream().forEach((meeting) -> {
				meetings.append(String.format("%s %s %s", meeting.getStartTime(), meeting.getEndTime(), meeting.getEmployeeId())).append(System.lineSeparator());
			});
		});
		return meetings.toString();
	}

}
