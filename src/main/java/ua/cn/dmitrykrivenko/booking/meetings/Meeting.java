package ua.cn.dmitrykrivenko.booking.meetings;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 *
 * @author Dmitry Krivenko <dmitrykrivenko at gmail.com>
 */
public class Meeting implements Comparable<Meeting> {

	private final LocalDate date;
	private final LocalTime startTime;
	private final LocalTime endTime;
	private final String employeeId;

	public Meeting(LocalDate date, LocalTime startTime, LocalTime endTime, String employeeId) {
		Objects.requireNonNull(date, "date must be not null");
		Objects.requireNonNull(startTime, "startTime must be not null");
		Objects.requireNonNull(endTime, "endTime must be not null");
		Objects.requireNonNull(employeeId, "employeeId must be not null");

		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.employeeId = employeeId;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	@Override
	public int compareTo(Meeting meeting) {
		int dateDiff = date.compareTo(meeting.date);
		if (dateDiff != 0) {
			return dateDiff;
		}
		return startTime.compareTo(meeting.startTime);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Meeting)) {
			return false;
		}
		if (!super.equals(object)) {
			return false;
		}
		final Meeting that = (Meeting) object;

		return date.equals(that.date)
			&& startTime.equals(that.startTime)
			&& endTime.equals(that.endTime)
			&& employeeId.equals(that.employeeId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + date.hashCode();
		result = prime * result + startTime.hashCode();
		result = prime * result + endTime.hashCode();
		result = prime * result + employeeId.hashCode();
		return result;
	}

}
