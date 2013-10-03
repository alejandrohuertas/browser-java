package standalone;

import java.awt.event.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class ControlaEventos implements ActionListener{
    BrowserFrame ventana;
    File fileImagen;
    public ControlaEventos(BrowserFrame browserFrame){
        ventana = browserFrame;
    }
    public void actionPerformed(ActionEvent evento)
    {
        /*    if (evento.getSource()==ventana.jbExaminar){//si hay evento en el boton examinar
            int returnVal = ventana.jfcExaminarEntrada.showOpenDialog(ventana);//mostramos el jFileChooser
            if (returnVal == ventana.jfcExaminarEntrada.APPROVE_OPTION) {//nos aseguramos q haya selecionado algun archivo
                fileImagen = ventana.jfcExaminarEntrada.getSelectedFile();//obtenemos el archivo selecionado
               
                ventana.jtfRutaEntrada.setText(fileImagen.toString());  }}//mostramos la ruta del archivo en la caja de texto

        if (evento.getSource()==ventana.jbLoad){//si hay evento en el boton load
            if ( fileImagen != null) {
                cargarImagen(ventana.jDesktopPane1,fileImagen);}}
    }

    //este metodo recibe el jdeskopPane y el archivo imagen
    public  void cargarImagen(javax.swing.JDesktopPane jDeskp,File fileImagen)
    {
        try{
            BufferedImage image = ImageIO.read(fileImagen);
            //jDeskp.setBorder(new PintaImagen(image));
            System.out.println("Pinta la imagen");
        }
        catch (Exception e){   System.out.println("No cargo imagen, sorry");   
        }
        */
    }
}