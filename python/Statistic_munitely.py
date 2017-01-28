from array import array
import numpy as np
import pandas as pd
from pandas.core.frame import DataFrame
import math

def get_pivots(data):
    data['swings'] = np.nan

    pivot = data.iloc[0].open
    offset = data[data['time'] ==  data.iloc[0].time].index.values.astype(int)[0]
    
    print(offset)
    
    last_pivot_id = 0
    up_down = 0

    diff = .002

    for i in range(0, len(data)):
        row = data.iloc[i]

        # We don't have a trend yet
        if up_down == 0:
            if row.low < pivot - diff:
                data.ix[i + offset, 'swings'] = row.low - pivot
                pivot, last_pivot_id = row.low, i
                up_down = -1
            elif row.high > pivot + diff:
                data.ix[i + offset, 'swings'] = row.high - pivot
                pivot, last_pivot_id = row.high, i
                up_down = 1

        # Current trend is up
        elif up_down == 1:
            # If got higher than last pivot, update the swing
            if row.high > pivot:
                # Remove the last pivot, as it wasn't a real one
                data.ix[i + offset, 'swings'] = data.ix[last_pivot_id + offset, 'swings'] + (row.high - data.ix[last_pivot_id + offset, 'high'])
                data.ix[last_pivot_id + offset, 'swings'] = np.nan
                pivot, last_pivot_id = row.high, i
            elif row.low < pivot - diff:
                data.ix[i + offset, 'swings'] = row.low - pivot
                pivot, last_pivot_id = row.low, i
                # Change the trend indicator
                up_down = -1

        # Current trend is down
        elif up_down == -1:
             # If got lower than last pivot, update the swing
            if row.low < pivot:
                # Remove the last pivot, as it wasn't a real one
                data.ix[i + offset, 'swings'] = data.ix[last_pivot_id + offset, 'swings'] + (row.low - data.ix[last_pivot_id + offset, 'low'])
                data.ix[last_pivot_id + offset, 'swings'] = np.nan
                pivot, last_pivot_id = row.low, i
            elif row.high > pivot - diff:
                data.ix[i + offset, 'swings'] = row.high - pivot
                pivot, last_pivot_id = row.high, i
                # Change the trend indicator
                up_down = 1

    
    print(data[data['swings'].notnull()])


df = pd.read_csv('D:/workspace/java/fx/files/eurusd/DAT_ASCII_EURUSD_M1_2016.csv', sep=';',names = ["time", "open", "high", "low","close","dummy"])
df = df.drop(labels='dummy', axis=1)

df = df[(df['time'] > '20161212 0629') & (df['time'] < '20161212 21')]

get_pivots(df)
