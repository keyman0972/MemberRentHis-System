<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/pages/include/include.Taglib.jsp"%>
<html>
<head>
<title></title>
<s:include value="/WEB-INF/pages/include/include.Scripts.jsp" />
<script type="text/javascript" src="<s:url value="/jquery/ui/jquery.ui.datepicker.min.js"/>"></script>
<script type="text/javascript" src="<s:url value="/jquery/jquery.alphanumeric.js"/>"></script>
<script type="text/javascript" src="<s:url value="/ddscPlugin/ddsc.gridList.plugin.js"/>"></script>
<script type="text/javascript" src="<s:url value="/ddscPlugin/ddsc.popupWindow.plugin.js"/>"></script>	
<script type="text/javascript" src="<s:url value="/js/ddsc.input.js"/>"></script>
<script type="text/javascript">
function validate(actType) {
	<%--
	if(actType.startsWith("search")){
		if($("#rentDate").val() == ""){
			alert('<s:text name="rentDate"><s:param value="getText(\"fix.00819\")"/></s:text>');
			return;
		}
	}
	 --%>
}
function getParameter(actType) {	
	var param = "labMemberMst.membId=" + $("#tblGrid").getSelectedRow().find('td').eq(2).find("#membId").text();
	param += "&labMemberMst.labMembRentHisList.rentDate=" + $("#tblGrid").getSelectedRow().find('td').eq(4).text();
	return param;
}
$(document).ready(function() {
	$("#tblGrid").initGrid({lines:3});
	$('#tb').initPopupWindow({dailogWidth:'960', dailogHeight:'640'});
	

});
</script>
</head>
<body> 
<s:form id="frmExam0A031K" theme="simple" action="%{progAction}" >
	<div class="progTitle">
  		<s:include value="/WEB-INF/pages/include/include.Title.jsp" />
	</div>
	<div id="tb">
		<fieldset id="listFieldset">
		<table width="100%" border="0" cellpadding="2" cellspacing="0">
			<tr class="trBgOdd">
				<td width="20%" class="colNameAlign">&nbsp;<s:text name="rentDate"/>：</td>
				<td width="30%"><s:textfield id="rentDate" name="beforeTime" cssClass="inputDate" maxlength="10" size="16" /></td>
				<td width="20%" class="colNameAlign">&nbsp;<s:text name="filmId"/>：</td>
				<td width="30%"><s:textfield name="labMemberMst.labMembRentHisList[0].labFilmMst.filmId" maxlength="16" size="32"/></td>
			</tr>
			<tr class="trBgEven">
				<td width="20%" class="colNameAlign">&nbsp;<s:text name="membId"/>：</td>
				<td width="30%"><s:textfield name="labMemberMst.membId" maxlength="16" size="16" /></td>
				<td width="20%" class="colNameAlign">&nbsp;<s:text name="membName"/>：</td>
				<td width="30%"><s:textfield name="labMemberMst.membName" maxlength="64" size="32"/></td>
			</tr>
		</table>
		<!-- 按鍵組合 --><s:include value="/WEB-INF/pages/include/include.ListButton.jsp" /><!-- 按鍵組合 --> 
		</fieldset>
		<table id="tblGrid" width="100%" border="0" cellpadding="2" cellspacing="1">
			<thead>
				<tr align="center" bgcolor="#e3e3e3">
					<th width="3%"><s:text name="fix.00164" /></th>
					<th width="10%"><s:text name="fix.00090" /></th>
					<th width="12%"><s:text name="membName" /></th>   
					<th width="12%"><s:text name="exam.00002" /></th> 
					<th width="12%"><s:text name="rentDate" /></th>
					<th width="12%"><s:text name="filmName" /></th>
					<th width="12%"><s:text name="rentAmt" /></th>
					<th width="12%"><s:text name="revertDate" /></th>
					<th><s:text name="fix.00114" /></th>
				</tr>
			 </thead>
			 <tbody>
				 <s:iterator value="labMembRentHisListMap" status="status">
				 	<tr>
						<td width="3%" id="sn" align="center"><s:property value="#status.index+1" /></td>
						<!-- 表單按鍵 --> 
						<td width="10%"><s:include value="/WEB-INF/pages/include/include.actionButton.jsp" /></td>
						<!-- 表單按鍵 -->
						<td width="12%"><label id="membId"><s:property value="MEMB_ID" /></label>&nbsp;-&nbsp;<label><s:property value="MEMB_NAME" /></label></td>
						<td width="12%" align="right"><label><s:property value="MEMB_ID_COUNT" /></label></td>	
						<td width="12%" align="center"><label><s:property value="RENT_DATE" /></label></td>
						<td width="12%"><label><s:property value="FILM_ID" />&nbsp;-&nbsp;<s:property value="FILM_NAME" /></label></td>
						<td width="12%"><label><s:property value="RENT_AMT" /></label></td>
						<td width="12%"><label><s:property value="EXT_RETURN_DATE" /></label></td>
						<td>
							<label>
								<s:if test="nowTime > EXT_RETURN_DATE">
									<s:text name="fix.03474" />
								</s:if><s:else>
									<s:text name="&nbsp;-&nbsp;" />
								</s:else>
							</label>
						</td>
					</tr>
				 </s:iterator>
			 </tbody>
		</table>
	</div>
	<!-- 分頁按鍵列 --><s:include value="/WEB-INF/pages/include/include.PaginationBar.jsp" /><!-- 分頁按鍵列 -->
</s:form>
</body>
</html>