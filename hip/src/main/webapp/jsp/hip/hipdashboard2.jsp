<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/multi.min.css">
<link href="${pageContext.request.contextPath}/assets/css/pagination.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/assets/css/bootstrap-table.min.css" rel="stylesheet">
<script>
$(document).ready(function () {
	var feed_id='${feed_id}'
	document.getElementById('feed_id').value=feed_id;
	//document.getElementById('feedid_s').value=feed_id;
	
	 $("#run_id").change(function() {
		 $('#loading').show();
			disableForm(f1);
			var run_id = $(this).val();
			var src_flag= document.getElementById('src_flag').value;
			var feed_id = document.getElementById('feed_id').value;	
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
</script>

<input type="hidden" id="src_flag" name="src_flag" value="${src_flag}">
<input type="hidden" name="eimvalidation" id="eimvalidation" value="${stat}">
<input type="hidden" id="x" value='${x}' name="x"/>
<input type="hidden" id="y" value='${y}'name="y"/>
<input type="hidden" id="sdate" value='${sdate}' name="sdate"/>
<input type="hidden" id="edate" value='${edate}'name="edate"/>

<c:if test="${stat eq 1}">  

<div class="alert alert-danger" id="error-alert">
    				<button type="button" class="close" data-dismiss="alert">x</button>
    		 		No feed run statistics available for ${feed_id} for the selected range
				</div>
</c:if>
	
<c:if test="${stat eq 0}">
	<div class="alert alert-danger" id="error-alert">
    				<button type="button" class="close" data-dismiss="alert">x</button>
    		 		Feed ID DOES NOT EXIST
				</div>
</c:if>
<c:if test="${stat eq 2}">

<fieldset class="fs">
<div class="row" >
			<div class="col-md-12">			
				<h1 align="center">FEED RUN STATISTICS</h1>
			</div>
</div>

<br>
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
	
	<br>


	<div class="form-group row">
	<div class="col-md-3" ></div>
	<div class="col-md-6" >
		<h4 align="center">Duration of Feed in Minutes</h4>
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
  <br>
	<div class="row">
		<div class="col-md-2" ></div>
		<div class="col-md-8" >
			<div class="col-md- grid-margin stretch-card" >
			<canvas id="areaChart1" style="width:100%"></canvas>
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
	                  Batch Date
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

<!--  <div class="form-group row">
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
	</div>
	<br>-->
	
</fieldset>	



<fieldset class="fs">

		<div class="col-md-12">			
			<h1 align="center">RECON STATISTICS</h1>
			</div>
<!-- 	
	<br>



	<div class="col-md-12" >
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
        	</div>
	 -->		
<br>	
	<div class="col-md-12" >
		<h4 align="center"> Table Level Recon Stats</h4>
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
				<option value="${id}">${id}</option>
			</c:forEach>
		</select>
		</div></div>
	</div>
<div  id="tblreconstats"> </div>

</fieldset>
</c:if>

	<script src="${pageContext.request.contextPath}/assets/js/bootstrap-table.min.js"></script>
	
