<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/include.Taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<base target="_self" />
<s:include value="/WEB-INF/pages/include/include.Scripts.jsp" />
<script type="text/javascript" src="<s:url value="/ddscPlugin/ddsc.gridList.plugin.js"/>"></script>
<script language="javascript">
$(document).ready(function() {
    $("#tblGrid").initGrid({height:'480'});   
});
</script>
</head>
<body>
<s:form method="post" theme="simple" action="%{progAction}">
	<s:hidden name="labMemberMst.ver" />
	<div class="progTitle"> 
       <!-- 程式標題 --> <s:include value="/WEB-INF/pages/include/include.ConfirmTitle.jsp" /> <!-- 程式標題 -->
    </div>
    <div id="tb">
    <table width="100%" border="0" cellpadding="4" cellspacing="0" >
		<tr class="trBgOdd">
			<td width="20%" class="colNameAlign required">*<s:text name="orderDate" />：</td>
			<td colspan="3">
				<s:property value="labMemberMst.labMembRentHisList[0].rentDate" />
				<s:hidden name="labMemberMst.labMembRentHisList[0].rentDate"/>
			</td>
		</tr>
		<tr class="trBgEven">
			<td width="20%" class="colNameAlign required">*<s:text name="custId" />：</td>
			<td width="30%">
				<s:property value="labMemberMst.membId" />&nbsp;-&nbsp;
				<s:property value="labMemberMst.membName" />
				<s:hidden name="labMemberMst.membId"/>
				<s:hidden name="labMemberMst.membName"/>
			</td>
			<td width="20%" class="colNameAlign">&nbsp;<s:text name="exam.00002" />：</td>
			<td width="30%">
				<s:label id="membIdCount" value="%{labMemberMst.labMembRentHisCount}" />
				<s:hidden id="hiddenmembIdCount" name="labMemberMst.labMembRentHisCount" />
			</td>
		</tr>
		<tr class="trBgOdd">
			<td width="20%" class="colNameAlign">&nbsp;<s:text name="membPrepaidVal" />：</td>
			<td width="30%">
				<s:property value="labMemberMst.membPrepaidVal"/>
				<s:hidden name="labMemberMst.membPrepaidVal"/>
			</td>
			<td width="20%" class="colNameAlign">&nbsp;<s:text name="prepaidAmt" />：</td>
			<td width="30%">
				<s:property value="labMemberMst.labMemberPrepaidHis[0].prepaidAmt" />
				<s:hidden name="labMemberMst.labMemberPrepaidHis[0].prepaidAmt" />
			</td>
		</tr>
	</table>
	<table width="100%" border="0" cellpadding="4" cellspacing="0" >
		<tbody>
		</tbody> 
    </table>
    <fieldset style="-moz-border-radius:4px;">
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
		<tbody id="tbGrid">
			<s:iterator value="labMemberMst.labMembRentHisList" status="status">
	        <tr>
	            <td id="SN" align="center" width="30"><s:property value="#status.index + 1" /></td>
	            <td align="center" width="20">
	                <label>
	                    <s:if test="operate eq \"insert\""><s:text name="fix.00001"/></s:if>
	                    <s:elseif test="operate eq \"update\""><s:text name="fix.00185"/></s:elseif>
	                    <s:elseif test="operate eq \"delete\""><s:text name="fix.00182"/></s:elseif>
	                    <s:else>&nbsp;</s:else>
	                </label>
	            </td>
	            <td width="28%">
	            	<s:property value="labMemberMst.labMembRentHisList[#status.index].labFilmMst.filmId"/>
	            	<s:property value="labMemberMst.labMembRentHisList[#status.index].labFilmMst.filmName"/>
               		<s:hidden  name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmId'}"/>
               		<s:hidden name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmName'}"/>
	            </td>
	            <td width="15%" align="right">
		            <s:property value="labMemberMst.labMembRentHisList[#status.index].rentAmt"/>
	            	<s:hidden name="%{'labMemberMst.labMembRentHisList['+#status.index+'].rentAmt'}" />
	            </td>
	            <td width="15%" align="center">
		            <s:property value="labMemberMst.labMembRentHisList[#status.index].extReturnDate"/>
	            	<s:hidden name="%{'labMemberMst.labMembRentHisList['+#status.index+'].extReturnDate'}" />
	            </td>
	            <td width="15%">
		            <s:property value="labMemberMst.labMembRentHisList[#status.index].labFilmMst.labFilmCorpMst.fmCorpName"/>
		            <s:hidden name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.labFilmCorpMst.fmCorpName'}" />
	            </td>
	            <td>
		            <s:label value="%{labMemberMst.labMembRentHisList[#status.index].labFilmMst.filmGrade}" />
		            <s:hidden name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmGrade'}"  />
		            <s:label value="%{labMemberMst.labMembRentHisList[#status.index].labFilmMst.filmgrade.optCdeNam}" />
		            <s:hidden name="%{'labMemberMst.labMembRentHisList['+#status.index+'].labFilmMst.filmgrade.optCdeNam'}"  />
		            
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
        <s:include value="/WEB-INF/pages/include/include.ConfirmButton.jsp" /> 
    <!-- 按鍵組合 -->
</s:form>
</body>
</html>