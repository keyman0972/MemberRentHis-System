<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/include.Taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<base target="_self" />
<s:include value="/WEB-INF/pages/include/include.Scripts.jsp" />
<script type="text/javascript" src="<s:url value="/ddscPlugin/ddsc.gridEditList.plugin.js"/>"></script>
<script type="text/javascript" src="<s:url value="/ddscPlugin/ddsc.validation.plugin.js"/>"></script>
<script type="text/javascript" src="<s:url value="/jquery/jquery.alphanumeric.js"/>"></script>
<script type="text/javascript" src="<s:url value="/js/ddsc.input.js"/>"></script>
<script type="text/javascript" src="<s:url value="/jquery/ui/jquery.ui.datepicker.min.js"/>"></script>
<script language="javascript">
var oTable;
//畫面欄位檢核
function validate() {
	$("#frmExam0A031K").validate("clearPrompt"); 
	
	$("#membId").validateRequired({fieldText:'<s:text name="membId" />'});
	<%-- 
	//時間
	//小於系統日
	var index = fnGetObjIndex($(this));
	if($("#extReturnDate_"+index).val()< '<s:property value="nowTime" />'){
		$("#extReturnDate_"+index).validate("sendPrompt",{message:'<s:text name="eC.0087"><s:param value="getText(\"fix.00819\")"/></s:text>'});
	}
	
	//大於系統日+天數
	if($("#extReturnDate_"+index).val()>= '<s:property value="new DateHelper().addDays(-commOptCdeFFive.charAttrFld3)" />'){
		$("#extReturnDate_"+index).validate("sendPrompt",{message:'<s:text name="eC.0107"><s:property value="new DateHelper().addDays(commOptCdeFFive.charAttrFld3)" /></s:text>'});
	}
	--%>
	//最小儲值金額
	var sum=0;
	for(var i=0;i<$("#tbGrid tr").length;i++){
		sum += $("#rentAmt_"+i).text().toNumber();
	}
	if($("#membPrepaidVal").text().toNumber()+$("#prepaidAmt").val().toNumber()-sum < <s:property value="commOptCdeFFive.charAttrFld2"/>){
		$("#membPrepaidVal").validate("sendPrompt",{message:'<s:text name="exam.e0005"><s:param value="getText(\"commOptCdeFFive.getCharAttrFld4()\")"/></s:text>'});
	}
	
	//最少儲值金額倍數
	if($("#prepaidAmt").val() !=""){
		if($("#prepaidAmt").val().toNumber() % <s:property value="commOptCdeFFive.charAttrFld1"/> !=0){
			$("#extReturnDate_"+index).validate("sendPrompt",{message:'<s:text name="exam.e0004"><s:param value="getText(\"prepaidAmt\")"/></s:text>'});
		}
	}
	if($("#membPrepaidVal").text().toNumber() < 0){
		$("#membPrepaidVal").validate("sendPrompt",{message:'<s:text name="eC.0082"><s:param value="getText(\"0\")"/></s:text>'});
	}
	
	//未還歸還數量
	if($("#membIdCount").text().toNumber()+$("#tbGrid tr").length > <s:property value="commOptCdeFFive.charAttrFld4" />){
		$("#membIdCount").validate("sendPrompt",{message:'<s:text name="exam.00002"><s:param value="getText(\"eC.0029\")"/></s:text>'});
	}
	
    if(!$("#frmExam0A031K").validate("isErrors")){
		fnSetOperate(oTable);
	}
    return $("#frmExam0A031K").validate("showPromptWithErrors");

}

