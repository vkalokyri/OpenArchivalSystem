<%-- 
    Document   : index
    Created on : Dec 14, 2011, 3:51:39 PM
    Author     : suitcase
--%>

<%@page import="dao.DAOFactory"%>
<%@page import="model.Book"%>
<%@page import="model.Collection"%>
<%@page import="model.LangString"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "_//W3C//DTD HTML 4.01 Transitional//EN"
    "Http://www.w3.org/TR/html4/loose.dtd">

<%  List<Collection> collectionList = DAOFactory.getDAOFactory(DAOFactory.EXIST).getCollectionDAO().getCollectionList();
    List<String> current = (ArrayList<String>)session.getAttribute("CurrentTree");
    if(current==null){
            current=new ArrayList();            
        }
    current.add(0, "-1");
    current.add(1, "-1");
    session.setAttribute("CurrentTree", current);
    current = (ArrayList)session.getAttribute("CurrentTree");%>

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
                        <li class="collection"><img id="treeButton_<%=i%>" name="treeButton" style="padding-left:6px" src="images/expand1_f.gif" alt="+" onclick="swapImage(<%=i%>,<%=((Collection)collectionList.get(i)).getBookList().size()%>,<%=collectionList.size()%>);" /><a href="Controller?action=showCollectionPage&collId=<%=((Collection)collectionList.get(i)).getId()%>" style="margin-left:10px; font-size: 12px; text-decoration: none"><%=((LangString)((Collection)collectionList.get(i)).getTitleList().get(0)).getValue()%></a><input type="image" onclick="location.href='Controller?action=newBookPage&collId=<%=((Collection)collectionList.get(i)).getId()%>'" style="margin-left:10px; padding-top:5px;" src="images/book_add.png"/></li>
                            <ul>
                              <%for(int j=0;j<((Collection)collectionList.get(i)).getBookList().size();j++){%>
                                    <li id="collection_<%=i%>_book_<%=j%>" class="sub_collection" style="display:none"><a href="Controller?action=showBookPage&collId=<%=((Collection)collectionList.get(i)).getId()%>&bookId=<%=((Book)((Collection)collectionList.get(i)).getBookList().get(j)).getBookId()%>"><%=((LangString)((Book)((Collection)collectionList.get(i)).getBookList().get(j)).getTitleList().get(0)).getValue()%></a><input type="image" onclick="location.href='Controller?action=exportDCFormatPage&collId=<%=((Collection)collectionList.get(i)).getId()%>'" style="margin-left:10px; padding-top:5px;" src="images/download.png"/></li>
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
                    <div style="float:right;">
                        <form>
                            <table>
                            <%for(int c=0;c<collectionList.size();c++){
                                 for(int i=0;i<((Collection)collectionList.get(c)).getBookList().size();i++){
                                    if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getTitleList()!=null)){
                                        for(int j=0;j<((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getTitleList().size();j++){%>
                                            <tr id="title_<%=c%>_<%=i%>" style="display: none">
                                                <td>Title:</td>
                                                <td><input size="30px" type="text" name="title" value="<%=((LangString)((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getTitleList().get(j)).getValue()%>"/></td>
                                                <td>Lang:</td>
                                                <td><input id="titleLang_<%=j%>" size="10px" type="text" name="langTitle" value="<%=((LangString)((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getTitleList().get(j)).getLang()%>" /></td>
                                            </tr>
                                    <%}}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getCreatorList()!=null)){
                                        for(int j=0;j<((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getCreatorList().size();j++){%>
                                            <tr id="creator_<%=i%>_<%=j%>" style="display: none">
                                                <td>Creator:</td>
                                                <td><input size="30px" type="text" name="creator" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getCreatorList().get(j)%>"/></td>
                                            </tr>
                                    <%}}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getIsbn()!=null)){%>
                                        <tr id="isbn_<%=i%>" style="display: none">
                                            <td>Isbn:</td>
                                            <td><input size="30px" type="text" name="isbn" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getIsbn()%>" /></td>
                                        </tr>
                                    <%}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getPublisherList()!=null)){
                                        for(int j=0;j<((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getPublisherList().size();j++){%>
                                    <tr id="publisher_<%=i%>_<%=j%>" style="display: none">
                                        <td>Publisher:</td>
                                        <td><input  size="30px" type="text" name="publisher" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getPublisherList().get(j)%>" /></td>
                                    </tr>
                                    <%}}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getEditorList()!=null)){
                                        for(int j=0;j<((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getEditorList().size();j++){%>
                                    <tr id="editor_<%=i%>_<%=j%>" style="display: none">
                                        <td>Editor:</td>
                                        <td><input  size="30px" type="text" name="editor" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getEditorList().get(j)%>"/></td>
                                    </tr>
                                    <%}}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getCopyrightDate()!=null)){%>
                                    <tr id="copyDate_<%=i%>" style="display: none">
                                        <td>Copyright Date:</td>
                                        <td><input  size="30px" type="text" name="copyDate" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getCopyrightDate()%>"/></td>
                                    </tr>
                                    <%}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getPublicationDate()!=null)){%>                                     
                                    <tr id="publicDate_<%=i%>" style="display: none">
                                        <td>Publication Date:</td>
                                        <td><input  size="30px" type="text" name="publicDate" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getPublicationDate()%>"/></td>
                                    </tr>
                                    <%}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getReprintingDate()!=null)){%>
                                    <tr id="reprintDate_<%=i%>" style="display: none">
                                        <td>Reprinting Date:</td>
                                        <td><input  size="30px" type="text" name="reprintDate" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getReprintingDate()%>"/></td>
                                    </tr>
                                    <%}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getDescriptionList()!=null)){
                                        for(int j=0;j<((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getDescriptionList().size();j++){%>
                                    <tr id="description_<%=i%>_<%=j%>" style="display: none">
                                        <td>Description:</td>
                                        <td><input  size="30px" type="text" name="description" value="<%=((LangString)((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getDescriptionList().get(j)).getValue()%>"/></td>
                                        <td>Lang:</td>
                                        <td><input id="descriptionLang_<%=j%>"size="10px" type="text" name="descriptLang" value="<%=((LangString)((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getDescriptionList().get(j)).getLang()%>"/></td>
                                    </tr>
                                    <%}}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getNoOfPages()!=0)){%>
                                    <tr id="noOfPages_<%=i%>" style="display: none">
                                        <td>Number of pages:</td>
                                        <td><input  size="30px" type="text" name="noOfPages" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getNoOfPages()%>"/></td>
                                    </tr>
                                    <%}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getPublicationPlace()!=null)){
                                        for(int j=0;j<((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getPublicationPlace().size();j++){%>
                                    <tr id="publicPlace_<%=i%>_<%=j%>" style="display: none">
                                        <td>Publication place:</td>
                                        <td><input  size="30px" type="text" name="publicPlace" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getPublicationPlace().get(j)%>"/></td>
                                    </tr>
                                    <%}}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getType()!=null)){
                                        for(int j=0;j<((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getType().size();j++){%>
                                    <tr id="type_<%=i%>_<%=j%>" style="display: none">
                                        <td>Type:</td>
                                        <td><input  size="30px" type="text" name="type" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getType().get(j)%>" /></td>
                                    </tr>
                                    <%}}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getRights()!=null)){%>
                                    <tr id="rights_<%=i%>" style="display: none">
                                        <td>Rights:</td>
                                        <td><input size="30px" type="text" name="rights" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getRights()%>"/></td>
                                    </tr>
                                    <%}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getFormat()!=null)){%>
                                    <tr id="format_<%=i%>" style="display: none">
                                        <td>Format:</td>
                                        <td><input size="30px" type="text" name="format" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getFormat()%>"/></td>
                                    </tr>
                                    <%}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getUri()!=null)){%>
                                    <tr id="uri_<%=i%>" style="display: none">
                                        <td>Object Uri:</td>
                                        <td><input  size="30px" type="text" name="noOfPages" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getUri()%>"/></td>
                                    </tr>
                                    <%}%>
                                    <%if((((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getCommentsList()!=null)){
                                        for(int j=0;j<((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getCommentsList().size();j++){%>
                                    <tr id="comment_<%=i%>_<%=j%>" style="display: none">
                                        <td>Comments:</td>
                                        <td><input  size="30px" type="text" name="noOfPages" value="<%=((Book)((Collection)collectionList.get(c)).getBookList().get(i)).getCommentsList().get(j)%>"/></td>
                                    </tr>  
                                    <%}}%>
                                <%}}%>                                                                 
                            </table>
                        </form>
                    </div>
                    
                </li>
            </ul>            
          </div>
    </body>
</html>
