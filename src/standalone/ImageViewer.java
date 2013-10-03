package standalone;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;


public class ImageViewer extends JFrame {

	private static final long serialVersionUID = 2819769939748143970L;
	
	private JLabel image;	
	private JList foldersList;
	private JPanel contentPane;
	private JPanel panel;
	
	/**
	 * Launch the application.
	 */
	
	/**
	 * Create the frame.
	 */
	public ImageViewer(File file, JList foldersList) {
		this.foldersList = foldersList;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 526, 521);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		panel = new JPanel();
		panel.setBounds(10, 76, 614, 365);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		JButton btnPrevious = new JButton("Previous Image");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveToPreviousFile();
			}
		});
		
		JButton btnNext = new JButton("Next Image");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveToNextFile();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(125)
					.addComponent(btnBack)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPrevious)
					.addGap(4)
					.addComponent(btnNext))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(7)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnPrevious)
							.addComponent(btnBack))
						.addComponent(btnNext)))
		);
		panel.setLayout(gl_panel);
		
		image = new JLabel("");
		image.setBounds(10, 11, 480, 70);
		image.setIcon(new ImageIcon(file.getPath()));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(85)
							.addComponent(image, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(image, GroupLayout.PREFERRED_SIZE, 443, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);

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
        updateImageIcon(foldersList.getSelectedIndex());
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
        updateImageIcon(foldersList.getSelectedIndex());
    }
    
    private void updateImageIcon(int selectedIndex) {
    	
		try {
			BufferedImage img = ImageIO.read((File) foldersList.getModel().getElementAt(selectedIndex));		
    	
			Image resizedImage = img.getScaledInstance(image.getWidth(), image.getHeight(), 0);
    	
    		image.setIcon(new ImageIcon(resizedImage));
		} catch (IOException e) {
			image.setIcon(new ImageIcon(((File) foldersList.getModel().getElementAt(selectedIndex)).getPath()));
		}
	}    
}

