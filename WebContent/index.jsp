
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.yc.dao.*,java.util.*"%>
<%@ include file="header.jsp" %>
    <div id="container">
    	<div class="sidebar">
        	<div class="side_list">
          		<img src="images/title_1.gif" />
                <ul class="left_new">
                <%
                  DBHelper db=new DBHelper();
                  List<Map<String,Object>> list=db.findMultiObject("select * from (select rownum rn,nid,title from news inner join newstype on news.tid=newstype.tid where tname='国内新闻') where rn>0 and rn<=5", null);
                  if(list!=null && list.size()>0){
                	  for(Map<String,Object> map:list){
                		  String oldtitle=map.get("TITLE").toString();
                		  String title=map.get("TITLE").toString();
                		  if(title.length()>8){
                			  title=title.substring(0,8)+"...";
                		  }
                %>
                  <li><a href="#" title="<%=oldtitle%>"><%=title %></a></li>
                <%
                	  }
                  }
                %>   
                </ul>
            </div>
            <div class="side_list">
          		<img src="images/title_2.gif" />
                 <ul class="left_new">
                 <%
                  
                  list=db.findMultiObject("select * from (select rownum rn,nid,title from news inner join newstype on news.tid=newstype.tid where tname='国内新闻') where rn>0 and rn<=5", null);
                  if(list!=null && list.size()>0){
                	  for(Map<String,Object> map:list){
                		  String oldtitle=map.get("TITLE").toString();
                		  String title=map.get("TITLE").toString();
                		  if(title.length()>8){
                			  title=title.substring(0,8)+"...";
                		  }
                %>
                  <li><a href="#" title="<%=oldtitle%>"><%=title%></a></li>
                <%
                	  }
                  }
                %>   
                </ul>
            </div>
            <div class="side_list">
          		<img src="images/title_3.gif" />
                 <ul class="left_new">
                 <%
                  list=db.findMultiObject("select * from (select rownum rn,nid,title from news inner join newstype on news.tid=newstype.tid where tname='国内新闻') where rn>0 and rn<=5", null);
                  if(list!=null && list.size()>0){
                	  for(Map<String,Object> map:list){
                		  String oldtitle=map.get("TITLE").toString();
                		  String title=map.get("TITLE").toString();
                		  if(title.length()>8){
                			  title=title.substring(0,8)+"...";
                		  }
                %>
                  <li><a href="#" title="<%=oldtitle%>"><%=title %></a></li>
                <%
                	  }
                  }
                %>   
                </ul>
            </div>
        </div>
        <div class="main">
        	<div class="class_type">
            	<img src="images/class_type.gif"/>
           	</div>
            <div class="content">
            	<div class="class_date" id="class_month">
                   
                    <%
                  
                    list=db.findMultiObject("select * from newstype where status=1 order by tid", null);
                    if(list!=null&&list.size()>0){
                    	for(Map<String,Object> map:list){
                    	 %>
                    	 	 <a href="#"><%=map.get("TNAME")%>></a>
                    	 <%
                    	      }
                            }
                    	%>
                   
                   
               </div>
               <div class="classlist">
               		<ul>
               		    <%
               		      list= db.findMultiObject("select a.* from (select rownum rn,n.* from news n order by ndate desc,weight desc) a where rn>0 and rn<=15", null);
               		      if(list!=null&&list.size()>0){
               		    	  int i=0;
               		    	  for(Map<String,Object> map:list){
               		    		  i++;

               		    %> 
                    	<li><a href="#"><%=map.get("TITLE") %></a><span><%=map.get("NDATE") %></span></li>
                    	<%
                    	    if(i%5==0){
                    	%>
                     			<li class="space"></li>
                     			
               			<%
                    	    }
               		    	  }
               			%>
               			
               			<li class="space"><span>当前页数：[1/2] <a href="#">下一页</a> <a href="#">末页</a></span></li>
               			  
               			  <%
               		      }else{
               			  %>
               			     <li>暂无新闻，，请通知管理上线通知</li>
               			  <%
               		      }
               			  %>
                    </ul>
               </div>
            </div>
            <div class="picnews">
                <%
                 list= db.findMultiObject("select * from (select rownum rn,n.* from news n where pic is not null order by ndate desc) where rn>0 and rn<=2", null);
                 if(list!=null && list.size()>0){
                	 for(Map<String,Object> map:list){
                %>
            	<li><img src="<%=map.get("PIC")%>"/><a href="#"><%=map.get("TITLE") %></a></li>
                <%
                	 }
                 }
                %>
            </div>
        </div>
    </div>
    
    
       <%@ include file="footer.jsp"%>
