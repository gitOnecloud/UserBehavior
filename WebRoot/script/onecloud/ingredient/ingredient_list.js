var igdli_data = {};

/**
 * 读取环境信息
 */
$.getJSON('arena_all_js.action', function(data) {
	var searchData1 = [];
	var searchData2 = [{'value': '', 'text': '全部'}];
	$.each(data, function(k, v) {
		searchData1.push(v);
		searchData2.push(v);
	});
	$('#serverli_addparty').combobox('loadData', searchData1);
	$('#serverli_updateparty').combobox('loadData', searchData1);
	$('#igli_party').combobox('loadData', searchData2);
});
/**
 * 组件查询框的工具栏，添加到这里是防止重复加载
 */
if($('#igli_toolbar2').length == 0) {
	$('body').append('<div id="igli_toolbar2" style="padding:5px;height:auto;">'+
		'<div style="padding: 5px 0 0 4px;">'+
		'组件名称：<input id="igli_name2" class="igdli_input160" />'+
		'&ensp;<a href="javascript:;" class="easyui-linkbutton"'+
			'onclick="$(\'#igli_igf\').combogrid(\'grid\').datagrid(\'load\');"'+
			'data-options="iconCls:\'icon-search\'">查找</a>'+
	'</div></div>');
} else {
	$('#igli_name2').val('');
}
/**
 * 组件查询框
 */
$('#igli_igf').combogrid({
	panelHeight: 250,
	panelWidth: 500,
	idField: 'id',
	textField: 'name',
	fitColumns: true,//自动调整单元格宽度
	pagination: true,//分页
	toolbar: '#igli_toolbar2',//工具条
	url: 'ingredientdefine_list_js.action?_=' + Math.random(),
	queryParams: {
    	"page.name": {"id":"igli_name2","type":"text"}
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns: [[
       {field: "name", title: "组件名称", width: 80, align: 'center'},
       {field: "port", title: "端口", width: 60, align: 'center',
    	   formatter: function(value, row ,index) {
    		   if(value == 0) {
    			   return '';
    		   } else {
    			   return value;
    		   }
       }},
       {field: "pname", title: "父组件", width: 80, align: 'center'},
       {field: "type", title: "类型", width: 60, align: 'center'},
       {field: "register", title: "注册组件", width: 40, align: 'center' ,
    	   formatter: function(value, row ,index) {
    		   if(value == 1) {
    			   return '是';
    		   } else {
    			   return '';
    		   }
       }}
     ]]
});



/**
 * 主界面
 */
$('#igli_table').datagrid({
	url: 'ingredient_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#igli_toolbar',//工具条
    queryParams: {
    	"page.party_id": {"id":"igli_party","type":"combobox"},
    	"page.idf_id": {"id":"igli_igf","type":"combogrid"},
    	"page.isActive": {"id":"igli_isActive","type":"combobox"},
    	"page.host": {"id":"igli_host","type":"combobox"},
    	"page.ip": {"id":"igli_ip","type":"combobox"},
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true,align: 'center'},
	   {field: "server_id", title: "ID"},
       {field: "ip", title: "Ip",width:15,align: 'center'},
       {field: "party", title: "环境",width:20,align: 'center'},
       {field: "idf", title: "组件名",width:40,align: 'center',
    	   formatter: function(value, row ,index) {
    		   var names = row.idf.split(',');
    		   var actives = row.ifd_active.split(',');
    		   var str = '';
    		   $.each(names, function(k, v) {
    			   if(actives[k] == 0) {
    				   str = str + '<s>' + v + '</s>, ';
    			   } else {
    				   str = str + v + ', ';
    			   }
    		   });
    		   return str.substring(0, str.length-2);
    	   }
       },
       {field: "isActive", title: "是否有效",width:15,align: 'center',
    	   formatter: function(value, row ,index) {
    		   if(value == 1) {
    			   return '启用';
    		   } else {
    			   return '停用';
    		   }
       }},
       {field: "host_ip", title: "物理机IP",width:15,align: 'center'},
       {field: "remark", title: "备注",width:15,align: 'center'},
       {field: 'action', title: '操作', width: 20,align: 'center',
			formatter: function(value, row, index) {
				var e = '<a href="javascript:;" onclick="server_detail(' + index + ')">软硬件信息</a>&emsp;' +
					'<a href="javascript:;" onclick="igdli_nagios(' + index + ')">监控</a>';
				return e;
		}}
	]]
});



