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
        # select 70% as train, 30% as test in each province
        train_num = int(0.7*num)
        test_num = num - train_num
        curlabel=shuffle([0]*test_num+[1]*train_num)
        labels+=curlabel
    df['istrain']=pd.Series(labels)
    return df

def freature_selection(train_df):
    train_x, train_y = train_df.iloc[:,3:43],train_df.iloc[:,2]
    XGB = XGBClassifier()
    XGB.fit(train_x, train_y)
    importance = list(zip(train_x.columns, XGB.feature_importances_))
    df_score = pd.DataFrame(importance, columns=['feature', 'fscore'])
    df_score['fscore'] = df_score['fscore'] / df_score['fscore'].sum()
    df_score.sort_values(by=['fscore'], ascending=True, inplace=True)
    df_score.plot(kind='barh', x='feature', y='fscore', legend=False, figsize=(11, 8))
    plt.title('XGBoost Feature Importance Ranking')
    plt.xlabel('relative importance')
    plt.show()

    # select top 15 important features to do model training
    importance.sort(key = lambda x: -x[1])
    topfeatures = ["Province","County","Povy"]
    topfeatures += [f for f,_ in importance[:15]]
    print(topfeatures)
    return train_df[topfeatures],topfeatures


if __name__ == '__main__':
    pd.set_option('display.width', None)
    English_columns=["Province","County",
                     "Povy","MeanNTL","RoadDensity","RailwayDensity","RoadAccess","AvgSlope","AvgEle","ConstructionLand","Corpland","PopuDensity","MalePopu","Popu65","ImmigrantPopu","NonAgriculturalPopu","PerHousing","EducationYear",
                    "v1","v2","v3","v4","v5","v6","v7","v8","v9","v10","v11","v12","v13","v14","v15","v16","v17","v18","v19","v20","v21","v22","v23","v24","v25",
                    "Istrain"]

    colors = ['blue', 'red', 'green', 'darkorange', 'lime', 'crimson', 'peru', 'gold', 'royalblue', 'olive', 'black',
              'salmon', 'deeppink', 'purple', 'violet', 'burlywood', 'maroon', 'springgreen', 'fuchsia', 'chocolate',
              'teal', 'goldenrod']

    # Generate Train Test Data
    # raw = pd.read_excel('全国各县指标统计总表.xlsx', sheet_name='Sheet1', usecols=[i for i in range(1,44)])
    # df=normanize(raw)
    # df=label_traintest(df)
    # train_df,test_df=df[df['istrain']==1],df[df['istrain']==0]
    # train_df.columns,test_df.columns=English_columns,English_columns
    # train_df.to_csv('train_data.csv',index=False)
    # test_df.to_csv('test_data.csv',index=False)

    train_df=pd.read_csv('train_data.csv')
    # test_df=pd.read_csv('test_data.csv')
    train_df_input,topfeatures = freature_selection(train_df)
    # test_df_input = test_df[topfeatures]
    # train_df_input.to_csv('train_data_input.csv',index=False)
    # test_df_input.to_csv('test_data_input.csv',index=False)

