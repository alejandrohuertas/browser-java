package standalone;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;

public class Browser extends JFrame{

    static Browser browser;
    
    JList foldersList;
    JPanel panel2 ;
    public Browser (){
        initComponents();
    }
    
    public void initComponents(){
        setSize(500, 500);
        JPanel panel1 = new JPanel();
        panel2 = new JPanel();
        add(panel1);
        add(panel2);
        File mainFolder = new File(System.getProperty("user.home"));
        
        loadDirectories(mainFolder);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void loadDirectories (File folder){
        FileFilter hiddenFilesFilter = new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden(); 
            }
        };
        
        File[] files = folder.listFiles(hiddenFilesFilter);
        foldersList = new JList(files);
        panel2.add(new JScrollPane(foldersList));
        foldersList.setVisibleRowCount(9);
        foldersList.setSelectedIndex(0);
        

    }

    
    public void moveToNextFile(){
        if (foldersList.getModel().getSize()>=(foldersList.getSelectedIndex()+1)){
            foldersList.setSelectedIndex(foldersList.getSelectedIndex()+1);
        }
    }
    public void moveToPreviousFile(){
        if (foldersList.getSelectedIndex()-1 >=0 ){
            foldersList.setSelectedIndex(foldersList.getSelectedIndex()-1);
        }
    }
    
    class LeapListenerController extends Listener {
        
        public void onInit(Controller controller) {
            System.out.println("Initialized");
        }
        
        public void onConnect(Controller controller) {
            System.out.println("Connected");
            controller.enableGesture(Gesture.Type.TYPE_SWIPE);
            controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        }
        
        public void onDisconnect(Controller controller) {
            //Note: not dispatched when running in a debugger.
            System.out.println("Disconnected");
        }
        
        public void onExit(Controller controller) {
            System.out.println("Exited");
        }
        
        
        public void onFrame(Controller controller) {
            // Get the most recent frame and report some basic information
            Frame frame = controller.frame();
            
            GestureList gestures = frame.gestures();
            for (int i = 0; i < gestures.count(); i++) {
                Gesture gesture = gestures.get(i);
                if (gesture.duration()>=10000 && gesture.state().toString().equals("STATE_STOP")){
                    System.out.println("Gesture type --> "+gesture.type()+ ", Duration -->" +gesture.duration());
                    
                    switch (gesture.type()) {
                    
                    case TYPE_SWIPE:
                        SwipeGesture swipe = new SwipeGesture(gesture);
                        // if it is left to right swipe 
                        if (swipe.direction().get(0)>0 && swipe.direction().get(1)>0){
                            browser.moveToNextFile();
                        }
                        // if it is right to left swipe
                        else if (swipe.direction().get(0)<0 && swipe.direction().get(1)<0){
                            browser.moveToPreviousFile();
                        }
                    break;
                        
                    default:
                        System.out.println("Unknown gesture type.");
                    break;
                    }
                }
            }
            
            if (frame.hands().isEmpty() && gestures.isEmpty()) {
            }
        }
    }
    
    public static void main (String... args){
        browser = new Browser();
        
        browser.setVisible(true);
        
        LeapListenerController leapListener = browser.new LeapListenerController();
        Controller controller = new Controller();
        
        // Have the sample listener receive events from the controller
        controller.addListener(leapListener);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(leapListener);
    }
}
