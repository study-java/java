@echo on
echo 正在执行脚本，请稍等......
::切换目录
cd C:\Users\hui\Desktop\project
::切换doc页面encode
chcp 65001
::生成提交修改日志
svn log -r 10:11 >>H:/test/result.txt
::文件锁定
svn lock http://192.168.99.60:18080/svn/project/trunk/业务培训/业务基础/股票业务基础知识.ppt -m "已确认"
::两个版本间的文件差异对比输出
svn diff --no-diff-deleted http://192.168.99.60:18080/svn/project/branches/0531/src/com.yss.acs.fundacc http://192.168.99.60:18080/svn/project/branches/0318/src/com.yss.acs.fundacc > H:/test/DiffFundacc01.txt
::创建文件夹
svn mkdir http://192.168.99.60:18080/svn/project/branches/Control/dev/"Project R & D"/PRL/ACS_5.0.3_DEV_20180731001.alpha -m "ACS_5.0.3_DEV_20180731001.alpha"
::文件复制
svn cp http://192.168.99.60:18080/svn/project/branches/0318/database/ http://192.168.99.60:18080/svn/project/branches/Control/dev/"Project R & D"/PRL/ACS_5.0.3_DEV_20180731001.alpha/ -m "database"
svn cp http://192.168.99.60:18080/svn/project/branches/0318/doc/ http://192.168.99.60:18080/svn/project/branches/Control/dev/"Project R & D"/PRL/ACS_5.0.3_DEV_20180731001.alpha/ -m "doc"
svn cp http://192.168.99.60:18080/svn/project/branches/0318/exe/ http://192.168.99.60:18080/svn/project/branches/Control/dev/"Project R & D"/PRL/ACS_5.0.3_DEV_20180731001.alpha/ -m "exe"
svn cp http://192.168.99.60:18080/svn/project/branches/0318/report/ http://192.168.99.60:18080/svn/project/branches/Control/dev/"Project R & D"/PRL/ACS_5.0.3_DEV_20180731001.alpha/ -m "report"
svn cp http://192.168.99.60:18080/svn/project/branches/0318/src/ http://192.168.99.60:18080/svn/project/branches/Control/dev/"Project R & D"/PRL/ACS_5.0.3_DEV_20180731001.alpha/ -m "src"

echo 脚本执行完毕......
echo

