package Address.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    /** Der Datentypumwandler. */
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
	 * Gibt den Wert vom (noch) DAtentyp LocalData als String zurück. Als vorlage
	 * wird das oben deklarierte DATE_PATTERN verwendet.
	 * 
	 */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    /**
	 * Wandelt den String in ein LocalDate Objekt um.
	 * 
	 * Wenn der String nicht konvertiert werden kann, wird null zurückgegeben.
	 */
    public static LocalDate parse(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
	 * Überprüft ob das Datum auch wirklich richtig ist.
	 * 
	 */
    public static boolean validDate(String dateString) {
    	// Versucht den String zu parsen.
        return DateUtil.parse(dateString) != null;
    }
}