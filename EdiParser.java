import java.io.*;

public class EdiParser {
    // Gibt Zahl der exportierten Zeilen zurück
    public static int parseAndWriteCsv(File ediFile, File csvFile, char sep) throws IOException {
        StringBuilder ediData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(ediFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ediData.append(line);
            }
        }

        String[] segments = ediData.toString().split("'");
        int count = 0;

        try (PrintWriter writer = new PrintWriter(csvFile, "UTF-8")) {
            writer.println("Segment" + sep + "Field1" + sep + "Field2" + sep + "Field3" + sep + "Field4" + sep + "Field5");
            for (String seg : segments) {
                if (seg.trim().isEmpty()) continue;
                String[] fields = seg.split("\\+");
                writer.print(fields[0]);
                for (int i = 1; i <= 5; i++) {
                    writer.print(sep);
                    if (fields.length > i) writer.print(fields[i]);
                }
                writer.println();
                count++;
            }
        }
        return count;
    }
}
