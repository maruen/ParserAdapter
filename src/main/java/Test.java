import static com.huskyenergy.parser.ParseCSV.parseCSV;

import java.util.List;

import com.huskyenergy.pojo.SMSPojo;

public class Test {

    public static void main(String[] args) {
     
       List<SMSPojo> smsPojoList = parseCSV("smslocn.csv.02-11-17_DataTrimedFinal.csv", SMSPojo.class, true);
      
       for (SMSPojo smsPojo : smsPojoList) {
           System.out.println(smsPojo.toString());
       }
       
       
    }

}
