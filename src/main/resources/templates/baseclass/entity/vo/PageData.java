package ${package.Entity}.vo;

import java.util.List;


public class PageData<T> {
	private int page = 1;//当前页
	private int total;//总记录数
	private int totalPage;//总页数
	private int offset = 0;//页开始
	private int limit = 5;//每页最大记录数
	private List<T> rows;
	private T condition;
	private String orderby = "";//排序
	
	
	
	public String getOrderby() {
		return orderby;
	}
	public void setOrderby(String orderby) {
		this.orderby = "ORDER BY " + orderby;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	
	public PageData() {
		//setPage(1);
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
		this.offset = (page-1)*limit;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
		this.totalPage = total % limit==0? total/limit : total/limit+1;
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
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
}
