<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>查询信息</title>
    <script src="jquery-1.12.3.min.js" type="text/javascript"></script>
</head>
<body>
<form class="form-horizontal" >
    <label for="firstname" class="col-sm-2 control-label">用户ID</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="user_id" placeholder="请输入用户ID">
    </div>
    <div class="col-sm-offset-2 col-sm-10">
      <button type="button" id="select" class="btn btn-default">查找</button>
    </div>

</form>
<div class="panel-body">
    <h2 class="text-danger text-center">
        <!-- 用来显示查找结果 -->
        <span class="glyphicon" id="select-box"></span><br>
        <span class="glyphicon" id="id"></span><br>
        <span class="glyphicon" id="name"></span><br>
        <span class="glyphicon" id="telephone"></span><br>
    </h2>
</div>
<script >
$(function(){
$("#select").click(function() {
            $.ajax({
                url : "test/showTest",
                type : "POST",
                dataType:"json",
                contentType : "application/json;charset=UTF-8",
                data : JSON.stringify({
                    id : $("#user_id").val(),
                }),
                success:function(result) {
                    var message= JSON.stringify(result);
                    $("#select-box").html("查询成功，String格式：" + message);
                    $("#id").html("账号：" + result.id);
                    $("#name").html("姓名：" + result.name);
                    $("#telephone").html("电话：" + result.telephone);
                },
                error:function(result){
                    $("#select-box").html("查询失败");
                }
            });
        });
});
</script>
</body>
</html>