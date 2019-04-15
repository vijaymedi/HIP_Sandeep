<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link href="${pageContext.request.contextPath}/assets/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/assets/js/select2.min.js"></script>
<script>
$(document).ready(function() {
	
	$('.js-example-basic-single').select2();
// 	$("#feedname").change(function() {
// 		disableForm(lineageView);
// 		$('#loading').show();
	
// 		var feed = document.getElementById('feedname').value;
		
// 		$.post('/lineage/Datasets', {
// 			feed : feed
// 		}, function(data) {
// 			$('#loading').hide();
// 			enableForm(lineageView);
// 			$('#loadDataSet').html(data);
// 		}).fail(function() {
// 			alert("System error: Something went wrong");
// 			$('#loading').hide();
// 		});
// 	})
	
});

</script>
	
	<br><br>
	
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
	                  FEED NAME
	                </th>
	                <th data-sortable="true" >
	                  PROJECT NAME
	                </th>
	                 <th data-sortable="true" >
	                  SOURCE EIM
	                </th>
	                <th data-sortable="true">
	                  TARGET EIM
	                </th>
	                <th data-sortable="true">
	                 
	                </th>
	              </tr>
	            </thead>
                <tbody>
               <c:forEach var="row" items="${feeddet}" varStatus="theCount">
                 <tr>
                 	<td><c:out value="${row.feed_name}" /></td>
                 	<td><c:out value="${row.project_id}" /></td>
                 	<td><c:out value="${row.src_conn}" /></td>
                 	<td><c:out value="${row.tgt_conn}" /></td>
					<td><input type="radio" id="feedname" name="feedname" class="form-control1" value="${row.feed_id}|${row.feed_name}" onclick="jsonConstructLineage();"  /></td>
					
				</tr>
             </c:forEach>
                  
                </tbody>
        	</table>
        	</div>
	
	
	
	
	
	
	
	

	
	
	

<!--   <div class="row" > -->
<!-- 				<div class="form-group col-md-3"> -->
<!-- 						<label>Feed Name<span style="color:red">*</span></label>  -->
<!-- 				</div> -->
<!-- 				<div class="form-group col-md-9" > -->
<!-- 					<select class="js-example-basic-single form-control form-control1" id="feedname" name="feedname" > -->
<!-- 						<option value="" selected disabled>Select feed name...</option> -->
<%-- 							<c:forEach items="${feed}" var="feed"> --%>
<%-- 								<option value="${feed.key}|${feed.value}">${feed.value}</option> --%>
<%-- 							</c:forEach> --%>
<!-- 					</select> -->
<!-- 				</div> -->
<!-- 			</div>	 -->
			
			
<!-- 			<div id="loadDataSet"></div> -->
			
		

<!-- /*<div class="row">	 -->
<!-- 							<label class=" col-md-2 form-check-label"> -->
<!-- 					            Select Feed : -->
<!-- 					        </label> -->
<!-- 					         <div class=" form-group col-md-8"> -->
<!-- 								<select name="feed_id" -->
<!-- 										id="feed_id" class="js-example-basic-single form-control"> -->
<!-- 										<option value="" selected disabled>Select Feed ...</option> -->
<%-- 										<c:forEach items="${feed}" var="feed"> --%>
<%-- 											<option value="${feed}">${feed}</option> --%>
<%-- 										</c:forEach> --%>
<!-- 									</select> -->
<!-- 							</div> -->
<!-- </div> -->


<script src="../../assets/js/bootstrap.min.js"></script>
		<script
			src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.3/js/bootstrap-select.min.js"></script>

