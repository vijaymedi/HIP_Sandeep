<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link href="${pageContext.request.contextPath}/assets/css/bootstrap-table.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/assets/css/pagination.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/assets/js/bootstrap-table.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
<script>

function showgraph()
{
	document.getElementById('graph1').style.display="block";
	document.getElementById('a1').style.display="none";
	
}
function showgraph1()
{
	document.getElementById('graph2').style.display="block";
	document.getElementById('a2').style.display="none";
	
}
</script>




<input type="hidden" name="tbl_eimvalidation" id="tbl_eimvalidation" value="${stat}">
<input type="hidden" id="tbl_x" value='${tbl_x}' name="tbl_x"/>
<input type="hidden" id="tbl_y" value='${tbl_y}' name="tbl_y"/>
<input type="hidden" id="tbl_z" value='${tbl_z}' name="tbl_z"/>
<p></p>
<c:if test="${stat eq 1}">  	

 <br>     <br>	
 <a  onclick="showgraph1()" id="a2"  style="text-color:#428bca; text-decoration:underline;font-weight:bold; cursor:pointer;">Click here to view table level statistics</a>

<div class="row" id="graph2" style="display:none;">
   <div class="col-md-12" >				
	<div class="row">
		<div class="col-md-3" ></div>
		<div class="col-md-6" >
			<h4 align="center">Duration of Feed in Minutes</h4>
			
		</div>
		<div class="col-md-3" ></div>
	</div>
	<div class="row">
		<div class="col-md-2" ></div>
		<div class="col-md-8" >
			<div class="col-md- grid-margin stretch-card" >
			<canvas id="tbl_areaChartDuration" style="width:100%"></canvas>
	    </div>
		</div>
		<div class="col-md-2" ></div>
	</div>
	<div class="row">
		<div class="col-md-2" ></div>
		<div class="col-md-8" >
			<div class="col-md- grid-margin stretch-card" >
			<canvas id="tbl_areaChartCount" style="width:100%"></canvas>
	    </div>
		</div>
		<div class="col-md-2" ></div>
	</div></div>
		
	<br>
	<div class="col-md-12" id="tbl1">
	
			<table id="tbl_dashboard" 
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
	                <th data-sortable="true" >
	                  Table Id
	                </th>
	                 <th data-sortable="true" >
	                  File Date
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
	                <th data-sortable="true">
	                Source Count
	                </th>
	              </tr>
	            </thead>
                <tbody>
               <c:forEach var="row" items="${tblArrHipDashboard}">
                 <tr>
                 	<td class="cust"><c:out value="${row.batch_id}" /></td>
                 	<td><c:out value="${row.table_name}" /></td>
                 	<td><c:out value="${row.file_date}" /></td>
                 	<td><c:out value="${row.batch_date}" /></td>
					<td><c:out value="${row.run_id}" /></td>
					<td><c:out value="${row.start_time}" /></td>
					<td><c:out value="${row.end_time}" /></td>
					<td><c:out value="${row.duration}" /></td>
					<td><c:out value="${row.table_count}" /></td>
				</tr>
             </c:forEach>
                  
                </tbody>
        	</table>
        		
		
	</div></div>

</c:if>
	
<c:if test="${stat eq 0}">
	<div class="alert alert-danger" id="error-alert">
    				<button type="button" class="close" data-dismiss="alert">x</button>
    		 		Feed ID DOESNOT EXIST
				</div>
</c:if>
<c:if test="${stat eq 2}">

 
<div class="form-group row">
	<div class="col-md-3" ></div>
	<div class="col-md-6" >
		<h4 align="center">Technical Metadata - Table Based</h4>
	</div>
	<div class="col-md-3" ></div></div>	

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
	              <th data-sortable="true" onclick="myFunction(this)">
	                  Column Name
	                </th>
	                <th data-sortable="true">
	                  Column Type
	                </th>
	                <th data-sortable="true">
	                  Interface Id 
	                </th>
	                <th data-sortable="true" >
	                 Logical File Id
	                </th>         
	                <th data-sortable="true">
	                  Logical File Name
	                </th>
	                <th data-sortable="true" >
	                  Logical File Description
	                </th>         
	                <th data-sortable="true">
	                  Data Categorization Code
	                </th>
	                <th data-sortable="true" >
	                  Data Categorization Indicator
	                </th>         
	                <th data-sortable="true">
	                  Masking Indicator
	                </th>
	                           	
	              </tr>
	            </thead>
                <tbody>
               <c:forEach var="row" items="${metadatarr}">
                 <tr>
                 	<td class="cust"><c:out value="${row.column_name}" /></td>
                 	<td class="cust"><c:out value="${row.column_type}" /></td>
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
        
 <br>     <br>	    	
 <a  onclick="showgraph()" id="a1"  style="text-color:#428bca; text-decoration:underline;font-weight:bold; cursor:pointer;">Click here to view table level statistics</a>

