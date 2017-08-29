package com.ddsc.km.exam.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.ddsc.core.entity.UserInfo;
import com.ddsc.core.exception.DdscApplicationException;
import com.ddsc.core.util.DateHelper;
import com.ddsc.core.util.Pager;
import com.ddsc.km.exam.entity.LabMemberMst;
import com.ddsc.km.exam.dao.ILabMembRentHisDao;
import com.ddsc.km.exam.dao.ILabMemberMstDao;
import com.ddsc.km.exam.dao.ILabMemberPrepaidHisDao;
import com.ddsc.km.exam.entity.LabMembRentHis;
import com.ddsc.km.exam.service.ILabMembRentHisService;
import com.ddsc.km.exam.entity.LabMemberPrepaidHis;
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

public class LabMembRentHisServiceImpl implements ILabMembRentHisService {
	//出租作業
	private ILabMembRentHisDao labMembRentHisDao;
	//會員主檔
	private ILabMemberMstDao labMemberMstDao;
	//儲值紀錄檔
	private ILabMemberPrepaidHisDao labMemberPrepaidHisDao;
	
	private ILabFilmMstService labFilmMstService;
	
	@Override
	public LabMembRentHis get(String id, UserInfo info) throws DdscApplicationException {
		try{
			
			return labMembRentHisDao.get(id, info);
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}

	@Override
	public LabMembRentHis create(LabMembRentHis entity, UserInfo info) throws DdscApplicationException {
		try{
			labMembRentHisDao.save(entity, info);
			return entity;
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}

	@Override
	public LabMembRentHis update(LabMembRentHis entity, UserInfo info) throws DdscApplicationException {
		try{
			
			return labMembRentHisDao.update(entity, info);
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
		
	}

	@Override
	public LabMembRentHis delete(LabMembRentHis entity, UserInfo info) throws DdscApplicationException {
		try{
			labMembRentHisDao.delete(entity, info);
			return entity;
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}
	
	@Deprecated
	@Override
	public List<LabMembRentHis> searchAll(UserInfo info) throws DdscApplicationException {
		return null;

	}

	@Override
	public Pager searchByConditions(Map<String, Object> conditions, Pager pager, UserInfo userInfo) throws DdscApplicationException {
		try{
			return labMembRentHisDao.searchByConditions(conditions, pager, userInfo);
			
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, userInfo);
		}
	}
	
	@Deprecated
	@Override
	public List<LabMembRentHis> searchByConditions(Map<String, Object> conditions, UserInfo userInfo) throws DdscApplicationException {
		return null;
	}
	
	@Deprecated
	@Override
	public List<Object> queryDataByParamsByService(Map<String, Object> conditions, UserInfo userInfo) throws DdscApplicationException {
		return null;
	}

	@Override
	public int getDataRowCountByConditions(Map<String, Object> conditions, UserInfo info) throws DdscApplicationException {
		try{
			return this.labMembRentHisDao.getDataRowCountByConditions(conditions, info);
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}

	@Override
	public LabMemberMst create(LabMemberMst labMerberMst, UserInfo info) throws DdscApplicationException {
		try{

			List<LabMemberPrepaidHis> labMemberPrepaidHisList =labMerberMst.getLabMemberPrepaidHis();
			
			String membId = labMerberMst.getMembId();
			BigDecimal prepaidAmt = BigDecimal.ZERO;
			if(labMemberPrepaidHisList.get(0).getPrepaidAmt()!= null){
				String prepaidDate = labMerberMst.getLabMembRentHisList().get(0).getRentDate();
				//新增儲值紀錄檔
				for(LabMemberPrepaidHis labMemberPrepaidHisPo:labMemberPrepaidHisList){
					labMemberPrepaidHisPo.setMembId(membId);
					labMemberPrepaidHisPo.setPrepaidDate(prepaidDate);
					this.labMemberPrepaidHisDao.save(labMemberPrepaidHisPo, info);
					prepaidAmt =labMemberPrepaidHisPo.getPrepaidAmt();
				}
			}
			//新增租借作業
			List<LabMembRentHis> labMembRentHisList = labMerberMst.getLabMembRentHisList();
			BigDecimal rentAmt = BigDecimal.ZERO;
			for(LabMembRentHis labMembRentHisPo:labMembRentHisList){
				rentAmt =rentAmt.add(labMembRentHisPo.getRentAmt());
				LabFilmMst labFilmMst = this.getLabFilmMstService().get(labMembRentHisPo.getLabFilmMst().getFilmId(), info);
				labMembRentHisPo.setMembId(membId);
				labMembRentHisPo.setLabFilmMst(labFilmMst);
				this.create(labMembRentHisPo, info);
			}
			
			//會員主檔
			LabMemberMst labMemberMstVO =this.labMemberMstDao.get(membId, info);
			
			BigDecimal sumVal =labMemberMstVO.getMembPrepaidVal().add(prepaidAmt).subtract(rentAmt);
			
			labMemberMstVO.setMembPrepaidVal(sumVal);
			this.labMemberMstDao.update(labMemberMstVO, info);
			
			return labMerberMst;
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}

	@Override
	public LabMemberMst update(LabMemberMst labMerberMst, UserInfo info)throws DdscApplicationException {
		try{
			
			List<LabMemberPrepaidHis> labMemberPrepaidHisList =labMerberMst.getLabMemberPrepaidHis();
			
			String membId = labMerberMst.getMembId();
			BigDecimal prepaidAmt = BigDecimal.ZERO;
			if(labMemberPrepaidHisList.get(0).getPrepaidAmt()!= null){
				String prepaidDate = labMerberMst.getLabMembRentHisList().get(0).getRentDate();
				prepaidAmt = labMemberPrepaidHisList.get(0).getPrepaidAmt();
				//新增儲值紀錄檔
				for(LabMemberPrepaidHis labMemberPrepaidHisPo:labMemberPrepaidHisList){
					labMemberPrepaidHisPo.setMembId(membId);
					labMemberPrepaidHisPo.setPrepaidDate(prepaidDate);
					this.labMemberPrepaidHisDao.save(labMemberPrepaidHisPo, info);
				}
			}
			
			//新增影片租借紀錄檔
			List<LabMembRentHis> newLabMembRentHisList = labMerberMst.getLabMembRentHisList();
			BigDecimal rentAmt = BigDecimal.ZERO;
			for (LabMembRentHis labMembRentHisListPo : newLabMembRentHisList) {
				LabMembRentHis oddLabMembRentHis= this.get(labMembRentHisListPo.getMembRentOid(), info);
				if ("insert".equals(labMembRentHisListPo.getOperate())) {
					// 新增
					this.create(labMembRentHisListPo, info);
					rentAmt =rentAmt.add(labMembRentHisListPo.getRentAmt());
				} else if ("update".equals(labMembRentHisListPo.getOperate())) {
					// 修改
					this.update(labMembRentHisListPo, info);
					BigDecimal newAmt = labMembRentHisListPo.getRentAmt();
					BigDecimal oddAmt =oddLabMembRentHis.getRentAmt();
					
					if(newAmt.compareTo(oddAmt) ==1 || newAmt.compareTo(oddAmt) ==-1){
						rentAmt =rentAmt.subtract(oddAmt).add(newAmt);
					}
				}  else if ("delete".equals(labMembRentHisListPo.getOperate())){
					//刪除
					this.delete(labMembRentHisListPo, info);
					rentAmt =rentAmt.subtract(labMembRentHisListPo.getRentAmt());
				} else {
					// 未異動, 不做任何處理
				}
			}
			
			//會員主檔
			LabMemberMst labMemberMstVO =this.labMemberMstDao.get(membId, info);
			BigDecimal sumVal = labMerberMst.getMembPrepaidVal().add(prepaidAmt).subtract(rentAmt);
			labMemberMstVO.setMembPrepaidVal(sumVal);
			this.labMemberMstDao.update(labMemberMstVO, info);
			
			return labMerberMst;
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}

	@Override
	public LabMemberMst delete(LabMemberMst labMerberMst, UserInfo info) throws DdscApplicationException {
		try{
			List<LabMembRentHis> LabMembRentHisListPo =labMerberMst.getLabMembRentHisList();
			String dateHelper = new DateHelper().getSystemDate("/");
			BigDecimal rentAmt = BigDecimal.ZERO;
			for(LabMembRentHis labMembRentHisPo:LabMembRentHisListPo){
				LabMembRentHis labMembRentHis =this.get(labMembRentHisPo.getMembRentOid(), info);
				labMembRentHis.setActualDate(dateHelper);
				this.labMembRentHisDao.delete(labMembRentHisPo, info);
				rentAmt=labMembRentHisPo.getRentAmt();
			}
			
			BigDecimal sumVal =labMerberMst.getMembPrepaidVal().add(rentAmt);
			LabMemberMst oddLabMerberMst =this.getLabMemberMstDao().get(labMerberMst.getMembId(), info);
			oddLabMerberMst.setMembPrepaidVal(sumVal);
			this.labMemberMstDao.save(oddLabMerberMst, info);
			
			return labMerberMst;
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}
	
	@Override
	public LabMemberMst queryMember(LabMemberMst entity, UserInfo info) throws DdscApplicationException {
		try{
			//會員主檔
			LabMemberMst labMemberMst =this.labMemberMstDao.get(entity.getMembId(), info);
			//明細檔
			List<LabMembRentHis> labMembRentHisList =this.getLabMembRentHisDao().queryMember(entity, info);
			//影片主檔
			for(LabMembRentHis labMembRentHis:labMembRentHisList){
				LabFilmMst labFilmMst =this.getLabFilmMstService().get(labMembRentHis.getLabFilmMst().getFilmId(), info);
				labMembRentHis.setLabFilmMst(labFilmMst);
			}
			labMemberMst.setLabMembRentHisList(this.getLabMembRentHisDao().queryMember(entity, info));
			
			
			int count = (this.getLabMembRentHisDao().queryMember(entity, info)).size();
			labMemberMst.setLabMembRentHisCount(String.valueOf(count));
			
			return labMemberMst;
			
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}
	
	@Override
	public List<Map<String, Object>> queryMemb(String id, UserInfo info) throws DdscApplicationException{
		try{
			return this.labMembRentHisDao.queryMemb(id, info);
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}
	
	@Override
	public List<Map<String, Object>> queryFilm(Map<String, Object> conditions,UserInfo info) throws DdscApplicationException {
		try{
			return this.labMembRentHisDao.queryFilm(conditions, info);
		}catch (DdscApplicationException e) {
			throw e;
		}catch (Exception e) {
			throw new DdscApplicationException(e, info);
		}
	}
	
	public ILabMembRentHisDao getLabMembRentHisDao() {
		return labMembRentHisDao;
	}

	public void setLabMembRentHisDao(ILabMembRentHisDao labMembRentHisDao) {
		this.labMembRentHisDao = labMembRentHisDao;
	}

	public ILabMemberMstDao getLabMemberMstDao() {
		return labMemberMstDao;
	}

	public void setLabMemberMstDao(ILabMemberMstDao labMemberMstDao) {
		this.labMemberMstDao = labMemberMstDao;
	}

	public ILabMemberPrepaidHisDao getLabMemberPrepaidHisDao() {
		return labMemberPrepaidHisDao;
	}

	public void setLabMemberPrepaidHisDao(ILabMemberPrepaidHisDao labMemberPrepaidHisDao) {
		this.labMemberPrepaidHisDao = labMemberPrepaidHisDao;
	}

	public ILabFilmMstService getLabFilmMstService() {
		return labFilmMstService;
	}

	public void setLabFilmMstService(ILabFilmMstService labFilmMstService) {
		this.labFilmMstService = labFilmMstService;
	}
}
