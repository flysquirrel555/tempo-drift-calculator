import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserInterface extends JPanel implements MouseListener {
    BlankArea blankArea;
    JFrame myFrame = new JFrame("Black Box");
    private ScheduledExecutorService executorService;
    static ArrayList<String> clickTime = new ArrayList<String>();
    static ArrayList<String> metTime = new ArrayList<String>();


    public void runner() {

        myFrame.setSize(300, 300);
        myFrame.setVisible(true);
        JButton startButton = new JButton("Start Metronome");
        startButton.setBounds(50, 150, 100, 30);

        myFrame.add(startButton);
        executorService = Executors.newSingleThreadScheduledExecutor();

        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Metronome.play(60, 0, 400, 100);
                    startButton.removeActionListener(this);
                    myFrame.remove(startButton);
                    JComponent newContentPane = new UserInterface();
                    newContentPane.setOpaque(true);
                    myFrame.setContentPane(newContentPane);
                    myFrame.pack();
                    myFrame.repaint();


                    executorService.schedule(() -> {
                        myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
                    }, 15, TimeUnit.SECONDS);


                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                }

            }
        });

        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    writeToCSV();
                    System.exit(0);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public UserInterface() {
        blankArea = new BlankArea(Color.YELLOW);
        blankArea.addMouseListener(this);
        addMouseListener(this);
        setPreferredSize(new Dimension(450, 450));
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        long time = System.currentTimeMillis();
        clickTime.add(String.valueOf(time));
        metTime.add(Metronome.getCurrentTime());
        System.out.println("Mouse pressed at " + time);
        System.out.println("last met was " + Metronome.getCurrentTime() + "\n");
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void writeToCSV() throws FileNotFoundException {
        System.out.println("Reached");
        PrintWriter outFile = new PrintWriter("logs.csv");
        outFile.println("Click Time, Metronome Time");
        for (int i = 0; i < clickTime.size(); i++) {
            outFile.println(clickTime.get(i) + "," + metTime.get(i));
        }
        outFile.close();
    }
}