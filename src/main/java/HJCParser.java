import exceptions.InvalidInmateLineException;
import exceptions.UnsupportedLineStateException;
import models.Charge;
import models.Inmate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HJCParser {
    private static final int HEADER_OFFSET = 8;
    private static final int FOOTER_OFFSET = 5;
    private static final String START_OF_PAGE_MARKER = "HARNEY COUNTY SHERIFF";
    private static final String END_OF_PAGE_MARKER = "Printed By/On";
    private static final int NO_OPT_ATTR_SIZE = 7;
    private static final int ONE_OPT_ATTR_SIZE = 8;
    private static final int TWO_OPT_ATTR_SIZE = 9;
    private static final int THREE_OPT_ATTR_SIZE = 10;

    enum LineState {
        AT_EMPTY_LINE,
        AT_HEADER,
        AT_FOOTER,
        AT_INMATE,
        AT_CHARGE
    }

    /**
     * Checks if given string is a common suffix
     *
     * @param input Potential suffix
     * @return t/f
     */
    private static boolean isCommonSuffix(final String input) {
        final String[] COMMON_SUFFIXES = {
                "II", "III", "IV", "JR.", "SR."
        };

        for (String suffix : COMMON_SUFFIXES) {
            if (suffix.equals(input)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Extract information from line containing inmate information
     * POSSIBLE LINE FORMATS:
     * 7 - NO_OPT_ATTR_SIZE
     * LAST, 	FIRST 			GENDER 	RACE 	DATE 	BOOKING INCIDENT
     * <p>
     * 8 - ONE_OPT_ATTR_SIZE
     * LAST, 	FIRST 	MIDDLE 	GENDER 	RACE 	DATE 	BOOKING INCIDENT
     * LAST, 	FIRST 	SFX 	GENDER 	RACE 	DATE 	BOOKING INCIDENT
     * LAST, 	FIRST 	DOB		GENDER 	RACE 	DATE 	BOOKING	INCIDENT
     * <p>
     * 9 - TWO_OPT_ATTR_SIZE
     * LAST, 	FIRST 	MIDDLE	SFX 	GENDER 	RACE 	DATE 	BOOKING INCIDENT
     * LAST, 	FIRST 	MIDDLE 	DOB 	RACE 	DATE 	BOOKING INCIDENT
     * LAST, 	FIRST 	SFX		DOB 	RACE 	DATE 	BOOKING	INCIDENT
     * <p>
     * 10 - THREE_OPT_ATTR_SIZE
     * LAST, 	FIRST 	MIDDLE	SFX 	DOB		GENDER 	RACE 	DATE 	BOOKING INCIDENT
     *
     * @param line Inmate line
     */
    private static Inmate parseInmateLine(final String line) throws InvalidInmateLineException {
        Inmate inmate;
        String firstName;
        String middleName = null; // TODO @Nullable?
        String lastName;
        String suffix = null; // TODO @Nullable?
        String dob = null; // TODO @Nullable?
        Inmate.Gender gender;
        Inmate.Race race;
        String date;
        String bookingNumber;
        String incidentTag;
        String[] attrs;
        int numAttrs;

        //System.out.println(line);
        attrs = line.split("[ ]+");
        numAttrs = attrs.length;

        // Always the same regardless of number of attributes

        lastName = attrs[0].substring(0, attrs[0 ].length() - 1); // "Ignore trailing ,"
        firstName = attrs[1];
        switch (attrs[numAttrs - 5]) {
            case "M":
                gender = Inmate.Gender.MALE;
                break;
            case "F":
                gender = Inmate.Gender.FEMALE;
                break;
            default:
                gender = Inmate.Gender.OTHER;
        }
        switch (attrs[numAttrs - 4]) {
            case "W":
                race = Inmate.Race.WHITE;
                break;
            case "B":
                race = Inmate.Race.BLACK;
                break;
            case "H":
                race = Inmate.Race.HISPANIC;
                break;
            case "A":
                race = Inmate.Race.ASIAN;
                break;
            case "I":
                race = Inmate.Race.NATIVE;
                break;
            default:
                race = Inmate.Race.OTHER;
        }
        date = attrs[numAttrs - 3];
        bookingNumber = attrs[numAttrs - 2];
        incidentTag = attrs[numAttrs - 1];

        // Handle "optional" attributes

        String attr;
        switch (numAttrs) {
            case NO_OPT_ATTR_SIZE:
                // noop
                break;
            case ONE_OPT_ATTR_SIZE:
                attr = attrs[2];

                if (isCommonSuffix(attr)) {
                    suffix = attr;
                } else if (attr.contains("/")) { // is DOB
                    dob = attr;
                } else { // assume middle name
                    middleName = attr;
                }

                break;
            case TWO_OPT_ATTR_SIZE:
                attr = attrs[2];

                if (isCommonSuffix(attr)) { // next attr must be dob
                    suffix = attrs[2];
                    dob = attrs[3];
                } else { // assume middle name
                    middleName = attrs[2];

                    if (attrs[3].contains("/")) { // next attr is dob
                        dob = attrs[3];
                    } else { // next attr is suffix
                        suffix = attrs[3];
                    }
                }

                break;
            case THREE_OPT_ATTR_SIZE:
                middleName = attrs[2];
                suffix = attrs[3];
                dob = attrs[4];

                break;
            default:
                throw new InvalidInmateLineException(line);
        }

        inmate = new Inmate(
                firstName,
                middleName,
                lastName,
                suffix,
                dob,
                gender,
                race,
                date,
                bookingNumber,
                incidentTag
        );

        //System.out.println(inmate.toString());

        return inmate;
    }

    /**
     * Extract information from line containing charge information
     *
     * EXAMPLE:
     * 0    1           2   3       ...     n
     *      STATUTE	    -   NAME 	?...    BAIL
     * @param line Charge line
     */
    private static Charge parseChargeLine(final String line) {
        Charge charge;
        String statute;
        String name;
        Float bailAmount;
        String[] attrs;
        int numAttrs;

        //System.out.println(line);
        attrs = line.split("[ ]+");
        numAttrs = attrs.length;

        statute = attrs[1];
        name = attrs[3];
        bailAmount = Float.valueOf(
                attrs[numAttrs - 1]
                    .replace("$", "")
                    .replace(",", ""));

        // If name has spaces
        if (numAttrs > 5) {
            // Build the rest of the charge name
            for (int i = 4; i < numAttrs - 1; i++) {
                name += " " + attrs[i];
            }
        }

        charge = new Charge(
                statute,
                name,
                bailAmount
        );

        return charge;
    }

    /**
     * Determine line state based off line contents
     * TODO example
     *
     * @param line
     * @return LineState
     * @throws UnsupportedLineStateException bad line
     */
    private static LineState determineLineState(final String line) throws UnsupportedLineStateException {
        if (line.isEmpty() || line.contains("***,***.**")) {
            return LineState.AT_EMPTY_LINE;
        } else if (line.startsWith(START_OF_PAGE_MARKER)) {
            return LineState.AT_HEADER;
        } else if (line.startsWith(END_OF_PAGE_MARKER)) {
            return LineState.AT_FOOTER;
        } else if (Character.isLetter(line.charAt(0))) {
            return LineState.AT_INMATE;
        } else if (line.startsWith(" ")) {
            return LineState.AT_CHARGE;
        } else {
            throw new UnsupportedLineStateException(line);
        }
    }

    /**
     * @param filename
     * @return File as array
     * @throws IOException e
     */
    private static ArrayList<String> readFileAsArray(final String filename) throws IOException {
        ArrayList<String> array = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                array.add(line);
            }
        }

        return array;
    }

    /**
     * Main
     *
     * @param args argv
     */
    public static void main(String[] args) {
        ArrayList<String> lines = null;
        ArrayList<Inmate> inmates = new ArrayList<>();
        Inmate currentInmate = null;

        // Get file
        try {
            lines = readFileAsArray("./Census-4-4-18.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Parse file lines
        for (int i = HEADER_OFFSET; i < lines.size(); i++) {
            final String line = lines.get(i);

            // Determine line state
            LineState state = null;
            try {
                state = determineLineState(line);
            } catch (UnsupportedLineStateException e) {
                e.printStackTrace();
                System.exit(1);
            }

            // Handle line state
            switch (state) {
                case AT_EMPTY_LINE:
                    // Skip line
                    break;
                case AT_HEADER:
                    i += HEADER_OFFSET - 1; // Skip ahead
                    break;
                case AT_FOOTER:
                    i += FOOTER_OFFSET - 1; // Skip ahead
                    break;
                case AT_INMATE:
                    if (currentInmate != null) {
                        inmates.add(currentInmate);
                    }

                    try {
                        currentInmate = parseInmateLine(line);
                    } catch (InvalidInmateLineException e) {
                        currentInmate = null;
                        e.printStackTrace();
                    }

                    break;
                case AT_CHARGE:
                    if (currentInmate != null) {
                        Charge charge = parseChargeLine(line);
                        currentInmate.addCharge(charge);
                    }

                    break;
                default:
                    System.out.println("Unsupported line state");
                    System.exit(1);
            }
        }

        System.out.println(inmates.toString());
    }
}
