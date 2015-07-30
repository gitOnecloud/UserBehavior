var url;
var fieldIndex;
$('#dropDownList_table').datagrid({
	queryParams:{
		"page.field_name": {"id":"dropDownList_tb_name","type":"text"}
	},
	columns:[[
	    {field:'field_name',title:'名称',width:80,align:'center'},
	    {field:'field_comment',title:'备注',width:80,align:'center'},
	    {field:'operation',title:'操作',width:80,align:'center',
	    	formatter:function(value,row,index){
            	var a = '<a href="javascript:;" onclick="addValue('+index+')">添加</a> ';
                var e = '<a href="javascript:;" onclick="editField('+index+')">编辑</a> ';
                var d = '<a href="javascript:;" onclick="deleteField('+index+')">删除</a>';
                return a+e+d;
            }
	    }
	]],
	paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    view: detailview,
    detailFormatter:function(index,row){
    	return '<div style="padding:2px"><table class="subgrid" id="subgrid_' + index +'"></table></div>';
    }, 
    onExpandRow: function(index,row){
    	var subgrid = $(this).datagrid('getRowDetail',index).find('table.subgrid');
    	subgrid.datagrid({
	    	url:'dropDownList_valuesList.action?field_id='+row.field_id,
	    	fitColumns:true,
	    	singleSelect:true,
	    	loadMsg:'',
	    	height:'auto',
	    	columns:[[
	    	{field:'value_name',title:'值',width:200,align:'center'},
	    	{field:'value_valid',title:'是否有效',width:100,align:'center'},
	    	{field:'seq',title:'顺序号',width:100,align:'center'},
	    	{field:'operation',title:'操作',width:80,align:'center',
		    	formatter:function(value,row,index2){
	                var e = '<a href="javascript:;" onclick="editValue('+index+','+index2+')">编辑</a> ';
	                var d = '<a href="javascript:;" onclick="deleteValue('+index+','+index2+')">删除</a>';
	                return e+d;
	            }
		    }
	    	]],
	    	onResize:function(){
	    		$('#dropDownList_table').datagrid('fixDetailRowHeight',index);
	    		$('#dropDownList_table').datagrid('resize');  
	    	},
	    	onLoadSuccess:function(){
		    	setTimeout(function(){
		    		$('#dropDownList_table').datagrid('fixDetailRowHeight',index);
		    		$('#dropDownList_table').datagrid('resize');  
		    	},1000);
	    	}
    	});
    	//$('#dropDownList_table').datagrid('resize');  
    }
});

//输入验证
$.extend($.fn.validatebox.defaults.rules, {   
    isletterOrNum: {   
        validator: function(value, param){ 
            return /^[A-Za-z0-9\_]+$/.test(value); 
        },   
        message: '只能输入字母、数字或下划线'  
    }   
});

function ddl_find(){
	$('#dropDownList_table').datagrid('load');
}

function addField(){		//添加下拉列表类型
	$('#ddlField_dlg').dialog('open').dialog('setTitle','数据字典');
	$('#ddlField_fm').form('clear');
	url = "dropDownList_addField.action?_" + Math.random();
}

function editField(index){
	if(index != null){
		$('#dropDownList_table').datagrid('unselectAll');
		$('#dropDownList_table').datagrid('selectRow', index);
	}
	var row = $('#dropDownList_table').datagrid('getSelected');
	if(row){
		$('#ddlField_dlg').dialog('open').dialog('setTitle','数据字典');
		$('#ddlField_dlg_title').html('编辑数据字典');
		$('#ddlField_fm').form('load',row);
		url = 'dropDownList_updateField.action?field_id=' + row.field_id;
	}else{
		show_msg("Tip","请选中一个数据字典");
	}
}

function deleteField(index){
//	$('#dropDownList_table').datagrid('unselectAll');
	$('#dropDownList_table').datagrid('selectRow',index);
	var row = $('#dropDownList_table').datagrid('getSelected');
    $.messager.confirm('Confirm','数据字典【'+row.field_comment+'】下所有值都将删除，确定删除?',function(r){
        if (r){

			$.post('dropDownList_deleteField.action',{field_id:row.field_id},function(result){
				if(result.status == 0){
					$('#dropDownList_table').datagrid('reload');	// reload data
					show_msg("Success",result.mess);
				} else {
					show_msg("Error",result.mess);
					$('#dropDownList_table').datagrid('reload');
				}
			},'json');
        }
    });
}

function saveField(index){
	$('#ddlField_fm').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			$('#ddlField_dlg').dialog('close');
			var result = eval('('+result+')');	
			if (result.status == "0"){
				$('#dropDownList_table').datagrid('reload');
				show_msg('Success',result.mess);
			}else{
				show_msg('Error',result.mess);
			}
		}
	});
}

function addValue(index){
	if(index != null){
		fieldIndex = index;
		$('#dropDownList_table').datagrid('unselectAll');
		$('#dropDownList_table').datagrid('selectRow', index);
	}
	var row = $('#dropDownList_table').datagrid('getSelected');
	if(row){
		$('#ddlValue_dlg').dialog('open').dialog('setTitle','数据字典');
		$('#ddlValue_dlg_title').html("【" + row.field_comment + "】添加选项");
		$('#ddlValue_fm').form('clear');
		url = "dropDownList_addValue.action?field_id="+ row.field_id +"&_=" + Math.random();
	}
}

function saveValue(){
	$('#ddlValue_fm').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			$('#ddlValue_dlg').dialog('close');
			var result = eval('('+result+')');	
			if (result.status == "0"){
				$('#dropDownList_table').datagrid('getRowDetail',fieldIndex).find('table.subgrid').datagrid('reload');
				show_msg('Success',result.mess);
			}else{
				show_msg('Error',result.mess);
			}
		}
	});
}

function editValue(f_index,v_index){
	fieldIndex = f_index;
	var subgrid = $('#dropDownList_table').datagrid('getRowDetail',f_index).find('table.subgrid');
	subgrid.datagrid('unselectAll');
	subgrid.datagrid('selectRow', v_index);
	var row = subgrid.datagrid('getSelected');
	if(row){
		$('#ddlValue_dlg').dialog('open');
		$('#ddlValue_dlg_title').html('编辑数据字典');
		$('#ddlValue_fm').form('load',row);
		url = 'dropDownList_updateValue.action?value_id=' + row.value_id;
	}else{
		show_msg("Tip","请选中一个数据字典");
	}
}

function deleteValue(f_index,v_index){
	$.messager.confirm('Confirm','删除该选项?',function(r){
        if (r){
        	var subgrid = $('#dropDownList_table').datagrid('getRowDetail',f_index).find('table.subgrid');
        	subgrid.datagrid('unselectAll');
        	subgrid.datagrid('selectRow', v_index);
        	var row = subgrid.datagrid('getSelected');
			$.post('dropDownList_deleteValue.action',{value_id:row.value_id},function(result){
				if(result.status == 0){
					subgrid.datagrid('reload');	// reload data
					show_msg("Success",result.mess);
				} else {
					show_msg("Error",result.mess);
					$('#dropDownList_table').datagrid('reload');
				}
			},'json');
        }
    });
}

function show_msg(title,msg){
	$.messager.show({
		title: title,
		msg: msg
	});
}