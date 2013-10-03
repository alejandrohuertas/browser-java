package standalone;

import java.awt.FileDialog;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

public class BrowserFrame extends JFrame{
    public BrowserFrame() {
        initComponents(); 
    }
    
/*    public JFileChooser jfcExaminarEntrada;
    public JButton jbExaminar;
    public JButton jbLoad;
    public JDesktopPane jDesktopPane1;
    public JPanel jPanel1;
    public JPanel jPanel3;
    public JTextField jtfRutaEntrada;

   private void initComponents() {
        setTitle("DEMO POR INFORUX");
        setResizable(false);
        jfcExaminarEntrada = new JFileChooser();
        jPanel1 = new  JPanel();
        jtfRutaEntrada = new  JTextField();
        jbExaminar = new  JButton();
        jbLoad = new  JButton();
        jPanel3 = new  JPanel();
        jDesktopPane1 = new  JDesktopPane();

        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBorder( BorderFactory.createTitledBorder("Ubica el Archivo Imagen"));
        jPanel1.setLayout(null);
        jPanel1.add(jtfRutaEntrada);
        jtfRutaEntrada.setBounds(20, 30, 350, 19);

        jbExaminar.setText("...");
        jPanel1.add(jbExaminar);
        jbExaminar.setBounds(400, 30, 50, 25);

        jbLoad.setText("load");
        jPanel1.add(jbLoad);
        jbLoad.setBounds(460, 30, 70, 25);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(30, 30, 550, 70);

        jPanel3.setBorder( BorderFactory.createTitledBorder("Imagen Cargada"));
        jPanel3.setLayout(null);
        jPanel3.add(jDesktopPane1);
        jDesktopPane1.setBounds(20, 30, 530, 340);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(20, 110, 570, 390);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-618)/2, (screenSize.height-542)/2, 618, 542);

        declaramos una referencia a nuestra clase control de eventos
        ControlaEventos controlaEventos =new ControlaEventos (this);
        jbExaminar.addActionListener(controlaEventos);
        jbLoad.addActionListener(controlaEventos);
    }
*/
    
    
    public void initComponents(){
        FileSystemView vueSysteme = FileSystemView.getFileSystemView(); 
        //récupération des répertoires
        File defaut = vueSysteme.getDefaultDirectory(); 
        File home = vueSysteme.getHomeDirectory(); 
//      création et affichage des JFileChooser
        JFileChooser defautChooser = new JFileChooser(defaut);
        defautChooser.getActionListeners();
        defautChooser.showOpenDialog(null);
        defautChooser.changeToParentDirectory();
        defautChooser.showDialog(null,null);
        JFileChooser homeChooser = new JFileChooser(home);
        homeChooser.showOpenDialog(null);
    }
    public static void main(String args[]) {
//        new BrowserFrame().setVisible(true);
        new BrowserFrame();
    }

}