<div class="row" id="graph1" style="display:none;">
<div class="col-md-12" >
<div class="row">
		<div class="col-md-6" >
			<h4 align="center">Duration of Feed in Minutes</h4>
			
		</div>
		<div class="col-md-6" >
			<h4 align="center">Table wise Record Count</h4>
		</div></div>
	</div>

<div class="col-md-12" >
<div class="row">
	      <div class="col-md-2">
	      <div class="card-counter success">
	        <i class="fa fa-ticket"></i>
	        <span class="count-numbers">${tbl_min_duration}</span>
	        <span class="count-name">Minimum</span>
	      </div>
	    </div>
		 <div class="col-md-2">
	      <div class="card-counter primary">
	        <i class="fa fa-code-fork"></i>
	        <span class="count-numbers">${tbl_avg_duration}</span>
	        <span class="count-name">Average</span>
	      </div>
	    </div>
	    <div class="col-md-2">
	      <div class="card-counter danger">
	        <i class="fa fa-database"></i>
	        <span class="count-numbers">${tbl_max_duration}</span>
	        <span class="count-name">Maximum</span>
	      </div>
	    </div>
	    <div class="col-md-2">
	      <div class="card-counter success">
	        <i class="fa fa-ticket"></i>
	        <span class="count-numbers">${tbl_min_count}</span>
	        <span class="count-name">Minimum</span>
	      </div>
	    </div>
		 <div class="col-md-2">
	      <div class="card-counter primary">
	        <i class="fa fa-code-fork"></i>
	        <span class="count-numbers">${tbl_avg_count}</span>
	        <span class="count-name">Average</span>
	      </div>
	    </div>
	    <div class="col-md-2">
	      <div class="card-counter danger">
	        <i class="fa fa-database"></i>
	        <span class="count-numbers">${tbl_max_count}</span>
	        <span class="count-name">Maximum</span>
	      </div>
	    </div>
	    </div>
	</div>
	
  <br>

<div class="col-md-12" >	
	<div class="row">
		<div class="col-md-6" >
			<div class="col-md- grid-margin stretch-card" >
				<canvas id="tbl_areaChartDuration" style="width:100%"></canvas>
	    	</div>
		</div>
		<div class="col-md-6" >
			<div class="col-md- grid-margin stretch-card" >
				<canvas id="tbl_areaChartCount" style="width:100%"></canvas>
	    	</div>
		</div>
	</div></div>
	<br>

		<div class="col-md-12" >
			<table id="tbl_dashboard" 
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
	                <th data-sortable="true" >
	                  Table Id
	                </th>
	                 <th data-sortable="true" >
	                  File Date
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
	                <th data-sortable="true">
	               Source Count
	                </th>
	              </tr>
	            </thead>
                <tbody>
               <c:forEach var="row" items="${tblArrHipDashboard}">
                 <tr>
                 	<td><c:out value="${row.batch_id}" /></td>
                 	<td><c:out value="${row.table_name}" /></td>
                 	<td><c:out value="${row.file_date}" /></td>
                 	<td><c:out value="${row.batch_date}" /></td>
					<td><c:out value="${row.run_id}" /></td>
					<td><c:out value="${row.start_time}" /></td>
					<td><c:out value="${row.end_time}" /></td>
					<td><c:out value="${row.duration}" /></td>
					<td><c:out value="${row.table_count}" /></td>
				</tr>
             </c:forEach>
                  
                </tbody>
        	</table>
        	</div>

</div>	


</c:if>
<script>
function myFunction(x) {
    alert("Row index is: " + x.rowIndex);
}

</script>