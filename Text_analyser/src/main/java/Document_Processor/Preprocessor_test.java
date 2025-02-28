package Document_Processor;

import java.io.IOException;

public class Preprocessor_test {
    public static void main(String args[]) throws IOException{
        Pre_Processor ps[] = new Pre_Processor[27];
        for(int i=1;i<27;i++){
            ps[i] = new Pre_Processor(i);
            ps[i].Close_connection();
        }
    }
}