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
            String regex = "^\\d{8}\\s\\d{6};\\d{1}(\\.\\d{4,6}\\s*)?;\\d{1}(\\.\\d{4,6}\\s*)?;\\d{1}(\\.\\d{4,6}\\s*)?;\\d{1}(\\.\\d{4,6}\\s*)?;0$";
            D1Loader loader = new D1Loader(regex, "eurusd");
            for(int i = 2000; i < 2015; i++)
            {
                loader.load(String.format("D:/Workspace/fx/files/eurusd/DAT_ASCII_EURUSD_M1_%d.csv", i));
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
