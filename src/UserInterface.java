import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserInterface extends JPanel implements MouseListener {
    static String previous = "000000";
    BlankArea blankArea;
    JFrame myFrame = new JFrame("Black Box");
    private ScheduledExecutorService executorService;
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
                    newContentPane.setOpaque(true); //content panes must be opaque
                    myFrame.setContentPane(newContentPane);
                    myFrame.pack();
                    myFrame.repaint();
                    executorService.schedule(() -> {
                        myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
                    }, 1, TimeUnit.MINUTES);



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
                    CleanData.sort();
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }
                System.out.println("bye bye");


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
        if(Metronome.getCount()>= Metronome.getBeat()){
            myFrame.dispose();
        }


    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}