package ua.cn.dmitrykrivenko.booking.meetings;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Dmitry Krivenko <dmitrykrivenko at gmail.com>
 */
public final class BookingRequest {

	private final LocalDateTime submissionTime;
	private final String employeeId;

	private final LocalDateTime meetingStartTime;
	private final int meetingDuration;

	/**
	 *
	 * @param submissionTime request submission time
	 * @param employeeId employee id
	 * @param meetingStartTime meeting start time
	 * @param meetingDuration meeting duration in hours
	 */
	public BookingRequest(LocalDateTime submissionTime, String employeeId, LocalDateTime meetingStartTime, int meetingDuration) {
		Objects.requireNonNull(submissionTime, "submissionTime must be not null");
		Objects.requireNonNull(employeeId, "employeeId must be not null");
		Objects.requireNonNull(meetingStartTime, "meetingStartTime must be not null");
		if (meetingDuration <= 0) {
			throw new IllegalArgumentException("Incorrect value of meetingDuration [" + meetingDuration + "] - must be greater than 0");
		}

		this.submissionTime = submissionTime;
		this.employeeId = employeeId;
		this.meetingStartTime = meetingStartTime;
		this.meetingDuration = meetingDuration;
	}

	public LocalDateTime getSubmissionTime() {
		return submissionTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public LocalDateTime getMeetingStartTime() {
		return meetingStartTime;
	}

	public int getMeetingDuration() {
		return meetingDuration;
	}

}
