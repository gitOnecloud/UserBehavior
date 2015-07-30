package cn.onecloud.model.cmdb.server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 机柜、机房样式
 */
@Entity
@Table(name="server_style")
public class Style {

	private int id;
	private String name;
//Horizontal 横
	private int horNum;//横数
	private int horInterval;//横间隔
//Vertical 竖
	private int verNum;//竖数
	private int verInterval;//竖间隔
	
	private int insideWidth;//服务器宽
	private int insideHeight;//服务器高
	
	public Style() {}
	//StyleDao.getAll
	public Style(int id, String name) {
		this.id = id;
		this.name = name;
	}
	//set get
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(nullable=false)
	public int getHorNum() {
		return horNum;
	}
	public void setHorNum(int horNum) {
		this.horNum = horNum;
	}
	@Column(nullable=false)
	public int getHorInterval() {
		return horInterval;
	}
	public void setHorInterval(int horInterval) {
		this.horInterval = horInterval;
	}
	@Column(nullable=false)
	public int getVerNum() {
		return verNum;
	}
	public void setVerNum(int verNum) {
		this.verNum = verNum;
	}
	@Column(nullable=false)
	public int getVerInterval() {
		return verInterval;
	}
	public void setVerInterval(int verInterval) {
		this.verInterval = verInterval;
	}
	@Column(nullable=false)
	public int getInsideWidth() {
		return insideWidth;
	}
	public void setInsideWidth(int insideWidth) {
		this.insideWidth = insideWidth;
	}
	@Column(nullable=false)
	public int getInsideHeight() {
		return insideHeight;
	}
	public void setInsideHeight(int insideHeight) {
		this.insideHeight = insideHeight;
	}

}
