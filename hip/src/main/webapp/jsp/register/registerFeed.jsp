<!DOCTYPE html>
<html lang="en">

<head>
  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Register Feed</title>
  <!-- plugins:css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/iconfonts/mdi/css/materialdesignicons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor.bundle.base.css">
  <!-- endinject -->
  <!-- plugin css for this page -->
  <!-- End plugin css for this page -->
  <!-- inject:css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style2.css">
  <!-- endinject -->
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/img/favicon.png" />
</head>
<script>
	function validate() {
		var file = form.file.value;
		var reg = /(uploadtemplate.xlsx)$/;
		if (!file.match(reg)) {
			alert("Invalid File");
			return false;
		}
	}
</script>

<body>
  <div class="container-scroller" >
    <div class="container-fluid page-body-wrapper full-page-wrapper">
      <div class="content-wrapper d-flex align-items-center auth" style="background-image: url('${pageContext.request.contextPath}/assets/img/city.jpg'); background-size: cover; background-position: top center;">
        <div class="row w-100">
          <div class="col-lg-4 mx-auto">
            <div class="auth-form-light text-left p-5">
              
               <h3>Register Feed</h3> 
              
              <form class="pt-3" action="${pageContext.request.contextPath}/register/submit" method="POST" enctype="multipart/form-data">
                <div class="form-group">
              
<a href='${pageContext.request.contextPath}/assets/feedtemplate/uploadtemplate.xlsx' target="_blank">Download Template</a>
				
                </div>
                <div class="form-group">
                      <label>File upload</label>
                      <input type="file" name="file" class="file-upload-default">
                      <div class="input-group col-xs-12">
                        <input type="text" class="form-control file-upload-info" disabled placeholder="Upload File">
                        <span class="input-group-append">
                          <button class="file-upload-browse btn btn-block btn-facebook auth-form-btn" type="button" >Browse</button>
                        </span>
                      </div>
                    </div>
                <div class="mt-3">
                	<button type="submit" class="btn btn-block btn-facebook auth-form-btn" >
                    Upload
                  </button>
                </div>
                
                <div class="mt-3" align="center">
                	<a href="/JuniperHome"><img src="${pageContext.request.contextPath}/assets/img/home.png" alt="Image"  height="30" width="30" class="rounded">
 </a>
                </div>
                                <div class="mt-3" align="center">
                
                <%
							if (request.getAttribute("successString") != null) {
						%>
						<p class="text-success h4">${successString}</p>
						<%
							}
						%>
						<%
							if (request.getAttribute("errorString") != null) {
						%>
						<p class="text-danger h4">${errorString}</p>
						<%
							}
						%>
    </div>
              </form>
               
            </div>
          </div>
         
        </div>
        
          
      </div>
      <!-- content-wrapper ends -->
    </div>
    <!-- page-body-wrapper ends -->
  </div>
  <!-- container-scroller -->
  <!-- plugins:js -->
  <script src="${pageContext.request.contextPath}/assets/js/vendor.bundle.base.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/vendor.bundle.addons.js"></script>
  <!-- endinject -->
  <!-- inject:js -->
  <script src="${pageContext.request.contextPath}/js/off-canvas.js"></script>
  <script src="${pageContext.request.contextPath}/js/misc.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/file-upload.js"></script>
  <!-- endinject -->
</body>

</html>