//初始化添加窗口
$('#server_add_dd').dialog({
	buttons:[{
		title: "添加服务器",
		text:'添加',
		iconCls:'icon-ok',
		handler:function(){
			$.post("server_add.action", $("#server_add_form").serialize(), function(data) {
				if(data.status == 0) {
					$('#server_add_dd').dialog('close');
					index_mess(data.mess, 4);
					$('#igli_table').datagrid('reload');
				} else {
					index_mess(data.mess, 3);
				}
			}, "json");
		}
	},{
		text:'&nbsp;取消&nbsp;',
		handler:function(){
			$('#server_add_dd').dialog('close');
		}
	}]
});

//初始化修改窗口
$('#server_update_dd').dialog({
	buttons:[{
		title: "修改服务器",
		text:'修改',
		iconCls:'icon-ok',
		handler:function(){
			$.post("server_update.action", $("#server_update_form").serialize(), function(data) {
				if(data.status == 0) {
					$('#server_update_dd').dialog('close');
					index_mess(data.mess, 4);
					$('#igli_table').datagrid('reload');
				} else {
					index_mess(data.mess, 3);
				}
			}, "json");
		}
	},{
		text:'&nbsp;取消&nbsp;',
		handler:function(){
			$('#server_update_dd').dialog('close');
		}
	}]
});

//初始化软硬件信息窗口
$('#server_detail_dd').dialog({
	resizable:true,
	maximizable:true,
	onMaximize:function(){
		get_server_detail($("#dserver_id").val());
	},
	onRestore:function(){
		get_server_detail($("#dserver_id").val());
	},
	buttons:[{
		title: "服务器软硬信息",
		text:'调用nagios更新',
		iconCls:'icon-reload',
		handler:function(){
			$.post("server_updatedetails.action", $("#detail_form").serialize(), function(data) {
				if(data.status == 0) {
					//server_detail();
					$('#server_detail').datagrid('reload');
				} else {
					index_mess(data.mess, 3);
					$('#server_detail').datagrid('reload');
				}
			}, "json"); 
		}
	},{
		text:'&nbsp;关闭&nbsp;',
		handler:function(){
			$('#server_detail_dd').dialog('close');
		}
	}]
});

//打开添加窗口
function server_add() {
	$('#server_add_dd').dialog('open').dialog('setTitle','服务器增加');
}

//打开修改窗口
function server_update() {
	var row = $('#igli_table').datagrid('getSelected');
	if(row){
		//alert(row.server_id);
		$("#bserver_id").val(row.server_id);
		$("#big_id").val(row.ig_id);
		$("#bip").val(row.ip);
		$("#bremark").val(row.remark);
		if(row.isActive == 1){ 
			$("#bisActive1").attr("checked","checked");
			$("#bisActive2").attr("checked",false);
		}else{
			$("#bisActive1").attr("checked",false);
			$("#bisActive2").attr("checked","checked");
		}
		$('#serverli_updateparty').combobox('setValue', row.party_id);
		
		server_setIgdefine((row.idf_id).split(","));
		
		$('#server_update_dd').dialog('open').dialog('setTitle','服务器修改');
	}else{
		index_mess("请选择一栏信息", 4);
	}
}
/**
 * 在更改服务器时，添加未读取的组件
 * @param idf_ids 服务器已选择的组件
 */
