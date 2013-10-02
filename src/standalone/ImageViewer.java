package standalone;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import standalone.utils.MyCellRenderer;
import javax.swing.ListSelectionModel;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;

public class ImageViewer extends JFrame {

	private static final long serialVersionUID = 2819769939748143970L;
	
	private static ImageViewer browser; 
	
	private JPanel contentPane;
	private JList foldersList;
	private JPanel panel;	
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageViewer frame = new ImageViewer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public ImageViewer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 526, 521);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panel = new JPanel();
		panel.setBounds(10, 76, 614, 365);
		contentPane.add(panel);
		
		File mainFolder = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures" + System.getProperty("file.separator"));
		
		loadDirectories (mainFolder);
	}
	
	public void loadDirectories (File folder){
        FileFilter hiddenFilesFilter = new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden() && 
                		pathname.isFile() && 
                		(pathname.getName().contains("png") || pathname.getName().contains("jpg") || pathname.getName().contains("gif")); 
            }
        };
        
        File[] files = folder.listFiles(hiddenFilesFilter);

        panel.setLayout(null);
        panel.removeAll();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 5, 480, 500);
        panel.add(scrollPane);
        
	    foldersList = new JList(files);
	    foldersList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
	    foldersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    foldersList.setBounds(10, 5, 50, 50);
	    
	    //Images
	    foldersList.setCellRenderer(new MyCellRenderer());
	    
        scrollPane.setViewportView(foldersList);
        
        foldersList.setVisibleRowCount(1);       
        foldersList.setSelectedIndex(0);
        panel.revalidate();
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
            controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
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
                if (gesture.duration()>=50000 && gesture.state().toString().equals("STATE_STOP")){
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
                        
                    case TYPE_KEY_TAP:
                        KeyTapGesture keytap = new KeyTapGesture(gesture);
                        if (keytap.duration()>100000){
                            //browser.openSelectedFile();
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
        browser = new  ImageViewer();
        
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
