from array import array
import numpy as np
import pandas as pd
from pandas.core.frame import DataFrame
import math
from datetime import datetime
import matplotlib.pyplot as plt
import matplotlib.dates as md

def get_pivots(data):

    pivot = data.iloc[0].open
    offset = data[data['time'] ==  data.iloc[0].time].index.values.astype(int)[0]
    
    print(offset)
    
    last_pivot_id = 0
    up_down = 0

    diff = .002

    for i in range(offset, len(data)+offset):
        row = data.loc[i]

        # We don't have a trend yet
        if up_down == 0:
            if row.low < pivot - diff:
                data.ix[i, 'swings'] = row.low - pivot
                pivot, last_pivot_id = row.low, i
                up_down = -1
            elif row.high > pivot + diff:
                data.ix[i, 'swings'] = row.high - pivot
                pivot, last_pivot_id = row.high, i
                up_down = 1

        # Current trend is up
        elif up_down == 1:
            # If got higher than last pivot, update the swing
            if row.high > pivot:
                # Remove the last pivot, as it wasn't a real one
                data.ix[i, 'swings'] = data.ix[last_pivot_id, 'swings'] + (row.high - data.ix[last_pivot_id, 'high'])
                data.ix[last_pivot_id, 'swings'] = np.nan
                pivot, last_pivot_id = row.high, i
            elif ((pivot-row.low)/diff) > 1:
                data.ix[i, 'swings'] = row.low - pivot
                pivot, last_pivot_id = row.low, i
                # Change the trend indicator
                up_down = -1

        # Current trend is down
        elif up_down == -1:
             # If got lower than last pivot, update the swing
            if row.low < pivot:
                # Remove the last pivot, as it wasn't a real one
                data.ix[i, 'swings'] = data.ix[last_pivot_id, 'swings'] + (row.low - data.ix[last_pivot_id, 'low'])
                data.ix[last_pivot_id, 'swings'] = np.nan
                pivot, last_pivot_id = row.low, i
            elif ((row.high-pivot)/diff) > 1:
                data.ix[i, 'swings'] = row.high - pivot
                pivot, last_pivot_id = row.high, i
                # Change the trend indicator
                up_down = 1


    minutes = data['time'].apply(lambda t: datetime.strptime(t, '%Y%m%d %H%M00')) 
    #minutes = matplotlib.dates.date2num(minutes)
    
    ax=plt.subplot()
    
    ax.xaxis_date()
    ax.xaxis.set_major_formatter(md.DateFormatter('%H:%M'))

    plt.plot_date(minutes, data['close'])
    plt.show()
    print(data[data['swings'].notnull()])


df = pd.read_csv('D:/workspace/java/fx/files/eurusd/DAT_ASCII_EURUSD_M1_2016.csv', sep=';',names = ["time", "open", "high", "low","close","dummy"])
df['swings'] = np.nan
df = df.drop(labels='dummy', axis=1)

dates = df['time'].apply(lambda x: x[:8]).unique()

for i in range(0, dates.size):
    dt = datetime.strptime(dates[i], '%Y%m%d')
    data = df[(df['time'] > dt.strftime('%Y%m%d 0630')) & (df['time'] < dt.strftime('%Y%m%d 2100'))] # active time
    print()
   
    get_pivots(data)


