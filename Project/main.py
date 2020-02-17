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
    clean up dirty data points
    """
    return df[(df['fare_amount']>0) &
              (df['pickup_longitude']>-80) & (df['pickup_longitude']<-70) &
              (df['pickup_latitude']>35) & (df['pickup_latitude']<45) &
              (df['dropoff_longitude']>-80) & (df['dropoff_longitude']<-70) &
              (df['dropoff_latitude']>35) & (df['dropoff_latitude']<45) &
              (df['passenger_count']>0) & (df['passenger_count']<10)]


def sphere_dist(pickup_lat, pickup_lon, dropoff_lat, dropoff_lon):
    """
    Return distance along great radius between pickup and dropoff coordinates.
    """
    # Define earth radius (km)
    R_earth = 6371

    # Convert degrees to radians
    pickup_lat, pickup_lon, dropoff_lat, dropoff_lon = map(np.radians,
                                                           [pickup_lat, pickup_lon,
                                                            dropoff_lat, dropoff_lon])
    # Compute distances along lat, lon dimensions
    dlat = dropoff_lat - pickup_lat
    dlon = dropoff_lon - pickup_lon

    # Compute haversine distance
    a = np.sin(dlat / 2.0) ** 2 + np.cos(pickup_lat) * np.cos(dropoff_lat) * np.sin(dlon / 2.0) ** 2

    return 2 * R_earth * np.arcsin(np.sqrt(a))


def add_airport_dist(df):
    """
    Return minumum distance from pickup or dropoff coordinates to each airport.
    JFK: John F. Kennedy International Airport
    EWR: Newark Liberty International Airport
    LGA: LaGuardia Airport
    """
    jfk_coord = (40.639722, -73.778889)
    ewr_coord = (40.6925, -74.168611)
    lga_coord = (40.77725, -73.872611)

    pickup_lat = df['pickup_latitude']
    dropoff_lat = df['dropoff_latitude']
    pickup_lon = df['pickup_longitude']
    dropoff_lon = df['dropoff_longitude']

    pickup_jfk = sphere_dist(pickup_lat, pickup_lon, jfk_coord[0], jfk_coord[1])
    dropoff_jfk = sphere_dist(jfk_coord[0], jfk_coord[1], dropoff_lat, dropoff_lon)
    pickup_ewr = sphere_dist(pickup_lat, pickup_lon, ewr_coord[0], ewr_coord[1])
    dropoff_ewr = sphere_dist(ewr_coord[0], ewr_coord[1], dropoff_lat, dropoff_lon)
    pickup_lga = sphere_dist(pickup_lat, pickup_lon, lga_coord[0], lga_coord[1])
    dropoff_lga = sphere_dist(lga_coord[0], lga_coord[1], dropoff_lat, dropoff_lon)

    df['jfk_dist'] = pd.concat([pickup_jfk, dropoff_jfk], axis=1).min(axis=1)
    df['ewr_dist'] = pd.concat([pickup_ewr, dropoff_ewr], axis=1).min(axis=1)
    df['lga_dist'] = pd.concat([pickup_lga, dropoff_lga], axis=1).min(axis=1)

    return df


def add_datetime_info(dataset):
    # Convert to datetime format
    dataset['pickup_datetime'] = pd.to_datetime(dataset['pickup_datetime'], format="%Y-%m-%d %H:%M:%S UTC")
    dataset['hour'] = dataset.pickup_datetime.dt.hour
    dataset['day'] = dataset.pickup_datetime.dt.day
    dataset['month'] = dataset.pickup_datetime.dt.month
    dataset['weekday'] = dataset.pickup_datetime.dt.weekday
    dataset['year'] = dataset.pickup_datetime.dt.year

    return dataset

# settings
warnings.filterwarnings("ignore")
pd.set_option('display.width',None)

raw=pd.read_csv("local_train.csv")

# data cleaning
raw=raw.dropna()
# raw.plot.scatter('pickup_longitude','pickup_latitude')
# raw.plot.scatter('dropoff_longitude','dropoff_latitude')
train_df=clean(raw)

# feature engineering
train_df = add_datetime_info(train_df)
train_df = add_airport_dist(train_df)
train_df['distance'] = sphere_dist(train_df['pickup_latitude'], train_df['pickup_longitude'],
                                   train_df['dropoff_latitude'], train_df['dropoff_longitude'])

# model training
dropp_attr={'key','pickup_datetime','fare_amount'}
features=[c for c in train_df.columns if c not in dropp_attr]
print(features)
train_x,test_x,train_y,test_y=train_test_split(train_df[features],train_df['fare_amount'],test_size=0.2)

RF=RandomForestRegressor()
RF.fit(train_x,train_y)
Prediction1=RF.predict(test_x)
print(r2_score(test_y,Prediction1))

XGB=XGBRegressor()
XGB.fit(train_x,train_y)
Prediction2=XGB.predict(test_x)
print(r2_score(test_y,Prediction2))

GBDT=GradientBoostingRegressor(n_estimators=300,min_samples_leaf=20,min_samples_split=100)
GBDT.fit(train_x,train_y)
Prediction3=GBDT.predict(test_x)
print(r2_score(test_y,Prediction3))

Prediction=0*Prediction1+1*Prediction2+0*Prediction3
print(r2_score(test_y,Prediction))


# performance visualization
error=Prediction-test_y
print(error.describe())
plt.figure(figsize=(15,7))
plt.subplot(2,1,2)
plt.plot(np.arange(len(error)),error,label='error')
plt.legend()
plt.subplot(2,1,1)
plt.plot(np.arange(len(Prediction)),Prediction,'ro--',label='Prediction')
plt.plot(np.arange(len(test_y)),test_y,'ko--',label='Real')
plt.legend()
plt.show()