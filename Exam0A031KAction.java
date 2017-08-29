package com.ddsc.km.exam.action;

import java.util.*;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import com.ddsc.common.comm.entity.CommOptCde;
import com.ddsc.common.comm.service.ICommOptCdeService;
import com.ddsc.core.action.AbstractAction;
import com.ddsc.core.action.IBaseAction;
import com.ddsc.core.entity.UserInfo;
import com.ddsc.core.exception.DdscApplicationException;
import com.ddsc.core.util.DateHelper;
import com.ddsc.core.util.Pager;
import com.ddsc.km.exam.entity.LabMembRentHis;
import com.ddsc.km.exam.entity.LabMemberMst;
import com.ddsc.km.exam.service.ILabMembRentHisService;
import com.ddsc.km.lab.entity.LabFilmMst;
import com.ddsc.km.lab.service.ILabFilmMstService;

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

public class Exam0A031KAction extends AbstractAction implements IBaseAction {

	private static final long serialVersionUID = -1338706455136016239L;
	
	private ILabMembRentHisService labMembRentHisService;
	private List<Map<String, Object>> labMembRentHisListMap;
	private LabMembRentHis labMembRentHis;
	private List<LabMembRentHis> labMembRentHisList;
	private LabMemberMst labMemberMst;
	
	private ICommOptCdeService commOptCdeService;
	private CommOptCde commOptCdeFFive;
	private ILabFilmMstService labFilmMstService;
	
	private String nowTime;
	private String beforeTime;
	
	private List<Map<String, Object>> results;
	private String params;