function server_setIgdefine(idf_ids) {
	var idfNoRead = new Array();
	var idfSet = {};
	$.each($('#serverli_updateigdefine').combogrid('grid').datagrid('getRows'), function(k, v) {
		idfSet[v.id] = true;
	});
	$.each(idf_ids, function(k, v) {
		if(! idfSet[v]) {//下拉框中不存在的idf
			idfNoRead.push(v);
		}
	});
	if(idfNoRead.length != 0) {
		index_mess("读取中...", 0);
		$.ajax({
			url: 'ingredientdefine_listByIds_js.action?json=' + idfNoRead.join('-'),
			async: false,
			cache: false,
			dataType: 'json',
			success: function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					if(data.login == false) {
						index_login();
					}
				} else {
					index_mess("读取完成！", 2);
					$.each(data, function(k, v) {
						$('#serverli_updateigdefine').combogrid('grid').datagrid('appendRow', v);
					});
				}
			}
		});
	}
	$('#serverli_updateigdefine').combogrid('clear');
	$('#serverli_updateigdefine').combogrid('setValues', idf_ids);
	//$('#serverli_updateigdefine').combogrid('setText', row.idf);
}

//删除服务器信息，采用server_id,ig_id是无效的
function server_delete() {
	var checkedItems = $('#igli_table').datagrid('getChecked');
	var array_server_id = [];
	var array_party_id = [];
	$.each(checkedItems, function(index, row){
		array_server_id.push(row.server_id);
		array_party_id.push(row.party_id);
	});    
	if(checkedItems){
		$.messager.confirm('Confirm','你是否要删除该服务器?',function(r){  
		    if (r){  
		    	$.getJSON("server_delete.action?array_server_id="+array_server_id+"&array_party_id="+array_party_id+"",function(data){
		    	//});
		        //$.post("server_delete.action", {"array_server_id":array_server_id.toString()}, function(data) {
					if(data.status == 0) {
						index_mess(data.mess, 4);
						$('#igli_table').datagrid('reload');
					} else {
						index_mess(data.mess, 3);
					}
				//}, "json"); 
		    	});
		    }  
		});
	}else{
		$.messager.show({	// show error message
			title: 'Tip',
			msg: "请选中一行记录"
		});
	}
}

//打开软硬件信息窗口
function server_detail(rowId){
	var row = $('#igli_table').datagrid('getRows')[rowId];
	get_server_detail(row.server_id);
	$('#server_detail_dd').dialog('open').dialog('setTitle','软硬件信息-' + row.ip);
}

function get_server_detail(value){
	$("#dserver_id").val(value);
	$('#server_detail').datagrid({  
        url: "server_details.action?server_id="+value+"",  
        fitColumns:true,  
        singleSelect:true,  
        rownumbers:true,  
        height:'auto',  
        columns:[[  
             {title:'硬项目',field:'hw_p',width:11,align: 'center'},    
             {title:'内容',field:'hw_c',width:38,align: 'center'},
             {title:'软项目',field:'sw_p',width:13,align: 'center'},    
             {title:'内容',field:'sw_c',width:28,align: 'center'},
        ]],  
    }); 
}


//初始化 
var menu = $('#tr_server_export').menubutton({ menu: '#serverli_export_menu' });  
  
