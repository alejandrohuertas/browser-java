package standalone;

import java.io.File;
import java.io.FileFilter;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
    
    public static void main (String... args){
        Browser browser = new Browser();
        
        browser.setVisible(true);
        
        browser.moveToNextFile();
        browser.moveToNextFile();
//        browser.moveToPreviousFile();
    }
}