// 取會員名稱
function getMembName() {
	var membId = $('#membId').val();
	if(membId != null){
    	$.ajax({
      		type: 'post',
	      	url:'<s:url value="/ajax/km/queryMemb.action" />',
	      	data:{queryName: 'findLabMemberMst', params: '{membId: "' + membId + '"}'},
	      	success: function (rtn_data) {
	      		if(rtn_data.results.length == 1 && rtn_data.results[0] != "" && rtn_data.results[0] != null){
			  		$('#membName').text(rtn_data.results[0].MEMB_NAME);
			  		$('#hiddenMembName').val(rtn_data.results[0].MEMB_NAME);
			  		$('#membPrepaidVal').text(rtn_data.results[0].MEMB_PREPAID_VAL);
			  		$('#hiddenMembPrepaidVal').val(rtn_data.results[0].MEMB_PREPAID_VAL);
			  		$('#hiddenMembGrade').val(rtn_data.results[0].OPT_CDE_MEMB_GRADE);
			  		if(rtn_data.results[0].ID_COUNT="null"){
			  			$('#membIdCount').text(0);
				  		$('#hiddenMembIdCount').val(0);
			  		}else{
				  		$('#membIdCount').text(rtn_data.results[0].MEMB_ID_COUNT);
				  		$('#hiddenMembIdCount').val(rtn_data.results[0].MEMB_ID_COUNT);
			  		}
	      		}else{
	      			$('#membName').html("");
	      			$('#membPrepaidVal').html("");
	      			$('#membIdCount').html("");
	      			$('#hiddenMembName').val("");
	      			$('#hiddenMembPrepaidVal').val("");
			  		$('#hiddenMembGrade').val("");
	      		}
		 	},
    	});
	}else{
		$('#membName').html("");
	}
}
function checkdisabled(){
	if($("#checkdisabled").prop("checked")){
		$("#prepaidAmt").prop("disabled", false);
	}else{
		$("#prepaidAmt").prop("disabled", true);
	}
}

