package ua.cn.dmitrykrivenko.booking.meetings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Dmitry Krivenko <dmitrykrivenko at gmail.com>
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Please specify the path to file with booking requests");
			System.exit(1);
		}
		Path path = Paths.get(args[0]);

		try (Stream<String> lines = Files.lines(path)) {
			List<String> bookings = lines.map(booking -> booking.trim()).collect(Collectors.toList());
			String meetings = new BookMeetingProcessor().bookMeetings(bookings);
			System.out.println(meetings);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

}
