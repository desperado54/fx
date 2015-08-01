package fx.statistic.invoker;

import java.io.Console;

import fx.statistic.indicators.SMAIndicator;
import fx.statistic.loaders.D1Loader;
import fx.statistic.loaders.H1Loader;
import fx.statistic.loaders.M1Loader;

public class LoadProcessor {

    public static void main(String[] args) {
        try
        {
            String cp = "eurusd";
            String regex = "^\\d{8}\\s\\d{6};\\d{1}(\\.\\d{4,6}\\s*)?;\\d{1}(\\.\\d{4,6}\\s*)?;\\d{1}(\\.\\d{4,6}\\s*)?;\\d{1}(\\.\\d{4,6}\\s*)?;0$";
//            M1Loader loader = new M1Loader();
//            for(int i = 201506; i < 201507; i++)
//            {
//                loader.load(String.format("D:/Workspace/java/fx/files/eurusd/DAT_ASCII_EURUSD_M1_%d.csv", i));
//            }
            D1Loader loader = new D1Loader(regex,cp);
            for(int i = 2015; i < 2016; i++)
            {
                loader.load(String.format("D:/Workspace/java/fx/files/%s/DAT_ASCII_%s_M1_%d.csv", cp, cp, i));
            }
            //SMAIndicator indicator = new SMAIndicator();
            //indicator.Generate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
            
    }

}
