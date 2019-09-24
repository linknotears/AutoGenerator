<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
	<!-- 开启二级缓存 -->
	<cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

</#if>
<#if baseResultMap>
	<!-- 通用查询映射结果 -->
	<resultMap id="BaseResultMap" type="${package.Entity}.${entity}">

<#list table.fields as field>
	<#if field.keyFlag>
	<id column="${table.name}_${field.name}" property="${field.propertyName}" />
	</#if>
</#list>
<#list table.commonFields as field>
	<result column="${field.propertyName}" property="${field.name}" />
</#list>
<#list table.fields as field>
	<#if !field.keyFlag><#-- 生成普通字段  -->
	<result column="${table.name}_${field.name}" property="${field.propertyName}" />
	</#if>
</#list>
	</resultMap>
</#if>

<#assign keys=cfg.absoluteNameStrMap?keys/>
<#list keys as key>
<#if key == table.name>
	<!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        ${cfg.absoluteNameStrMap["${key}"]}
    </sql>
</#if>
</#list>

<#if cfg.conjInfoMap[table.name]["resultStr"]??>
	<!-- 联合查询结果集 -->
${cfg.conjInfoMap[table.name]["resultStr"]}
	<!-- 联合查询 -->
${cfg.conjInfoMap["${table.name}"]["conjQueryStr"]}
</#if>



  <select id="selectList" resultMap="BaseResultMap" parameterType="${package.Entity}.${entity}">
  	select 
   	<include refid="Base_Column_List" />
   	from ${table.name}
   	<where>
   		<if test="condition!=null">
			<#list table.fields as field>
    		<if test="condition.${field.propertyName}!=null">
    			and ${field.name} = <#noparse>#</#noparse>{condition.${field.propertyName},javaType=${field.columnType}}
    		</if>
			</#list>
    	</if>
    	<#noparse>${condition.customCondition}</#noparse>
   	</where>
   	<#noparse>${condition.orderby}</#noparse>
  </select>
  
  <select id="selectCount" parameterType="${package.Entity}.${entity}" resultType="java.lang.Integer">
  	select 
   	count(1)
   	from ${table.name}
   	<where>
   		<if test="condition!=null">
			<#list table.fields as field>
    		<if test="condition.${field.propertyName}!=null">
    			and ${field.name} = <#noparse>#</#noparse>{condition.${field.propertyName},javaType=${field.columnType}}
    		</if>
			</#list>
    	</if>
    	<#noparse>${condition.customCondition}</#noparse>
   	</where>
  </select>
  
  <select id="selectPage" resultMap="BaseResultMap" parameterType="${package.Entity}.vo.PageData">
  	select 
   	<include refid="Base_Column_List" />
   	from ${table.name}
   	<where>
   		<if test="condition!=null">
			<#list table.fields as field>
    		<if test="condition.${field.propertyName}!=null">
    			and ${field.name} = <#noparse>#</#noparse>{condition.${field.propertyName},javaType=${field.columnType}}
    		</if>
			</#list>
    	</if>
    	<#noparse>${condition.customCondition}</#noparse>
   	</where>
    <#noparse>${orderby}</#noparse>
    limit <#noparse>#</#noparse>{start },<#noparse>#</#noparse>{limit }
  </select>
  
  <insert id="insertByList" parameterType="${package.Entity}.${entity}" useGeneratedKeys="true" keyProperty="${table.fields[0].propertyName}" keyColumn="${table.fields[0].name}">
  	insert into ${table.name} 
    values 
	<foreach collection="list" item="item" separator=",">
		(<#list table.fields as field><#noparse>#</#noparse>{item.${field.propertyName},javaType=${field.columnType}}<#if field_has_next> , </#if> </#list>)
    </foreach>
  </insert>
  
  <select id="selectById" resultMap="BaseResultMap" parameterType="${package.Entity}.${entity}" >
    select 
    <include refid="Base_Column_List" />
    from ${table.name}
    where ${table.fields[0].name} = <#noparse>#</#noparse>{${table.fields[0].propertyName},javaType=${table.fields[0].columnType}}
  </select>
  
  <delete id="deleteById" parameterType="${package.Entity}.${entity}" >
    delete from ${table.name}
    where ${table.fields[0].name} = <#noparse>#</#noparse>{${table.fields[0].propertyName},javaType=${table.fields[0].columnType}}
  </delete>
  
  <delete id="deleteBatchIds" parameterType="${package.Entity}.${entity}" >
    delete from ${table.name}
    where ${table.fields[0].name} in 
	<foreach collection="array" item="item" open="(" separator="," close=")">
		<#noparse>#</#noparse>{item,javaType=${table.fields[0].columnType}}
	</foreach>
  </delete>
  
  <delete id="deleteByCondition" parameterType="${package.Entity}.${entity}" >
    delete from ${table.name}
	<where>
		<#list table.fields as field>
		<if test="${field.propertyName}!=null">
			and ${field.name} = <#noparse>#</#noparse>{${field.propertyName},javaType=${field.columnType}}
		</if>
		</#list>
   	</where>
  </delete>
  
  <insert id="insert" parameterType="${package.Entity}.${entity}" useGeneratedKeys="true" keyProperty="${table.fields[0].propertyName}" keyColumn="${table.fields[0].name}">
    insert into ${table.name}
    <trim prefix="(" suffix=")" suffixOverrides="," >
	<#list table.fields as field>
	  <if test="${field.propertyName} != null" >
        ${field.name},
	  </if>
    </#list>
    </trim>
    <trim prefix="values (" suffix=")" suffixOverrides="," >
    <#list table.fields as field>
      <if test="${field.propertyName} != null" >
        <#noparse>#</#noparse>{${field.propertyName},javaType=${field.columnType}},
      </if>
    </#list>
    </trim>
  </insert>
  
  <update id="updateByIdIgnoreNull" parameterType="${package.Entity}.${entity}" >
    update ${table.name}
    <set>
	<#list table.fields as field>
	<#if !field.keyFlag>
      <if test="${field.propertyName} != null" >
        ${field.name} = <#noparse>#</#noparse>{${field.propertyName},javaType=${field.columnType}},
      </if>
	</#if>
	</#list>
    </set>
    where ${table.fields[0].name} = <#noparse>#</#noparse>{${table.fields[0].propertyName},javaType=${table.fields[0].columnType}}
  </update>
  
  <update id="updateById" parameterType="${package.Entity}.${entity}" >
    update ${table.name}
    set 
	#foreach($field in ${table.fields})#if(${field.keyFlag})#continue#end${field.name} = <#noparse>#</#noparse>{${field.propertyName},javaType=${field.columnType}}#if(field_has_next),#end#end  
    where ${table.fields[0].name} = <#noparse>#</#noparse>{${table.fields[0].propertyName},javaType=${table.fields[0].columnType}}
  </update>
  
  <update id="updateByPartIdIgnoreNull" parameterType="${package.Entity}.${entity}" >
    update ${table.name}
    <set >
	<#list table.fields as field>
	<#if !field.keyFlag>
	  <if test="entity.${field.propertyName} != null" >
        ${field.name} = <#noparse>#</#noparse>{entity.${field.propertyName},javaType=${field.columnType}},
      </if>
	</#if>
    </#list>
    </set>
    where ${table.fields[0].name} = <#noparse>#</#noparse>{id,javaType=${table.fields[0].columnType}}
  </update>
  
  <update id="updateByPartId" parameterType="${package.Entity}.${entity}" >
    update ${table.name}
    set 
	<#list table.fields as field>
	<#if !field.keyFlag>
		${field.name} = <#noparse>#</#noparse>{entity.${field.propertyName},javaType=${field.columnType}}<#if field_has_next>,</#if>
	</#if>
	</#list>
    where ${table.fields[0].name} = <#noparse>#</#noparse>{id,javaType=${table.fields[0].columnType}}
  </update>
  
  <update id="updateByCondition" parameterType="${package.Entity}.${entity}" >
    update ${table.name}
    <set >
	<#list table.fields as field>
	<#if !field.keyFlag>
	  <if test="entity.${field.propertyName} != null" >
        ${field.name} = <#noparse>#</#noparse>{entity.${field.propertyName},javaType=${field.columnType}},
      </if>
	</#if>
    </#list> 
    </set>
    <where>
    <#list table.fields as field>
      <if test="condition.${field.propertyName} != null" >
        and ${field.name} = <#noparse>#</#noparse>{condition.${field.propertyName},javaType=${field.columnType}}
      </if>
	</#list>
    </where>
  </update>
</mapper>
