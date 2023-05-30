# FQWeb
[![GitHub license](https://img.shields.io/github/license/duzhaokun123/FuckCainiao?style=flat-square)](https://github.com/fengyuecanzhu/FQWeb/blob/master/LICENSE)
![Android SDK min 24](https://img.shields.io/badge/Android%20SDK-%3E%3D%2024-brightgreen?style=flat-square&logo=android)
![Android SDK target 33](https://img.shields.io/badge/Android%20SDK-target%2033-brightgreen?style=flat-square&logo=android)
![Xposed Module](https://img.shields.io/badge/Xposed-Module-blue?style=flat-square)

番茄小说Web服务

## 使用
web服务可在番茄小说设置界面开启，默认运行在9999端口

## API列表
### 搜索
```
method：GET
url：http://localhost:9999/search?query=关键字
```
### 获取书籍详情
```
method：GET
url：http://localhost:9999/info?book_id=书籍ID
```
### 获取书籍目录
```
method：GET
url：http://localhost:9999/catalog?book_id=书籍ID
```
### 获取章节内容
```
method：GET
url：http://localhost:9999/content?item_id=章节itemId
```
### 获取发现书籍
```
method：GET
url：http://localhost:9999/reading/bookapi/bookmall/cell/change/v1/
params：和番茄保持一致

method：GET
url：http://localhost:9999/reading/bookapi/new_category/landing/v/
params：和番茄保持一致
```

## 下载
从[RELEASE](https://github.com/fengyuecanzhu/FQWeb/releases/latest)下载
