package standalone;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

public class Browser extends JFrame{
    
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
//        foldersList.setCellRenderer(new FileRenderer(true));
        panel2.add(new JScrollPane(foldersList));
        foldersList.setVisibleRowCount(9);
        foldersList.setSelectedIndex(0);
        
        addWindowListener(new WindowAdapter() {
        
            @Override
            public void windowClosing(WindowEvent e) {
                
            }
        });
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
    
    public static void main (String... args){
        Browser browser = new Browser();
        
        browser.setVisible(true);
        
        browser.moveToNextFile();
        browser.moveToNextFile();
//        browser.moveToPreviousFile();
    }
}
