package backend_processes;

import java.io.IOException;

public class documents_test {
    public static void main(String args[]) throws IOException
    {
        Documents doc = new Documents();
        System.out.println("Data : "+doc.Get_Documentdata("Documents/Venom.txt"));
        doc.Close_connection();
    }
}