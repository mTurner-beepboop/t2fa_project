import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

filepath = 'C:/Users/Turne/Documents/Project Folder/Project Repo/data/raw/study' #Absolute for ease of use, will require alteration for replication
files = ['/log1.csv','/log2.csv','/log3.csv']
plt.figure(figsize=(12,4))
i=0

mean_times = []
max_times = []
min_times = []

success_rates_at = []
success_rates_au = []

for file in files:
    f = filepath + file

    #First read from file   
    data = pd.read_csv(f, names=['Time Began', 'Success', 'Attempts', 'Total time', 'Model', 'Location'])[1:]
    data = data.dropna()
    
    #Select relevant data for the table
    data['Time per attempt'] = data['Total time'] / data['Attempts']
    data['tpa rolling'] = data['Time per attempt'].rolling(window=5, min_periods=1).mean()
    attempt_time_sec = data['Time per attempt'] / 1000

    #Find data on overall time taken
    max_times.append(data['Time per attempt'].max()/1000)
    min_times.append(data['Time per attempt'].min()/1000)
    mean_times.append(data['Time per attempt'].mean()/1000)
    
    #Find success rates
    attempts = data['Attempts'].sum()
    successes = data['Success'].loc[data['Success'] == 'TRUE'].count()
    success_rates_at.append((successes/attempts)*100)
    success_rates_au.append((successes/data['Success'].count())*100)

    #Create graph with mean window
    total = attempt_time_sec.count()
    window = total/7
    plt.subplot(131+i)
    plt.plot(attempt_time_sec, label='Attempt time (sec)')
    plt.plot(data['tpa rolling']/1000, label='Rolling mean (window=5)')
    plt.xlabel("Attempt number")
    plt.ylabel("Attempt time (sec)")
    plt.legend()
    i+=1

#plt.savefig("timetaken_with_rolling_mean.png")
print(mean_times)
print(max_times)
print(min_times)
print(success_rates_at)
print(success_rates_au)