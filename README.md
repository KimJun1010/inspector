# SecInspector  
## 介绍
- SecInspector为IDEA静态代码扫描插件，侧重于在编码过程中发现项目潜在的安全风险（一键搜索所有的sink点，替代传统control+F大法），部分漏洞并提供一键修复能力，提升安全攻防人员代码审计效率、开发人员代码安全质量
- 插件利用IDEA原生Inspection机制检查项目，自动检查当前活跃窗口的活跃文件，检查速度快，占用资源少，官方介绍：https://www.jetbrains.com/help/idea/inspections-settings.html#5
- 插件提供的规则名称均以"SecInspector"开头，由深信服深蓝实验室天威战队强力驱动

## 版本支持  
Intellij IDEA ( Community / Ultimate ) >= 2021.3  


## 安装使用  
IDEA "Settings" --> "Plugins"，获取SecInspector.jar后，选中从本地磁盘安装该插件  
<img width="1035" alt="image" src="https://github.com/KimJun1010/inspector/assets/49397311/23d1e611-0bca-4e1e-abca-c6c8bdead910">


### 使用：方法一
该插件会在您编码过程中自动扫描当前编辑的代码，并实时提醒安全风险
![image](https://github.com/KimJun1010/inspector/assets/49397311/d3400947-c32f-4ca9-84ff-9254f6c4e042)


### 使用：方法二
IDEA 提供Inspect Code功能支持对整个项目/指定范围文件进行自定义规则的扫描
![image](https://github.com/KimJun1010/inspector/assets/49397311/e2f5b2b7-9eba-4e0d-938a-50a90492d9e7)

可以漏洞扫描需求，选中SecInspector规则
![image](https://github.com/KimJun1010/inspector/assets/49397311/0405b8f7-5fcc-4d5f-9fc6-99d1b295730a)

插件规则
覆盖常见的RCE、反序列化、SQL注入、JNDI注入、任意文件读取/写入等类型的sink点
![image](https://github.com/KimJun1010/inspector/assets/49397311/9db30d24-9c0e-4fd8-a572-e8e766b9f936)


## 项目实战
以项目审计为例，反编译jar包后，把源码标记为Source Root，扫描业务代码，跟踪该方法即可分析出rce的漏洞
<img width="905" alt="image" src="https://github.com/KimJun1010/inspector/assets/49397311/c8884a34-4d5d-4da4-a762-1d18e19fffee">


## 项目参考
https://github.com/momosecurity/momo-code-sec-inspector-java
