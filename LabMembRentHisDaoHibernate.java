package com.ddsc.km.exam.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;

import com.ddsc.core.dao.hibernate.GenericDaoHibernate;
import com.ddsc.core.entity.UserInfo;
import com.ddsc.core.exception.DdscApplicationException;
import com.ddsc.core.util.HibernateScalarHelper;
import com.ddsc.core.util.LocaleDataHelper;
import com.ddsc.core.util.Pager;
import com.ddsc.km.exam.dao.ILabMembRentHisDao;
import com.ddsc.km.exam.entity.LabMembRentHis;
import com.ddsc.km.exam.entity.LabMemberMst;

/**
 * <table>
 * <tr>
 * <th>版本</th>
 * <th>日期</th>
 * <th>詳細說明</th>
 * <th>modifier</th>
 * </tr>
 * <tr>
 * <td>1.0</td>
 * <td>2017/8/23</td>
 * <td>新建檔案</td>
 * <td>"keyman"</td>
 * </tr>
 * </table>
 * @author "keyman"
 *
 * 類別說明 :
 *
 *
 * 版權所有 Copyright 2008 © 中菲電腦股份有限公司 本網站內容享有著作權，禁止侵害，違者必究。 <br>
 * (C) Copyright Dimerco Data System Corporation Inc., Ltd. 2009 All Rights
 */

public class LabMembRentHisDaoHibernate extends GenericDaoHibernate<LabMembRentHis,String> implements ILabMembRentHisDao {

