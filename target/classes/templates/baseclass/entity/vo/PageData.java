package ${package.Entity}.vo;

import java.util.List;


public class PageData<T> {
	private int page = 1;//当前页
	private int totalCount;//总记录数
	private int totalPage;//总页数
	private int start = 0;//页开始
	private int limit = 10;//每页最大记录数
	private List<T> list;
	private T condition;
	private String orderby = "";//排序
	
	
	
	public String getOrderby() {
		return orderby;
	}
	public void setOrderby(String orderby) {
		this.orderby = orderby==null? "" : orderby;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	
	
	public PageData() {
		setPage(1);
	}
	
	
	public T getCondition() {
		return condition;
	}
	public void setCondition(T condition) {
		this.condition = condition;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
		this.start = (page-1)*limit;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.totalPage = totalCount%limit==0? totalCount/limit :totalCount/limit+1;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
}