//取影片名稱
function getfilmName(){
	var index = fnGetObjIndex($(this)); 
	var filmId = $('#filmId_'+index).val();
	var membGrade = $("#hiddenMembGrade").val();
	if(filmId!= "" && membGrade!=""){
		$.ajax({
      		type: 'post',
	      	url:'<s:url value="/ajax/km/queryFilmName.action" />',
	      	data:{params: '{filmId: "' + filmId + '",membGrade: "' + membGrade + '"}'},
	      	success: function (rtn_data) {
	      		if(rtn_data.results[0] != "" && rtn_data.results[0] != null){
	      			$("#filmName_"+index).html(rtn_data.results[0].FILM_NAME);
	      			$("#rentAmt_"+index).html(rtn_data.results[0].RENT_AMT);
	      			$("#fmCorpName_"+index).html(rtn_data.results[0].FM_CORP_NAME);
	      			$("#filmGrade_"+index).html(rtn_data.results[0].FILM_GRADE);
	      			$("#filmGradeName_"+index).html(rtn_data.results[0].OPT_CDE_GRADE_NAM);
	      			
	      			$("#hiddenFilmName_"+index).val(rtn_data.results[0].FILM_NAME);
	      			$("#hiddenRentAmt_"+index).val(rtn_data.results[0].RENT_AMT);
	      			$("#hiddenFmCorpName_"+index).val(rtn_data.results[0].FM_CORP_NAME);
	      			$("#hiddenFilmGrade_"+index).val(rtn_data.results[0].FILM_GRADE);
	      			$("#hiddenFilmGradeName_"+index).val(rtn_data.results[0].OPT_CDE_GRADE_NAM);
	      			$("#hiddenFmCropId_"+index).val(rtn_data.results[0].FM_CROP_ID);
	      		}else{
	      			$("#filmName_"+index).html("");
	      			$("#rentAmt_"+index).html("");
	      			$("#fmCorpName_"+index).html("");
	      			$("#filmGrade_"+index).html("");
	      			$("#filmGradeName_"+index).html("");
	      			
	      			
	      			$("#hiddenFilmName_"+index).val("");
	      			$("#hiddenRentAmt_"+index).val("");
	      			$("#hiddenFmCorpName_"+index).val("");
	      			$("#hiddenFilmGrade_"+index).val("");
	      			$("#hiddenFilmGradeName_"+index).val("");
	      		}
		 	}
    	});
	}else{
		$("#filmName_"+index).html("");
		$("#rentAmt_"+index).html("");
		$("#fmCorpName_"+index).html("");
		$("#filmGrade_"+index).html("");
		$("#filmGradeName_"+index).html("");
	}
}
$(document).ready(function() {
	oTable = $('#tblGrid').initEditGrid({height:'480'});
	
	$('#btnInsRow').click(function() {
		fnAddTableRow(oTable, function(newRow) {
		});
		return false;
	});
	
	$('#membId').bind("change", getMembName);
	$(".AjaxfilmId").bind("change", getfilmName);
	$("#checkdisabled").bind("click",checkdisabled);
	
	// 單筆刪除
	$('.imgDelete').bind("click", function(event) {
		fnDelTableRow($(this), oTable);
		return false;
	});

	//多筆刪除
	$('#btnRmvRow').click(function() {
		fnDelAllRow(oTable);
		return false;
	});
});
</script>
</head>
<body>
<s:form id="frmExam0A031K" method="post" theme="simple" action="%{progAction}" target="ifrConfirm">
<s:hidden name="labMemberMst.ver" />
 	<div class="progTitle"> 
		<!-- 程式標題 --> <s:include value="/WEB-INF/pages/include/include.EditTitle.jsp" /> <!-- 程式標題 -->
    </div>
    <div id="tb">
    <table width="100%" border="0" cellpadding="4" cellspacing="0" >
		<tr class="trBgOdd">
			<td width="20%" class="colNameAlign required">*<s:text name="rentDate" />：</td>
			<td colspan="3" id="rentDate">
				<s:property value="labMemberMst.labMembRentHisList[0].rentDate" />
				<s:hidden name="labMemberMst.labMembRentHisList[0].rentDate"/>
			</td>
		</tr>
		<tr class="trBgEven">
			<td width="20%" class="colNameAlign required">*<s:text name="membId" />：</td>
			<td width="30%">
				<s:textfield id="membId" name="labMemberMst.membId" cssClass="enKey" size="16" maxlength="16" />
				<input type="image" id="imgMembId" class="imgPopUp" src="<s:url value="/image_icons/search.png"/>" />
				<s:label id="membName" name="labMemberMst.membName" />
				<s:hidden id="hiddenMembName" name="labMemberMst.membName" />
				<s:hidden id="hiddenMembGrade" disabled="true" />
			</td>
			<td width="20%" class="colNameAlign"><s:text name="exam.00002" />：</td>
			<td width="30%">
				<s:label id="membIdCount" name="labMemberMst.labMembRentHisCount" />
				<s:hidden id="hiddenMembIdCount" name="labMemberMst.labMembRentHisCount" />
			</td>
		</tr>
		<tr class="trBgOdd">
			<td width="20%" class="colNameAlign">&nbsp;<s:text name="membPrepaidVal" />：</td>
			<td width="30%">
				<s:label id="membPrepaidVal" name="labMemberMst.membPrepaidVal"/>
				<s:hidden id="hiddenMembPrepaidVal" name="labMemberMst.membPrepaidVal"/>
			</td>
			<td width="20%" class="colNameAlign">&nbsp;<s:text name="prepaidAmt" />：</td>
			<td width="30%">
				<input type="checkbox" id="checkdisabled">
				<s:textfield id="prepaidAmt" name="labMemberMst.labMemberPrepaidHis[0].prepaidAmt" maxlength="8" size="20"  cssClass="numKey" disabled="true" />
				<s:hidden id="oddPrepaidAmt" disabled="true" />
			</td>
		</tr>
	</table>
    <fieldset style="-moz-border-radius:4px;">
    <div style="width:100%; display:block; float:left; background-color:#b7d3d6">
        <button class="btnInsRow" id="btnInsRow"><s:text name="fix.00255" /></button>
        <button class="btnRmvRow" id="btnRmvRow"><s:text name="fix.00256" /></button>
    </div>
    <table id="tblGrid" width="100%" border="0" cellpadding="2" cellspacing="1">
        <thead>
            <tr align="center" bgcolor="#e3e3e3">
                <th width="30"><s:text name="fix.00164" /></th>
                <th width="20">&nbsp;</th>
                <th width="28%"><s:text name="filmId" /></th>
                <th width="15%"><s:text name="rentAmt" /></th>
                <th width="15%"><s:text name="revertDate" /></th>
                <th width="15%"><s:text name="fmCorpName" /></th>
                <th><s:text name="filmGrade" /></th>
                <th style="display: none;">&nbsp;</th>          
            </tr>
        </thead>
		<tbody id="tbGrid">
	        <s:iterator value="labMemberMst.labMembRentHisList" status="status">
	        <tr>
	            <td id="SN" align="center" align="center" width="30"><s:property value="#status.index + 1" /></td>
	            <td align="center" width="20">
	                <input class="imgDelete" type="image" src="<s:url value="/image_icons/delete.png"/>" width="16" height="16" title="<s:text name="fix.00182"/>">
	            </td>
	            <td width="28%">
	                <s:textfield id="%{'filmId_'+ #status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmId'}" value="%{labMemberMst.labMembRentHisList[#status.index].labFilmMst.filmId}" cssClass="enKey AjaxfilmId"  maxLength="32" size="16" />
	                <input type="image" id="%{'imgfilmId'+#status.index}" class="programPopUp imgfilmId" src="<s:url value="/image_icons/search.png"/>" />
	            	<s:label id="%{'filmName_'+#status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmName'}" value="%{labMemberMst.labMembRentHisList[#status.index].labFilmMst.filmName}" cssClass="labelCut"/>
	            	<s:hidden id="%{'hiddenFilmName_'+#status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmName'}" value="%{labMemberMst.labMembRentHisList[#status.index].labFilmMst.filmName}" />
	            </td>
	            <td width="15%" align="right">
	            	<s:label id="%{'rentAmt_'+#status.index}" name="%{labMemberMst.labMembRentHisList['+#status.index+'].rentAmt}" value="%{labMemberMst.labMembRentHisList[#status.index].rentAmt}" cssClass="labelCut" />
	            	<s:hidden id="%{'hiddenRentAmt_'+#status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].rentAmt'}" value="%{labMemberMst.labMembRentHisList[#status.index].rentAmt}" />
	            </td>
	            <td width="15%" align="center">
	            	<s:textfield id="%{'extReturnDate_'+ #status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].extReturnDate'}" value="%{labMemberMst.labMembRentHisList[#status.index].extReturnDate}" cssClass="inputDate" maxlength="10" size="16"  />
	            </td>
	            <td width="15%">
	            	<s:label id="%{'fmCorpName_' + #status.index}" name="%{labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.labFilmCorpMst.fmCorpName}" value="%{labMemberMst.labMembRentHisList[#status.index].labFilmMst.labFilmCorpMst.fmCorpName}" cssClass="labelCut" />
	            	<s:hidden id="%{'hiddenFmCorpName_'+#status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.labFilmCorpMst.fmCorpName'}" value="%{labMemberMst.labMembRentHisList[#status.index].labFilmMst.labFilmCorpMst.fmCorpName}" />
	            </td>
	            <td align="right">
	            	<s:label id="%{'filmGrade_' + #status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmGrade'}" value="%{labMemberMst.labMembRentHisList[#status.index].labFilmMst.filmGrade}" cssClass="labelCut" />
	            	<s:label id="%{'filmGradeName_' + #status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmgrade.optCdeNam'}"  value="%{labMemberMst.labMembRentHisList[#status.index].labFilmMst.filmgrade.optCdeNam}" cssClass="labelCut" />
	            	<s:hidden id="%{'hiddenFilmGrade_' + #status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmGrade'}" />
	            	<s:hidden id="%{'hiddenFilmGradeName_' + #status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmgrade.optCdeNam'}" />
	            </td>
	            <td style="display: none;">
	            	<s:hidden id="%{'operate_' + #status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].operate'}"/>
		            <s:hidden id="%{'ver_' + #status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].ver'}"/>
	                <s:hidden id="%{'membRentOid_' + #status.index}" name="%{'labMemberMst.labMembRentHisList['+#status.index+'].membRentOid'}" />
	            </td>
	        </tr>
	        </s:iterator>   
		</tbody>
    </table>
    </fieldset>
    </div>
	<!-- 按鍵組合 --> 
	<s:include value="/WEB-INF/pages/include/include.EditButton.jsp" />
	<!-- 按鍵組合 -->
</s:form>
<iframe id="ifrConfirm" name="ifrConfirm" width="100%" height="768" frameborder="0" marginwidth="0" marginheight="0" scrolling="no" style="display:none; border: 0px none"></iframe>
</body>
</html>