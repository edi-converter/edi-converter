import java.io.*;

public class Vorschau {
    public static String zeigeVorschau(File ediFile, int maxZeilen) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(ediFile))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < maxZeilen) {
                sb.append(line).append("\n");
                count++;
            }
        } catch (IOException e) {
            sb.append("Fehler beim Lesen: ").append(e.getMessage());
        }
        return sb.toString();
    }
}
