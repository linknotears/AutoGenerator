<?php
#macro(d)$#end
namespace app\api\model;

use think\Model;
//use think\Db;

class ${entity} extends Model {
    protected #d()table = "${table.name}";
	
}