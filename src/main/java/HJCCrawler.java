import com.github.kevinsawicki.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;

public class HJCCrawler {
    private static final String HC_BASE_URL = "http://www.co.harney.or.us";
    private static final String HC_ROSTER_PATH = "/index.php/features/sheriff-s-office/inmate-rosters";
    private static final String USER_AGENT_IDENTIFER = "Mozilla/5.0";

    public static void main(String[] args) {
        FileOutputStream stream = null;
        String filename;
        String html;
        Document doc;
        Elements elements;
        Element currentRoster;
        Element rosterAnchor;
        String rosterDate;
        String currentRosterPath;
        byte[] pdf;

        System.out.println("Fetching roster html...");
        html = HttpRequest.get(HC_BASE_URL + HC_ROSTER_PATH)
                .userAgent(USER_AGENT_IDENTIFER)
                .accept("text/html")
                .body();
        System.out.println("Got roster html.");

        doc = Jsoup.parse(html);
        elements = doc.getElementsByClass("style572");

        // Get element containing html anchor to roster pdf
        currentRoster = elements.get(2);

        // Get anchor element
        rosterAnchor = currentRoster.getAllElements().get(1);

        // Get path from anchor and encode space(s)
        currentRosterPath = rosterAnchor.attr("href").replace(" ", "%20");

        // Get roster pdf date
        rosterDate = rosterAnchor.text();

        System.out.println("Roster PDF Date: " + rosterDate);
        System.out.println("Roster PDF Absolute Path: " + (HC_BASE_URL + currentRosterPath));

        // Get pdf
        System.out.println("Fetching roster pdf...");
        pdf = HttpRequest.get(HC_BASE_URL + currentRosterPath)
                .userAgent(USER_AGENT_IDENTIFER)
                .accept("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .bytes();
        System.out.println("Got roster pdf.");

        // Write pdf to file
        try {
            filename = rosterDate
                    .replace(" ", "-")
                    .replace(",", ""); // TODO regex expression

            stream = new FileOutputStream(filename + ".pdf");
            stream.write(pdf);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
