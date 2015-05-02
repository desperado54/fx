package fx.statistic.invoker;

import fx.statistic.indicators.BBIndicator;
import fx.statistic.indicators.IIndicator;
import fx.statistic.indicators.MACDIndicator;
import fx.statistic.indicators.RecentNIndicator;
import fx.statistic.indicators.SMAIndicator;
import fx.statistic.indicators.StochasticIndicator;

public class IndicatorGenerator {

    public static void main(String[] args) {
        try
        {
            IIndicator[] indicators = new IIndicator[] {
//                new SMAIndicator("eurusd","d1"),
//                new MACDIndicator("eurusd","d1"),
//                new StochasticIndicator("eurusd","d1"),
//                new BBIndicator("eurusd","d1"),
                new RecentNIndicator("eurusd", "d1")
            };
            for(IIndicator indicator : indicators)
            {
                indicator.Generate();
                Thread.sleep(10 * 1000);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
            
    }

}
