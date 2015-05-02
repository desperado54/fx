package fx.statistic.loaders;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.regex.Pattern;

import fx.statistic.model.Rate;

public abstract class BaseLoader {

    private String regex;
    private String table;
    public BaseLoader(String regex, String table)
    {
        this.regex = regex;
        this.table = table;
    }
    public rx.Observable<Rate> getObservable(String path) throws Exception {
        return rx.Observable.create(o -> {
            try {
                FileInputStream fstream = new FileInputStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        fstream));

                String line;
                // Read File Line By Line
                while ((line = br.readLine()) != null) {

                    // Create a Pattern object
                    if(!Pattern.matches(this.regex, line))
                    {
                        System.out.println("dispose line: " + line);
                        continue;
                    }
                    // Print the content on the console
                    String[] items = line.split(";");

                    Rate r = new Rate();
                    r.t = items[0];
                    r.o = new Double(items[1]);
                    r.h = new Double(items[2]);
                    r.l = new Double(items[3]);
                    r.c = new Double(items[4]);

                    o.onNext(r);
                }

                // Close the input stream
                br.close();
                o.onCompleted();
            } catch (Exception e) {
                o.onError(e);
            }

        });

    }

    public void Push2DB(List<Rate> list) throws Exception {
        String myDriver = "org.gjt.mm.mysql.Driver";
        String myUrl = "jdbc:mysql://localhost/fx";
        Class.forName(myDriver);
        Connection conn = DriverManager
                .getConnection(myUrl, "root", "emcom123");

        String query = " insert into " + this.table + " (t, o, h, l, c)"
                + " values (?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = conn.prepareStatement(query);

        int count = 0;
        for (Rate rate : list) {
            preparedStmt.setString(1, rate.t);
            preparedStmt.setDouble(2, rate.o);
            preparedStmt.setDouble(3, rate.h);
            preparedStmt.setDouble(4, rate.l);
            preparedStmt.setDouble(5, rate.c);
            preparedStmt.addBatch();
            count++;

            if (count == 1000) {
                preparedStmt.executeBatch();
                count = 0;
            }
        }
        
        preparedStmt.executeBatch();
        
        preparedStmt.close();
        conn.close();
    }
}
