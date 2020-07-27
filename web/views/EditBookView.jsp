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

<% Book book = (Book)request.getAttribute("model");
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
                        <li class="collection"><img id="treeButton_<%=i%>" name="treeButton" style="padding-left:6px" <%if(current.get(0).equals(((Collection)collectionList.get(i)).getId())){%> src="images/expand1_uf.gif" <%}else{%> src="images/expand1_f.gif" <%}%> src="images/expand1_f.gif" alt="+" onclick="swapImage(<%=i%>,<%=((Collection)collectionList.get(i)).getBookList().size()%>);" /><a href="Controller?action=showCollectionPage&collId=<%=((Collection)collectionList.get(i)).getId()%>" style="margin-left:10px; font-size: 12px"><%=((LangString)((Collection)collectionList.get(i)).getTitleList().get(0)).getValue()%></a><img style="margin-left:10px; padding-top:5px;"src="images/book_add.png"/></li>
                            <ul>
                              <%for(int j=0;j<((Collection)collectionList.get(i)).getBookList().size();j++){%>
                                    <li id="collection_<%=i%>_book_<%=j%>" class="sub_collection" <%if(current.get(0).equals(((Collection)collectionList.get(i)).getId())){%> style="display:list-item" <%}else{%> style="display:none" <%}%>><a href="Controller?action=showBookPage&collId=<%=((Collection)collectionList.get(i)).getId()%>&bookId=<%=((Book)((Collection)collectionList.get(i)).getBookList().get(j)).getBookId()%>"><%=((LangString)((Book)((Collection)collectionList.get(i)).getBookList().get(j)).getTitleList().get(0)).getValue()%></a></a><input type="image" onclick="location.href='Controller?action=exportDCFormatPage&collId=<%=((Collection)collectionList.get(i)).getId()%>&bookId=<%=((Book)((Collection)collectionList.get(i)).getBookList().get(j)).getBookId()%>&title=<%=((LangString)((Book)((Collection)collectionList.get(i)).getBookList().get(j)).getTitleList().get(0)).getValue()%>'" style="margin-left:10px; padding-top:5px;" src="images/download.png"/></li></li>
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
                  <form name ="bookMetadata"  method="Post"  action="Controller?action=showBookUpdatePage&collId=<%=current.get(0)%>&bookId=<%=current.get(1)%>">
                    <div class="gray">
                        <button type="submit" style="float:right; margin: 5px" class="green_button">Save</button>
                        <button style="float:right; margin: 5px" class="red_button" onclick="location.href='Controller?action=showDeletedPage&collId=<%=current.get(0)%>&bookId=<%=current.get(1)%>'">Delete</button>
                    </div> 
                    <div>
                        <%if(book.getUri()!=null){
                            if(!book.getUri().equals("")){%>
                                <img style="margin-left:10px; float: left" height="200px" width="200px" src="<%=book.getUri()%>"/>
                            <%}else{%>
                                <img style="margin-left:10px; float: left" height="200px" width="180px" src="images/no-image.jpg"/>
                            <%}%>
                        <%}else{%>
                            <img style="margin-left:10px; float: left" height="200px" width="180px" src="images/no-image.jpg"/>
                        <%}%>
                    </div>
                    <div style="margin-left:200px;">
                        
                            <table id="titleTable">
                                <%if(book.getTitleList().size()!=0){
                                        for(int j=0;j<book.getTitleList().size();j++){%>
                                            <tr>
                                                <td>Title:</td>
                                                <td><input style="width:273px" type="text" name="title" value="<%=((LangString)book.getTitleList().get(j)).getValue()%>"/></td>
                                                <td>Lang:</td>
                                                <td><input id="titleLang_<%=j%>" style="width:139px" type="text" name="titleLang" value="<%=((LangString)book.getTitleList().get(j)).getLang()%>" /></td>
                                                <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('titleTable',this)"/></td>
                                            </tr>
                                    <%}%>
                                            <tr>
                                                <td colspan="4"><a style="cursor:pointer" onclick="addRow('titleTable','Title:', '1', 'title')"><img style="cursor:pointer" src="images/add.jpg"/> add title</a></td>
                                            </tr>
                                    <%}else{%>
                                            <tr>
                                                <td>Title:</td>
                                                <td><input style="width:273px" type="text" name="title"/></td>
                                                <td>Lang:</td>
                                                <td><input style="width:139px" type="text" name="langTitle" /></td>
                                                <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('titleTable',this)"/></td>
                                            </tr>
                                            <tr>
                                                <td colspan="4"><a style="cursor:pointer" onclick="addRow('titleTable','Title:', '1' , 'title')"><img style="cursor:pointer" src="images/add.jpg"/> add title</a></td>
                                            </tr>
                                    <%}%>
                             </table>
                             <hr>
                             <table id="creatorTable">
                                    <%if(book.getCreatorList().size()!=0){
                                        for(int j=0;j<book.getCreatorList().size();j++){%>
                                            <tr >
                                                <td>Creator:</td>
                                                <td><input style="width:273px" type="text" name="creator" value="<%=book.getCreatorList().get(j)%>"/></td>
                                                <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('creatorTable',this)"/></td>
                                            </tr>
                                     <%}%>
                                            <tr>
                                                <td colspan="4"><a style="cursor:pointer" onclick="addRow('creatorTable','Creator', '0', 'creator')"><img style="cursor:pointer" src="images/add.jpg"/> add creator</a></td>
                                            </tr>
                                    <%}else{%>
                                        <tr >
                                            <td>Creator:</td>
                                            <td><input style="width:273px" type="text" name="creator" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('creatorTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('creatorTable','Creator', '0', 'creator')"><img style="cursor:pointer" src="images/add.jpg"/> add creator</a></td>
                                        </tr>
                                     <%}%>
                             </table>
                              <hr>
                              <table id="isbnTable">
                                    <%if((book.getIsbn()!=null)){%>
                                        <tr >
                                            <td>Isbn:</td>
                                            <td><input style="width:273px" type="text" name="isbn" value="<%=book.getIsbn()%>" /></td>
                                        </tr>                                        
                                    <%}else{%>
                                        <tr >
                                            <td>Isbn:</td>
                                            <td><input style="width:273px" type="text" name="isbn" /></td>
                                        </tr>                                       
                                    <%}%> 
                              </table>
                              <hr>
                              <table id="publisherTable">
                                    <%if((book.getPublisherList().size()!=0)){
                                        for(int j=0;j<book.getPublisherList().size();j++){%>
                                        <tr >
                                            <td>Publisher:</td>
                                            <td><input  style="width:273px" type="text" name="publisher" value="<%=book.getPublisherList().get(j)%>" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('publisherTable',this)"/></td>
                                        </tr>
                                    <%}%>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('publisherTable','Publisher', '0', 'publisher')"><img style="cursor:pointer" src="images/add.jpg"/> add publisher</a></td>
                                        </tr>
                                    <%}else{%>
                                         <tr>
                                            <td>Publisher:</td>
                                            <td><input  style="width:273px" type="text" name="publisher"  /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('publisherTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('publisherTable','Publisher', '0', 'publisher')"><img style="cursor:pointer" src="images/add.jpg"/> add publisher</a></td>
                                        </tr>
                                    <%}%> 
                              </table>
                               <hr>
                               <table id="editorTable">
                                    <%if((book.getEditorList().size()!=0)){
                                        for(int j=0;j<book.getEditorList().size();j++){%>
                                        <tr >
                                            <td>Editor:</td>
                                            <td><input  style="width:273px" type="text" name="editor" value="<%=book.getEditorList().get(j)%>"/></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('editorTable',this)"/></td>
                                        </tr>
                                    <%}%>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('editorTable','Editor', '0', 'editor')"><img style="cursor:pointer" src="images/add.jpg"/> add editor</a></td>
                                        </tr>
                                    <%}else{%>
                                        <tr >
                                            <td>Editor:</td>
                                            <td><input  style="width:273px" type="text" name="editor" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('editorTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('editorTable','Editor', '0', 'editor')"><img style="cursor:pointer" src="images/add.jpg"/> add editor</a></td>
                                        </tr>
                                    <%}%>   
                               </table>
                                <hr>
                                <table id="copyDateTable">
                                    <%if((book.getCopyrightDate()!=null)){%>
                                        <tr>
                                            <td>Copyright Date:</td>
                                            <td><input  style="width:273px" type="text" name="copyDate" value="<%=book.getCopyrightDate()%>"/></td>
                                        </tr>
                                        
                                    <%}else{%>
                                        <tr >
                                            <td>Copyright Date:</td>
                                            <td><input  style="width:273px" type="text" name="copyDate" /></td>
                                        </tr>
                                        
                                    <%}%> 
                                </table>
                                 <hr>
                                 <table id="publicDateTable">
                                    <%if((book.getPublicationDate()!=null)){%>                                     
                                        <tr >
                                            <td>Publication Date:</td>
                                            <td><input  style="width:273px" type="text" name="publicDate" value="<%=book.getPublicationDate()%>"/></td>
                                        </tr>                                        
                                    <%}else{%>
                                        <tr >
                                            <td>Publication Date:</td>
                                            <td><input  style="width:273px" type="text" name="publicDate" /></td>
                                        </tr>
                                    <%}%>
                                 </table>
                                  <hr>
                                 <table id="reprintDateTable">
                                    <%if((book.getReprintingDate()!=null)){%>
                                        <tr>
                                            <td>Reprinting Date:</td>
                                            <td><input  style="width:273px" type="text" name="reprintDate" value="<%=book.getReprintingDate()%>"/></td>
                                        </tr>
                                    <%}else{%>
                                        <tr>
                                            <td>Reprinting Date:</td>
                                            <td><input  style="width:273px" type="text" name="reprintDate" /></td>
                                        </tr>
                                     <%}%>   
                                 </table>
                                  <hr>
                                 <table id="descriptionTable">
                                    <%if((book.getDescriptionList().size()!=0)){
                                        for(int j=0;j<book.getDescriptionList().size();j++){%>
                                        <tr>
                                            <td>Description:</td>
                                            <td><input  style="width:273px" type="text" name="description" value="<%=((LangString)book.getDescriptionList().get(j)).getValue()%>"/></td>
                                            <td>Lang:</td>
                                            <td><input id="descriptionLang_<%=j%>"style="width:139px" type="text" name="descriptionLang" value="<%=((LangString)book.getDescriptionList().get(j)).getLang()%>"/></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('descriptionTable',this)"/></td>
                                        </tr>
                                    <%}%>
                                            <tr>
                                                <td colspan="4"><a style="cursor:pointer" onclick="addRow('descriptionTable','Description', '1', 'description')"><img style="cursor:pointer" src="images/add.jpg"/> add description</a></td>
                                            </tr>
                                    <%}else{%>
                                        <tr>
                                            <td>Description:</td>
                                            <td><input  style="width:273px" type="text" name="description"/></td>
                                            <td>Lang:</td>
                                            <td><input style="width:139px" type="text" name="descriptionLang"/></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('descriptionTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('descriptionTable','Description', '1', 'description')"><img style="cursor:pointer" src="images/add.jpg"/> add description</a></td>
                                        </tr>
                                    <%}%>  
                                 </table>
                                  <hr>
                                 <table id="noOfPagesTable">
                                    <%if((book.getNoOfPages()!=0)){%>
                                        <tr >
                                            <td>Number of pages:</td>
                                            <td><input  style="width:273px" type="text" name="noOfPages" value="<%=book.getNoOfPages()%>"/></td>
                                        </tr>
                                    <%}else{%>
                                        <tr >
                                            <td>Number of pages:</td>
                                            <td><input  style="width:273px" type="text" name="noOfPages" /></td>
                                        </tr>
                                    <%}%>   
                                 </table>
                                  <hr>
                                  <table id="publicPlaceTable">
                                    <%if((book.getPublicationPlace().size()!=0)){
                                        for(int j=0;j<book.getPublicationPlace().size();j++){%>
                                        <tr>
                                            <td>Publication place:</td>
                                            <td><input  style="width:273px" type="text" name="publicPlace" value="<%=book.getPublicationPlace().get(j)%>"/></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('publicPlaceTable',this)"/></td>
                                        </tr>
                                      <%}%>
                                            <tr>
                                                <td colspan="4"><a style="cursor:pointer" onclick="addRow('publicPlaceTable','Publication place', '0', 'publicPlace')"><img style="cursor:pointer" src="images/add.jpg"/> add publication place</a></td>
                                            </tr>
                                    <%}else{%>
                                        <tr>
                                            <td>Publication place:</td>
                                            <td><input  style="width:273px" type="text" name="publicPlace" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('publicPlaceTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('publicPlaceTable','Publication place', '0', 'publicPlace')"><img style="cursor:pointer" src="images/add.jpg"/> add publication place</a></td>
                                        </tr>
                                     <%}%>  
                                  </table>
                                   <hr>
                                  <table id="typeTable">
                                    <%if((book.getType().size()!=0)){
                                        for(int j=0;j<book.getType().size();j++){%>
                                        <tr >
                                            <td>Type:</td>
                                            <td><input  style="width:273px" type="text" name="type" value="<%=book.getType().get(j)%>" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('typeTable',this)"/></td>
                                        </tr>
                                    <%}%>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('typeTable','Type', '0', 'type')"><img style="cursor:pointer" src="images/add.jpg"/> add type</a></td>
                                        </tr>
                                    <%}else{%>
                                        <tr>
                                            <td>Type:</td>
                                            <td><input  style="width:273px" type="text" name="type" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('typeTable',this)"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('typeTable','Type', '0', 'type')"><img style="cursor:pointer" src="images/add.jpg"/> add type</a></td>
                                        </tr>
                                    <%}%>
                                  </table>
                                   <hr>
                                  <table id="rightsTable">
                                    <%if((book.getRights()!=null)){%>
                                        <tr  >
                                            <td>Rights:</td>
                                            <td><input style="width:273px" type="text" name="rights" value="<%=book.getRights()%>"/></td>
                                        </tr>
                                    <%}else{%>
                                        <tr  >
                                            <td>Rights:</td>
                                            <td><input style="width:273px" type="text" name="rights" /></td>
                                        </tr>
                                    <%}%>
                                  </table>
                                   <hr>
                                  <table id="formatTable">
                                    <%if((book.getFormat()!=null)){%>
                                        <tr >
                                            <td>Format:</td>
                                            <td><input style="width:273px" type="text" name="format" value="<%=book.getFormat()%>"/></td>
                                        </tr>
                                    <%}else{%>
                                        <tr >
                                            <td>Format:</td>
                                            <td><input style="width:273px" type="text" name="format" /></td>
                                        </tr>
                                    <%}%>
                                  </table>
                                   <hr>
                                  <table id="uriTable">
                                    <%if((book.getUri()!=null)){%>
                                    <tr  >
                                        <td>Object Uri:</td>
                                        <td><input  style="width:273px" type="text" name="uri" value="<%=book.getUri()%>"/></td>
                                    </tr>
                                    <%}else{%>
                                        <tr  >
                                            <td>Object Uri:</td>
                                            <td><input  style="width:273px" type="text" name="uri" /></td>
                                        </tr>
                                    <%}%>
                                  </table>
                                   <hr>
                                  <table id="commentsTable">
                                    <%if((book.getCommentsList().size()!=0)){
                                        for(int j=0;j<book.getCommentsList().size();j++){%>
                                        <tr >
                                            <td>Comments:</td>
                                            <td><input  style="width:273px" type="text" name="comments" value="<%=book.getCommentsList().get(j)%>"/></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('commentsTable',this)"/></td>
                                        </tr>  
                                   <%}%>
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('commentsTable','Comments', '0', 'comments')"><img style="cursor:pointer" src="images/add.jpg"/> add comment</a></td>
                                        </tr>
                                    <%}else{%>
                                        <tr >
                                            <td>Comments:</td>
                                            <td><input  style="width:273px" type="text" name="comments" /></td>
                                            <td><img style="float:left; cursor:pointer" src="images/delete.gif" onclick="deleteRow('commentsTable',this)"/></td>
                                        </tr> 
                                        <tr>
                                            <td colspan="4"><a style="cursor:pointer" onclick="addRow('commentsTable','Comments', '0', 'comments')"><img style="cursor:pointer" src="images/add.jpg"/> add comment</a></td>
                                        </tr>
                                    <%}%>
                                </table>
                        </div>
                  </form>                
              </ul>            
          </div>
    </body>
</html>

