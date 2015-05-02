package fx.statistic.indicators;

import java.util.ArrayList;
import java.util.List;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import fx.statistic.model.Rate;

public class BBIndicator extends BaseIndicator {
    
    public BBIndicator(String cp, String period) {
        super(cp, period);
        // TODO Auto-generated constructor stub
    }

    public static int PERIOD = 20;
    public static double DEV_UP = 2d;
    public static double DEV_DOWN = 2d;
    
    public void Generate() throws Exception {
        Rate[] origin = this.Load().toArray(new Rate[0]);
        int size = origin.length;
        double[] closePrice = new double[size];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        double[] outMid = new double[size];
        double[] outUp = new double[size];
        double[] outDown = new double[size];

        for (int i = 0; i < size; i++) {
            Rate r = origin[i];
            closePrice[i] = r.c;
        }

        Core c = new Core();
        RetCode retCode = c.bbands(0, closePrice.length - 1, closePrice, PERIOD, DEV_UP, DEV_DOWN, MAType.Sma, 
                begin, length, outUp, outMid, outDown);

        if (retCode == RetCode.Success) {
            System.out.println("Output Begin:" + begin.value);
            System.out.println("Output Length:" + length.value);

            List<double[]> values = new ArrayList<double[]>();
            List<String> keys = new ArrayList<String>();
            String[] fields = new String[] { "bb_m","bb_u","bb_d" };
            for (int i = begin.value; i < closePrice.length; i++) {
                double[] value = new double[3];
                value[0] = outMid[i - begin.value];
                value[1] = outUp[i - begin.value];
                value[2] = outDown[i - begin.value];
                values.add(value);
                keys.add(origin[i].t);

            }

            this.Push2DB(keys, values, fields);
        }
    }
}
