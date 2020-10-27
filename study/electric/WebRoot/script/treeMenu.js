var menu = {
	setting: {
        isSimpleData: true,
        treeNodeKey: "mid",
        treeNodeParentKey: "pid",
        showLine: true,
        root: {
            isRoot: true,
            nodes: []
        }
    },
	loadMenuTree:function(){
		//使用ajax完整对数据的加载
		$.post("elecMenuAction_showMenu.do",{},function(privilegeDate){
			$("#menuTree").zTree(menu.setting, privilegeDate);
		});
	}
};

$().ready(function(){
	menu.loadMenuTree();
});
