<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.zufangbao.gluon.spec.global.GlobalCodeSpec"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<fmt:setBundle basename="ApplicationMessage" />
	<fmt:setLocale value="zh_CN" />

	<%@ include file="/WEB-INF/include/meta.jsp"%>
	<%@ include file="/WEB-INF/include/css-newlayout.jsp"%>
	<title>公告管理 - 五维金融金融管理平台</title>
	<style type="text/css">
		.field-menu {
			border: 1px solid #436ba7;
			margin: 0 0 20px 0px;
			width: 160px;
			height: 20px;
			box-sizing: initial;
		}
		.field-menu a {
			width: 80px;
			display: inline-block;
			height: 20px;
			line-height: 20px;
			text-align: center; 
			text-decoration: none;
		}

		.field-menu .notice {
			float: left;
		}
		.field-menu .operation {
			float: right;
		}
		.field-menu .active {
			background: #436ba7;
			color: #fff;
		}

		.log-recordContent{
			overflow: hidden;
			text-overflow: ellipsis;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			max-height: 35px;
		}
		.notice-content{
			overflow: hidden;
			text-overflow: ellipsis;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			max-height: 35px;
		}
	</style>
</head>
<body>

	<%@ include file="/WEB-INF/include/header.jsp"%>

	<div class="web-g-main">
		<%@ include file="/WEB-INF/include/aside.jsp"%>
		<div data-commoncontent='true' class="content">
			<div class="scroller">
				<div class="lookup-params">
					<div class="inner clearfix">
						<span class="item">
							<button type="submit" id="lookup" class="btn btn-primary">
								新建公告
							</button>
						</span>
					</div>
				</div>
				<div class="table-area">
					<table class="data-list" 
						data-action="${ctx}/notice/notice-list" data-autoload="true"
						data-autoload="true">
						<thead>
							<tr>
								<th>标题</th>
								<th style="width: 40%">内容</th>
								<th>状态变更时间</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<script type="script/template" id="tableFieldTmpl">
							    {% _.each(list, function(item, index){ %}
							        <tr data-ititle="{%= item.title %}"
							        	data-uuid="{%= item.uuid %}"
							        	data-noticeStatus="{%= item.noticeStatus %}">
							            <td class="title">{%= item.title %}</td>
							            <td><span class='notice-content'>{%= item.content %}</span></td>
							            <td>{%= new Date(item.statusChangeTime).format('yyyy-MM-dd HH:mm:ss') %}</td>
							            {% if(item.noticeStatus == 'INVALID') { %}
								            <td class="color-danger">已作废</td>
								            <td><a href="" class="check">查看</a></td>
							        	{% }else if(item.noticeStatus == 'UNRELEASED') { %}
							        		<td class="color-warning">未发布</td>
							        		<td><a href="" class="edit">编辑</a><a href="" class="obsolete">作废</a></td>
							        	{% }else if(item.noticeStatus == 'RELEASED'){ %}
											<td>已发布</td>
											<td><a href="" class="check">查看</a><a href="" class="obsolete">作废</a></td>
										{% } %}
							        </tr>
							    {% }); %}
							</script>
						</tbody>
					</table>
				</div>
			</div>
			<div class="operations">
				<jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
		            <jsp:param value="page-control" name="type"/>
		            <jsp:param value="true" name="advanced"/>
	            </jsp:include>
			</div>	
		</div>
	</div>

	<%@ include file="/WEB-INF/include/script-newlayout.jsp"%>
	<script type="script/template" id='addNoticeTmpl'>
		<div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close close-dialog" aria-label="关闭" data-dismiss="modal"><span aria-hidden="true">×</span></button>
		        <h4 class="modal-title" id="dialoglabel" style="cursor: move;">新建公告</h4>
		      </div>
		      <div class="modal-body form-wrapper">
		        <form class="form adapt" enctype="multipart/form-data" novalidate="novalidate">
		          <div class="field-row">
		            <label for="" class="field-title require">标题</label>
		            <div class="field-value">
			           <input class="form-control real-value large" name="title">
		            </div>
		          </div>
		          <div class="field-row">
		            <label for="" class="field-title require">内容</label>
		            <div class="field-value">
		              <textarea class="form-control real-value large" name="content" rows="10" cols="50"></textarea>
		            </div> 
		          </div>
		        </form>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default cancel-dialog" data-dismiss="modal">取消</button>
		        <button type="button" class="btn btn-success submit">确定</button>
		      </div>
		    </div>
		  </div>
	</script>
	
	<script type="template" id="editNoticeTmpl">
		<div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close close-dialog" aria-label="关闭" data-dismiss="modal"><span aria-hidden="true">×</span></button>
		        <h4 class="modal-title" id="dialoglabel" style="cursor: move;">编辑公告</h4>
		      </div>
		      <div class="modal-body form-wrapper">
		      <div class="field-menu" >
		      	<a href="" class="menu-item notice active">编辑公告</a>
		      	<a href="" class="menu-item operation">操作日志</a>
		      </div>
	          <form class="form adapt notice" novalidate="novalidate">
		    	  <div class="field-row">
		    	    <label for="" class="field-title require" >标题</label>
		    	    <div class="field-value">
		    	       <input class="form-control real-value large" {%= typeof obj.noticeStatus =='undefined'||obj.noticeStatus == "UNRELEASED" ? '':'disabled' %} value="{%= _.unescape(obj.title).replace(/\'/g, "&#39;").replace(/\"/g, "&#34;") %}" name="title" >
		    	    </div>
		    	  </div>
		    	  <div class="field-row">
		    	    <label for="" class="field-title require">内容</label>
		    	    <div class="field-value">
		    	      <textarea class="form-control real-value large" name="content" rows="7" cols="50">{%= _.unescape(obj.content) %}</textarea>
		    	    </div>
		    	  </div>
		      </form>
		      <div class="sys-log form hide "> 
		      	<div class="lookup-params hide">
		      		<input class="real-value" type="hidden" name="objectUuid" value="{%= obj.uuid %}">
		      	</div>
		      	<div class="bd" style="width:83%">
		      		<table  class="logs" data-action="${ctx}/system-operate-log/query?noticeUuid">
		      			<thead >
		      				<tr>
		      					<th>序号</th>
		      					<th>操作发生时间</th>
		      					<th>操作员登录名</th>
		      					<th style="width: 40%">操作内容</th>
		      				</tr>
		      			</thead>
		      			<tbody>
		      			</tbody>
		      		</table>
		      	</div>
		      	<div class="ft" style="width:83%"	>
		      		<jsp:include page="/WEB-INF/include/plugins/assembly.jsp">
		                  <jsp:param value="page-control" name="type"/>
		                  <jsp:param value="true" name="advanced"/>
		                  <jsp:param value="5" name="pageRecordNum"/>
		              </jsp:include>
		      	</div>
		      </div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default cancel-dialog" data-dismiss="modal">取消</button>
		        {% if(obj.noticestatus == "	UNRELEASED") { %}
			        <button type="button" class="btn btn-success save">保存</button>
			        <button type="button" class="btn btn-success submit">发布</button>
		      	{% } %}
		      </div>
		    </div>
	  	</div>
	</script>

	<script type="script/template" id='obsoleteTmpl'>
		<div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close close-dialog" aria-label="关闭" data-dismiss="modal"><span aria-hidden="true">×</span></button>
		        <h4 class="modal-title" id="dialoglabel" style="cursor: move;">作废公告</h4>
		      </div>
		      <div class="modal-body form-wrapper">
		        <form class="form adapt" enctype="multipart/form-data" novalidate="novalidate">
		          <div class="field-row">
		            <label for="" class="field-title"></label>
		            <div class="field-value">
		            <span style="padding-left:50px">是否作废公告?</span>
		            </div>
		          </div>
		          <div class="field-row">
		            <label for="" class="field-title">备注:</label>
		            <div class="field-value">
			           <input class="form-control real-value middle" name="remarks" placeholder="请输入作废原因">
		            </div> 
		          </div>
		        </form>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default cancel-dialog" data-dismiss="modal">取消</button>
		        <button type="button" class="btn btn-success submit">确定</button>
		      </div>
		    </div>
		  </div>
	</script>
	<script class="template" type="script/template" id="operateTable">
	    {% _.each(list, function(log, index) { %}
	        <tr style="">
				<td>{%= index + 1 %}</td>
				<td>{%= log.occurTime %}</td>
				<td>{%= log.operateName %}</td>
				<td><a href="javascript:void(0)"  class="log-recordContent color-dim">{%= log.recordContent %}</a></td>
	        </tr>
	    {% }); %}
	</script>
</body>
</html>
