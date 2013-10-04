package standalone;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

public class Viewer extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextArea textArea;	
	private JList foldersList;
	private JLabel image;	
	private JScrollPane scrollPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Viewer frame = new Viewer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Viewer(File file, JList foldersList) {
		this.foldersList = foldersList;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		setResizable(false);
		
		contentPane = new JPanel();
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
		
		textArea = new JTextArea();
		textArea.setText(readFile(file.getPath()));
		
		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(400, 410));
		centerPanel.add(scrollPane);
		
		image = new JLabel("");
		image.setBounds(10, 11, 400, 410);
		image.setIcon(new ImageIcon(file.getPath()));
		centerPanel.add(image);
		
		updateViewerByTypeOfFile();		
	}
	
	private void moveToNextFile(){
        if (foldersList.getModel().getSize() > (foldersList.getSelectedIndex()+1)){
        	foldersList.setSelectedIndex(foldersList.getSelectedIndex()+1);
        	if (((File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex())).isDirectory()){
        		moveToNextFile();
        	}
        }else{
        	foldersList.setSelectedIndex(0);
        }
        updateViewerByTypeOfFile();
    }
	
    private void moveToPreviousFile(){
        if (foldersList.getSelectedIndex()-1 >=0 ){
            foldersList.setSelectedIndex(foldersList.getSelectedIndex()-1);
            if (((File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex())).isDirectory()){
        		moveToPreviousFile();
        	}
            
        }else{
        	foldersList.setSelectedIndex(foldersList.getModel().getSize() - 1);	
        }
        updateViewerByTypeOfFile();
    }
    
    private void updateViewerByTypeOfFile(){
    	if(isAnImage()){
    		updateImageIcon(foldersList.getSelectedIndex());
            scrollPane.setVisible(false);
            image.setVisible(true);
        }else{            
            updateText(foldersList.getSelectedIndex());
            scrollPane.setVisible(true);
        	image.setVisible(false);
        }
    }
    
    private Boolean isAnImage(){   	
    	File file = ((File) foldersList.getModel().getElementAt(foldersList.getSelectedIndex()));    	
    	return file.getName().contains(".jpg") || file.getName().contains(".png") || file.getName().contains(".gif");
    
    }
    
    private void updateText(int selectedIndex) {
    	
		try {
			textArea.setText(readFile(((File) foldersList.getModel().getElementAt(selectedIndex)).getPath()));
		} catch (Exception e) {
			textArea.setText("la la la");
		}
	} 
    
    private String readFile(String path){
    	String content = "";
    	BufferedReader br = null;
    	
        try {
        	br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line + '\n');
                line = br.readLine();
            }
            content = sb.toString();
            br.close();
        }catch(IOException e){
        	try {
				br.close();
			} catch (IOException e1) {
				//DO nothing
			}
        } 
    	
    	return content;    	
    }
    	
	private void updateImageIcon(int selectedIndex) {
		
		try {
			BufferedImage img = ImageIO.read((File) foldersList.getModel().getElementAt(selectedIndex));	
			
			Image resizedImage = img.getScaledInstance(image.getWidth(), image.getHeight(), 0);
		
			image.setIcon(new ImageIcon(resizedImage));
		} catch (IOException e) {
			System.out.println("Exc");
			image.setIcon(new ImageIcon(((File) foldersList.getModel().getElementAt(selectedIndex)).getPath()));
		}
	} 
	
	/**
	 * Create the frame.
	 */
	public Viewer(){
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 451, 500);
		contentPane = new JPanel();
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
		textArea.setText(readFile("C:\\Users\\amilcar.infante\\Pictures\\Deploys.txt"));
		
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(400, 410));
		centerPanel.add(scrollPane);

	}

}
