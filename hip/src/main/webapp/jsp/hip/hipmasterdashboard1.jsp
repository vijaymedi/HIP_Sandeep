<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/multi.min.css">
<link href="${pageContext.request.contextPath}/assets/css/pagination.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/assets/css/bootstrap-table.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/assets/js/bootstrap-table.min.js"></script>
<script>
$(document).ready(function () {
	var feed_id='${feed_id}'
	document.getElementById('feed_id').value=feed_id;
	$('#clear').click(function() {
		location.reload();
	});
	$("#datepicker1").change(function() {
		document.getElementById("datepicker2").value=null;
	});
	
	$("#datepicker2").change(function() {
		
		disableForm(f1);
		$('#loading').show();
					
					
					$("#info-alert1").hide();
					
					if(document.getElementById("datepicker1").value < document.getElementById("datepicker2").value){
						if(document.getElementById("datepicker1").value=="")
						{
						 alert("START DATE equals END DATE");
						 var date1 = document.getElementById("datepicker2").value;
						}
						else
						{
							var date1 = document.getElementById("datepicker1").value;
						}
					var date2 = document.getElementById("datepicker2").value;
					var date = date1 + " - "+ date2;
					var src_flag= document.getElementById('src_flag').value;
					$("#second").hide();
					if (feed_id != '' && date != '') {
					$.post('${pageContext.request.contextPath}/hip/datefilter',
					{
						feed_id : feed_id,
						date : date,
						src_flag : src_flag
					},
					function(data) {
						$('#loading').hide();
						enableForm(f1);
						$('#adddetails2').html(data);
						var x = document.getElementById("x").value;
						var y = document.getElementById("y").value;
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
									'rgba(255, 99, 132, 0.2)', ],
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
						if ($("#areaChart1").length) {
							var areaChartCanvas = $("#areaChart1").get(0).getContext("2d");
							var areaChart = new Chart(areaChartCanvas,
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
					}
					}else{
						alert("START DATE IS GREATER THAN END DATE");
						$('#loading').hide();
						enableForm(f1);
					}
				
	});									
	 $("#run_id").change(function() {
		 
		 disableForm(f1);
			$('#loading').show();
			var run_id = $(this).val();
			var feed_id = document.getElementById('feed_id').value;	
			var src_flag= document.getElementById('src_flag').value;
			var sdate = document.getElementById('sdate').value;	
			var edate = document.getElementById('edate').value;	
			$.post('${pageContext.request.contextPath}/hip/tablerecon',{
					feed_id : feed_id,
					run_id : run_id,
					src_flag : src_flag,
					sdate : sdate,
					edate : edate
						},function(data) {
							$('#loading').hide();
							enableForm(f1);
							$('#tblreconstats').html(data);
			}).fail(function() {
				alert("System error: Something went wrong");
				$('#loading').hide();
			});
;
	 });
	
});


//function showtbl()
//{
//	document.getElementById('t1').style.display="block";
//	document.getElementById('t2').style.display="block";
//	document.getElementById('t3').style.display="none";
//	document.getElementById('t4').style.display="block";
	
//}

//function hidetbl()
//{
	
//	document.getElementById('t1').style.display="block";
//	document.getElementById('t2').style.display="none";
//	document.getElementById('t3').style.display="block";
//	document.getElementById('t4').style.display="none";
	
//}
	
</script>

<div id="first">

<c:set var="classif" scope="page" value="" />


<fieldset class="fs" >
<c:forEach items="${feed}" var="feed">
<c:choose>
		<c:when test="${classif eq ''}">
			<c:set var="classif" scope="page" value="${feed.classification}" />

			<div class="row">
			
			<div class="col-md-12">			
			<h1 align="center" style="text-shadow:2px 2px #6c757d">FEED MASTER STATISTICS</h1>
			</div>
			
			</div>

			
			<div class="row">
				<div class="col-md-4 stretch-card" style="padding:8px;">
					<div class="card bg-gradient-info card-img-holder text-white">
						<div class="card-body" style="overflow-x: scroll; position:relative">
							<img src="/assets/img/circle.svg" class="card-img-absolute"
								alt="circle-image" />
							<c:if test="${'Table' eq feed.classification}">
									<h2 class="font-weight-normal mb-3">Tables/Files</h2>
								</c:if>
								<c:if test="${'Table' ne feed.classification}">
									<h2 class="font-weight-normal mb-3">${feed.classification}</h2>
								</c:if>
							<table border="0" style="font-size:100%;">
						<tr><td style="text-align:left;">${feed.subclassification}</td><td>:</td><td  style="text-align:left;">${feed.value}</td></tr>
		</c:when>
		<c:when test="${classif eq feed.classification}">
			<c:set var="classif" scope="page" value="${feed.classification}" />
			<c:choose>
			<c:when test="${feed.classification == 'Table' }"></c:when>
			<c:otherwise><tr><td style="text-align:left;">${feed.subclassification}</td><td>:</td><td  style="text-align:left;">${feed.value}</td></tr></c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<c:set var="classif" scope="page" value="${feed.classification}" />
			</table>
			</div>
			</div>
			</div>
				<div class="col-md-4 stretch-card" style="padding:10px;">
				<div class="card bg-gradient-info card-img-holder text-white">
						<div class="card-body" style="overflow-x: scroll; position:relative">
							<img src="/assets/img/circle.svg" class="card-img-absolute"
								alt="circle-image" />
							<c:if test="${'Table' eq feed.classification}">
									<h2 class="font-weight-normal mb-3">Tables/Files</h2>
								</c:if>
								<c:if test="${'Table' ne feed.classification}">
									<h2 class="font-weight-normal mb-3">${feed.classification}</h2>
								</c:if>
							<table border="0" style="font-size:100%;">
							<c:choose>
								 <c:when test="${feed.classification == 'Table' }">
								 	<tr id="t1">
								 <td style="text-align:left;">Count</td><td>:</td><td style="text-align:left;">${tbl_sz}</td>
								</tr>
								<tr>
								<td colspan="10" style="height: 20px;"></td></tr>
								 <tr id="t3" style="display:none;position:relative;">
								  <td onclick="showtbl() " style="text-decoration:underline;font-weight:bold; cursor:pointer;">Show Tables/Files</td> 
								 <table id="t4" border="0" style="font-size:1.3em; display:block; position: relative; height:70px;">
								 <c:forEach items="${tbls}" var="tbl">
								 	<tr style="font-size: small;" >
								 		<td>${tbl}</td>
								 	</tr>
								 	 </c:forEach>
								 </table>
								
								 </tr>
								 </c:when>
			   					 <c:otherwise><tr><td style="text-align:left;">${feed.subclassification}</td><td>:</td><td  style="text-align:left;">${feed.value}</td></tr></c:otherwise>
			
														</c:choose>
		</c:otherwise>
	</c:choose>
</c:forEach>
</table>
</div>
</div>
</div>


</div>




</fieldset>


<c:choose>
     <c:when test="${fn:length(arrHipDashboard) gt 0}"> 
     
 <br><br>
 
<input type="hidden" id="feed_id" name="feed_id" value="${feed_id}">
<input type="hidden" id="src_flag" name="src_flag" value="${src_flag}">
<input type="hidden" id="sdate" value='${sdate}' name="sdate"/>
<input type="hidden" id="edate" value='${edate}'name="edate"/>


<div class="col-md-12">    
<div class=" form-row align-items-center" >
			<div class="col-md-3 " >
				<label class="form-check-label" style="color: black; text-shadow: -1px 0">
					Date Range :
				</label>
			</div>
			<div class="col-md-9 " >
				<div class="input-group">
					<input id="datepicker1" name="datepicker1" type="date" class="form-control" style="border-color: #428bca; border-width: 3px" >
					<input id="datepicker2" name="datepicker2" type="date" class="form-control" style="border-color: #428bca; border-width: 3px">
				</div>	
			</div>
			
			</div>

<div class="alert alert-info col-md-12" id="info-alert1">
    		 	<p>	The below statistics are shown for a default range of last 60 days.
    		 		Please select a date range to filter!
    		 		
    		
</div>
</div>
 <br><br>

 
<div id="adddetails2"></div>
 
<div id="second">
<fieldset class="fs">
		<br>
		<div class="row" >
			<div class="col-md-12">			
				<h1 align="center" style="text-shadow:2px 2px #6c757d">FEED RUN STATISTICS</h1>
			</div>
		</div>
	<br>
	<input type="hidden" id="x" value='${x}' name="x"/>
	<input type="hidden" id="y" value='${y}'name="y"/>			

<div class="form-group row">
	<div class="col-md-3" ></div>
	<div class="col-md-6" >
		<h4 align="center">Technical Metadata with Business Glossary</h4>
	</div>
	<div class="col-md-3" ></div></div>
	<div class="row">
		
		<div class="col-md-12" >
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
	                  Run Id
	                </th>
	                <th data-sortable="true" >
	                  Table Name
	                </th>
	                <th data-sortable="true"  >
	                  Where Clause
	                </th>           
	                <th data-sortable="true"  >
	                  Interface Id 
	                </th>
	                <th data-sortable="true"  >
	                 Logical File Id
	                </th>         
	                <th data-sortable="true"  >
	                  Logical File Name
	                </th>
	                <th data-sortable="true" >
	                  Logical File Description
	                </th>         
	                <th data-sortable="true" >
	                  Data Categorization Code
	                </th>
	                <th data-sortable="true"  >
	                  Data Categorization Indicator
	                </th>         
	                <th data-sortable="true" >
	                  Masking Indicator
	                </th>
	                       	
	              </tr>
	            </thead>
                <tbody>
               <c:forEach var="row" items="${arr}">
                 <tr>
                 	<td ><c:out value="${row.run_id}" /></td>
                 	<td ><c:out value="${row.table_name}" /></td>
					<td class="cust"><c:out value="${row.where_clause}" /></td>	
					<td class="cust"><c:out value="${row.interface_id}" /></td>
                 	<td class="cust"><c:out value="${row.log_file_id}" /></td>
					<td class="cust"><c:out value="${row.log_file_name}" /></td>
					<td class="cust"><c:out value="${row.log_file_desc}" /></td>
                 	<td class="cust"><c:out value="${row.data_cat_code}" /></td>
					<td class="cust"><c:out value="${row.data_cat_indicator}" /></td>
					<td class="cust"><c:out value="${row.masking_indicator}" /></td>				
				</tr>
             </c:forEach>
                  
                </tbody>
        	</table>
        	</div>
	</div>
<br><br>




	
<div class="form-group row">
	<div class="col-md-3" ></div>
	<div class="col-md-6" >
		<h4 align="center" >Duration of Feed in Minutes</h4>
	</div>
	<div class="col-md-3" ></div>
</div>	
<div class="container">
	    <div class="row">
	    <div class="col-md-2"></div>
	      <div class="col-md-3">
	      <div class="card-counter success">
	        <i class="fa fa-ticket"></i>
	        <span class="count-numbers">${min}</span>
	        <span class="count-name">Minimum</span>
	      </div>
	    </div>
		 <div class="col-md-3">
	      <div class="card-counter primary">
	        <i class="fa fa-code-fork"></i>
	        <span class="count-numbers">${average}</span>
	        <span class="count-name">Average</span>
	      </div>
	    </div>
		
	    <div class="col-md-3">
	      <div class="card-counter danger">
	        <i class="fa fa-database"></i>
	        <span class="count-numbers">${max}</span>
	        <span class="count-name">Maximum</span>
	      </div>
	    </div>
	    <div class="col-md-1"></div>
	</div>
  </div>	
	<div class="row">
		<div class="col-md-2" ></div>
		<div class="col-md-8" >
			<div class="col-md- grid-margin stretch-card" >
			<canvas id="areaChart" style="width:100%"></canvas>
	    </div>
		</div>
		<div class="col-md-2" ></div>
	</div>
	<br>
	<div class="row">
		<div class="col-md-1" ></div>
		<div class="col-md-10" >
			<table id="dashboard" 
			class="table table-hover table-sm table-striped table-bordered shadow p-3 mb-5 bg-white rounded table-condensed"
			data-show-header="true"
			data-classes="table table-hover table-striped table-sm table-bordered shadow p-3 mb-5 bg-white rounded table-condensed" 
			data-toggle="table"  
			data-striped="true"
		    data-sort-name="Feed Id"
		    data-sort-order="desc"
		   	data-pagination="true"  
		   	data-id-field="name" 
		   	data-page-size="5"
		   	data-page-list="[5, 10, 25, 50, 100, ALL]" >
	             <thead>
	              <tr>
	              <th data-sortable="true" onclick="myFunction(this)">
	                  Feed Id
	                </th>
	                <th data-sortable="true">
	                  Run Date
	                </th>
	                <th data-sortable="true">
	                 Run Id
	                </th>
	                <th data-sortable="true">
	                 Start Time
	                </th>
	            	<th data-sortable="true">
	                 End Time
	                </th>
	                <th data-sortable="true">
	                Duration
	                </th>
	              </tr>
	            </thead>
                <tbody>
               <c:forEach var="row" items="${arrHipDashboard}">
                 <tr>
                 	<td><c:out value="${row.batch_id}" /></td>
                 	<td><c:out value="${row.batch_date}" /></td>
					<td><c:out value="${row.run_id}" /></td>
					<td><c:out value="${row.start_time}" /></td>
					<td><c:out value="${row.end_time}" /></td>
					<td><c:out value="${row.duration}" /></td>
				</tr>
             </c:forEach>
                  
                </tbody>
        	</table>
        	</div>
		<div class="col-md-1" ></div>
	</div>




<!-- <div class="form-group row">
	<div class="col-md-3" ></div>
	<div class="col-md-6" >
		<h4 align="center">Failure Statistics</h4>
	</div>
	<div class="col-md-3" ></div></div>
	<div class="row">
		<div class="col-md-3" ></div>
		<div class="col-md-6" >
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
	              <th data-sortable="true" onclick="myFunction(this)">
	                  Feed Id
	                </th>
	                <th data-sortable="true">
	                  Run Date
	                </th>
	                <th data-sortable="true">
	                 Run Id
	                </th>
	                <th data-sortable="true">
	                 Status
	                </th>
	            	
	              </tr>
	            </thead>
                <tbody>
               <c:forEach var="row" items="${arrHipDashboard1}">
                 <tr>
                 	<td><c:out value="${row.batch_id}" /></td>
                 	<td><c:out value="${row.batch_date}" /></td>
					<td><c:out value="${row.run_id}" /></td>
					<td><c:out value="${row.status}" /></td>
					
				</tr>
             </c:forEach>
                  
                </tbody>
        	</table>
        	</div>
		<div class="col-md-3" ></div>
	</div> -->
	<br>
	

</fieldset>



<fieldset class="fs"> 

			<div class="col-md-12">			
			<h1 align="center" style="text-shadow:2px 2px #6c757d">RECON STATISTICS</h1>
			</div>
			
	<br>



	<!--  <div class="col-md-12" >
		<h4 align="center"> Last Successful Runs</h4>
	</div>


		<div class="col-md-12" >
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
	                  Run Id
	                </th>
	                <th data-sortable="true" >
	                  Batch Date
	                </th>
	                
	              </tr>
	            </thead>
                <tbody>
               <c:forEach var="row" items="${sArr}">
                 <tr>
                 	<td ><c:out value="${row.run_id}" /></td>
                 	<td ><c:out value="${row.batch_date}" /></td>
								
				</tr>
             </c:forEach>
                  
                </tbody>
        	</table>
        	</div>

	
	<br>				
<div class="col-md-12" >
		<h4 align="center"> Last Failed Runs</h4>
	</div>
	
		<div class="col-md-12" >
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
	                  Run Id
	                </th>
	                <th data-sortable="true" >
	                  Batch Date
	                </th>
	                <th data-sortable="true" >
	                  TOTAL TABLE COUNT
	                </th>
	                <th data-sortable="true"  >
	                  FAILED TABLE COUNT
	                </th>         
	              </tr>
	            </thead>
                <tbody>
               <c:forEach var="row" items="${fArr}">
                 <tr>
                 	<td ><c:out value="${row.run_id}" /></td>
                 	<td ><c:out value="${row.batch_date}" /></td>
					<td ><c:out value="${row.src_count}" /></td>
                 	<td ><c:out value="${row.tgt_count}" /></td>
								
				</tr>
             </c:forEach>
                  
                </tbody>
        	</table>
        	</div> -->
	
	<br>
	<div class="col-md-12" >
		<h4 align="center"> Table/File Level Recon Stats</h4>
	</div>
<br>	
	<div class="col-md-12" >	
	
	<div class=" form-row align-items-center" >
		 <div class="col-md-3">	
		<label class="form-check-label" style="color: black; text-shadow: -1px 0 black">
            Select Run Id :
       	</label></div>
        <div class="col-md-8">
		<select name="run_id" id="run_id" class="form-control" style="color:black;font-weight:bold;">
			<option value="" selected disabled>Select run id...</option>
			<c:forEach items="${runidlist}" var="id">
				<option value="${id}"> ${id} </option> 
			</c:forEach>
		</select>
		</div></div>
	</div>
	

<div  id="tblreconstats"> </div>



</fieldset>

 </div>


 </c:when>
     <c:otherwise> No feed run statistics available for ${feed_id}
</c:otherwise></c:choose>





<script>
function myFunction(x) {
    alert("Row index is: " + x.rowIndex);
}

</script>