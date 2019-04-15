<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link href="${pageContext.request.contextPath}/assets/css/bootstrap-table.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/assets/css/pagination.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/assets/js/bootstrap-table.min.js"></script>
<script>
$(document).ready(function () {
	var run_id=document.getElementById("run_id").value;
	$("#tbl_id").change(function() {
		disableForm(f1);
		$('#loading').show();
		var tbl_id = $(this).val();
		var feed_id = document.getElementById('feed_id').value;	
		var run_id = document.getElementById('run_id').value;	
		var sdate = document.getElementById('sdate').value;	
		var edate = document.getElementById('edate').value;	
		$.post('${pageContext.request.contextPath}/hip/tableIdFilter',{
				feed_id : feed_id,
				run_id : run_id,
				tbl_id : tbl_id,
				sdate : sdate,
				edate : edate
					},function(data) {
						$('#loading').hide();
						enableForm(f1);
						$('#tableChartDuration').html(data);
						var x = document.getElementById("tbl_x").value;
						var y = document.getElementById("tbl_y").value;
						var newx = x.split(',');
						var newy = y.split(',');
						newx[0] = newx[0].replace("[","");
						newy[0] = newy[0].replace("[","")
						newx[newx.length - 1] = newx[newx.length - 1].replace("]","");
						newy[newy.length - 1] = newy[newy.length - 1].replace("]","");
						var areaData = {
							labels : newx,
							datasets : [ {
								label : 'Duration of Feed in Minutes',
								data : newy,
								backgroundColor : [
										'rgba(54, 162, 235, 0.2)',
										'rgba(255, 206, 86, 0.2)',
										'rgba(75, 192, 192, 0.2)',
										'rgba(153, 102, 255, 0.2)',
										'rgba(255, 159, 64, 0.2)',
										'rgba(255, 99, 132, 0.2)' ],
								borderColor : [
										'rgba(54, 162, 235, 1)',
										'rgba(255, 206, 86, 1)',
										'rgba(75, 192, 192, 1)',
										'rgba(153, 102, 255, 1)',
										'rgba(255, 159, 64, 1)' ],
								borderWidth : 1,
								fill : true, 
							} ]
						};
						var areaOptions = {
							plugins : {
								filler : {
									propagate : true
								}
							}
						}
						if ($("#tbl_areaChartDuration").length) {var areaChartCanvas = $("#tbl_areaChartDuration").get(0).getContext("2d");
							var areaChart = new Chart(
									areaChartCanvas,
									{
										type : 'line',
										data : areaData,
										options : areaOptions
									});
						}
						var x1 = document.getElementById("tbl_x").value;
						var y1 = document.getElementById("tbl_z").value;
						var newx1 = x1.split(',');
						var newy1 = y1.split(',');
						newx1[0] = newx1[0].replace("[","");
						newy1[0] = newy1[0].replace("[","")
						newx1[newx1.length - 1] = newx1[newx1.length - 1].replace("]","");
						newy1[newy1.length - 1] = newy1[newy1.length - 1].replace("]","");
						var areaData = {
							labels : newx1,
							datasets : [ {
								label : 'Count of Feed in numbers',
								data : newy1,
								backgroundColor : [
										'rgba(255, 99, 132, 0.2)',
										'rgba(54, 162, 235, 0.2)',
										'rgba(255, 206, 86, 0.2)',
										'rgba(75, 192, 192, 0.2)',
										'rgba(153, 102, 255, 0.2)',
										'rgba(255, 159, 64, 0.2)',
										'rgba(255, 99, 132, 0.2)' ],
								borderColor : [
										'rgba(255, 159, 64, 1)',
										'rgba(255,99,132,1)',
										'rgba(54, 162, 235, 1)',
										'rgba(255, 206, 86, 1)',
										'rgba(75, 192, 192, 1)',
										'rgba(153, 102, 255, 1)',
										'rgba(255, 159, 64, 1)' ],
								borderWidth : 1,
								fill : true, // 3: no fill
							} ]
						};

						var areaOptions = {
							plugins : {
								filler : {
									propagate : true
								}
							}
						}

						if ($("#tbl_areaChartCount").length) {var areaChartCanvas = $("#tbl_areaChartCount").get(0).getContext("2d");
							var areaChart = new Chart(
									areaChartCanvas,
									{
										type : 'line',
										data : areaData,
										options : areaOptions
									});
						}		
				}).fail(function() {
					alert("System error: Something went wrong");
					$('#loading').hide();
				});
;

	
	})
	
});
</script>
<br><br>
<input type="hidden" id="sdate" value='${sdate}' name="sdate"/>
<input type="hidden" id="edate" value='${edate}'name="edate"/>

