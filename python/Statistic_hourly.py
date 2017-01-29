from array import array
import numpy as np
import pandas as pd
from pandas.core.frame import DataFrame

df=pd.read_csv('D:/workspace/java/fx/files/eurusd/DAT_ASCII_EURUSD_M1_2016.csv', sep=';',names = ["t", "o", "h", "l","c","dummy"])

df=df[df.t > '20161212']

df['date'] = df.t.str[:8]
df['hour'] = df.t.str[9:11]
df['minute'] = df.t.str[11:13]

cols = df.columns.tolist()
df = df[['date','hour','minute','o','h','l','c']]
days = df['date'].unique()

data = np.empty(shape=[0, 6])

for i in range(0, days.size):
    for j in range(0, 24):
        #print(str(df['date'].ix[i]) + ":" + str(j))
        hourly = df[(df['date']==days[i]) & (df['hour']== str(j).zfill(2))]
        if(not hourly.empty):
            o = (hourly.o.values[0])
            c = (hourly.c.values[len(hourly) -1 ])
            h = hourly['h'].max()
            l = hourly['l'].min()
            data = np.append(data, [[days[i], j, o, h, c, l]], axis=0)

print(data)
