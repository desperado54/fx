package fx.statistic.indicators;

import java.util.ArrayList;
import java.util.List;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import fx.statistic.model.Rate;

public class StochasticIndicator extends BaseIndicator {
    
    public StochasticIndicator(String cp, String period) {
        super(cp, period);
        // TODO Auto-generated constructor stub
    }

    public static int FASTK_PERIOD = 5;
    public static int SLOWK_PERIOD = 3;
    public static int SLOWD_PERIOD = 3;
    
    public void Generate() throws Exception {
        Rate[] origin = this.Load().toArray(new Rate[0]);
        int size = origin.length;
        double[] closePrice = new double[size];
        double[] highPrice = new double[size];
        double[] lowPrice = new double[size];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        double[] outSlowK = new double[size];
        double[] outSlowD = new double[size];

        for (int i = 0; i < size; i++) {
            Rate r = origin[i];
            closePrice[i] = r.c;
            highPrice[i] = r.h;
            lowPrice[i] = r.l;            
        }

        Core c = new Core();
        RetCode retCode = c.stoch(0, closePrice.length - 1, highPrice, lowPrice, closePrice, 
                FASTK_PERIOD, SLOWK_PERIOD, MAType.Sma, SLOWD_PERIOD, MAType.Sma, 
                begin, length, outSlowK, outSlowD);

        if (retCode == RetCode.Success) {
            System.out.println("Output Begin:" + begin.value);
            System.out.println("Output Length:" + length.value);

            List<double[]> values = new ArrayList<double[]>();
            List<String> keys = new ArrayList<String>();
            String[] fields = new String[] { "sto_sk","sto_sd" };
            for (int i = begin.value; i < closePrice.length; i++) {
                double[] value = new double[2];
                value[0] = outSlowK[i - begin.value];
                value[1] = outSlowD[i - begin.value];
                values.add(value);
                keys.add(origin[i].t);

            }

            this.Push2DB(keys, values, fields);
        }
    }
}
