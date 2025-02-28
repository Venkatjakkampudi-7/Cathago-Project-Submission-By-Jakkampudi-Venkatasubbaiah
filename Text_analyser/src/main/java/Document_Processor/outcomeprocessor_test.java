package Document_Processor;

public class outcomeprocessor_test {
    public static void main(String args[]){
        Outcome_Processor opr = new Outcome_Processor(35);
        System.out.println(opr.Get_Doc_Scores());
        opr.Close_connection();
    }
}