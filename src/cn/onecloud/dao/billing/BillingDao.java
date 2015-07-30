package cn.onecloud.dao.billing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.billing.Billing;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.billing.BillingPage;

@Repository("billingDao")
public class BillingDao extends UtilDao {

	/**
	 * 根据查询条件获取应用信息
	 */
	public Billing getAcount(BillingPage page) {
		List<String> params = new ArrayList<String>();
		StringBuilder sql = new StringBuilder();
		if(page.getAppname()!=null && !page.getAppname().isEmpty()) {
			sql.append("and bill.appname like (?)");
			params.add("%" + page.getAppname() + "%");
		}
		if(page.getOaid()!=null && !page.getOaid().isEmpty()) {
			sql.append(" and bill.oaid like (?)");
			params.add("%" + page.getOaid());
		}
		//如果不查询就返回全部
		if(params.size() == 0) {
			return new Billing("全部", "全部", "全部", 0);
		}
		List<Billing> bills = super.getObjs("select new Billing(" +
				"bill.user, bill.appname, bill.oaid, bill.accountId)" +
				"from Billing bill where 1=1 " + sql, params, 1);
		if(bills.size() == 0) {
			return null;
		}
		return bills.get(0);
	}
	/**
	 * 获取某一云架构的各项费用
	 * @param id 为0时表示查询全部
	 */
	@SuppressWarnings("unchecked")
	public List<Billing> getAllByCId(BillingPage page, int id) {
		if(id == 0) {
			return super.ht.find("select new Billing(bill.date, sum(bill.cpuAmount), sum(bill.cpuFee)," +
					"sum(bill.memoryAmount), sum(bill.memoryFee)," +
					"sum(bill.bandwidthAmount), sum(bill.bandwidthFee)," +
					"sum(bill.storageAmount), sum(bill.storageFee)," +
					"sum(bill.vmAmount), sum(bill.vmFee), sum(bill.totalFee)) " +
					"from Billing bill " +
					"where " + StaticMethod.DateScope(page, "bill") +
					" group by bill.date order by bill.date");
		}
		
		return super.ht.find("select new Billing(bill.date, bill.cpuAmount, bill.cpuFee," +
				"bill.memoryAmount, bill.memoryFee," +
				"bill.bandwidthAmount, bill.bandwidthFee," +
				"bill.storageAmount, bill.storageFee," +
				"bill.vmAmount, bill.vmFee, bill.totalFee) " +
				"from Billing bill " +
				"where " + StaticMethod.DateScope(page, "bill") +
				" and bill.accountId=" + id +
				" order by bill.date");
	}
	/**
	 * 获取某个应用一个月总费用
	 * @param day 日期范围
	 * @param id countID 为0返回全部
	 */
	public Billing getAllByBIdOnMonth(Calendar day, int id) {
		return (Billing) super.ht.find("select new Billing(sum(bill.cpuAmount), sum(bill.cpuFee)," +
				"sum(bill.memoryAmount), sum(bill.memoryFee)," +
				"sum(bill.bandwidthAmount), sum(bill.bandwidthFee)," +
				"sum(bill.storageAmount), sum(bill.storageFee)," +
				"sum(bill.vmAmount), sum(bill.vmFee), sum(bill.totalFee)) " +
				"from Billing bill " +
				"where " + StaticMethod.getDateSql(day,
						StaticMethod.DateToMouth(day.getTime()), "bill") +
				(id==0?"" : " and bill.accountId=" + id)).get(0);
	}

}
