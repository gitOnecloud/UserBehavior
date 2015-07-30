var url;
$('#log_table').datagrid({
	queryParams: {
		"page.sdate": {"id":"log_tb_sdate","type":"datebox"},
		"page.edate": {"id":"log_tb_edate","type":"datebox"},
		"page.Ip": {"id":"log_tb_ip","type":"text"},
		"page.type":{"id":"log_tb_type","type":"combobox"}
	}, paramsName: {
    	page: "page.page",
    	num: "page.num"
    },onDblClickRow: function(index, data) {
    	show_logDetail(null);
    }
});

/**
 * 读取日志类型
 */
$.getJSON('dropDownList_getComboboxData.action?type=log_type', function(data) {
	var searchData = [{'value': '', 'text': '全部'}];
	$.each(data, function(k, v) {
		searchData.push(v);
	});
	$('#log_tb_type').combobox('loadData', searchData);
});

$('#log_searchbox').searchbox({
	width:150,
    prompt:'内容关键字搜索',
    searcher:function(keyword){
    	//置空查询条件
    	if(keyword == ""){
    		show_msg("Tip","请输入关键字");
    	}else{  //关键字不为空
    		$('#log_table').datagrid('options').url = 'log_listTable.action';
    		var queryParams = $('#log_table').datagrid('options').queryParams;
    		queryParams.keyword = keyword;
    		$('#log_table').datagrid('options').queryParams = queryParams;
    		
    		
    		/***** 关键字高亮  *****/
    		var fields = $('#log_table').datagrid('getColumnFields',false);
    		$('#log_table').datagrid('getColumnOption',fields[2]).formatter = 
    			 					function(value, rowData, rowIndex){
    									value = value.replace(keyword,"<font color=red>"+keyword+"</font>");
    									return value;
    								};
    		$('#log_table').datagrid('load');
    	}
    }
});

function log_new(){
	$('#log_dlg').dialog('open').dialog('setTitle','日志查询');
	$('#log_fm').form('clear');
	$('#log_type').combobox('reload','dropDownList_getComboboxData.action?type=log_type');
	url = "log_addLog.action?_=" + Math.random();
}

function log_edit(rowIndex){
	if(rowIndex != null){
		$('#log_table').datagrid('unselectAll');
		$('#log_table').datagrid('selectRow', rowIndex);
	}
	var rows = $('#log_table').datagrid('getSelections');
	if(rows.length == 1){
		$('#log_dlg').dialog('open');
		$('#log_dlg_title').html('编辑日志');
		$('#log_type').combobox('reload','dropDownList_getComboboxData.action?type=log_type');
		$('#log_fm').form('load',rows[0]);
		url = 'log_updateLog.action?id=' + rows[0].id;
	}else if(rows.length == 0){
		show_msg("Tip","请选中一条日志");
	}else{
		show_msg("Tip","只能选中一条日志");
	}
}

function log_remove(rowIndex){
	if(rowIndex != null){
		$('#log_table').datagrid('unselectAll');
		$('#log_table').datagrid('selectRow', rowIndex);
	}
	var rows = $('#log_table').datagrid('getSelections');
	var ids;
	for(var i=0; i<rows.length; i++){
		if(i == 0){
			ids = 'idList=' + rows[i].id;
		}else{
			ids += '&idList=' + rows[i].id;
		}
	}
	if(rows){
		$.messager.confirm('Confirm','确定删除选中的日志?',function(r){
			if (r){
				$.post('log_delLog.action',ids,function(result){
					if (result.status == 0){
						$('#log_table').datagrid('reload');	// reload data
						show_msg("Success",result.mess);
					} else {
						show_msg("Error",result.mess);
						$('#log_table').datagrid('reload');
					}
				},'json');
			}
		});
	}else{
		show_msg("Tip","请选中一行记录");
	}
}

function log_save(){
	$('#log_fm').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			$('#log_dlg').dialog('close');
			var result = eval('('+result+')');	
			if (result.status == "0"){
				$('#log_table').datagrid('reload');
				show_msg('Success',result.mess);
			}else{
				show_msg('Error',result.mess);
			}
		}
	});
}

function log_find(){
	var queryParams = $('#log_table').datagrid('options').queryParams;
	queryParams.keyword = '';
	
	/**** 清除搜索框做过的设置 ****/
	$('#log_searchbox').searchbox('setValue','');
	var fields = $('#log_table').datagrid('getColumnFields',false);
	$('#log_table').datagrid('getColumnOption',fields[2]).formatter = '';
	
	$('#log_table').datagrid("load");
}

var log_memu = $('#log_export').menubutton({ menu: '#log_export_menu' });
$(log_memu.menubutton('options').menu).menu({ 
    onClick: function (item) { 
        if(item.text == "导出全部"){
        	var form = $("<form>");  		//jquery ajax不支持文件下载，所以使用下面的方法解决文件下载问题
            form.attr('style','display:none');  
            form.attr('target','');  
            form.attr('method','post');  
            form.attr('action','log_exportAll2Excel.action');  
            $('body').append(form);  
            form.submit();  
            form.remove(); 
        }else if(item.text == "导出当前"){
        	var form = $("<form>");  
            form.attr('style','display:none');  
            form.attr('target','');  
            form.attr('method','post');  
            form.attr('action','log_exportCurrent2Excel.action');  
            
            var input1 = $('<input>');  
            input1.attr('type','hidden');  
            input1.attr('name','sdate');  
            input1.attr('value',$('#log_tb_sdate').datebox('getValue'));
            var input2 = $('<input>');  
            input2.attr('type','hidden');  
            input2.attr('name','edate');  
            input2.attr('value',$('#log_tb_edate').datebox('getValue'));
            var input3 = $('<input>');  
            input3.attr('type','hidden');  
            input3.attr('name','IP');  
            input3.attr('value',$('#log_tb_ip').val());
            form.append(input1);
            form.append(input2);
            form.append(input3);
            $('body').append(form);  
            form.submit();  
            form.remove();
        }
    } 
});

function log_operation(value, rowData, rowIndex){
	var log_e = "<a href='javascript:;' onclick='log_edit("+rowIndex+")'>编辑</a> ";
	var log_d = "<a href='javascript:;' onclick='log_remove("+rowIndex+")'>删除</a> ";
	var log_c = "<a href='javascript:;' onclick='show_logDetail("+rowIndex+")'>详情</a>";
	return log_e + log_d + log_c;
}

function show_logDetail(rowIndex){
	if(rowIndex != null){
		$('#log_table').datagrid('unselectAll');
		$('#log_table').datagrid('selectRow', rowIndex);
	}
	var row = $('#log_table').datagrid('getSelected');
	$('#log_dlg_detail').html("<strong>IP：</strong>" + row.IP +
			"<br><br><strong>内容：</strong><br>" + row.content.replace(/\r\n+/g,"<br>") +
			"<br><br><strong>创建时间：</strong><br>" + row.createTime +
			"<br><br><strong>更新时间：</strong><br>" + row.updateTime);
	$('#log_detail_dlg').dialog('open').dialog('setTitle','日志详情');
}

function show_msg(title,msg){
	$.messager.show({
		title: title,
		msg: msg
	});
}