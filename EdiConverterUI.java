import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class EdiConverterUI {
    private JFrame frame;
    private JLabel ediLabel, csvLabel, statusLabel;
    private File ediFile, csvFile;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EdiConverterUI::new);
    }

    public EdiConverterUI() {
        frame = new JFrame("EDI2Excel – 100% offline");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        JPanel panel = new JPanel();
        panel.setBackground(new Color(246, 249, 252));
        panel.setBorder(new EmptyBorder(34, 48, 34, 48));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("EDI/EDIFACT/X12 ➔ Excel/CSV Konverter");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(22, 50, 92));

        JLabel subtitle = new JLabel("100% offline, keine Cloud, keine Datenübertragung.");
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        subtitle.setForeground(new Color(90, 108, 138));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));

        // Labels für Dateipfade
        ediLabel = new JLabel("Noch keine Datei gewählt...");
        ediLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        ediLabel.setOpaque(true);
        ediLabel.setBackground(Color.WHITE);
        ediLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(7, 8, 7, 8)
        ));
        ediLabel.setPreferredSize(new Dimension(420, 32));

        JButton ediBtn = niceButton("EDI-Datei wählen");
        ediBtn.setPreferredSize(new Dimension(170, 36));
        ediBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("EDI/EDIFACT/X12-Datei auswählen");
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                ediFile = fc.getSelectedFile();
                ediLabel.setText(shortenPath(ediFile.getAbsolutePath(), 48));
                ediLabel.setToolTipText(ediFile.getAbsolutePath());
                status("EDI-Datei gewählt.", false);
            }
        });

        csvLabel = new JLabel("Noch kein Ziel gewählt...");
        csvLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        csvLabel.setOpaque(true);
        csvLabel.setBackground(Color.WHITE);
        csvLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(7, 8, 7, 8)
        ));
        csvLabel.setPreferredSize(new Dimension(420, 32));

        JButton csvBtn = niceButton("Ziel (Excel/CSV) wählen");
        csvBtn.setPreferredSize(new Dimension(170, 36));
        csvBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Ziel für exportierte CSV-Datei auswählen");
            fc.setSelectedFile(new File("export.csv"));
            if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File chosen = fc.getSelectedFile();
                String path = chosen.getAbsolutePath();
                if (!path.toLowerCase().endsWith(".csv")) path += ".csv";
                csvFile = new File(path);
                csvLabel.setText(shortenPath(csvFile.getAbsolutePath(), 48));
                csvLabel.setToolTipText(csvFile.getAbsolutePath());
                status("Export-Ziel gewählt.", false);
            }
        });

        // Felder und Buttons
        JPanel filePanel = new JPanel(new GridBagLayout());
        filePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.gridx = 0; gbc.gridy = 0; filePanel.add(new JLabel("EDI-Datei:"), gbc);
        gbc.gridx = 1; filePanel.add(ediLabel, gbc);
        gbc.gridx = 2; filePanel.add(ediBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 1; filePanel.add(new JLabel("CSV/Excel-Ziel:"), gbc);
        gbc.gridx = 1; filePanel.add(csvLabel, gbc);
        gbc.gridx = 2; filePanel.add(csvBtn, gbc);

        panel.add(filePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));

        // Buttons
        JButton previewBtn = niceButton("Vorschau");
        previewBtn.setPreferredSize(new Dimension(170, 38));
        previewBtn.addActionListener(e -> showPreview());

        JButton convertBtn = niceButton("Exportieren");
        convertBtn.setPreferredSize(new Dimension(170, 38));
        convertBtn.addActionListener(e -> export());

        JButton infoBtn = new JButton("ℹ️");
        infoBtn.setFocusPainted(false);
        infoBtn.setBorderPainted(false);
        infoBtn.setBackground(new Color(246, 249, 252));
        infoBtn.setForeground(new Color(80, 80, 80));
        infoBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 23));
        infoBtn.setPreferredSize(new Dimension(50, 38));
        infoBtn.setToolTipText("Informationen und Datenschutz");
        infoBtn.addActionListener(e -> showInfo());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 38, 2));
        btnPanel.setOpaque(false);
        btnPanel.add(previewBtn);
        btnPanel.add(convertBtn);
        btnPanel.add(infoBtn);
        panel.add(btnPanel);

        // Statuszeile
        statusLabel = new JLabel("Bereit.", SwingConstants.CENTER);
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        statusLabel.setForeground(new Color(37, 88, 155));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(new EmptyBorder(14, 8, 2, 8));
        panel.add(Box.createRigidArea(new Dimension(0, 14)));
        panel.add(statusLabel);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private JButton niceButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(226, 234, 247));
        btn.setForeground(new Color(22, 50, 92));
        btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(179, 205, 244), 1, true),
                new EmptyBorder(10, 28, 10, 28)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void status(String text, boolean error) {
        statusLabel.setText(text);
        statusLabel.setForeground(error ? Color.RED : new Color(37, 88, 155));
    }

    private void showInfo() {
        JOptionPane.showMessageDialog(frame,
                "<html><h3>EDI/EDIFACT/X12 ➔ Excel/CSV Konverter</h3>" +
                        "<p>Version 1.0 | 100% Offline | Keine Cloud</p>" +
                        "<ul>" +
                        "<li>Alle Daten bleiben auf Ihrem Rechner.</li>" +
                        "<li>Keine Internetverbindung, keine Datenübertragung.</li>" +
                        "<li>Kompatibel mit Excel, LibreOffice, Google Sheets.</li>" +
                        "</ul>" +
                        "<p style='color:gray'>Dieses Tool ist keine offizielle Software der EDI-Verbände. Nur zur unterstützenden Datenkonvertierung.</p></html>",
                "Info & Datenschutz", JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showPreview() {
        if (ediFile == null) {
            status("Bitte zuerst eine EDI-Datei wählen!", true);
            return;
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new FileReader(ediFile))) {
            String l; int z=0;
            while ((l = r.readLine()) != null && z < 12) { sb.append(l).append("\n"); z++; }
        } catch (IOException e) {
            sb.append("Fehler beim Lesen: ").append(e.getMessage());
        }
        JTextArea area = new JTextArea(sb.toString(), 12, 58);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        area.setEditable(false);
        JOptionPane.showMessageDialog(frame, new JScrollPane(area), "Vorschau (erste Zeilen)", JOptionPane.PLAIN_MESSAGE);
    }

    private void export() {
        if (ediFile == null || csvFile == null) {
            status("Bitte beide Dateien wählen.", true);
            return;
        }
        int rows = 0;
        try {
            StringBuilder ediData = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(ediFile))) {
                String line;
                while ((line = reader.readLine()) != null) ediData.append(line);
            }
            String[] segments = ediData.toString().split("'");
            try (PrintWriter writer = new PrintWriter(csvFile, "UTF-8")) {
                writer.println("Segment;Field1;Field2;Field3;Field4;Field5");
                for (String seg : segments) {
                    if (seg.trim().isEmpty()) continue;
                    String[] fields = seg.split("\\+");
                    writer.print(fields[0]);
                    for (int i = 1; i <= 5; i++) {
                        writer.print(";");
                        if (fields.length > i) writer.print(fields[i]);
                    }
                    writer.println();
                    rows++;
                }
            }
            status("Fertig! " + rows + " Zeilen exportiert.", false);
        } catch (Exception e) {
            status("Fehler: " + e.getMessage(), true);
        }
    }

    private String shortenPath(String path, int maxLen) {
        if (path.length() <= maxLen) return path;
        String start = path.substring(0, Math.min(16, path.length()));
        String end = path.substring(path.length() - 24);
        return start + "..." + end;
    }
}
