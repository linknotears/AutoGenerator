<?php
global $_GPC, $_W;
$GLOBALS['frames'] = $this->getMainMenu();
$info = pdo_get('store',array('id'=>$_GPC['id']));
//二维数组转一维

if($info['ad']){
    if(strpos($info['ad'], ',')){
        $ad= explode(',', $info['ad']);
    }else{
        $ad=array(0 => $info['ad']);
    }
}
/*	 $coordinates=explode(',',$info['coordinates']);
 $list['coordinates']=array(
 'lat'=>$coordinates['0'],
 'lng'=>$coordinates['1'],
 ); */
if(checksubmit('submit')){
    if($_GPC['ad']) {
        $data['ad'] = implode(",",$_GPC['ad']);
    } else {
        $data['ad'] = '';
    }
    if(empty($_GPC['user_id'])){
        message('没有绑定管理员,请绑定后提交','','error');
    }
    //查询钱
    $data['store_name'] = $_GPC['store_name'];
    $data['tel'] = $_GPC['tel'];
    $data['address'] = $_GPC['address'];
    $data['logo'] = $_GPC['logo'];
    $data['details'] = $_GPC['details'];
    $data['coordinates'] = $_GPC['coordinates'];
    $data['user_name'] = $_GPC['user_name'];
    $pwd = $_GPC['pwd'];
    if($pwd!='' && $pwd!=null){        
        $data['pwd'] = md5($pwd);
    }
    $data['user_id'] = $_GPC['user_id'];
    $data['wxuser_name'] = $_GPC['wxuser_name'];
    $data['yyzz_img'] = $_GPC['yyzz_img'];
    $data['start_time'] = $_GPC['start_time'];
    $data['end_time'] = $_GPC['end_time'];
    $data['state'] = 2;
    $data['views'] = $_GPC['views'];
    
    //额外的
    $data['least_price'] = $_GPC['least_price'];//起送价
    $data['delivery_time'] = $_GPC['delivery_time'];//配送时间/分钟
    $data['join_switch'] = $_GPC['join_switch'];//加盟开关
    $data['partner_switch'] = $_GPC['partner_switch'];//入股开关
    $data['is_recommend'] = $_GPC['is_recommend'];//品牌推荐开关
    $data['set_yptg'] = $_GPC['set_yptg'];//优品推广开关
    $data['set_fxhd'] = $_GPC['set_fxhd'];//发现好店开关
    $data['fare'] = $_GPC['fare'];//运费
    //$data['apply_time'] = time();//入驻时间
    //$data['sh_time'] = time();//审核时间
    
    if($_GPC['id'] == ''){
        $res = pdo_insert('store', $data);
        //pdo_debug();exit;
        if($res) {
            message('新增成功',$this->createWebUrl('store',array('type'=>'all')),'success');
        } else {
            message('新增失败','','error');
        }
    }else {
        $res = pdo_update('store', $data,array('id'=>$_GPC['id']));
        if($res) {
            message('编辑成功', $this->createWebUrl('store',array('type'=>'all')),'success');
        } else {
            message('编辑失败', '', 'error');
        }
    }
}
include $this->template('web/storeinfo2');