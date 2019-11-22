<?php
global $_GPC, $_W;
$GLOBALS['frames'] = $this->getMainMenu();
$type = isset($_GPC['type'])?$_GPC['type']:'wait';
$state = $_GPC['state'];
if($type=='wait'){
  $state = 1;
}
$where = "";
//搜索
if(isset($_GPC['keywords'])){
  $where.=" and (a.store_name LIKE  concat('%', :name,'%') || a.tel LIKE  concat('%', :name,'%')) ";
  $data[':name']=$_GPC['keywords'];
  $type = 'all'; 
}else{
  if($state){
      $where.=" where a.state = :state ";
      $data[':state'] = $state;
  }
}

$pageindex = max(1, intval($_GPC['page']));
$pagesize=10;


$sql="SELECT a.* FROM " . tablename('store') . " a" . $where . " ORDER BY a.id DESC";
$total=pdo_fetchcolumn("select count(*) from " .tablename('store'). " a" . $where , $data );
//echo $sql;die;
$select_sql = $sql . " LIMIT " .($pageindex - 1) * $pagesize . "," . $pagesize;

$list = pdo_fetchall($select_sql, $data);

$pager = pagination($total, $pageindex, $pagesize);
if($_GPC['op']=='delete'){
    $res=pdo_delete('store',array('id'=>$_GPC['id']));
    if($res){
         message('删除成功！', $this->createWebUrl('store'), 'success');
    }else{
         message('删除失败！','','error');
    }
}
if($_GPC['op']=='tg'){
    $rst=pdo_get('store',array('id'=>$_GPC['id']));
    if(!$rst['sh_time']){//增加
        $time=9999999999;
        $time2=time();
        $res = pdo_update('store', array('state'=>2, 'sh_time'=>$time2, 'dq_time'=>$time), array('id'=>$_GPC['id']));
    }else{//修改
        $res = pdo_update('store', array('state'=>2), array('id'=>$_GPC['id']));
    }
    if($res){
        message('通过成功！', $this->createWebUrl('store'), 'success');
    }else{
        message('通过失败！','','error');
    }
}
if($_GPC['op'] == 'jj'){
    $res = pdo_update('store', array('state' => 3, 'sh_time' => time()),array('id' => $_GPC['id']));
    if($res){
        message('拒绝成功！', $this->createWebUrl('store'), 'success');
    }else{
        message('拒绝失败！','','error');
    }
}

include $this->template('web/store');