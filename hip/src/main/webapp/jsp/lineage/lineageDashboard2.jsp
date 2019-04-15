<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose >
<c:when test="${fn:length(datasets) gt 0}">
  <div class="row" >
				<div class="form-group col-md-3">
						<label>DataSet<span style="color:red">*</span></label> 
				</div>
				<div class="form-group col-md-9" >
					<select class=" form-control form-control1" id="feed" name="feed" >
						<option value="" selected disabled>Select dataset...</option>
							<c:forEach items="${datasets}" var="dataset">
								<option value="${dataset.key}">${dataset.value}</option>
							</c:forEach>
					</select>
				</div>
			</div>	
</c:when>	
<c:otherwise>
<div id="m1"><p>${message}</p></div>	
</c:otherwise>
</c:choose>	