	@Override
	public String init() throws Exception {
		try {
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()),e.getMsgFullMessage() }));
		}
		setNextAction(ACTION_SEARCH);
		return SUCCESS;
	}
	
	@Deprecated
	@Override
	public String approve() throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String search() throws Exception {
		try {			
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("rentDate", beforeTime);
			conditions.put("filmId", labMemberMst.getLabMembRentHisList().get(0).getLabFilmMst().getFilmId());
			conditions.put("membId", labMemberMst.getMembId());
			conditions.put("membName", labMemberMst.getMembName());
			

			//主檔資料
			Pager resultPager = getLabMembRentHisService().searchByConditions(conditions, getPager(), getUserInfo());
			
			labMembRentHisListMap = (List<Map<String, Object>>) resultPager.getData();
			
			setPager(resultPager);
			if (labMembRentHisListMap == null || labMembRentHisListMap.size() <= 0) {
				this.addActionError(this.getText("w.0001"));
			}
		} catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()),e.getMsgFullMessage() }));
		} catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()),e.getMsgFullMessage() }));
		}
		setNextAction(ACTION_SEARCH);
		return SUCCESS;
	}
	
	@Override
	public String query() throws Exception {
		try{
			labMemberMst =getLabMembRentHisService().queryMember(labMemberMst, this.getUserInfo());
			
			
		} catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()),e.getMsgFullMessage() }));
		} catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()),e.getMsgFullMessage() }));
		}
		setNextAction(ACTION_QUERY);
		return SUCCESS;
	}
	
	@Override
	public String create() throws Exception {
		try{
			//取得系統時間
			String dateHelper = new DateHelper().getSystemDate("/");
			
			LabMembRentHis labMembRentHis = new LabMembRentHis();
			labMembRentHis.setRentDate(dateHelper);
			//取的系統當天+天數
			List<CommOptCde> commOptCdeList = this.getCommOptCdeService().getList("F0005", this.getUserInfo());
			int days = Integer.parseInt(commOptCdeList.get(0).getCharAttrFld3());
			labMembRentHis.setExtReturnDate(new DateHelper().addDays(days));
			
			List<LabMembRentHis> labMembRentHisList = new ArrayList<LabMembRentHis>();
			labMembRentHisList.add(labMembRentHis);
			
			labMemberMst = new LabMemberMst();
			labMemberMst.setLabMembRentHisList(labMembRentHisList);
			
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		setNextAction(ACTION_CREATE_SUBMIT);
		return SUCCESS;
	}

	@Override
	public String createSubmit() throws Exception {
		try {
			if (this.hasConfirm() == true) {
				setNextAction(ACTION_CREATE_CONFIRM);
				return RESULT_CONFIRM;
			}
			else {
				return this.createConfirm();
			}
		}
		catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			return RESULT_EDIT;
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			return RESULT_EDIT;
		}
	}

	@Override
	public String createConfirm() throws Exception {
		try{
			labMemberMst = getLabMembRentHisService().create(labMemberMst, this.getUserInfo());
			setNextAction(ACTION_CREATE);
			return RESULT_SHOW;
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_CREATE_SUBMIT);
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_CREATE_SUBMIT);
			return RESULT_EDIT;
		}
	}

	@Override
	public String update() throws Exception {
		try{
			labMemberMst =getLabMembRentHisService().queryMember(labMemberMst, this.getUserInfo());
			setNextAction(ACTION_UPDATE_SUBMIT);
			return SUCCESS;
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			return RESULT_EDIT;
		}
	}

	@Override
	public String updateSubmit() throws Exception {
		try {
			if (hasConfirm()) {
				setNextAction(ACTION_UPDATE_CONFIRM);
				return RESULT_CONFIRM;
			}else {
				return this.updateConfirm();
			}
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_UPDATE_SUBMIT);
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_UPDATE_SUBMIT);
			return RESULT_EDIT;
		}
	}

	@Override
	public String updateConfirm() throws Exception {
		try{
			labMemberMst = getLabMembRentHisService().update(labMemberMst, getUserInfo());
			setNextAction(ACTION_UPDATE);
			return RESULT_SHOW;
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_UPDATE_SUBMIT);
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_UPDATE_SUBMIT);
			return RESULT_EDIT;
		}
	}

	@Override
	public String delete() throws Exception {
		try{
			labMemberMst =getLabMembRentHisService().queryMember(labMemberMst, this.getUserInfo());
			
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		setNextAction(ACTION_DELETE_CONFIRM);
		return SUCCESS;
	}

	@Override
	public String deleteConfirm() throws Exception {
		try {
			labMemberMst = getLabMembRentHisService().delete(labMemberMst, this.getUserInfo());
			setNextAction(ACTION_DELETE);
			return RESULT_SHOW;
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_DELETE_CONFIRM);
			return RESULT_CONFIRM;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_DELETE_CONFIRM);
			return RESULT_CONFIRM;
		}
	}

	@Override
	public String copy() throws Exception {
		try {
			String dateHelper = new DateHelper().getSystemDate("/");
			
			labMemberMst =getLabMembRentHisService().queryMember(labMemberMst, this.getUserInfo());
			
			labMemberMst.getLabMembRentHisList().get(0).setRentDate(dateHelper);
			
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		setNextAction(ACTION_COPY_SUBMIT);
		return SUCCESS;
	}

	@Override
	public String copySubmit() throws Exception {
		try {
			if (this.hasConfirm() == true) {
				// 有確認頁
				setNextAction(ACTION_COPY_CONFIRM);
				return RESULT_CONFIRM;
			}
			else {
				// 沒有確認頁, 直接存檔
				return this.copyConfirm();
			}
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_COPY_SUBMIT);
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_COPY_SUBMIT);
			return SUCCESS;
		}
	}

	@Override
	public String copyConfirm() throws Exception {
		try {
			labMemberMst = getLabMembRentHisService().create(labMemberMst, getUserInfo());
			setNextAction(ACTION_COPY);
			return RESULT_SHOW;
		}catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_COPY_SUBMIT);
			return RESULT_EDIT;
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
			setNextAction(ACTION_COPY_SUBMIT);
			return RESULT_EDIT;
		}
	}
	
	@Override
	public void validate() {
		try {
			setUpInfo();
		}catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] { e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage() }));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] { e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage() }));
		}
	}
	
	/**
	 * 檢核 - 按送出鈕(新增頁)
	 */
	public void validateCreateSubmit() {
		try {
			this.checkPrimaryKey();
			this.checkValidateRule();
		}
		catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}

	/**
	 * 檢核 - 按確定鈕(新增頁)
	 */
	public void validateCreateConfirm() {
		// 先執行Action所對應的 validate, 再執行 validate(). (即 validateCreateSubmit 執行完後, 再執行 validate())
		try {
			this.checkPrimaryKey();
			this.checkValidateRule();
		}catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}

	/**
	 * 檢核 - 按送出鈕(新增頁)
	 */
	public void validateUpdateSubmit() {
		try {
			this.checkValidateRule();
		}catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}

	/**
	 * 檢核 - 按確定鈕(新增頁)
	 */
	public void validateUpdateConfirm() {
		// 先執行Action所對應的 validate, 再執行 validate(). (即 validateCreateSubmit 執行完後, 再執行 validate())
		try {
			this.checkValidateRule();
		}catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}
	
	/**
	 * 檢核 - 按確定鈕(刪除頁)
	 */
	public void validateDeleteConfirm() {
		try {
			this.checkValidateRule();
			
		}catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] { e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage() }));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] { e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage() }));
		}
	}

	/**
	 * 檢核 - 按送出鈕(複製頁)
	 */
	public void validateCopySubmit() {
		// 先執行Action所對應的 validate, 再執行 validate(). (即 validateCreateSubmit 執行完後, 再執行 validate())
		try {
			this.checkPrimaryKey();
			this.checkValidateRule();
		}catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}

	/**
	 * 檢核 - 按確定鈕(複製頁)
	 */
	public void validateCopyConfirm() {
		// 先執行Action所對應的 validate, 再執行 validate(). (即 validateCreateSubmit 執行完後, 再執行 validate())
		try {
			this.checkPrimaryKey();
			this.checkValidateRule();
		}catch (DdscApplicationException e) {
			// 取得 SQL 錯誤碼，並依多國語系設定顯示於Message box
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
	}

	/**
	 * 檢核ID是否重複
	 *
	 * @return
	 * @throws Exception
	 */
	private boolean checkPrimaryKey() throws Exception {
		boolean isValid = true;
		
		
		return isValid;
	}

	/**
	 * 資料檢核
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean checkValidateRule() throws Exception {
		boolean isValid = true;
		try {
			// 檢查參數代碼是否存在 (區域)
			if(labMemberMst.getLabMembRentHisList().size()>0){
				for(LabMembRentHis labMembRentHis:labMemberMst.getLabMembRentHisList()){
					if (null != labMembRentHis.getLabFilmMst().getFilmId() && StringUtils.isNotEmpty(labMembRentHis.getLabFilmMst().getFilmId())){
						LabFilmMst labFilmMst =this.getLabFilmMstService().get(labMembRentHis.getLabFilmMst().getFilmId(), this.getUserInfo());
						if(labFilmMst == null){
							this.addFieldError("filmId", this.getText("filmId")+ this.getText("eP.0003"));
							isValid = false;
						}else{
							labMembRentHis.setLabFilmMst(labFilmMst);
							
						}
					}
				}
			}
		}catch (DdscApplicationException e) {
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}catch (Exception ex) {
			DdscApplicationException e = new DdscApplicationException(ex, this.getUserInfo());
			this.addActionError(this.getText("eP.0022", new String[] {e.getMsgCode(), this.getText(e.getMsgCode()), e.getMsgFullMessage()}));
		}
		return isValid;
	}
	//查詢會員資料
	public String queryMemb() throws Exception{
		try{
			UserInfo info = getUserInfo();
			if(params != null && StringUtils.isNotEmpty(params)){
				JSONObject jo = JSONObject.fromObject(params);
				
				String date = (String) jo.get("membId");
				info.setLocale(this.getLocale());
				List<Map<String,Object>> aListMap = this.getLabMembRentHisService().queryMemb(date, info);
				System.out.println(aListMap);
				results = aListMap;
			}else {
				results = new ArrayList<Map<String, Object>>();
			}
		}catch (Exception e) {
			return e.getMessage();
		}
		return SUCCESS;
	}
	//查詢影片資料
	public String queryFilmName() throws Exception{
		try{
			UserInfo info = getUserInfo();
			if(params != null && StringUtils.isNotEmpty(params)){
				JSONObject jo = JSONObject.fromObject(params);
				
				Map<String, Object> conditions = new HashMap<String, Object>();
				conditions.put("filmId", (String) jo.get("filmId"));
				conditions.put("membGrade", (String) jo.get("membGrade"));
				info.setLocale(this.getLocale());
				List<Map<String,Object>> aListMap = this.getLabMembRentHisService().queryFilm(conditions, info);
				System.out.println(aListMap);
				results = aListMap;
			}else {
				results = new ArrayList<Map<String, Object>>();
			}
		}catch (Exception e) {
			return e.getMessage();
		}
		return SUCCESS;
	}
	
	@JSON(serialize = false)
	public ILabMembRentHisService getLabMembRentHisService() {
		return labMembRentHisService;
	}

	public void setLabMembRentHisService(ILabMembRentHisService labMembRentHisService) {
		this.labMembRentHisService = labMembRentHisService;
	}
	
	@JSON(serialize = false)
	public ILabFilmMstService getLabFilmMstService() {
		return labFilmMstService;
	}

	public void setLabFilmMstService(ILabFilmMstService labFilmMstService) {
		this.labFilmMstService = labFilmMstService;
	}
	
	@JSON(serialize = false)
	public ICommOptCdeService getCommOptCdeService() {
		return commOptCdeService;
	}

	public void setCommOptCdeService(ICommOptCdeService commOptCdeService) {
		this.commOptCdeService = commOptCdeService;
	}
	
	public List<Map<String, Object>> getLabMembRentHisListMap() {
		return labMembRentHisListMap;
	}

	public void setLabMembRentHisListMap(List<Map<String, Object>> labMembRentHisListMap) {
		this.labMembRentHisListMap = labMembRentHisListMap;
	}
	
	public List<LabMembRentHis> getLabMembRentHisList() {
		return labMembRentHisList;
	}

	public void setLabMembRentHisList(List<LabMembRentHis> labMembRentHisList) {
		this.labMembRentHisList = labMembRentHisList;
	}
	
	public List<Map<String, Object>> getResults() {
		return results;
	}

	public void setResults(List<Map<String, Object>> results) {
		this.results = results;
	}
	
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
	//取得現在時間
	public String getNowTime() {
		if(nowTime == null){
			String dateHelper = new DateHelper().getSystemDate("/");
			this.setNowTime(dateHelper);
		}
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
	
	//取的系統日-天數
	public String getBeforeTime() {
		if(beforeTime == null){
			List<CommOptCde> commOptCdeList =this.getCommOptCdeService().getList("F0005", this.getUserInfo());
			int num = Integer.parseInt(commOptCdeList.get(0).getCharAttrFld3());
			this.setBeforeTime(new DateHelper().addDays(-num));
		}
		return beforeTime;
	}

	public void setBeforeTime(String beforeTime) {
		this.beforeTime = beforeTime;
	}
	
	public LabMemberMst getLabMemberMst() {
		return labMemberMst;
	}

	public void setLabMemberMst(LabMemberMst labMemberMst) {
		this.labMemberMst = labMemberMst;
	}
	

	public LabMembRentHis getLabMembRentHis() {
		return labMembRentHis;
	}

	public void setLabMembRentHis(LabMembRentHis labMembRentHis) {
		this.labMembRentHis = labMembRentHis;
	}

	public CommOptCde getCommOptCdeFFive() {
		if(commOptCdeFFive == null){
			this.setCommOptCdeFFive(this.getCommOptCdeService().getByKey("F0005", null, this.getUserInfo()));
		}
		return commOptCdeFFive;
	}

	public void setCommOptCdeFFive(CommOptCde commOptCdeFFive) {
		this.commOptCdeFFive = commOptCdeFFive;
	}
	
}
