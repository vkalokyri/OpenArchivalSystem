var intImage = 2;
function swapImage(i, j) {
    switch (intImage) {
     case 1:
       document.getElementById('treeButton_'+i).src = "images/expand1_f.gif";
       intImage = 2
       for(var k=0; k<j; k++)
            document.getElementById('collection_'+i+'_book_'+k).style.display='none';
       return(false);
    case 2:
       document.getElementById('treeButton_'+i).src = "images/expand1_uf.gif";
       intImage = 1
        for(var k=0; k<j; k++)
            document.getElementById('collection_'+i+'_book_'+k).style.display='list-item';
       return(false);
   }
}

function addRow(tableID, element, lang, name) {
 
    var table = document.getElementById(tableID);

    var rowCount = table.rows.length-1;
    var row = table.insertRow(rowCount);
    row.setAttribute("id","row_"+rowCount);
    var cell1 = row.insertCell(0);
    cell1.innerHTML = element;

    var cell2 = row.insertCell(1);
    var element1 = document.createElement("input");
    element1.type = "text";
    element1.name= name;
    element1.style.width = "273px";
    cell2.appendChild(element1);
    
    if(lang==1){
        var cell3 = row.insertCell(2);
        cell3.innerHTML = 'Lang:';

        var cell4 = row.insertCell(3);
        var element2 = document.createElement("input");
        element2.type = "text";
        element2.name= name+'Lang';
        element2.style.width = "139px";
        cell4.appendChild(element2);
        var cell5 = row.insertCell(4);
        var img = document.createElement('img');
        img.src = 'images/delete.gif';
        img.style.cursor = 'pointer';
        img.style.cssFloat = 'left';
        img.setAttribute ("onclick", "deleteRow('"+tableID+"',this)");
        cell5.appendChild(img);
    }else{
        var cell5 = row.insertCell(2);
        var img = document.createElement('img');
        img.src = 'images/delete.gif';
        img.style.cursor = 'pointer';
        img.style.cssFloat = 'left';
        img.setAttribute ("onclick", "deleteRow('"+tableID+"',this)");
        cell5.appendChild(img);
    }
    
   

}

function deleteRow(tableID,src) {
     var oRow = src.parentElement.parentElement;  
    
    //once the row reference is obtained, delete it passing in its rowIndex   
    document.getElementById(tableID).deleteRow(oRow.rowIndex);  
    //var row = document.getElementById(rowID);
    //row.parentNode.removeChild(row);

}