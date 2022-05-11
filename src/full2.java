import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class full2 extends JPanel implements MouseListener {
    static String previous = "";
    BlankArea blankArea;

    public void runner() throws IOException {
        JFrame myFrame = new JFrame("Black Box");
        myFrame.setSize(300, 300);
        myFrame.setVisible(true);
        JPanel panel = new JPanel();
        JButton startButton = new JButton("Start Metronome");
        startButton.setBounds(50, 150, 100, 30);

        myFrame.add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Metronome.play(100, 0, 400, 100);
                    startButton.removeActionListener(this);
                    myFrame.remove(startButton);
                    myFrame.repaint();

                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
        });

        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                myFrame.dispose();
                try {
                    main.sort();
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }
                System.out.println("bye bye");
                System.exit(0);
            }
        });


    }

    public full2() {
        blankArea = new BlankArea(Color.YELLOW);
        blankArea.addMouseListener(this);
        addMouseListener(this);

    }

    public void mouseClicked(MouseEvent e) {
        long time = System.currentTimeMillis();
        String stepOne = String.valueOf(time);
        stepOne = stepOne.substring(6);
        //eventOutput("Mouse pressed at " + time,e);
        System.out.println("Mouse pressed at " + time);
        try {
            previous = timeKeeper(stepOne, previous);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String timeKeeper(String time, String previous) throws FileNotFoundException {
        previous = previous + "\n" + time;
        writeFile(previous);
        return previous;
    }

    public void writeFile(String time) throws FileNotFoundException {
        PrintWriter outFile = new PrintWriter("logs.txt");
        outFile.println(time);
        outFile.close();

    }

    public void mousePressed(MouseEvent e) {
        System.out.println("pressed");
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("released");
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}