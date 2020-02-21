import pandas as pd
import numpy as np
import collections
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import GridSearchCV
import matplotlib.pyplot as plt
from sklearn.utils import shuffle
import xlrd

pd.set_option('display.width',None)
plt.rcParams['font.sans-serif']=['SimHei'] #解决中文问题
plt.rcParams['axes.unicode_minus'] = False #解决中文问题
colors=['blue','red','green','darkorange','lime','crimson','peru','gold','royalblue','olive','black','salmon','deeppink','purple','violet','burlywood','maroon','springgreen','fuchsia','chocolate','teal','goldenrod']

raw=pd.read_excel('全国各县指标统计总表（以此表为基准）.xlsx',sheet_name='Sheet1',usecols=[1,2,3,4,5,6,7,8,9,10,11,12,13])
print(raw.describe())

def normanize(df):
    # Data Normalization
    for i in range(3,raw.shape[1]):
        df.iloc[:,i]=(df.iloc[:,i]-df.iloc[:,i].min())/(df.iloc[:,i].max()-df.iloc[:,i].min())
    return df

def split_label(df):
    # create istrain label
    labels=[]
    provinces=collections.Counter(df['省'])
    for prov,num in provinces.items():
        curlabel=shuffle([0]*(num//2)+[1]*(num-num//2))
        labels+=curlabel
    df['istrain']=pd.Series(labels)
    return df

if __name__ == '__main__':
    df=normanize(raw)
    df=split_label(df)
    train_df,test_df=df[df['istrain']==1],df[df['istrain']==0]
    train_df.to_csv('train_data.csv',index=False)
    test_df.to_csv('test_data.csv',index=False)


# raw=pd.concat([raw.iloc[:,:2],np.log1p(raw.iloc[:,2:])],axis=1)
# povy=raw.iloc[:,1]
# train_data=raw.iloc[:,2:]


#寻找最佳参数
# clf1=RandomForestClassifier()
# param_dic={'n_estimators':[300,500,800,1000],'max_features':[4,5,6,7,8,9],'min_samples_leaf':[20,50,100],'max_depth':[1,10,20,100]}
# grid_search=GridSearchCV(clf1,param_dic,scoring="neg_mean_squared_error",cv=5)
# grid_search.fit(train_data,povy)
# print(grid_search.best_params_)

#模型训练-overall
# RF=RandomForestClassifier(n_estimators=500,min_samples_leaf=50,max_features=7,max_depth=100)
# RF.fit(train_data,povy)
# importance=pd.Series(RF.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance.sort_values(ascending=False,inplace=True)
# plt.bar(importance.index,importance.values,color=colors[0])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('总样本',fontsize=15)
# plt.show()


# #分省训练-安徽省
# Anhui=raw[raw['省']=='安徽省']
# Anhui_povy=Anhui.iloc[:,1]
# Anhui_data=Anhui.iloc[:,2:]
#
# RF_Anhui=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Anhui.fit(Anhui_data,Anhui_povy)
# importance_Anhui=pd.Series(RF_Anhui.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Anhui.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Anhui.index,importance_Anhui.values,color=colors[1])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('安徽省',fontsize=15)
# plt.show()
#
#
# #分省训练-甘肃省
# Gansu=raw[raw['省']=='甘肃省']
# Gansu_povy=Gansu.iloc[:,1]
# Gansu_data=Gansu.iloc[:,2:]
#
# RF_Gansu=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Gansu.fit(Gansu_data,Gansu_povy)
# importance_Gansu=pd.Series(RF_Gansu.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Gansu.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Gansu.index,importance_Gansu.values,color=colors[2])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('甘肃省',fontsize=15)
# plt.show()
#
#
# #分省训练-广西省
# Guangxi=raw[raw['省']=='广西壮族自治区']
# Guangxi_povy=Guangxi.iloc[:,1]
# Guangxi_data=Guangxi.iloc[:,2:]
#
# RF_Guangxi=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Guangxi.fit(Guangxi_data,Guangxi_povy)
# importance_Guangxi=pd.Series(RF_Guangxi.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Guangxi.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Guangxi.index,importance_Guangxi.values,color=colors[3])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('广西壮族自治区',fontsize=15)
# plt.show()
#
#
# #分省训练-贵州省
# Guizhou=raw[raw['省']=='贵州省']
# Guizhou_povy=Guizhou.iloc[:,1]
# Guizhou_data=Guizhou.iloc[:,2:]
#
# RF_Guizhou=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Guizhou.fit(Guizhou_data,Guizhou_povy)
# importance_Guizhou=pd.Series(RF_Guizhou.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Guizhou.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Guizhou.index,importance_Guizhou.values,color=colors[4])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('贵州省',fontsize=15)
# plt.show()
#
#
# #分省训练-海南省
# Hainan=raw[raw['省']=='海南省']
# Hainan_povy=Hainan.iloc[:,1]
# Hainan_data=Hainan.iloc[:,2:]
#
# RF_Hainan=RandomForestClassifier(n_estimators=1)
# RF_Hainan.fit(Hainan_data,Hainan_povy)
# importance_Hainan=pd.Series(RF_Hainan.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Hainan.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Hainan.index,importance_Hainan.values,color=colors[5])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('海南省',fontsize=15)
# plt.show()
#
#
# #分省训练-河北省
# Hebei=raw[raw['省']=='河北省']
# Hebei_povy=Hebei.iloc[:,1]
# Hebei_data=Hebei.iloc[:,2:]
#
# RF_Hebei=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Hebei.fit(Hebei_data,Hebei_povy)
# importance_Hebei=pd.Series(RF_Hebei.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Hebei.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Hebei.index,importance_Hebei.values,color=colors[6])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('河北省',fontsize=15)
# plt.show()
#
#
# #分省训练-河南省
# Henan=raw[raw['省']=='河南省']
# Henan_povy=Henan.iloc[:,1]
# Henan_data=Henan.iloc[:,2:]
#
# RF_Henan=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Henan.fit(Henan_data,Henan_povy)
# importance_Henan=pd.Series(RF_Henan.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Henan.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Henan.index,importance_Henan.values,color=colors[7])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('河南省',fontsize=15)
# plt.show()
#
#
# #分省训练-黑龙江省
# Heilongjiang=raw[raw['省']=='黑龙江省']
# Heilongjiang_povy=Heilongjiang.iloc[:,1]
# Heilongjiang_data=Heilongjiang.iloc[:,2:]
#
# RF_Heilongjiang=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Heilongjiang.fit(Heilongjiang_data,Heilongjiang_povy)
# importance_Heilongjiang=pd.Series(RF_Heilongjiang.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Heilongjiang.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Heilongjiang.index,importance_Heilongjiang.values,color=colors[8])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('黑龙江省',fontsize=15)
# plt.show()
#
#
# #分省训练-湖北省
# Hubei=raw[raw['省']=='湖北省']
# Hubei_povy=Hubei.iloc[:,1]
# Hubei_data=Hubei.iloc[:,2:]
#
# RF_Hubei=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Hubei.fit(Hubei_data,Hubei_povy)
# importance_Hubei=pd.Series(RF_Hubei.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Hubei.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Hubei.index,importance_Hubei.values,color=colors[9])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('湖北省',fontsize=15)
# plt.show()
#
#
# #分省训练-湖南省
# Hunan=raw[raw['省']=='湖南省']
# Hunan_povy=Hunan.iloc[:,1]
# Hunan_data=Hunan.iloc[:,2:]
#
# RF_Hunan=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Hunan.fit(Hunan_data,Hunan_povy)
# importance_Hunan=pd.Series(RF_Hunan.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Hunan.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Hunan.index,importance_Hunan.values,color=colors[10])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('湖南省',fontsize=15)
# plt.show()
#
#
# #分省训练-吉林省
# Jilin=raw[raw['省']=='吉林省']
# Jilin_povy=Jilin.iloc[:,1]
# Jilin_data=Jilin.iloc[:,2:]
#
# RF_Jilin=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Jilin.fit(Jilin_data,Jilin_povy)
# importance_Jilin=pd.Series(RF_Jilin.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Jilin.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Jilin.index,importance_Jilin.values,color=colors[11])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('吉林省',fontsize=15)
# plt.show()
#
#
# #分省训练-江西省
# Jiangxi=raw[raw['省']=='江西省']
# Jiangxi_povy=Jiangxi.iloc[:,1]
# Jiangxi_data=Jiangxi.iloc[:,2:]
#
# RF_Jiangxi=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Jiangxi.fit(Jiangxi_data,Jiangxi_povy)
# importance_Jiangxi=pd.Series(RF_Jiangxi.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Jiangxi.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Jiangxi.index,importance_Jiangxi.values,color=colors[12])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('江西省',fontsize=15)
# plt.show()
#
#
# #分省训练-内蒙古自治区
# Neimenggu=raw[raw['省']=='内蒙古自治区']
# Neimenggu_povy=Neimenggu.iloc[:,1]
# Neimenggu_data=Neimenggu.iloc[:,2:]
#
# RF_Neimenggu=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Neimenggu.fit(Neimenggu_data,Neimenggu_povy)
# importance_Neimenggu=pd.Series(RF_Neimenggu.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Neimenggu.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Neimenggu.index,importance_Neimenggu.values,color=colors[13])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('内蒙古自治区',fontsize=15)
# plt.show()
#
#
# #分省训练-宁夏回族自治区
# Ningxia=raw[raw['省']=='宁夏回族自治区']
# Ningxia_povy=Ningxia.iloc[:,1]
# Ningxia_data=Ningxia.iloc[:,2:]
#
# RF_Ningxia=RandomForestClassifier(n_estimators=2)
# RF_Ningxia.fit(Ningxia_data,Ningxia_povy)
# importance_Ningxia=pd.Series(RF_Ningxia.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Ningxia.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Ningxia.index,importance_Ningxia.values,color=colors[14])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('宁夏回族自治区',fontsize=15)
# plt.show()
#
#
# #分省训练-青海省
# Qinghai=raw[raw['省']=='青海省']
# Qinghai_povy=Qinghai.iloc[:,1]
# Qinghai_data=Qinghai.iloc[:,2:]
#
# RF_Qinghai=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Qinghai.fit(Qinghai_data,Qinghai_povy)
# importance_Qinghai=pd.Series(RF_Qinghai.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Qinghai.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Qinghai.index,importance_Qinghai.values,color=colors[15])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('青海省',fontsize=15)
# plt.show()
#
#
# #分省训练-山西省
# Shanxi=raw[raw['省']=='山西省']
# Shanxi_povy=Shanxi.iloc[:,1]
# Shanxi_data=Shanxi.iloc[:,2:]
#
# RF_Shanxi=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Shanxi.fit(Shanxi_data,Shanxi_povy)
# importance_Shanxi=pd.Series(RF_Shanxi.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Shanxi.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Shanxi.index,importance_Shanxi.values,color=colors[16])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('山西省',fontsize=15)
# plt.show()
#
#
# #分省训练-陕西省
# Shanxi2=raw[raw['省']=='陕西省']
# Shanxi2_povy=Shanxi2.iloc[:,1]
# Shanxi2_data=Shanxi2.iloc[:,2:]
#
# RF_Shanxi2=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Shanxi2.fit(Shanxi2_data,Shanxi2_povy)
# importance_Shanxi2=pd.Series(RF_Shanxi2.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Shanxi2.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Shanxi2.index,importance_Shanxi2.values,color=colors[17])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('陕西省',fontsize=15)
# plt.show()
#
#
# #分省训练-四川省
# Sichuan=raw[raw['省']=='四川省']
# Sichuan_povy=Sichuan.iloc[:,1]
# Sichuan_data=Sichuan.iloc[:,2:]
#
# RF_Sichuan=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Sichuan.fit(Sichuan_data,Sichuan_povy)
# importance_Sichuan=pd.Series(RF_Sichuan.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Sichuan.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Sichuan.index,importance_Sichuan.values,color=colors[18])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('四川省',fontsize=15)
# plt.show()
#
#
# #分省训练-新疆维吾尔自治区
# Xinjiang=raw[raw['省']=='新疆维吾尔自治区']
# Xinjiang_povy=Xinjiang.iloc[:,1]
# Xinjiang_data=Xinjiang.iloc[:,2:]
#
# RF_Xinjiang=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Xinjiang.fit(Xinjiang_data,Xinjiang_povy)
# importance_Xinjiang=pd.Series(RF_Xinjiang.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Xinjiang.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Xinjiang.index,importance_Xinjiang.values,color=colors[19])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('新疆维吾尔自治区',fontsize=15)
# plt.show()
#
# #分省训练-云南省
# Yunnan=raw[raw['省']=='云南省']
# Yunnan_povy=Yunnan.iloc[:,1]
# Yunnan_data=Yunnan.iloc[:,2:]
#
# RF_Yunnan=RandomForestClassifier(n_estimators=10,min_samples_leaf=10,max_features=7,max_depth=10)
# RF_Yunnan.fit(Yunnan_data,Yunnan_povy)
# importance_Yunnan=pd.Series(RF_Yunnan.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Yunnan.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Yunnan.index,importance_Yunnan.values,color=colors[20])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('云南省',fontsize=15)
# plt.show()
#
#
# #分省训练-重庆市
# Chongqing=raw[raw['省']=='重庆市']
# Chongqing_povy=Chongqing.iloc[:,1]
# Chongqing_data=Chongqing.iloc[:,2:]
#
# RF_Chongqing=RandomForestClassifier(n_estimators=2)
# RF_Chongqing.fit(Chongqing_data,Chongqing_povy)
# importance_Chongqing=pd.Series(RF_Chongqing.feature_importances_,index=raw.columns.values.tolist()[2:])
# importance_Chongqing.sort_values(ascending=False,inplace=True)
# plt.bar(importance_Chongqing.index,importance_Chongqing.values,color=colors[21])
# plt.xticks(rotation=30,fontsize=14)
# plt.yticks(fontsize=13)
# plt.title('重庆市',fontsize=15)
# plt.show()