	@Override
	public Pager searchByConditions(Map<String, Object> conditions, Pager pager, UserInfo userInfo) throws DdscApplicationException {
		
		String filmName_lang = LocaleDataHelper.getPropertityWithLocalUpper("FILM_NAME", userInfo.getLocale());
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT LMRH.MEMB_RENT_OID, LMRH.MEMB_ID, LMM.MEMB_NAME, LMRH.RENT_DATE, ");
		sbsql.append("	LMRH.FILM_ID, LFM."+filmName_lang+" AS FILM_NAME, LMRH.RENT_AMT, ");
		sbsql.append("	LMRH.EXT_RETURN_DATE, COUNT(LMRH2.MEMB_ID) AS MEMB_ID_COUNT ");
		sbsql.append("FROM LAB_MEMB_RENT_HIS LMRH ");
		sbsql.append("INNER JOIN LAB_MEMBER_MST LMM ON LMRH.MEMB_ID = LMM.MEMB_ID ");
		sbsql.append("INNER JOIN LAB_FILM_MST LFM ON LMRH.FILM_ID = LFM.FILM_ID ");
		sbsql.append("LEFT JOIN LAB_MEMB_RENT_HIS LMRH2 ON LMRH.MEMB_ID = LMRH2.MEMB_ID ");
		sbsql.append("WHERE LMRH.ACTUAL_DATE IS NULL AND LMRH2.ACTUAL_DATE IS NULL ");
		
		String keyword = "AND ";
		List<Object> value = new ArrayList<Object>();
		if (StringUtils.isNotEmpty((String) conditions.get("rentDate"))) {
			sbsql.append(keyword + "LMRH.RENT_DATE = ? ");
			value.add(conditions.get("rentDate"));
			keyword = "AND ";
		}
		if (StringUtils.isNotEmpty((String) conditions.get("filmId"))) {
			sbsql.append(keyword +"LMRH.FILM_ID LIKE ? ");
			value.add(conditions.get("filmId") + "%");
			keyword = "AND ";
		}
		if (StringUtils.isNotEmpty((String) conditions.get("filmId"))) {
			sbsql.append(keyword +"LMRH.FILM_ID LIKE ? ");
			value.add(conditions.get("filmId") + "%");
			keyword = "AND ";
			sbsql.append(keyword +"LMRH2.FILM_ID LIKE ? ");
			value.add(conditions.get("filmId") + "%");
			keyword = "AND ";
		}
		if (StringUtils.isNotEmpty((String) conditions.get("membId"))) {
			sbsql.append(keyword +"LMRH.MEMB_NAME LIKE ? ");
			value.add("%"+conditions.get("membId") + "%");
			keyword = "AND ";
		}
		if (StringUtils.isNotEmpty((String) conditions.get("membName"))) {
			sbsql.append(keyword +"LMRH.MEMB_ID LIKE ? ");
			value.add("%" + conditions.get("membName") + "%");
			keyword = "AND ";
		}
		
		sbsql.append("GROUP BY LMRH.MEMB_RENT_OID, LMRH.MEMB_ID, LMM.MEMB_NAME, LMRH.RENT_DATE, LMRH.FILM_ID, LFM."+filmName_lang+", LMRH.RENT_AMT, LMRH.EXT_RETURN_DATE ");
		sbsql.append("ORDER BY LMRH.MEMB_ID ASC,LMRH.RENT_DATE DESC ");
		
		// 建立List<HibernateScalarHelper> scalarList = new ArrayList<HibernateScalarHelper>(); 並add
		List<HibernateScalarHelper> scalarList = new ArrayList<HibernateScalarHelper>();
		scalarList.add(new HibernateScalarHelper("MEMB_RENT_OID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("MEMB_ID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("MEMB_NAME", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("RENT_DATE", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("FILM_ID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("FILM_NAME", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("RENT_AMT", Hibernate.BIG_DECIMAL));
		scalarList.add(new HibernateScalarHelper("EXT_RETURN_DATE", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("MEMB_ID_COUNT", Hibernate.STRING));
		
		// 回傳
		return super.findBySQLQueryMapPagination(sbsql.toString(), pager, scalarList, value, userInfo);
	}

	@Override
	public List<LabMembRentHis> queryMember(LabMemberMst entity,UserInfo info) throws DdscApplicationException {
		
		List<Object> values = new ArrayList<Object>();
		StringBuffer sbsql= new StringBuffer();
		sbsql.append("select lmrs ");
		sbsql.append("from LabMembRentHis lmrs ");
		
		String keyword = "where ";
		if(StringUtils.isNotEmpty((String)entity.getMembId())){
			sbsql.append(keyword + "lmrs.membId = ? ");
			values.add((String)entity.getMembId());
			keyword = "and ";
		}
		if(StringUtils.isNotEmpty((String)entity.getLabMembRentHisList().get(0).getRentDate())){
			sbsql.append(keyword + "lmrs.rentDate = ? ");
			values.add((String)entity.getLabMembRentHisList().get(0).getRentDate());
			keyword = "and ";
		}
		
		return super.findByHQLString(sbsql.toString(), values, info);
	}

	@Override
	public List<Map<String, Object>> queryMemb(String id, UserInfo info) throws DdscApplicationException {
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT LMM.MEMB_ID, LMM.MEMB_NAME, LMM.MEMB_PREPAID_VAL, ");
		sbsql.append("	COUNT(LMRH.MEMB_ID) AS MEMB_ID_COUNT, COC.OPT_CDE AS OPT_CDE_MEMB_GRADE ");
		sbsql.append("FROM LAB_MEMBER_MST LMM ");
		sbsql.append("LEFT JOIN LAB_MEMB_RENT_HIS LMRH ON LMM.MEMB_ID = LMRH.MEMB_ID ");
		sbsql.append("LEFT JOIN COMM_OPT_CDE COC ON LMM.MEMB_GRADE = COC.OPT_CDE_OID ");
		
		String keyword = "WHERE ";
		List<Object> value = new ArrayList<Object>();
		if (StringUtils.isNotEmpty(id)) {
			sbsql.append(keyword + "LMM.MEMB_ID = ? ");
			value.add(id);
			keyword = "AND ";
		}
		
		sbsql.append("GROUP BY LMM.MEMB_ID, LMM.MEMB_NAME, LMM.MEMB_PREPAID_VAL, COC.OPT_CDE ");
		
		// 建立List<HibernateScalarHelper> scalarList = new ArrayList<HibernateScalarHelper>(); 並add
		List<HibernateScalarHelper> scalarList = new ArrayList<HibernateScalarHelper>();
		scalarList.add(new HibernateScalarHelper("MEMB_ID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("MEMB_NAME", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("MEMB_PREPAID_VAL", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("MEMB_ID_COUNT", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("OPT_CDE_MEMB_GRADE", Hibernate.STRING));
		
		return super.findBySQLQueryMap(sbsql.toString(), scalarList, value, info);
	}
	
	public List<Map<String, Object>> queryFilm(Map<String, Object> conditions,UserInfo info) throws DdscApplicationException {
		
		String filmName_lang = LocaleDataHelper.getPropertityWithLocalUpper("FILM_NAME", info.getLocale());
		String fmCorpName_lang = LocaleDataHelper.getPropertityWithLocalUpper("FM_CORP_NAME", info.getLocale());
		String optCdeNam_lang = LocaleDataHelper.getPropertityWithLocalUpper("OPT_CDE_NAM", info.getLocale());
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("SELECT LFM.FILM_ID, LFM."+filmName_lang+" AS FILM_NAME, LFM.FILM_HOT, ");
		sbsql.append("	LFRM.RENT_HOT_AMT, LFRM.RENT_AMT, LFCM."+fmCorpName_lang+" AS FM_CORP_NAME, LFM.FILM_GRADE, ");
		sbsql.append("	COC."+optCdeNam_lang+" AS OPT_CDE_GRADE_NAM ");
		sbsql.append("FROM LAB_FILM_MST LFM ");
		sbsql.append("INNER JOIN LAB_FILM_CORP_MST LFCM ON LFM.FM_CORP_ID = LFCM.FM_CORP_ID ");
		sbsql.append("INNER JOIN LAB_FILM_RENT_MST LFRM ON LFM.FILM_ID = LFRM.FILM_ID ");
		sbsql.append("LEFT JOIN LAB_MEMB_RENT_HIS LMRH ON LFM.FILM_ID = LMRH.FILM_ID ");
		sbsql.append("LEFT JOIN LAB_MEMBER_MST LMM ON LMRH.MEMB_ID= LMM.MEMB_ID ");
		sbsql.append("LEFT JOIN COMM_OPT_CDE COC ON LFM.FILM_GRADE = COC.OPT_CDE AND COC.OPT_CTG_CDE = 'F0002' ");
		sbsql.append("LEFT JOIN COMM_OPT_CDE COC2 ON LFRM.MEMB_GRADE = COC2.OPT_CDE_OID ");
		
		String keyword = "WHERE ";
		List<Object> value = new ArrayList<Object>();
		if (StringUtils.isNotEmpty((String)conditions.get("filmId"))) {
			sbsql.append(keyword + "LFM.FILM_ID = ? ");
			value.add((String)conditions.get("filmId"));
			keyword = "AND ";
		}
		
		if (StringUtils.isNotEmpty((String)conditions.get("membGrade"))) {
			sbsql.append(keyword + "COC2.OPT_CDE = ? ");
			value.add((String)conditions.get("membGrade"));
			keyword = "AND ";
		}
		
		// 建立List<HibernateScalarHelper> scalarList = new ArrayList<HibernateScalarHelper>(); 並add
		List<HibernateScalarHelper> scalarList = new ArrayList<HibernateScalarHelper>();
		scalarList.add(new HibernateScalarHelper("FILM_ID", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("FILM_NAME", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("FILM_HOT", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("RENT_HOT_AMT", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("RENT_AMT", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("FM_CORP_NAME", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("FILM_GRADE", Hibernate.STRING));
		scalarList.add(new HibernateScalarHelper("OPT_CDE_GRADE_NAM", Hibernate.STRING));
		
		
		return super.findBySQLQueryMap(sbsql.toString(), scalarList, value, info);
	}

}
