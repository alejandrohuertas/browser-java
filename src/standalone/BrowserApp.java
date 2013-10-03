package standalone;

//import java.awt.EventQueue;

import java.awt.Font;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import standalone.utils.MyFileCellRenderer;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;

public class BrowserApp {
	
    private static BrowserApp browser; 
    private JFrame frame;
    private JList foldersList;
    private JPanel panel;
    private JLabel workingWith;
    private ImageViewer imageViewFrame;
	protected boolean isViewerActive; 
	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BrowserApp window = new BrowserApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public BrowserApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	    isViewerActive = false;
		frame = new JFrame();
		frame.setBounds(100, 100, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setBounds(10, 5, 614, 22);
		frame.getContentPane().add(headerPanel);
		headerPanel.setLayout(null);
		
		JLabel title = new JLabel("Leap Motion Java Browser");
		title.setBounds(175, 0, 232, 22);
		headerPanel.add(title);
		title.setFont(new Font("Arial", Font.BOLD, 18));
		
		JPanel urlPanel = new JPanel();
		urlPanel.setBounds(10, 38, 614, 27);
		frame.getContentPane().add(urlPanel);
				
		File mainFolder = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures" + System.getProperty("file.separator"));

        workingWith = new JLabel("Working with " + mainFolder.getAbsolutePath());
		urlPanel.add(workingWith);
		workingWith.setHorizontalAlignment(SwingConstants.LEFT);
		
		panel = new JPanel();
		panel.setBounds(10, 76, 614, 365);
		frame.getContentPane().add(panel);
        
        loadDirectories(mainFolder);
	}
	
	public void loadDirectories (File folder){
        FileFilter hiddenFilesFilter = new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden(); 
            }
        };
        
        File[] files = folder.listFiles(hiddenFilesFilter);
        panel.setLayout(null);
        panel.removeAll();
        workingWith.setText("Working with " + folder.getAbsolutePath());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 5, 594, 349);
        panel.add(scrollPane);
        
	    foldersList = new JList(files);
	    //List
	    foldersList.setCellRenderer(new MyFileCellRenderer());
	    //Images
	    //foldersList.setCellRenderer(new MyCellRenderer());
	    
        scrollPane.setViewportView(foldersList);
        foldersList.setVisibleRowCount(9);       
        foldersList.setSelectedIndex(0);
        panel.revalidate();
        this.frame.repaint();
        this.frame.validate();

    }


    public void openSelectedFile(){
        if (foldersList.getModel().getSize()>0){
            File file = (File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex());
            if (file.isDirectory()){
                loadDirectories(file);
            }
            else{
                if (isFileImage(file)){
                    imageViewFrame= new ImageViewer(file, foldersList);
                    imageViewFrame.setVisible(true);
                    isViewerActive=true;
                }
                else{
                    JOptionPane.showMessageDialog(null, "Can't open the file! it's not an Image");
                }
            }
        }
    }

    public boolean isFileImage(File file){

        MimetypesFileTypeMap mtFileTypeMap = new MimetypesFileTypeMap();
        mtFileTypeMap.addMimeTypes("image/png png PNG");
        String mimetype= mtFileTypeMap.getContentType(file);
        String type = mimetype.split("/")[0];
        return type.equals("image");
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
    
    public void goToParentFolder() {
        File file = (File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex());
        File currentFolder = new File(file.getParent());
        if (currentFolder.getParent()!=null){
            File parent = new File(currentFolder.getParent());
            loadDirectories(parent);
        }
        else{
            JOptionPane.showMessageDialog(null, "There's no upper Level from this folder");
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
                if (gesture.duration()>=50000 && gesture.state().toString().equals("STATE_STOP")){
                    System.out.println("Gesture type --> "+gesture.type()+ ", Duration -->" +gesture.duration());
                    
                    switch (gesture.type()) {
                    
                    case TYPE_SWIPE:
                        SwipeGesture swipe = new SwipeGesture(gesture);
                        // if it is left to right swipe 
                        if (swipe.direction().get(0)>0 && swipe.direction().get(1)>0){
                            if (isViewerActive){
                                browser.imageViewFrame.moveToNextFile();
                            }else{
                                browser.moveToNextFile();
                            }
                        }
                        // if it is right to left swipe
                        else if (swipe.direction().get(0)<0 && swipe.direction().get(1)<0){
                            if (isViewerActive){
                                browser.imageViewFrame.moveToPreviousFile();
                            }else{
                                browser.moveToPreviousFile();
                            }
                        }
                    break;
                    case TYPE_CIRCLE:
                        CircleGesture circle = new CircleGesture(gesture);
                        if (circle.radius()>100){
                            // Calculate clock direction using the angle between circle normal and pointable
                            boolean clockwise;
                            if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/4) {
                                // Clockwise if angle is less than 90 degrees
                                clockwise = true;
                            } else {
                                clockwise = false;
                            }
                            
                            if (!clockwise){
                                if(isViewerActive){
                                    browser.imageViewFrame.setVisible(false);
                                    isViewerActive=false;
                                }
                                else{
                                    browser.goToParentFolder();
                                }
                            }
                        }

                    break;
                    case TYPE_KEY_TAP:
                        KeyTapGesture keytap = new KeyTapGesture(gesture);
                        if (keytap.duration()>100000){
                            browser.openSelectedFile();
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
        browser = new BrowserApp();
        
        browser.frame.setVisible(true);
        
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
