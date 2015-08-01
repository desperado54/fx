package fx.statistic.indicators;

import java.util.ArrayList;
import java.util.List;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import fx.statistic.model.Rate;

public class SMAIndicator extends BaseIndicator {

    public SMAIndicator(String cp, String period) {
        super(cp, period);
        // TODO Auto-generated constructor stub
    }

    public static int PERIOD = 14;

    public void Generate() throws Exception {
        Rate[] origin = this.Load().toArray(new Rate[0]);
        int size = origin.length;
        double[] closePrice = new double[size];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        double[] out = new double[size];

        for (int i = 0; i < size; i++) {
            Rate r = origin[i];
            closePrice[i] = r.c;
        }

        Core c = new Core();
        RetCode retCode = c.sma(0, closePrice.length - 1, closePrice, PERIOD,
                begin, length, out);

        if (retCode == RetCode.Success) {
            System.out.println("Output Begin:" + begin.value);
            System.out.println("Output Length:" + length.value);

            List<double[]> values = new ArrayList<double[]>();
            List<String> keys = new ArrayList<String>();
            String[] fields = new String[] { "sma14" };
            for (int i = begin.value; i < closePrice.length; i++) {
                double[] value = new double[1];
                value[0] = out[i - begin.value];
                values.add(value);
                keys.add(origin[i].t);
            }

            this.Push2DB(keys, values, fields);
        }
    }
}
