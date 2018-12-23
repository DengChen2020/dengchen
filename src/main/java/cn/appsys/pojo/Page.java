package cn.appsys.pojo;

//分页所需要的实体类
public class Page {

	private Integer currentPageNo; // 当前页码 curr是current单词的简写，当前的意思
	private Integer pageSize; // 每一页显示的数据行数，页面大小
	private Integer totalCount; // 总的记录数。
	private Integer totalPageCount; // 总的页数
	
	public Integer getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(Integer currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	// 在为总记录数赋值时，顺便把总的页数给计算出来。 题目： 总记录是31，每一页显示5条，问：总共有多少页？ 答案：7页
	public void setTotalCount(Integer totalCount) {
		if (totalCount > 0) {
			this.totalCount = totalCount;

			//题目： 总记录是31，每一页显示5条，问：总共有多少页？ 答案：7页
			this.totalPageCount = this.totalCount % this.pageSize == 0 ? this.totalCount
					/ this.pageSize
					: this.totalCount / this.pageSize + 1;
		}
	}

	public Integer getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(Integer totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
}
