package fx.statistic.indicators;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fx.statistic.model.Rate;

public abstract class BaseIndicator implements IIndicator {
        
    protected String table;
    
    public BaseIndicator(String cp, String period)
    {
        this.table = cp + "_" + period;
    }
        
    protected List<Rate> Load() throws Exception {
        List<Rate> list = new ArrayList<Rate>();
        
        String myDriver = "org.gjt.mm.mysql.Driver";
        String myUrl = "jdbc:mysql://localhost/fx";
        Class.forName(myDriver);
        Connection conn = DriverManager
                .getConnection(myUrl, "root", "emcom123");

        String query = String.format("SELECT * FROM %s", table);

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Rate r = new Rate();
            r.t = rs.getString("t");
            r.o = rs.getDouble("o");
            r.h = rs.getDouble("h");
            r.l = rs.getDouble("l");
            r.c = rs.getDouble("c");
            list.add(r);
        }
        rs.close();
        st.close();
        conn.close();
        
        return list;
    }
    
    public void Push2DB(List<String> keys, List<double[]> values, String[] fields) throws Exception {
        if(keys.size() != values.size())
            throw new IndexOutOfBoundsException("key size doesn't equal to value size");
        
        String myDriver = "org.gjt.mm.mysql.Driver";
        String myUrl = "jdbc:mysql://localhost/fx";
        Class.forName(myDriver);
        Connection conn = DriverManager
                .getConnection(myUrl, "root", "emcom123");

        String query = " update " + table + " set ";
        for(int i = 0; i < fields.length; i++)
        {
            if(i==0)
                query += (fields[i] + " = ?");
            else
                query += ("," + fields[i] + " = ?");                
        }
        query += " where t = ? ";

        PreparedStatement preparedStmt = conn.prepareStatement(query);

        int count = 0;
        for (int j = 0; j < values.size(); j++) {
            double[] value = values.get(j);
            for(int i = 0; i < value.length; i++)
            {
                preparedStmt.setDouble(i + 1, value[i]);
            }
            preparedStmt.setString(value.length + 1, keys.get(j));
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