<c:forEach items="${hm}" var="myMap">

<div class="col-md-12">
<p style="text-align: center;color:blue;"> Target - ${myMap.key} </p>
</div>

<div class="col-md-12">
				<table id="dashboard1" 
			class="table table-hover table-sm table-striped table-bordered shadow p-3 mb-5 bg-white rounded table-condensed"
			data-show-header="true"
			data-classes="table table-hover table-striped table-sm table-bordered shadow p-3 mb-5 bg-white rounded table-condensed"
			 data-toggle="table"  
			data-striped="true"
		    data-sort-order="desc"
		   	data-pagination="true"  
		   	data-id-field="name2" 
		   	data-page-size="5"
		   	data-page-list="[5, 10, 25, 50, 100, ALL]" > 
	             <thead>
	              <tr>
	              <th data-sortable="true" onclick="myFunction(this)"  >
	                  Table/File Name
	                </th>
	                 <th data-sortable="true" >
	                  File Date
	                </th>
	                <th data-sortable="true" >
	                  Source Counts/Size
	                </th>
	                <th data-sortable="true" >
	                  Target Counts/Size
	                </th>
	                <th>
	                  Status
	                </th>
	                
	              </tr>
	            </thead>
                <tbody>
               <c:forEach var="row" items="${myMap.value}">
              
                 <tr >
                 	<td ><c:out value="${row.table_name}" /></td>
                 	<td ><c:out value="${row.file_date}" /></td>
                 	<td ><c:out value="${row.src_count}" /></td>
                 	<td ><c:out value="${row.tgt_count}" /></td>
				 <c:if test="${row.src_count eq row.tgt_count}">	
				 	<td > <img src="${pageContext.request.contextPath}/assets/img/smileygreen.jpg" alt="smiley1" border=3 height=10px width=10px></img> </td>
				  </c:if>	
				  <c:if test="${row.tgt_count ne 'NA' and row.src_count ne row.tgt_count}">
				  	<td ><img src="${pageContext.request.contextPath}/assets/img/smiley2.jpg" alt="smiley2" border=3 height=10px width=10px></img> </td>
				  </c:if>	
				  	 <c:if test="${row.tgt_count eq 'NA'}">
				  	 <td ><c:out value="Target counts not yet written"/></td>
				  	 </c:if>
				</tr>
				
				 
				 
				 
                 
				 
             </c:forEach>
                  
                </tbody>
        	</table>
        	</div>
</c:forEach>

<br>
<c:if test="${src_flag eq '1'}">
<div class="form-group" style="display:none"> 
</c:if>
<c:if test="${src_flag ne '0'}">
<div class="form-group"> 
</c:if>		
			<div class="col-md-12">		
			<h1 align="center" style="text-shadow:2px 2px #6c757d">TABLE DASHBOARD</h1>
			</div>
		<br>	
<input type="hidden" id="tbl_plt_id2" name="tbl_plt_id2" value="${plt_id}">
<input type="hidden" id="tbl_feed_id2" name="tbl_feed_id2" value="${tbl_feed_id}"> 
<input type="hidden" id="run_id" name="run_id" value="${run_id}">  
<div class="col-md-12 ">

<div class="form-group row">
	 <div class="col-md-3">
		<label class=" form-check-label" style="color: black; text-shadow: -1px 0 black">
            Select Table :
       	</label></div>
        <div class="col-md-8">
		<select name="tbl_id" id="tbl_id" class="form-control" style="color:black;font-weight:bold;">
			<option value="" selected disabled>Select table...</option>
			<c:forEach items="${table_list}" var="tbl">
				<option value="${tbl}">${tbl}</option>
			</c:forEach>
		</select>
		</div>
</div>
</div>
	
	<div id="tableChartDuration" ></div>
</div>