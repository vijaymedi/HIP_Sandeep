<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="cdg_header.jsp" />

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
	
function getFeedList(x){
	disableForm(f1);
	$('#loading').show();
	
								var plt_id=x;
								if(document.getElementById('prev_plt1').value==""){
									document.getElementById(x).style.border="3px solid red";
									document.getElementById('prev_plt1').value=plt_id;
									
								}else{
									var prev_plt=document.getElementById('prev_plt1').value
									document.getElementById(prev_plt).style.border="";
									document.getElementById(x).style.border="3px solid red";
									document.getElementById('prev_plt1').value=x;
								}
								
								$.post('${pageContext.request.contextPath}/hip/hipfeedregisterd', {
									plt_id : plt_id
								}, function(data) {
									$('#loading').hide();
									enableForm(f1);
									$('#addFeed').html(data);
									
								}).fail(function() {
									alert("System error: Something went wrong");
									$('#loading').hide();
								});
;
								
}

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
								
	//Document Ready----Start
			

</script>


						
					

<div class="content-wrapper">
<input type="hidden" id="prev_plt1"  name="prev_plt1" />						
<div class="main-panel" style="width: 100%">
	

<form id="f1">

<div class="row">
		
		
			<div class="col-12 grid-margin stretch-card">
			<div class="card">
					<div class="card-body">
					
						<div class="row">
						
							<div class="col-md-10"></div>
							
							<div class="col-md-2">
								<h5 style="text-align: right;">
									<a style="text-decoration: none;" href="/JuniperHome">Home</a>
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

</form>		
	</div>
</div>	

		<jsp:include page="cdg_footer.jsp" />