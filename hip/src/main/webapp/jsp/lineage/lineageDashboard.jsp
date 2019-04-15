<jsp:include page="../cdg_header2.jsp" />

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link href="${pageContext.request.contextPath}/assets/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/assets/js/select2.min.js"></script>
<script>
function jsonConstructLineage()
{
	
	var data = {};
	$(".form-control1").serializeArray().map(function(x){data[x.name] = x.value;});
	var x = '{"header":{"user":"cdg_admin","service_account":"Publishing_CDG_UK","reservoir_id":"R0001","event_time":"today"},"body":{"data":'+JSON.stringify(data)+'}}';
	
	document.getElementById('x').value = x;
	//alert(x);
  	//console.log(x);
	document.getElementById('lineageView').submit();
}


</script>

			
<div class="content-wrapper">
<div class="row">
   <div class="col-12 grid-margin stretch-card">
      <div class="card">
         <div class="card-body">
         
         <%
            if(request.getAttribute("errorString") != null) {
            %>
           	<div class="alert alert-danger" id="error-alert">
   				<button type="button" class="close" data-dismiss="alert"></button>
   		 		${errorString}
			</div>
           <%
            }
           %>
         
           
           <div class="row">
						
							<div class="col-md-10"></div>
							
							<div class="col-md-2">
								<h5 style="text-align: right;">
									<a style="text-decoration: none;" href="/JuniperHome1">Home</a>
								</h5>
							</div>
						</div>
           
            <p class="card-description">
               Table Lineage
            </p>
            
            
    <form class="forms-sample" id="lineageView" name="lineageView" method="POST"
							action="/dashboard/getLineageView" target="_blank"
						enctype="application/json" data-toggle="validator" role="form" >
		<input type="hidden" id="x" name="x">
            <div class="row" id="sa_dtls" >
            
           
				<div class="form-group col-md-3">
						<label>EIM<span style="color:red">*</span></label> 
				</div>
				<div class="form-group col-md-9" >
				<input list="eim" name="eim_val" id="eim_val" class="form-control">
				<datalist id="eim" >
					<c:forEach items="${eim}" var="eim">
						<option value="${eim}">
					</c:forEach>
				</datalist>
				
<!-- 					<select class="js-example-basic-single form-control" id="eim" name="eim" > -->
<!-- 						<option value="" selected disabled>Select eim...</option> -->
<%-- 							<c:forEach items="${eim}" var="eim"> --%>
<%-- 								<option value="${eim}">${eim}</option> --%>
<%-- 							</c:forEach> --%>
<!-- 					</select> -->
				</div>
		
            
            
<!-- 				<div class="form-group col-md-3"> -->
<!-- 						<label>Project Name<span style="color:red">*</span></label>  -->
<!-- 				</div> -->
<!-- 				<div class="form-group col-md-9" > -->
<!-- 					<select class="form-control form-control1 " id="eim" name="eim" > -->
<!-- 						<option value="" selected disabled>EIM...</option> -->
<%-- 							<c:forEach items="${eim}" var="eim"> --%>
<%-- 								<option value="${eim}">${eim}</option> --%>
<%-- 							</c:forEach> --%>
<!-- 					</select> -->
<!-- 				</div> -->
			</div>
			
			
			
			<div id="loadFeedList"></div>
						
	<br>		
<!--      	<div class="form-group row"> -->
<!-- 		  		<div class="col-md-7"> -->
<!-- 		       		<button type="submit" id="ex_sub" class="btn btn-rounded btn-gradient-info btn-group float-right" onclick="jsonConstructLineage();"> -->
<!-- 					Publish Graph! -->
<!-- 					</button> -->
<!-- 		      	</div> -->
<!-- 		       	<div class="col-md-9"></div> -->
<!-- 		    </div>  -->
		</form>
		
<div class="row" >
	   <div class="col-12 grid-margin stretch-card">
	      <div class="card">
	         <div class="card-body">
	         	<div class="col-md-9">
	         	${resp}
    	</div>
</div></div></div></div>
</div></div></div></div>

<jsp:include page="../cdg_footer.jsp" />
<script>
$(document).ready(function () {
	

	
	$("#eim_val").change(function() {
		disableForm(lineageView);
		$('#loading').show();
		var eim = document.getElementById('eim_val').value;
		$.post('/lineage/FeedList', {
			eim : eim
		}, function(data) {
			$('#loading').hide();
			enableForm(lineageView);
			$('#loadFeedList').html(data)
		}).fail(function() {
			alert("System error: Something went wrong");
			$('#loading').hide();
		});
})
	
	$('#lineageModal').modal('show');	
	
});


function disableForm(theform) {
    if (document.all || document.getElementById) {
        for (i = 0; i < theform.length; i++) {
        var formElement = theform.elements[i];
            if (true) {
                formElement.disabled = true;
            }
        }
    }
}


function enableForm(theform) {
    if (document.all || document.getElementById) {
        for (i = 0; i < theform.length; i++) {
        var formElement = theform.elements[i];
            if (true) {
                formElement.disabled = false;
            }
        }
    }
}
	 
</script>
<script src="../../assets/js/bootstrap.min.js"></script>
		<script
			src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.3/js/bootstrap-select.min.js"></script>
