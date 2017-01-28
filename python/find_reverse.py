import pandas as pd
import numpy as np

def get_pivots():
    data = pd.DataFrame.from_csv('D:/workspace/java/fx/files/eurusd/DAT_ASCII_EURUSD_M1_2016.csv')
    data['swings'] = np.nan

    pivot = data.irow(0).open
    last_pivot_id = 0
    up_down = 0

    diff = .3

    for i in range(0, len(data)):
        row = data.irow(i)

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
            elif row.low < pivot - diff:
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
            elif row.high > pivot - diff:
                data.ix[i, 'swings'] = row.high - pivot
                pivot, last_pivot_id = row.high, i
                # Change the trend indicator
                up_down = 1

    print(data)
    
get_pivots()
