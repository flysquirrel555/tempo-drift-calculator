import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class cleanData {
    public static void sort() throws FileNotFoundException {
        File fileName = new File("metlogs.txt");
        File file2 = new File("logs.txt");
        Scanner inFile = new Scanner(fileName);
        int nextMet;
        int nextLog;
        String stepone = "";
        String stream = "";
        while (inFile.hasNext()) {
            nextMet = Integer.parseInt(inFile.nextLine());
            Scanner inFile2 = new Scanner(file2);

            while (inFile2.hasNext()) {
                nextLog = Integer.parseInt(inFile2.next());
                if (Math.abs(nextLog - nextMet) <= 150) {
                    stepone = String.valueOf(nextMet);
                    stream = stream + "\n" + stepone;
                    inFile2.close();
                    break;
                }
            }
        }


        PrintWriter outFile = new PrintWriter("finallog.txt");
        outFile.println(stream);
        outFile.close();
        System.out.println("finished");
    }
}