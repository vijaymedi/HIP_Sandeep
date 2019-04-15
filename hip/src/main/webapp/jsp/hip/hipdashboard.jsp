<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="../cdg_header.jsp" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
	//home button function-start
	function homebutton() {
		window.location.href = "${pageContext.request.contextPath}/";
	}
	//home button function-end
		
function getFeedList(x){
								var plt_id=x;
								document.getElementById(x).style.border="3px solid red";
								$.post('${pageContext.request.contextPath}/hip/hipfeedregisterd', {
									plt_id : plt_id
								}, function(data) {
									$('#addFeed').html(data);
									document.getElementById(x).style.border="";
								});
}
								
	//Document Ready----Start
			

</script>
							
						
						
<div class="main-panel" style="width: 100%">
<div class="content-wrapper">
		<div class="row">
			<div class="col-12 grid-margin stretch-card">
			<div class="card">
					<div class="card-body">
					
						<div class="row">
						
							<div class="col-md-10"></div>
							
							<div class="col-md-2">
								<h5 style="text-align: right;">
									<a style="text-decoration: none;" href="/">Home</a>
								</h5>
							</div>
						</div>
						<div class="row">	
							<label class=" col-md-2 form-check-label">
					             Select Platform :
					        </label>
					         <div class=" form-group col-md-8">
								<c:forEach var="myMap" items="${pltList}">
									<button type="button" class="btn btn-danger" value="${myMap}" id="${myMap}" onclick="getFeedList(this.value)"><c:out value="${myMap}"/></button>
								</c:forEach>
							</div>
						</div>
							
							<div id="addFeed"></div>
													   		 			
						 </div>
				</div>
			</div>
		</div>
	</div>
</div>
		<jsp:include page="../cdg_footer.jsp" />


		