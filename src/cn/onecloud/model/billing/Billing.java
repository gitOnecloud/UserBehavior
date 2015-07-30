package cn.onecloud.model.billing;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="app_daily_billing")
public class Billing {

	private int id;
	private String user;
	private String appname;
	private String oaid;
	private Date date;
	private String day;//标记时间段
	private int accountId;
	private long cpuAmount;
	private long cpuFee;
	private long memoryAmount;
	private long memoryFee;
	private long bandwidthAmount;
	private long bandwidthFee;
	private long storageAmount;
	private long storageFee;
	private long vmAmount;
	private long vmFee;
	private long totalFee;
	
	public Billing(){}
	//BillingDao.getAcount
	public Billing(String user, String appname, String oaid, int accountId) {
		this.user = user;
		this.appname = appname;
		this.oaid = oaid;
		this.accountId = accountId;
	}
	//BillingDao.getAllByCId
	public Billing(Date date, long cpuAmount, long cpuFee, 
			long memoryAmount, long memoryFee,
			long bandwidthAmount, long bandwidthFee,
			long storageAmount, long storageFee,
			long vmAmount, long vmFee, long totalFee) {
		this.date = date;
		this.cpuAmount = cpuAmount;
		this.cpuFee = cpuFee;
		this.memoryAmount = memoryAmount;
		this.memoryFee = memoryFee;
		this.bandwidthAmount = bandwidthAmount;
		this.bandwidthFee = bandwidthFee;
		this.storageAmount = storageAmount;
		this.storageFee = storageFee;
		this.vmAmount = vmAmount;
		this.vmFee = vmFee;
		this.totalFee = totalFee;
	}
	//BillingDao.getAllByBIdOnMonth
		public Billing(Object cpuAmount, Object cpuFee, 
				Object memoryAmount, Object memoryFee,
				Object bandwidthAmount, Object bandwidthFee,
				Object storageAmount, Object storageFee,
				Object vmAmount, Object vmFee, Object totalFee) {
			if(cpuAmount != null) {
				this.cpuAmount = (Long) cpuAmount;
			}
			if(cpuFee != null) {
				this.cpuFee = (Long) cpuFee;
			}
			if(memoryAmount != null) {
				this.memoryAmount = (Long) memoryAmount;
			}
			if(memoryFee != null) {
				this.memoryFee = (Long) memoryFee;
			}
			if(bandwidthAmount != null) {
				this.bandwidthAmount = (Long) bandwidthAmount;
			}
			if(bandwidthFee != null) {
				this.bandwidthFee = (Long) bandwidthFee;
			}
			if(storageAmount != null) {
				this.storageAmount = (Long) storageAmount;
			}
			if(storageFee != null) {
				this.storageFee = (Long) storageFee;
			}
			if(vmAmount != null) {
				this.vmAmount = (Long) vmAmount;
			}
			if(vmFee != null) {
				this.vmFee = (Long) vmFee;
			}
			if(totalFee != null) {
				this.totalFee = (Long) totalFee;
			}
		}
	
	public Billing(String day) {
		this.day = day;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	@Column
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	@Column
	public String getOaid() {
		return oaid;
	}
	public void setOaid(String oaid) {
		this.oaid = oaid;
	}
	@Column
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Transient//不保存到数据库
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	@Column
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	@Column
	public long getCpuAmount() {
		return cpuAmount;
	}
	public void setCpuAmount(long cpuAmount) {
		this.cpuAmount = cpuAmount;
	}
	@Column
	public long getCpuFee() {
		return cpuFee;
	}
	public void setCpuFee(long cpuFee) {
		this.cpuFee = cpuFee;
	}
	@Column
	public long getMemoryAmount() {
		return memoryAmount;
	}
	public void setMemoryAmount(long memoryAmount) {
		this.memoryAmount = memoryAmount;
	}
	@Column
	public long getMemoryFee() {
		return memoryFee;
	}
	public void setMemoryFee(long memoryFee) {
		this.memoryFee = memoryFee;
	}
	@Column
	public long getBandwidthAmount() {
		return bandwidthAmount;
	}
	public void setBandwidthAmount(long bandwidthAmount) {
		this.bandwidthAmount = bandwidthAmount;
	}
	@Column
	public long getBandwidthFee() {
		return bandwidthFee;
	}
	public void setBandwidthFee(long bandwidthFee) {
		this.bandwidthFee = bandwidthFee;
	}
	@Column
	public long getStorageAmount() {
		return storageAmount;
	}
	public void setStorageAmount(long storageAmount) {
		this.storageAmount = storageAmount;
	}
	@Column
	public long getStorageFee() {
		return storageFee;
	}
	public void setStorageFee(long storageFee) {
		this.storageFee = storageFee;
	}
	@Column
	public long getVmAmount() {
		return vmAmount;
	}
	public void setVmAmount(long vmAmount) {
		this.vmAmount = vmAmount;
	}
	@Column
	public long getVmFee() {
		return vmFee;
	}
	public void setVmFee(long vmFee) {
		this.vmFee = vmFee;
	}
	@Column
	public long getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(long totalFee) {
		this.totalFee = totalFee;
	}
	
}
