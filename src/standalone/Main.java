package standalone;

import java.io.IOException;

public class Main {

    
    public static void main(String... args){
        String path = "C:\\Users\\alejandro.huertas\\Desktop\\LeapMotion";
        ProcessBuilder pb = new ProcessBuilder("explorer", path);
        Process p;
        try {
            p = pb.start();
            int exitCode = p.waitFor();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
}
