<?php
#macro(d)$#end
## 生成指定数目的制表符
#macro(tab $countArr)#foreach($i in $countArr)	#end#end
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
namespace app\api\controller;

use app\api\model\\${entity} as ${entity}Model;
//use think\Db;

class ${entity} extends Controller
{
    public function findList(#foreach($field in ${table.fields})#if(!$cfg.colExclude.get($table.name).get($field.name))#d()${field.propertyName} = null#if(${foreach.hasNext}), #end#end#end) {
        #tab([1..2])#d()filter = [];
#foreach($field in ${table.fields})
##判断排除字段
#if(!$cfg.colExclude.get($table.name).get($field.name))
		if(#d()${field.propertyName} != null){
			#tab([1..3])#d()filter['${field.name}'] = #d()${field.propertyName};
		}
#end
#end
        #tab([1..2])#d()model = new ${entity}Model();
        #tab([1..2])#d()res = collection( 
			#tab([1..3])#d()model
			->where(#d()filter)
            ->select()
		)->toArray();
        //$res["sql"] = $model->getLastSql();
        return #d()res;
    }
	
	public function remove($id) {
		#tab([1..2])#d()model = new ${entity}Model();
		#tab([1..2])#d()res = #d()model->where('${colId}',#d()id)->delete();
		return ["count" => #d()res];
	}
	
	public function saveOrUpdate($id = null,#foreach($field in ${table.fields})#if(!$cfg.colExclude.get($table.name).get($field.name))#d()${field.propertyName} = null#if(${foreach.hasNext}), #end#end#end) {
		#tab([1..2])#d()model = new ${entity}Model();
		#tab([1..2])#d()entity = [];
#foreach($field in ${table.fields})
##判断排除字段
#if(!$cfg.colExclude.get($table.name).get($field.name))
#if(!${field.keyFlag})
		if(#d()${field.propertyName} != null){
			#tab([1..3])#d()entity['${field.name}'] = #d()${field.propertyName};
		}
#end
#end
#end
		$res = null;
		if($id != null){
			#tab([1..3])#d()res = #d()model->where('id='.id)->update(#d()entity);
		}else{
			//#tab([1..3])#d()entity['id'] = md5(uniqid());
			#tab([1..3])#d()res = #d()model->data(#d()entity)->add();
		}
		return ["count" => #d()res];
	}
	
	public function save(#foreach($field in ${table.fields})#if(!$cfg.colExclude.get($table.name).get($field.name))#d()${field.propertyName} = null#if(${foreach.hasNext}), #end#end#end) {
		#tab([1..2])#d()model = new ${entity}Model();
		#tab([1..2])#d()entity = [];
#foreach($field in ${table.fields})
##判断排除字段
#if(!$cfg.colExclude.get($table.name).get($field.name))
		if(#d()${field.propertyName} != null){
			#tab([1..3])#d()entity['${field.name}'] = #d()${field.propertyName};
		}
#end
#end
		#tab([1..2])#d()res = #d()model->data(#d()entity)->add();
		return ["count" => #d()res];
	}
	
	public function findById($id = null){
		#tab([1..2])#d()model = new ${entity}Model();
		#tab([1..2])#d()res = #d()model->where('${colId}',#d()id)->find();
		return $res;
	}
	
	public function findPage(#d()offset = 0,#d()limit = 10,#foreach($field in ${table.fields})#if(!$cfg.colExclude.get($table.name).get($field.name))#d()${field.propertyName} = null#if(${foreach.hasNext}), #end#end#end){
	    #tab([1..2])#d()filter = [];
#foreach($field in ${table.fields})
##判断排除字段
#if(!$cfg.colExclude.get($table.name).get($field.name))
		if(#d()${field.propertyName} != null){
			#tab([1..3])#d()filter['${field.name}'] = #d()${field.propertyName};
		}
#end
#end
        #tab([1..2])#d()model = new ${entity}Model();
        #tab([1..2])#d()res = collection( 
			#tab([1..3])#d()model
	    #d()res = #d()model->where(#d()filter)->limit(#d()offset,#d()limit)->select();
	    #d()total = #d()model->field("count(1) total")->where(#d()filter)->select();//distinct ${colId}
	    return ["rows" => #d()res,"total" => #d()total["total"]];
	}
}