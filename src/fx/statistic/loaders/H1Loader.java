package fx.statistic.loaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fx.statistic.model.Rate;

public class H1Loader extends BaseLoader{
    
    public H1Loader(String regex, String cp) {
        super(regex, cp + "_h1");
        // TODO Auto-generated constructor stub
    }

    public static String DATE_FORMAT = "yyyyMMdd HHmmss";
     
    public void load (String path) throws Exception
    {     
        List<Rate> list = Collections.synchronizedList(new ArrayList<Rate>());
        
        rx.Observable<Rate> stream = this.getObservable(path);
        
        stream.groupBy((r)-> { return r.t.substring(0, 11);})
        .subscribe(g ->
        {
            Rate rate = new Rate();
            rate.t = g.getKey() + "0000";
            g.subscribe(r -> 
            {
                if(rate.o == 0.0D)
                {
                    rate.o = r.o;
                    rate.l = r.l;
                }
                if(rate.h < r.h)
                    rate.h = r.h;
                if(rate.l > r.l)
                    rate.l = r.l;
                
                rate.c = r.c;
            },
            e -> {},
            () ->
            {
                list.add(rate);
            });
        });
        
        Collections.sort(list, new Comparator<Rate>(){

            @Override
            public int compare(Rate o1, Rate o2) {
                return o1.t.compareTo(o2.t);
            }
            
        });

        Push2DB(list);

    }
}
