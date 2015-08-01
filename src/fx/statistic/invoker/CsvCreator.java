package fx.statistic.invoker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import fx.statistic.model.Rate;

public class CsvCreator {
    public static String DATE_FORMAT = "yyyyMMdd";

    public static void main(String[] args) {
        String table = "eurusd_d1";
        String outputT = "D:/Workspace/octave/fx/eurusd_t.csv";
        String outputV = "D:/Workspace/octave/fx/eurusd_v.csv";
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/fx";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root",
                    "emcom123");

            String query = String.format("SELECT * FROM %s", table);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            File fout1 = new File(outputT);
            FileOutputStream fos1 = new FileOutputStream(fout1);
            BufferedWriter bw1= new BufferedWriter(new OutputStreamWriter(fos1));

            File fout2 = new File(outputV);
            FileOutputStream fos2 = new FileOutputStream(fout2);
            BufferedWriter bw2= new BufferedWriter(new OutputStreamWriter(fos2));
            
            int index = 0;
            Random rand = new Random();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            while (rs.next()) {
                index++;
                if (index < 100)
                    continue;
                double n1 = rs.getDouble("n1");//ignore last
                if(n1 == -1d)
                    continue;
                String t = rs.getString("t").substring(0, 8);
                cal.setTime(sdf.parse(t));
                int weekday = cal.get(Calendar.DAY_OF_WEEK);
                double o = rs.getDouble("o");
                double c = rs.getDouble("c");
                double p0 = rs.getDouble("p0");
                double p1 = rs.getDouble("p1");
                double p2 = rs.getDouble("p3");
                double p3 = rs.getDouble("p3");
                double p4 = rs.getDouble("p4");
                double sma14 = rs.getDouble("sma14");
                double macd = rs.getDouble("macd");
                double macdSig = rs.getDouble("macd_sig");
                double stoK = rs.getDouble("sto_sk");
                double stoD = rs.getDouble("sto_sd");
                double bbM = rs.getDouble("bb_m");
                double bbU = rs.getDouble("bb_u");
                double bbD = rs.getDouble("bb_d");
                int y;
                if(Math.abs(n1) < 0.001)
                    y = 0;
                else if(n1 < 0)
                    y = -1;
                else
                    y = 1;
                String line = String.format("%s,%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%d", t, weekday,
                        o,c,p0,p1,p2,p3,p4,
                        sma14, macd, macdSig, stoK, stoD, bbM, bbU, bbD, y);
                
                if(rand.nextInt(3) == 0)
                {
                    bw2.write(line);
                    bw2.newLine();
                }
                else
                {
                    bw1.write(line);
                    bw1.newLine();
                }
            }
            
            bw1.close();
            fos1.close();
            
            bw2.close();
            fos2.close();
            
            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
