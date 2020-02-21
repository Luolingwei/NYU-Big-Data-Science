import pandas as pd
import numpy as np
import collections
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import GridSearchCV
import matplotlib.pyplot as plt
from sklearn.utils import shuffle
from xgboost import XGBClassifier
import xlrd

def normanize(df):
    # Data Normalization
    for i in range(3,df.shape[1]):
        df.iloc[:,i]=(df.iloc[:,i]-df.iloc[:,i].min())/(df.iloc[:,i].max()-df.iloc[:,i].min())
    return df

def label_traintest(df):
    # create istrain label
    labels=[]
    provinces=collections.Counter(df['省'])
    for prov,num in provinces.items():
        curlabel=shuffle([0]*(num//2)+[1]*(num-num//2))
        labels+=curlabel
    df['istrain']=pd.Series(labels)
    return df

def freature_selection(train_df):
    train_x, train_y = train_df.iloc[:,3:13],train_df.iloc[:,2]
    XGB = XGBClassifier()
    XGB.fit(train_x, train_y)
    importance = list(zip(train_x.columns, XGB.feature_importances_))
    df_score = pd.DataFrame(importance, columns=['feature', 'fscore'])
    df_score['fscore'] = df_score['fscore'] / df_score['fscore'].sum()
    df_score.sort_values(by=['fscore'], ascending=True, inplace=True)
    df_score.plot(kind='barh', x='feature', y='fscore', legend=False, figsize=(11, 8))
    plt.title('XGBoost Feature Importance')
    plt.xlabel('relative importance')
    plt.show()


if __name__ == '__main__':
    pd.set_option('display.width', None)
    English_columns=["Province","County","Povy","NTL","Slope","RoadDensity","RailwayDensity","PopuDensity","ConstructionLand","EducationYear","ImmigrantPopu","PerHousing","NonAgriculturalPopu","Istrain"]
    colors = ['blue', 'red', 'green', 'darkorange', 'lime', 'crimson', 'peru', 'gold', 'royalblue', 'olive', 'black',
              'salmon', 'deeppink', 'purple', 'violet', 'burlywood', 'maroon', 'springgreen', 'fuchsia', 'chocolate',
              'teal', 'goldenrod']

    # Generate Train Test Data
    # raw = pd.read_excel('全国各县指标统计总表（以此表为基准）.xlsx', sheet_name='Sheet1', usecols=[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13])
    # df=normanize(raw)
    # df=label_traintest(df)
    # train_df,test_df=df[df['istrain']==1],df[df['istrain']==0]
    # train_df.columns,test_df.columns=English_columns,English_columns
    # train_df.to_csv('train_data.csv',index=False)
    # test_df.to_csv('test_data.csv',index=False)

    train_df=pd.read_csv('train_data.csv')
    freature_selection(train_df)