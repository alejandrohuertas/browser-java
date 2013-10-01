package standalone.utils;

import java.awt.Component;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MyFileCellRenderer  extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;
	final static ImageIcon fileIcon = new ImageIcon(new File(System.getProperty("user.dir")+"\\images\\file.png").getAbsolutePath());
    final static ImageIcon folderIcon = new ImageIcon(new File(System.getProperty("user.dir")+"\\images\\folder.png").getAbsolutePath());
        
    // This is the only method defined by ListCellRenderer.
    // We just reconfigure the JLabel each time we're called.
	public Component getListCellRendererComponent(
      JList list,              // the list
      Object value,            // value to display
      int index,               // cell index
      boolean isSelected,      // is the cell selected
      boolean cellHasFocus)    // does the cell have focus
    {

		File f = (File) value;
        setText(f.getName() + (f.isFile()?" - " + (f.length()/1024) + "Kb":""));
       
        setIcon((f.isFile())?fileIcon:folderIcon);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }

}
