<?xml version="1.0" encoding="UTF-8" ?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ page session="false"%>
<html>
<head>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
<title>Zookie</title>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/overcast/jquery-ui.css" type="text/css" media="all" />

</head>
<body>
  <div id="header">
    <h1>
      <a href="<c:url value="/"/>">home</a>
    </h1>
  </div>
  <div><br/><br/><br/><br/></div>
  <div id="leftNav">
    <tiles:insertTemplate template="menu.jsp" />
  </div>

  <div id="content" style="width: 800px;">
    <tiles:insertAttribute name="content" />
  </div>
</body>
</html>
