package cn.onecloud.util.page.cmdb;


public class DropDownListFieldPage extends Page{
	
	private String field_name;
	
	/**
	 * 计算页数
	 * @param countNums
	 */
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100)
			super.setNum(20);
		super.countPage(countNums);
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
}
