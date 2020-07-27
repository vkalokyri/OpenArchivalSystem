<%-- 
    Document   : ShowBookView
    Created on : Jan 11, 2012, 10:20:57 AM
    Author     : suitcase
--%>

<%@page import="dao.DAOFactory"%>
<%-- 
    Document   : index
    Created on : Dec 14, 2011, 3:51:39 PM
    Author     : suitcase
--%>

<%@page import="model.Book"%>
<%@page import="model.Collection"%>
<%@page import="model.LangString"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "_//W3C//DTD HTML 4.01 Transitional//EN"
    "Http://www.w3.org/TR/html4/loose.dtd">

<% Collection collection = (Collection)request.getAttribute("model");
   List<Collection> collectionList = DAOFactory.getDAOFactory(DAOFactory.EXIST).getCollectionDAO().getCollectionList();
   List<String> current = (ArrayList<String>)session.getAttribute("CurrentTree");
    if(current==null){
            current=new ArrayList();
            current.add(0, "-1");
            current.add(1, "-1");
            session.setAttribute("CurrentTree", current);
            current = (ArrayList)session.getAttribute("CurrentTree");
        }%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Book Editor</title>
        <script type="text/javascript" src="treeOperations.js"></script>
        <link href="style.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div id="wrap">
          <div id="masthead">
            <div id="logo">
                <h1 style="padding-top: 25px; color:blue">Book Metadata Editor</h3>
            </div>
          </div>
          <div id="menucontainer">
            <div id="menu">
                <ul>
                    <li><a href="Controller?action=newCollectionPage">New Collection</a></li>
                    <li><a href="Controller?action=showSearchPage">Search</a></li>
                </ul>
            </div>
          </div>
          <div id="LeftMenu">
            <ul>                
                <li class="collectionHeader"><a style="font-size: 13px">Collection Browser</a></li>                
                    <ul>
                        <%for(int i=0;i<collectionList.size();i++){%>
                        <li class="collection"><img id="treeButton_<%=i%>" name="treeButton" style="padding-left:6px" <%if(current.get(0).equals(((Collection)collectionList.get(i)).getId())){%> src="images/expand1_uf.gif" <%}else{%> src="images/expand1_f.gif" <%}%> src="images/expand1_f.gif" alt="+" onclick="swapImage(<%=i%>,<%=((Collection)collectionList.get(i)).getBookList().size()%>);" /><a href="Controller?action=showCollectionPage&collId=<%=((Collection)collectionList.get(i)).getId()%>" style="margin-left:10px; font-size: 12px"><%=((LangString)((Collection)collectionList.get(i)).getTitleList().get(0)).getValue()%></a><input type="image" onclick="location.href='Controller?action=newBookPage&collId=<%=((Collection)collectionList.get(i)).getId()%>'" style="margin-left:10px; padding-top:5px;" src="images/book_add.png"/></li>
                            <ul>
                              <%for(int j=0;j<((Collection)collectionList.get(i)).getBookList().size();j++){%>
                                    <li id="collection_<%=i%>_book_<%=j%>" class="sub_collection" <%if(current.get(0).equals(((Collection)collectionList.get(i)).getId())){%> style="display:list-item" <%}else{%> style="display:none" <%}%>><a href="Controller?action=showBookPage&collId=<%=((Collection)collectionList.get(i)).getId()%>&bookId=<%=((Book)((Collection)collectionList.get(i)).getBookList().get(j)).getBookId()%>"><%=((LangString)((Book)((Collection)collectionList.get(i)).getBookList().get(j)).getTitleList().get(0)).getValue()%></a></a><input type="image" onclick="location.href='Controller?action=exportDCFormatPage&collId=<%=((Collection)collectionList.get(i)).getId()%>&bookId=<%=((Book)((Collection)collectionList.get(i)).getBookList().get(j)).getBookId()%>&title=<%=((LangString)((Book)((Collection)collectionList.get(i)).getBookList().get(j)).getTitleList().get(0)).getValue()%>'" style="margin-left:10px; padding-top:5px;" src="images/download.png"/></li>
                                <%}%>
                            </ul>
                        <%}%>
                    </ul>
                </li>
            </ul>
          </div>   
          <div id="RightMenu">
              <ul>                
                <li class="collectionHeader"><a style="font-size: 13px">Collection</a></li>  
                    <div class="gray">
                        <button style="margin: 5px" class="blue_button" onclick="location.href='Controller?action=editCollectionPage&collId=<%=current.get(0)%>'">Edit</button>
                        <button style="float:right; margin: 5px" class="red_button" onclick="location.href='Controller?action=showDeletedPage&collId=<%=current.get(0)%>'">Delete</button>
                    </div> 
                    <div style="float:left; margin:10px;">
                        <form>
                            <table>
                                <%if(collection.getTitleList()!=null){
                                        for(int j=0;j<collection.getTitleList().size();j++){%>
                                            <tr >
                                                <td><b>Title:</b></td>
                                                <td><%=((LangString)collection.getTitleList().get(j)).getValue()%></td>
                                                <td><b>Lang:</b></td>
                                                <td><%=((LangString)collection.getTitleList().get(j)).getLang()%></td>
                                            </tr>
                                    <%}}%>                                                          
                            </table>
                        </form>
                    </div>
                    
                </li>
            </ul>            
          </div>
    </body>
</html>

