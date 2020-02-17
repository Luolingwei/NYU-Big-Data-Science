import pandas as pd
from sklearn.ensemble import RandomForestRegressor,GradientBoostingRegressor
from sklearn.model_selection import train_test_split,GridSearchCV
from sklearn.metrics import mean_squared_error,r2_score
from xgboost import XGBRegressor
import matplotlib.pyplot as plt
import numpy as np
import warnings


def clean(df):
    """
    clean up points with incorrect location and abnormal duration
    """
    df=df.dropna()
    m = np.mean(df['trip_duration'])
    s = np.std(df['trip_duration'])
    df = df[df['trip_duration'] <= m + 2 * s]
    df = df[df['trip_duration'] >= m - 2 * s]

    return df[(df['pickup_longitude']>-80) & (df['pickup_longitude']<-70) &
              (df['pickup_latitude']>35) & (df['pickup_latitude']<45) &
              (df['dropoff_longitude']>-80) & (df['dropoff_longitude']<-70) &
              (df['dropoff_latitude']>35) & (df['dropoff_latitude']<45)]


def sphere_dist(lat1, lng1, lat2, lng2):
    '''
    calculate sphere distance between start and end point
    '''
    lat1, lng1, lat2, lng2 = map(np.radians, (lat1, lng1, lat2, lng2))
    AVG_EARTH_RADIUS = 6371  # in km
    lat = lat2 - lat1
    lng = lng2 - lng1
    d = np.sin(lat * 0.5) ** 2 + np.cos(lat1) * np.cos(lat2) * np.sin(lng * 0.5) ** 2
    h = 2 * AVG_EARTH_RADIUS * np.arcsin(np.sqrt(d))
    return h

def manhattan_dist(lat1, lng1, lat2, lng2):
    '''
    calculate manhattan distance between start and end point
    '''
    a = sphere_dist(lat1, lng1, lat1, lng2)
    b = sphere_dist(lat1, lng1, lat2, lng1)
    return a + b

def direction_cal(lat1, lng1, lat2, lng2):
    '''
    calculate move direction
    '''
    AVG_EARTH_RADIUS = 6371  # in km
    lng_delta_rad = np.radians(lng2 - lng1)
    lat1, lng1, lat2, lng2 = map(np.radians, (lat1, lng1, lat2, lng2))
    y = np.sin(lng_delta_rad) * np.cos(lat2)
    x = np.cos(lat1) * np.sin(lat2) - np.sin(lat1) * np.cos(lat2) * np.cos(lng_delta_rad)
    return np.degrees(np.arctan2(y, x))


def add_datetime_info(dataset):
    '''
    add year,month,day,hour,weekday info
    '''
    # Convert to datetime format
    dataset['pickup_datetime'] = pd.to_datetime(dataset['pickup_datetime'], format="%Y-%m-%d %H:%M:%S")
    dataset['hour'] = dataset.pickup_datetime.dt.hour
    dataset['day'] = dataset.pickup_datetime.dt.day
    dataset['month'] = dataset.pickup_datetime.dt.month
    dataset['weekday'] = dataset.pickup_datetime.dt.weekday
    dataset['year'] = dataset.pickup_datetime.dt.year

    return dataset

def add_routeInfo_train(train):
    '''
    add additional routeInfo to train
    '''
    fr1 = pd.read_csv('fastest_routes_train_part_1.csv',usecols=['id', 'total_distance', 'total_travel_time', 'number_of_steps'])
    fr2 = pd.read_csv('fastest_routes_train_part_2.csv',usecols=['id', 'total_distance', 'total_travel_time', 'number_of_steps'])
    train_routeInfo = pd.concat((fr1, fr2))
    train = train.merge(train_routeInfo, how='left', on='id')
    return train

def add_routeInfo_test(test):
    '''
    add additional routeInfo to test
    '''
    test_routeInfo = pd.read_csv('fastest_routes_test.csv',usecols=['id', 'total_distance', 'total_travel_time', 'number_of_steps'])
    test = test.merge(test_routeInfo, how='left', on='id')
    return test


# settings
warnings.filterwarnings("ignore")
pd.set_option('display.width',None)

# load data
raw=pd.read_csv("train.csv")
test_df=pd.read_csv("test.csv")

# data cleaning
train_df=clean(raw)
test_df=test_df.fillna(0)
print(train_df.describe())
print(test_df.describe())

# feature engineering
train_df = add_datetime_info(train_df)
test_df = add_datetime_info(test_df)
train_df['sphere_dist'] = sphere_dist(train_df['pickup_latitude'], train_df['pickup_longitude'],
                                      train_df['dropoff_latitude'], train_df['dropoff_longitude'])
train_df['manhattan_dist'] = manhattan_dist(train_df['pickup_latitude'], train_df['pickup_longitude'],
                                      train_df['dropoff_latitude'], train_df['dropoff_longitude'])
train_df['direction'] = direction_cal(train_df['pickup_latitude'], train_df['pickup_longitude'],
                                      train_df['dropoff_latitude'], train_df['dropoff_longitude'])

test_df['sphere_dist'] = sphere_dist(test_df['pickup_latitude'], test_df['pickup_longitude'],
                                      test_df['dropoff_latitude'], test_df['dropoff_longitude'])
test_df['manhattan_dist'] = manhattan_dist(test_df['pickup_latitude'], test_df['pickup_longitude'],
                                      test_df['dropoff_latitude'], test_df['dropoff_longitude'])
test_df['direction'] = direction_cal(test_df['pickup_latitude'], test_df['pickup_longitude'],
                                      test_df['dropoff_latitude'], test_df['dropoff_longitude'])

train_df=add_routeInfo_train(train_df)
test_df=add_routeInfo_test(test_df)
train_df=train_df.dropna()
test_df.fillna(0)
print(train_df.head())
print(test_df.head())


# model training
drop_attr={'id','vendor_id','pickup_datetime','dropoff_datetime','store_and_fwd_flag','trip_duration'}
features=[c for c in train_df.columns if c not in drop_attr]
train_x,train_y=train_df[features],train_df['trip_duration']
test_x=test_df[features]
print(features)

# RF=RandomForestRegressor()
# RF.fit(train_x,train_y)
# Prediction1=RF.predict(test_x)
# print(r2_score(test_y,Prediction1))

XGB=XGBRegressor()
XGB.fit(train_x,train_y)
Prediction2=XGB.predict(test_x)
# print(r2_score(test_y,Prediction2))

# GBDT=GradientBoostingRegressor(n_estimators=300,min_samples_leaf=20,min_samples_split=100)
# GBDT.fit(train_x,train_y)
# Prediction3=GBDT.predict(test_x)
# print(r2_score(test_y,Prediction3))

Prediction=Prediction2
# Prediction=0*Prediction1+1*Prediction2+0*Prediction3
# print(r2_score(test_y,Prediction))


# performance visualization
# error=Prediction-test_y
# print(error.describe())
# plt.figure(figsize=(15,7))
# plt.subplot(2,1,2)
# plt.plot(np.arange(len(error)),error,label='error')
# plt.legend()
# plt.subplot(2,1,1)
# plt.plot(np.arange(len(Prediction)),Prediction,'ro--',label='Prediction')
# plt.plot(np.arange(len(test_y)),test_y,'ko--',label='Real')
# plt.legend()
# plt.show()


# prediction output
submission=pd.DataFrame({'id':test_df.id,'trip_duration':Prediction})
submission.to_csv('submission.csv',index=False)