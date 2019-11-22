<?php
## 首字母小写
#macro(d)$#end
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
$GLOBALS['frames'] = $this->getMainMenu();
$where = "";
//搜索
/*if(isset($_GPC['keywords'])){
  $where .= " and (a.store_name LIKE  concat('%', :name,'%') || a.tel LIKE  concat('%', :name,'%')) ";
  $data[':name']=$_GPC['keywords'];
  $type = 'all'; 
}*/

$pageindex = max(1, intval($_GPC['page']));
$pagesize=10;

$sql = "SELECT a.* FROM " . tablename('$shortName') . " a" . $where . " ORDER BY a.id DESC";
$total = pdo_fetchcolumn("select count(1) from " . tablename('$shortName') . " a" . $where , $data );
$select_sql = $sql . " LIMIT " .($pageindex - 1) * $pagesize . "," . $pagesize;

$list = pdo_fetchall($select_sql, $data);

$pager = pagination($total, $pageindex, $pagesize);
if($_GPC['op']=='delete'){
    $res=pdo_delete('$shortName',array('id'=>$_GPC['id']));
    if($res){
         message('删除成功！', $this->createWebUrl('#tolowercase($entity)'), 'success');
    }else{
         message('删除失败！','','error');
    }
}
include $this->template('web/#tolowercase($entity)');