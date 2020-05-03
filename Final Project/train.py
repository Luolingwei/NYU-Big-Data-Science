from sklearn.ensemble import RandomForestClassifier,GradientBoostingClassifier
from sklearn.svm import SVC
from xgboost import XGBClassifier
from sklearn.model_selection import train_test_split,GridSearchCV
from sklearn.metrics import r2_score
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import warnings

warnings.filterwarnings("ignore")
pd.set_option('display.width',None)

#准备数据
train_df=pd.read_csv('train_data_input.csv')
test_df=pd.read_csv('test_data_input.csv')
train_x,train_y = train_df.iloc[:,3:10],train_df.iloc[:,2]
test_x, test_y = test_df.iloc[:,3:10],test_df.iloc[:,2]

#寻找最佳参数
# clf1=GradientBoostingClassifier()
# param_dist={'n_estimators':[300,500,800,1000],'min_samples_leaf':[20,50,100],'min_samples_split':[100,300,800]}
# grid_search=GridSearchCV(clf1,param_dist,scoring='neg_mean_squared_error',cv=5)
# grid_search.fit(train_x,train_y)
# print(grid_search.best_params_)
#
# clf2=XGBRegressor()
# param_dist={'n_estimators':[300,500,800,1000,2000],'learning_rate':[0,1,0.2,0.5,0.8],'gamma':[0,1,10,50,100],
#             'min_child_weight':range(1,6,2),'subsample':[i/100 for i in range(75,90,5)],'max_depth':[1,10,20,100,500]}
# grid_search=GridSearchCV(clf2,param_dist,scoring='neg_mean_squared_error',cv=5)
# grid_search.fit(train_x,train_y)
# print(grid_search.best_params_)
#
# clf3=RandomForestRegressor()
# param_dist={'n_estimators':[300,500,800,1000],'max_features':[1,2,3,4,5,6],'min_samples_leaf':[20,50,100],
#             'min_samples_split':[100,200,300],'max_depth':[1,10,20,100]}
# grid_search=GridSearchCV(clf3,param_dist,scoring='neg_mean_squared_error',cv=5)
# grid_search.fit(train_x,train_y)
# print(grid_search.best_params_)

#模型训练
# GBDT=GradientBoostingClassifier(n_estimators=280,min_samples_leaf=20,min_samples_split=150)
# GBDT.fit(train_x,train_y)
# Prediction1=GBDT.predict(test_x)

# XGB=XGBClassifier(n_estimators=300,max_depth=3,scale_pos_weight=2)
# XGB.fit(train_x,train_y)
# Prediction2=XGB.predict(test_x)

RF=RandomForestClassifier(n_estimators=300,max_depth=15)
RF.fit(train_x,train_y)
Prediction3=RF.predict(test_x)

Prediction=Prediction3

correctNum = sum([Prediction[i]==test_y[i] for i in range(len(Prediction))])
correctNum1 = sum([(Prediction[i]==test_y[i] and test_y[i]==1) for i in range(len(Prediction))])
correctNum0 = sum([(Prediction[i]==test_y[i] and test_y[i]==0) for i in range(len(Prediction))])
totalNum = len(Prediction)
print("Total test counties: %d"%totalNum)
print("Number of correctly predicted counties: %d"%correctNum)
print("Total poor counties: %d"%sum(test_y))
print("Number of counties predicted as poor: %d"%sum(Prediction))
print("Overall AUC is %f"%(correctNum/totalNum))
print("Poor County AUC is %f"%(correctNum1/sum(test_y)))
print("Non-Poor County AUC is %f"%(correctNum0/(totalNum-sum(test_y))))

#精度可视化
plt.figure(figsize=(15,7))
plt.subplot(2,1,1)
plt.title("Chinese county-level poverty prediction")
plt.plot(np.arange(len(Prediction)),Prediction,'ro',label='Prediction')
plt.legend()
plt.subplot(2,1,2)
plt.plot(np.arange(len(test_y)),test_y,'ko',label='Real')
plt.legend()
plt.show()

# 数据输出
# submission=pd.DataFrame({'Id':test.Id,'SalePrice':Prediction})
# submission.to_csv('C:/Users/asus/Desktop/submission.csv',index=False)