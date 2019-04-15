<link href="../../assets/css/bootstrap.min.css" rel="stylesheet">

<style>
.node {
        cursor: pointer;
}

.node circle {
        fill: #fff;
        stroke: steelblue;
        stroke-width: 3px;
}

.node text {
        font: 12px sans-serif;
}

.link {
        fill: none;
        stroke: #ccc;
        stroke-width: 2px;
}

.svg-container {
    display: inline-block;
    position: relative;
    width: 50%;
        height: 50%;
    padding-bottom: 100%;
    vertical-align: top;
    overflow: hidden;
}
.svg-content {
    display: inline;
    position: absolute;
    width: 100%;
        height: 100%;
    top: 0;
    left: 0;
}

</style>
			
<script src="http://d3js.org/d3.v3.min.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="main-panel">
<div class="content-wrapper">
<div class="row" >
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

	   <div class="col-12 grid-margin stretch-card">
	      <div class="card">
	         <div class="card-body">
	         	<div class="form-group row">
  					<div class="col-md-3"></div>
	 				<div class="col-md-9"></div>
				</div>
				${resp}
</div></div></div></div>
