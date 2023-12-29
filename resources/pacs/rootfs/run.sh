service pacs restart

# Docker容器后台运行,就必须有一个前台进程
dummy=/dummy
if [ ! -f "$dummy" ]; then
	touch $dummy
fi
tail -f $dummy