//menubutton 依赖于 menu、linkbutton 这两个插件，所以可以用如下代码实现：
$(menu.menubutton('options').menu).menu({ 
            onClick: function (item) { 
                if(item.text == "导出全部"){
                	var form = $("<form>");  		//jquery ajax不支持文件下载，所以使用下面的方法解决文件下载问题
                    form.attr('style','display:none');  
                    form.attr('target','');  
                    form.attr('method','post');  
                    form.attr('action','server_currentexport.action');  
                    var input_p = $('<input>');  
                    input_p.attr('type','hidden');  
                    input_p.attr('name','page.party_id');  
                    input_p.attr('value','0');
                    form.append(input_p);
                    $('body').append(form);  
                    form.submit();  
                    form.remove(); 
                }else if(item.text == "导出当前"){
                	var form = $("<form>");  
                    form.attr('style','display:none');  
                    form.attr('target','');  
                    form.attr('method','post');  
                    form.attr('action','server_currentexport.action');  
                    
                    var input1 = $('<input>');  
                    input1.attr('type','hidden');  
                    input1.attr('name','page.party_id');  
                    input1.attr('value',$('#igli_party').combobox('getValue'));
                    var input1_text = $('<input>');  
                    input1_text.attr('type','hidden');  
                    input1_text.attr('name','page.party');  
                    input1_text.attr('value',$('#igli_party').combobox('getText'));
                    
                    var input2 = $('<input>');  
                    input2.attr('type','hidden');  
                    input2.attr('name','page.idf_id');  
                    input2.attr('value',$('#igli_igf').combobox('getValue'));
                    var input2_text = $('<input>');  
                    input2_text.attr('type','hidden');  
                    input2_text.attr('name','page.idf');  
                    input2_text.attr('value',$('#igli_igf').combobox('getText'));
                    
                    var input3 = $('<input>');  
                    input3.attr('type','hidden');  
                    input3.attr('name','page.isActive');  
                    input3.attr('value',$('#igli_isActive').combobox('getValue'));
                    var input3_text = $('<input>');  
                    input3_text.attr('type','hidden');  
                    input3_text.attr('name','page.isActiceName');  
                    input3_text.attr('value',$('#igli_isActive').combobox('getText'));
                    
                    var input4 = $('<input>');  
                    input4.attr('type','hidden');  
                    input4.attr('name','page.host');  
                    input4.attr('value',$('#igli_host').combobox('getValue'));
                    var input4_text = $('<input>');  
                    input4_text.attr('type','hidden');  
                    input4_text.attr('name','page.hostname');  
                    input4_text.attr('value',$('#igli_host').combobox('getText'));
                    
                    var input5 = $('<input>');  
                    input5.attr('type','hidden');  
                    input5.attr('name','page.ip');  
                    input5.attr('value',$('#igli_ip').combobox('getText'));
                    
                    form.append(input1);
                    form.append(input2);
                    form.append(input3);
                    form.append(input4);
                    form.append(input5);
                    
                    form.append(input1_text);
                    form.append(input2_text);
                    form.append(input3_text);
                    form.append(input4_text);
                    $('body').append(form);  
                    form.submit();  
                    form.remove();
                }
            } 
});

/**
 * 清除搜索数据
 */
function igli_clearData() {
	$('#igli_party').combobox('setValue', '');
	$('#igli_igf').combogrid('clear');
	$('#igli_isActive').combobox('setValue', '');
	$('#igli_host').combobox('setValue', '');
	$('#igli_ip').combobox('clear');
}
//初始化nagios监控设置窗口
$('#igdli_nagios').dialog({
	width: 360,
	height: 220,
	closed: true,
	resizable:true,
	//maximizable:true,
	buttons:[{
		text:'更新',
		iconCls:'icon-edit',
		handler:function(){
			var nodes = $('#igdli_nagios_ul').tree('getRoots');
			var ids = new Array();
			var actives = new Array();
			$.each(nodes, function(k, v) {
				ids.push(v.id);
				actives.push(v.checked? 1: 0);
			});
			index_mess('更新中...', 0);
			$.getJSON('ingredient_changeStatus_js.action?json=' + igdli_data.serverId + 'A' +
					ids.join('_') + 'A' + actives.join('_'), function(data) {
				if(data.status == 0) {
					$('#igdli_nagios').dialog('close');
					$('#igli_table').datagrid('reload');
					index_mess(data.mess, 2);
				} else {
					index_mess(data.mess, 1);
				}
			});
		}
	},{
		text:'&nbsp;关闭&nbsp;',
		handler:function(){
			$('#igdli_nagios').dialog('close');
		}
	}]
});
/**
 * 组件监控设置窗口
 */
function igdli_nagios(rowId) {
	var row = $('#igli_table').datagrid('getRows')[rowId];
	igdli_data.serverId = row.server_id;
	$('#igdli_nagios').dialog('open').dialog('setTitle','nagios监控设置-' + row.ip);;
	var treeData = new Array();
	var names = row.idf.split(',');
	var ids = row.idf_id.split(',');
	var actives = row.ifd_active.split(',');
	$.each(ids, function(k, v) {
		treeData.push({
			id: v,
			text: names[k],
			checked: (actives[k]==0?false:true)
		});
	});
	$('#igdli_nagios_ul').tree({
		checkbox: true,
		data: treeData
	});
}
