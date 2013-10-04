package standalone.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;

public class FileUtils {

	public static boolean isFileImage(File file){

        MimetypesFileTypeMap mtFileTypeMap = new MimetypesFileTypeMap();
        mtFileTypeMap.addMimeTypes("image/png png PNG");
        String mimetype= mtFileTypeMap.getContentType(file);
        String type = mimetype.split("/")[0];
        return type.equals("image");
    }
    
    public static boolean isFileText(File file){

        MimetypesFileTypeMap mtFileTypeMap = new MimetypesFileTypeMap();
        String mimetype= mtFileTypeMap.getContentType(file);
        String type = mimetype.split("/")[0];
        return type.equals("text");
    }
    
    public static String readFile(String path){
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
    
}
