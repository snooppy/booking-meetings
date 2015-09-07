package ua.cn.dmitrykrivenko.booking.meetings;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 *
 * @author Dmitry Krivenko <dmitrykrivenko at gmail.com>
 */
public class MeetingCalendar {

	private final LocalTime startWorkTime;
	private final LocalTime endWorkTime;

	private final Map<LocalDate, Set<Booking>> bookings;

	public MeetingCalendar(LocalTime startWorkTime, LocalTime endWorkTime) {
		this.startWorkTime = startWorkTime;
		this.endWorkTime = endWorkTime;
		bookings = new TreeMap<>();
	}

	public boolean bookMeeting(BookingRequest bookingRequest) {
		LocalDateTime submissionTime = bookingRequest.getSubmissionTime();
		LocalDate meetingStartDate = bookingRequest.getMeetingStartTime().toLocalDate();
		LocalTime meetingStartTime = bookingRequest.getMeetingStartTime().toLocalTime();
		LocalTime meetingEndTime = meetingStartTime.plusHours(bookingRequest.getMeetingDuration());
		
		if (!inRangeOfWorkingHours(meetingStartTime, meetingEndTime)) {
			return false;
		}

		Set<Booking> localBookings = bookings.get(meetingStartDate);
		if (localBookings == null) {
			localBookings = new TreeSet<>();
			Booking booking = new Booking(submissionTime, new Meeting(meetingStartDate, meetingStartTime, meetingEndTime, bookingRequest.getEmployeeId()));
			localBookings.add(booking);
			bookings.put(meetingStartDate, localBookings);
			return true;
		} else {
			for (Iterator<Booking> iterator = localBookings.iterator(); iterator.hasNext();) {
				Booking booking = iterator.next();
				if (meetingStartTime.equals(booking.getMeeting().getStartTime())) {
					if (submissionTime.isBefore(booking.getSubmissionTime())) {
						iterator.remove();
						break;
					} else {
						return false;
					}
				}
				if ((meetingStartTime.isAfter(booking.getMeeting().getStartTime()) && meetingStartTime.isBefore(booking.getMeeting().getEndTime()))
					|| (meetingEndTime.isAfter(booking.getMeeting().getStartTime()) && meetingEndTime.isBefore(booking.getMeeting().getEndTime()))) {
					if (submissionTime.isBefore(booking.getSubmissionTime())) {
						iterator.remove();
					} else {
						return false;
					}
				}
			}
			Booking booking = new Booking(submissionTime, new Meeting(meetingStartDate, meetingStartTime, meetingEndTime, bookingRequest.getEmployeeId()));
			localBookings.add(booking);
			return true;
		}
	}

	public Map<LocalDate, Set<Meeting>> getMeetings() {
		return bookings.entrySet().stream()
			.collect(Collectors.toMap(
					entry -> entry.getKey(),
					entry -> entry.getValue().stream()
					.map(booking -> booking.getMeeting())
					.collect(Collectors.toCollection(TreeSet::new)),
					(v1, v2) -> v1,
					TreeMap::new));
	}

	private boolean inRangeOfWorkingHours(LocalTime meetingStartTime, LocalTime meetingEndTime) {
		return !(meetingStartTime.isBefore(startWorkTime) || meetingEndTime.isAfter(endWorkTime));
	}

}
