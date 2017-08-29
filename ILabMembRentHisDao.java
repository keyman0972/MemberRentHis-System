package com.ddsc.km.exam.dao;

import java.util.*;

import com.ddsc.core.dao.IGenericDao;
import com.ddsc.core.entity.UserInfo;
import com.ddsc.core.exception.DdscApplicationException;
import com.ddsc.core.util.Pager;
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

public interface ILabMembRentHisDao  extends IGenericDao<LabMembRentHis, String> {
	
	public Pager searchByConditions(Map<String, Object> conditions,Pager pager,UserInfo userInfo) throws DdscApplicationException;
	
	public List<LabMembRentHis> queryMember(LabMemberMst entity,UserInfo info) throws DdscApplicationException;
	
	public List<Map<String,Object>> queryMemb(String id,UserInfo info) throws DdscApplicationException;

	public List<Map<String, Object>> queryFilm(Map<String, Object> conditions,UserInfo info) throws DdscApplicationException;
	
}
