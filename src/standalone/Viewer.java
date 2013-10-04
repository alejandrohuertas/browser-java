package standalone;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import standalone.utils.FileUtils;

public class Viewer extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final static String contentNotSupportedURL = System.getProperty("user.dir")+"\\images\\content_not_supported.gif";
	
	private JTextArea textArea;	
	private JList foldersList;
	private JLabel image;	
	private JScrollPane scrollPane;	

	public Viewer(File file, JList foldersList) {
		this.foldersList = foldersList;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		setResizable(false);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		setContentPane(contentPane);
		
		JPanel horizontalPanel = new JPanel();
		contentPane.add(horizontalPanel, BorderLayout.NORTH);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		JButton btnPrevious = new JButton("Previous File");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveToPreviousFile();
			}
		});
		
		JButton btnNext = new JButton("Next File");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveToNextFile();
			}
		});
		
		horizontalPanel.add(btnBack);
		horizontalPanel.add(btnPrevious);
		horizontalPanel.add(btnNext);
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		
		//TextArea used to display txt files
		textArea = new JTextArea();
		textArea.setText(FileUtils.readFile(file.getPath()));
		
		//The text are is wrapped into a ScrollPane
		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(400, 410));
		
		centerPanel.add(scrollPane);
		
		//JLabel used to display the images
		image = new JLabel("");
		image.setBounds(10, 11, 400, 410);
		image.setIcon(new ImageIcon(file.getPath()));
		centerPanel.add(image);
		
		//Render the viewer accordingly the type of file (image or txt)
		updateViewerByTypeOfFile();		
	}
	
	/* 
	 * Moves to next file in the folder list and render it accordingly	 * 
	 */
	protected void moveToNextFile(){
        if (foldersList.getModel().getSize() > (foldersList.getSelectedIndex()+1)){
        	foldersList.setSelectedIndex(foldersList.getSelectedIndex()+1);
        	//Skip directory folders
        	if (((File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex())).isDirectory()){
        		moveToNextFile();
        	}
        }else{
        	//Moves to the first file in the folder if the index is positioned on the last file
        	foldersList.setSelectedIndex(0);
        }
        //Render the viewer accordingly the type of file (image or txt)
        updateViewerByTypeOfFile();
    }
	
	/* 
	 * Moves to previous file in the folder list and render it accordingly	 
	 */
    protected void moveToPreviousFile(){
        if (foldersList.getSelectedIndex()-1 >=0 ){
            foldersList.setSelectedIndex(foldersList.getSelectedIndex()-1);
           //Skip directory folders
            if (((File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex())).isDirectory()){
        		moveToPreviousFile();
        	}
            
        }else{
        	//Moves to the last file in the folder if the index is positioned on the first file
        	foldersList.setSelectedIndex(foldersList.getModel().getSize() - 1);	
        }
       //Render the viewer accordingly the type of file (image or txt)
        updateViewerByTypeOfFile();
    }
    
    /*
     * Updates and make visible the main panel accordingly with the type of file to show
     */
    private void updateViewerByTypeOfFile(){
    	if(isAnImage()){
    		updateImageIcon();
            scrollPane.setVisible(false);
            image.setVisible(true);
        }else if (isATxt()){            
            updateText();
            scrollPane.setVisible(true);
        	image.setVisible(false); 
        }else{
        	//If the file has a not supported content an image with the legend "Content Not Supported" is displayed
        	updateImageIcon(null);
            scrollPane.setVisible(false);
            image.setVisible(true);
        }
    }
    
    /*
     * Returns if the file is an image (jpg, png, gif, bmp, etc)
     */
    private Boolean isAnImage(){   	
    	return FileUtils.isFileImage(((File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex())));    
    }
    
    /*
     * Returns if the file is a text file
     */
    private Boolean isATxt(){   
    	return FileUtils.isFileText(((File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex())));    
    }
    
    /*
     * Updates the textArea with the current file selected on the folderList
     */
    private void updateText() {    	
		try {
			textArea.setText(FileUtils.readFile(((File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex())).getPath()));
			
			//Force the scrollpanel to start at the top
	    	textArea.setSelectionStart(0);
	    	textArea.setSelectionEnd(0);
		} catch (Exception e) {
			textArea.setText("Content Not Supported");
		}
	}        
    	
    /*
     * Updates the textArea with the current file selected on the folderList
     */
	private void updateImageIcon() {
		updateImageIcon((File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex()));
	} 
	
	/*
     * Set the image.Icon with a scaled instance of 'file'
     * file: input file
     */
	private void updateImageIcon(File file){
		if(file != null){
			try {
				BufferedImage img = ImageIO.read(file);	
				
				Image resizedImage = img.getScaledInstance(image.getWidth(), image.getHeight(), 0);
			
				image.setIcon(new ImageIcon(resizedImage));
			} catch (IOException e) {
				//Display the Content Not Supported image 
				image.setIcon(new ImageIcon(new File(contentNotSupportedURL).getAbsolutePath()));
			}	
		}else{
			//Display the Content Not Supported image 
			image.setIcon(new ImageIcon(new File(contentNotSupportedURL).getAbsolutePath()));
		}
	}
	
	/**
	 * Create the frame.
	 */
	public Viewer(){
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 451, 500);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel horizontalPanel = new JPanel();
		contentPane.add(horizontalPanel, BorderLayout.NORTH);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JButton btnPrevious = new JButton("Previous Image");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JButton btnNext = new JButton("Next Image");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		horizontalPanel.add(btnBack);
		horizontalPanel.add(btnPrevious);
		horizontalPanel.add(btnNext);
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setText("Test");
		
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(400, 410));
		centerPanel.add(scrollPane);

	}

}
