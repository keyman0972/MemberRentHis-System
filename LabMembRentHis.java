package com.ddsc.km.exam.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ddsc.core.entity.BaseEntity;
import com.ddsc.km.lab.entity.LabFilmMst;

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

@Entity
@Table(name="LAB_MEMB_RENT_HIS")
public class LabMembRentHis extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 6585692631035502734L;
	
	private String membRentOid;
	private String rentDate;
	private String membId;
	private LabFilmMst labFilmMst;
	private BigDecimal rentAmt;
	private String extReturnDate;
	private String actualDate;
	
	
	@Id
	@Column (name = "MEMB_RENT_OID")
	@GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getMembRentOid() {
		return membRentOid;
	}
	
	public void setMembRentOid(String membRentOid) {
		this.membRentOid = membRentOid;
	}
	
	@Column (name = "RENT_DATE")
	public String getRentDate() {
		return rentDate;
	}
	
	public void setRentDate(String rentDate) {
		this.rentDate = rentDate;
	}
	
	@Column (name = "MEMB_ID")
	public String getMembId() {
		return membId;
	}

	public void setMembId(String membId) {
		this.membId = membId;
	}

	@OneToOne(targetEntity = LabFilmMst.class, fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "FILM_ID", referencedColumnName = "FILM_ID")
	public LabFilmMst getLabFilmMst() {
		return labFilmMst;
	}

	public void setLabFilmMst(LabFilmMst labFilmMst) {
		this.labFilmMst = labFilmMst;
	}

	@Column (name = "RENT_AMT")
	public BigDecimal getRentAmt() {
		return rentAmt;
	}
	
	public void setRentAmt(BigDecimal rentAmt) {
		this.rentAmt = rentAmt;
	}
	
	@Column (name = "EXT_RETURN_DATE")
	public String getExtReturnDate() {
		return extReturnDate;
	}
	
	public void setExtReturnDate(String extReturnDate) {
		this.extReturnDate = extReturnDate;
	}
	
	@Column (name = "ACTUAL_DATE")
	public String getActualDate() {
		return actualDate;
	}
	
	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}
	

}
