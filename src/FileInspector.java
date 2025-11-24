import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class FileInspector {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setVisible(true);

        File selectedFile = chooseFile(frame);

        if (selectedFile != null) {
            System.out.println("Processing file: " + selectedFile.getName());
            processFile(selectedFile);
        } else {
            System.out.println("File selection cancelled.");
        }

        frame.dispose();
    }

    /**
     * Uses JFileChooser to let the user select a text file.
     * The dialog opens in the 'src' directory of the current project.
     * @param parentFrame The parent component for the dialog.
     * @return The selected File object, or null if cancelled or an error occurred.
     */
    private static File chooseFile(JFrame parentFrame) {
        String currentDir = System.getProperty("user.dir");
        File srcDir = new File(currentDir, "src");

        JFileChooser fileChooser;
        if (srcDir.exists() && srcDir.isDirectory()) {
            fileChooser = new JFileChooser(srcDir);
        } else {
            fileChooser = new JFileChooser(currentDir);
        }

        fileChooser.setDialogTitle("Select a text file to inspect");
        int userSelection = fileChooser.showOpenDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    /**
     * Reads the selected file line by line, echoes lines to the console, and prints a summary report.
     * @param file The file to process.
     */
    private static void processFile(File file) {
        int lineCount = 0;
        int wordCount = 0;
        long charCount = 0;

        System.out.println("\n--- File Content ---");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);

                lineCount++;
                charCount += line.length();
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (!word.trim().isEmpty()) {
                        wordCount++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found: " + file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        charCount += lineCount > 0 ? lineCount - 1 : 0;

        System.out.println("\n--- Summary Report ---");
        System.out.println("• The name of the file the user chose to process: " + file.getName() + "");
        System.out.println("• Number of lines in the file: " + lineCount + "");
        System.out.println("• Number of words in the file: " + wordCount + "");
        System.out.println("• Number of characters in the file: " + charCount + "");
    }
}
