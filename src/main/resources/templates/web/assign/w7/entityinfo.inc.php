<?php
## 首字母小写
## 生成指定数目的制表符
#macro(tab $countArr)#foreach($i in $countArr)	#end#end
#macro(d)$#end
#macro(tolowercaseall $str)$str.toLowerCase()#end
#macro(tolowercase $str)$str.substring(0,1).toLowerCase()$str.substring(1)#end
##找出第一个id
#foreach($field in ${table.fields})
#if(${field.keyFlag})
#set( $propId = $field.propertyName )
#set( $colId = $field.name )
#break
#end
#end
#if(!$colId)
#set( $propId = $table.fields[0].propertyName )
#set( $colId = $table.fields[0].name )
#end
#set($shortName = ${table.name.replace($cfg.tablePrefix,'')})
global $_GPC, $_W;
$GLOBALS['frames'] = #d()this->getMainMenu();
$info = pdo_get("$shortName",array('$colId'=>$_GPC['id']));
//二维数组转一维

if(checksubmit('submit')){
#foreach($field in ${table.fields})
#if(!${field.keyFlag})
    if(!empty(#d()_GPC['${field.name}'])){
		#tab([1..2])#d()data['${field.name}'] = #d()_GPC['${field.name}'];
    }
#end
#end
    if($_GPC['id'] == ''){
        $res = pdo_insert('$shortName', $data);
        //pdo_debug();exit;
        if($res) {
            message('新增成功',$this->createWebUrl('#tolowercaseall($entity)',array('type'=>'all')),'success');
        } else {
            message('新增失败','','error');
        }
    }else {
        $res = pdo_update('$shortName', $data, array('$colId'=>$_GPC['id']));
        if($res) {
            message('编辑成功', $this->createWebUrl('#tolowercaseall($entity)',array('type'=>'all')),'success');
        } else {
            message('编辑失败', '', 'error');
        }
    }
}
include $this->template('web/#tolowercaseall($entity)info');