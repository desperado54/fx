package fx.statistic.loaders;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class M1Loader {
    
    public static String DATE_FORMAT = "yyyyMMdd HHmmss";
     
    public void load (String path) throws Exception
    {
         String myDriver = "org.gjt.mm.mysql.Driver";
        String myUrl = "jdbc:mysql://localhost/fx";
        Class.forName(myDriver);
        Connection conn = DriverManager.getConnection(myUrl, "root", "emcom123");
       
        String query = " insert into eurusd_m1 (t, o, h, l, c)"
          + " values (?, ?, ?, ?, ?)";
        
        PreparedStatement preparedStmt = conn.prepareStatement(query);
      
        FileInputStream fstream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String line;

        int count = 0;
        //Read File Line By Line
        while ((line = br.readLine()) != null)   {
          // Print the content on the console
          String[] items = line.split(";");
          
//          SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//          java.sql.Date t = new java.sql.Date(sdf.parse(items[0]).getTime());
//          
//          System.out.println(t.toGMTString());
          
          preparedStmt.setString(1, items[0]);
          preparedStmt.setDouble(2, new Double(items[1]));
          preparedStmt.setDouble(3, new Double(items[2]));
          preparedStmt.setDouble(4, new Double(items[3]));
          preparedStmt.setDouble(5, new Double(items[4]));
          
          preparedStmt.addBatch();

          count++;
          if(count == 1000)
          {
              preparedStmt.executeBatch();
              count = 0;
          }
        }
        preparedStmt.executeBatch();
        
        //Close the input stream
        br.close();
        
        preparedStmt.close();
        conn.close();
    }
    
}
