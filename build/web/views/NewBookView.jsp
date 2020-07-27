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

<% List<Collection> collectionList = DAOFactory.getDAOFactory(DAOFactory.EXIST).getCollectionDAO().getCollectionList();
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
                <li class="collectionHeader"><a style="font-size: 13px">Book</a></li> 
                  <form name ="bookMetadata"  method="Post"  action="Controller?action=showBookUpdatePage&collId=<%=current.get(0)%>&bookId=-1">
                    <div class="gray">
                        <button type="submit" style="float:right; margin: 5px" class="green_button">Save</button>
                    </div>    
                    <div style="float:right;">
                        
                            <table id="titleTable">                                
                                <tr>
                                    <td>Title:</td>
                                    <td><input size="25px" type="text" name="title" /></td>
                                    <td>Lang:</td>
                                    <td><input id="titleLang" size="10px" type="text" name="titleLang"  /></td>
                                    <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('titleTable',this)"/></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><a style="cursor:pointer" onclick="addRow('titleTable','Title:', '1', 'title')"><img style="cursor:pointer" src="images/add.jpg"/> add title</a></td>
                                </tr>                                  
                             </table>
                             <hr>
                             <table id="creatorTable">
                                <tr >
                                    <td>Creator:</td>
                                    <td><input size="25px" type="text" name="creator" /></td>
                                    <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('creatorTable',this)"/></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><a style="cursor:pointer" onclick="addRow('creatorTable','Creator', '0', 'creator')"><img style="cursor:pointer" src="images/add.jpg"/> add creator</a></td>
                                </tr>
                             </table>
                              <hr>
                              <table id="isbnTable">
                                <tr >
                                    <td>Isbn:</td>
                                    <td><input size="25px" type="text" name="isbn" /></td>
                                </tr>
                              </table>
                              <hr>
                              <table id="publisherTable">
                                    
                                         <tr>
                                            <td>Publisher:</td>
                                            <td><input  size="25px" type="text" name="publisher"  /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('publisherTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('publisherTable','Publisher', '0', 'publisher')"><img style="cursor:pointer" src="images/add.jpg"/> add publisher</a></td>
                                        </tr>

                              </table>
                               <hr>
                               <table id="editorTable">
                                   
                                        <tr >
                                            <td>Editor:</td>
                                            <td><input  size="25px" type="text" name="editor" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('editorTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('editorTable','Editor', '0', 'editor')"><img style="cursor:pointer" src="images/add.jpg"/> add editor</a></td>
                                        </tr> 
                               </table>
                                <hr>
                                <table id="copyDateTable">
                                   
                                        <tr >
                                            <td>Copyright Date:</td>
                                            <td><input  size="25px" type="text" name="copyDate" /></td>
                                        </tr>
                                </table>
                                 <hr>
                                 <table id="publicDateTable">
                                        <tr >
                                            <td>Publication Date:</td>
                                            <td><input  size="25px" type="text" name="publicDate" /></td>
                                        </tr>
                                 </table>
                                  <hr>
                                 <table id="reprintDateTable">
                                        <tr>
                                            <td>Reprinting Date:</td>
                                            <td><input  size="25px" type="text" name="reprintDate" /></td>
                                        </tr>  
                                 </table>
                                  <hr>
                                 <table id="descriptionTable">
                                        <tr>
                                            <td>Description:</td>
                                            <td><input  size="25px" type="text" name="description"/></td>
                                            <td>Lang:</td>
                                            <td><input size="10px" type="text" name="descriptionLang"/></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('descriptionTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('descriptionTable','Description', '1', 'description')"><img style="cursor:pointer" src="images/add.jpg"/> add description</a></td>
                                        </tr>
                                 </table>
                                  <hr>
                                 <table id="noOfPagesTable">
                                    
                                        <tr >
                                            <td>Number of pages:</td>
                                            <td><input  size="25px" type="text" name="noOfPages" /></td>
                                        </tr>
                                 </table>
                                  <hr>
                                  <table id="publicPlaceTable">
                                        <tr>
                                            <td>Publication place:</td>
                                            <td><input  size="25px" type="text" name="publicPlace" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('publicPlaceTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('publicPlaceTable','Publication place', '0', 'publicPlace')"><img style="cursor:pointer" src="images/add.jpg"/> add publication place</a></td>
                                        </tr> 
                                  </table>
                                   <hr>
                                  <table id="typeTable">
                                    
                                        <tr>
                                            <td>Type:</td>
                                            <td><input  size="25px" type="text" name="type" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('typeTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('typeTable','Type', '0', 'type')"><img style="cursor:pointer" src="images/add.jpg"/> add type</a></td>
                                        </tr>
                                  </table>
                                   <hr>
                                  <table id="rightsTable">
                                   
                                        <tr  >
                                            <td>Rights:</td>
                                            <td><input size="25px" type="text" name="rights" /></td>
                                        </tr>
                                  </table>
                                   <hr>
                                  <table id="formatTable">
                                  
                                        <tr >
                                            <td>Format:</td>
                                            <td><input size="25px" type="text" name="format" /></td>
                                        </tr>
                                  </table>
                                   <hr>
                                  <table id="uriTable">
                                    
                                        <tr  >
                                            <td>Object Uri:</td>
                                            <td><input  size="25px" type="text" name="uri" /></td>
                                        </tr>
                                  </table>
                                   <hr>
                                  <table id="commentsTable">
                                    
                                        <tr >
                                            <td>Comments:</td>
                                            <td><input  size="25px" type="text" name="comments" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('commentsTable',this)"/></td>
                                        </tr> 
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('commentsTable','Comments', '0', 'comments')"><img style="cursor:pointer" src="images/add.jpg"/> add comment</a></td>
                                        </tr>
                                </table>
                        </div>
                    <div>
                        <img style="margin-left:10px" height="200px" src="http://ocean1.ee.duth.gr/LaTeXBook/bookcover2.jpg"/>
                    </div>
                  </form>                
              </ul>            
          </div>
    </body>
</html>

