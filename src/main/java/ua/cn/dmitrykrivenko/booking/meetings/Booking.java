package ua.cn.dmitrykrivenko.booking.meetings;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Decorator for {@link Meeting} with one extra field submissionTime.
 *
 * @author Dmitry Krivenko <dmitrykrivenko at gmail.com>
 */
final class Booking implements Comparable<Booking> {

	private final LocalDateTime submissionTime;

	private final Meeting meeting;

	public Booking(LocalDateTime submissionTime, Meeting meeting) {
		Objects.requireNonNull(submissionTime, "submissionTime must be not null");
		Objects.requireNonNull(meeting, "meeting must be not null");

		this.submissionTime = submissionTime;
		this.meeting = meeting;
	}

	public LocalDateTime getSubmissionTime() {
		return submissionTime;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	@Override
	public int compareTo(Booking booking) {
		return meeting.compareTo(booking.getMeeting());
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Booking)) {
			return false;
		}
		if (!super.equals(object)) {
			return false;
		}
		final Booking that = (Booking) object;

		return submissionTime.equals(that.submissionTime) && meeting.equals(that.meeting);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + submissionTime.hashCode();
		result = prime * result + meeting.hashCode();
		return result;
	}

}
