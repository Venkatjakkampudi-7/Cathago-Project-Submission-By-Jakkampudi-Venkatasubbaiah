package backend_processes;

public class credit_test {
    public static void main(String args[]){
        Credits cre = new Credits(2);
        cre.Process_credit_request("granted", 5, 14);
        cre.Close_connection();
    }
}
