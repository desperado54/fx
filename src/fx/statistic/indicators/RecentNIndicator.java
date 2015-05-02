package fx.statistic.indicators;

import java.util.ArrayList;
import java.util.List;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import fx.statistic.model.Rate;

public class RecentNIndicator extends BaseIndicator {

    public RecentNIndicator(String cp, String period) {
        super(cp, period);
        // TODO Auto-generated constructor stub
    }

    public static int PERIOD = 5;

    public void Generate() throws Exception {
        Rate[] origin = this.Load().toArray(new Rate[0]);
        int size = origin.length;
        double[][] data = new double[size - PERIOD + 1][PERIOD + 1];
        
        int begin = PERIOD - 1;
        for(int i = begin; i < size; i++)
        {
            for(int j = 0; j < PERIOD; j++)
                data[i - begin][j] = (origin[i - j].c - origin[i - j].o) / origin[i - j].o;
            if(i != size - 1)
                data[i - begin][PERIOD] = (origin[i + 1].c - origin[i + 1].o) / origin[i + 1].o;
            else
                data[i - begin][PERIOD] = -1;
        }
        
        int length = size - PERIOD;

        System.out.println("Output Begin:" + begin);
        System.out.println("Output Length:" + length);

        List<double[]> values = new ArrayList<double[]>();
        List<String> keys = new ArrayList<String>();
        
        String[] fields = new String[PERIOD+1];
        for(int i = 0; i < fields.length -1; i++)
            fields[i] = "p" + i;
        fields[fields.length - 1] = "n1";
        for (int i = begin; i < size; i++) {
            double[] value = new double[PERIOD + 1];
            for(int j = 0; j < PERIOD; j ++)
                value[j] = data[i - begin][j];
            value[PERIOD] = data[i - begin][PERIOD];
            values.add(value);
            keys.add(origin[i].t);

        }
        if(keys.size() != values.size())
            throw new IndexOutOfBoundsException("key size doesn't equal to value size");
        this.Push2DB(keys, values, fields);

    }
}
