<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link href="${pageContext.request.contextPath}/assets/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/assets/js/select2.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	
	$('.js-example-basic-single').select2();
	$("#info-alert").hide();
    $("#info-alert").fadeTo(5000,10).slideUp(2000, function() {
    });   
	$("#feed_id").change(function() {
		disableForm(f1);
		$('#loading').show();
			document.getElementById("addFeed1").innerHTML="";
			var feed_id = $(this).val();	
			var plt_id = document.getElementById('plt_id').value;
			var feed_len=document.getElementById('feed_len').value;
			document.getElementById('select').innerHTML="<p><i>There are "+ feed_len + " feeds registered for " + plt_id + " platform.</i></p>";
			$.post('${pageContext.request.contextPath}/hip/hipmasterdashboard1', {
				feed_id : feed_id,
				plt_id : plt_id,
				feed_len : feed_len
			}, function(data) {
				$('#loading').hide();
				enableForm(f1);
				$('#addFeed1').html(data);
			if(document.getElementById("x").value) { 
				var x = document.getElementById("x").value;
				var y = document.getElementById("y").value;
				var newx = x.split(',');
				var newy = y.split(',');
				newx[0] = newx[0].replace("[","");
				newy[0] = newy[0].replace("[","")
				newx[newx.length - 1] = newx[newx.length - 1].replace("]","");
				newy[newy.length - 1] = newy[newy.length - 1].replace("]","");

				var areaData = {labels : newx,datasets : [ {
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
					}}
				}
				if ($("#areaChart").length) {
					var areaChartCanvas = $("#areaChart").get(0).getContext("2d");
					var areaChart = new Chart(areaChartCanvas,{
					type : 'line',
					data : areaData,
					options : areaOptions
					});
				}}
			}).fail(function() {
				alert("System error: Something went wrong");
				$('#loading').hide();
			});
;							
	})

	});	

	
	
</script>
<div class="row">
<input type="hidden" id="plt_id" name="plt_id">
<input type="hidden" id="feed_len" name="feed_len" value="${feed_len}">
<div class="alert alert-info col-md-12" id="info-alert">
<p id="select"> <i>There are ${feed_len} feeds registered for ${plt_id} platform</i> </p></div>
</div>
	

<div class="row">	
							<label class=" col-md-2 form-check-label">
					            Select Feed :
					        </label>
					         <div class=" form-group col-md-8">
								<select name="feed_id"
										id="feed_id" class="js-example-basic-single form-control">
										<option value="" selected disabled>Select Feed ...</option>
										<c:forEach items="${feed}" var="feed">
											<option value="${feed}">${feed}</option>
										</c:forEach>
									</select>
							</div>
</div>


			
								<div id="addFeed1" ></div>

		<script src="../../assets/js/bootstrap.min.js"></script>
		<script
			src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.3/js/bootstrap-select.min.js"></script>