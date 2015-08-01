package fx.statistic.invoker;

import fx.statistic.indicators.IIndicator;
import fx.statistic.indicators.RecentNIndicator;
import fx.statistic.probablity.NextDayContinuation;

public class ProbablityCalculator {

    public static void main(String[] args) {
        try
        {
            NextDayContinuation p = new NextDayContinuation("eurusd", "d1");
            p.Calcuate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
            
    }

}
