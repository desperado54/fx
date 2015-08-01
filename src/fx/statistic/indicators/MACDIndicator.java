package fx.statistic.indicators;

import java.util.ArrayList;
import java.util.List;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import fx.statistic.model.Rate;

public class MACDIndicator extends BaseIndicator {
    
    public MACDIndicator(String cp, String period) {
        super(cp, period);
        // TODO Auto-generated constructor stub
    }

    public static int FAST_PERIOD = 12;
    public static int SLOW_PERIOD = 26;
    public static int SIGNAL_PERIOD = 9;
    
    public void Generate() throws Exception {
        Rate[] origin = this.Load().toArray(new Rate[0]);
        int size = origin.length;
        double[] closePrice = new double[size];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        double[] outMacd = new double[size];
        double[] outSignal = new double[size];
        double[] outHisto = new double[size];

        for (int i = 0; i < size; i++) {
            Rate r = origin[i];
            closePrice[i] = r.c;
        }

        Core c = new Core();
        RetCode retCode = c.macd(0, closePrice.length - 1, closePrice, 
                FAST_PERIOD, SLOW_PERIOD, SIGNAL_PERIOD, 
                begin, length, outMacd, outSignal, outHisto);

        if (retCode == RetCode.Success) {
            System.out.println("Output Begin:" + begin.value);
            System.out.println("Output Length:" + length.value);

            List<double[]> values = new ArrayList<double[]>();
            List<String> keys = new ArrayList<String>();
            String[] fields = new String[] { "macd","macd_sig","macd_histo" };
            for (int i = begin.value; i < closePrice.length; i++) {
                double[] value = new double[3];
                value[0] = outMacd[i - begin.value];
                value[1] = outSignal[i - begin.value];
                value[2] = outHisto[i - begin.value];
                values.add(value);
                keys.add(origin[i].t);

            }

            this.Push2DB(keys, values, fields);
        }
    }
}
