package fx.statistic.loaders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import fx.statistic.model.Rate;

public class D1Loader extends BaseLoader {

    public D1Loader(String regex, String cp) {
        super(regex, cp + "_d1");
        // TODO Auto-generated constructor stub
    }

    public static String DATE_FORMAT = "yyyyMMdd HHmmss";
    static final int SHIFT_DURATION = 7;
    
    public void load(String path) throws Exception {
        List<Rate> list = Collections.synchronizedList(new ArrayList<Rate>());

        rx.Observable<Rate> stream = this.getObservable(path);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        stream.groupBy((r) -> {
            try {
                cal.setTime(sdf.parse(r.t));
            } catch (Exception e) {
                e.printStackTrace();
            }
            cal.add(Calendar.HOUR_OF_DAY, SHIFT_DURATION);
            String shiftedTime = sdf.format(cal.getTime());
            return shiftedTime.substring(0, 8);
        }).subscribe(g -> {
            Rate rate = new Rate();
            rate.t = g.getKey() + " 000000";
            g.subscribe(r -> {
                if (rate.o == 0.0D) {
                    rate.o = r.o;
                    rate.l = r.l;
                }
                if (rate.h < r.h)
                    rate.h = r.h;
                if (rate.l > r.l)
                    rate.l = r.l;

                rate.c = r.c;
            }, e -> {
            }, () -> {
                list.add(rate);
            });
        });

        Collections.sort(list, new Comparator<Rate>() {

            @Override
            public int compare(Rate o1, Rate o2) {
                return o1.t.compareTo(o2.t);
            }

        });

        Push2DB(list);

    }
}
