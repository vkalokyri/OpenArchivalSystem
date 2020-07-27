# OpenArchivalSystem
Open Archival System: XML Database, Application Programming Interface, Client Test Application

This project was part of the "Design, Development and Management of Web Services" class in Technical University of Crete (Fall 2011-2012). 

It is an open archival system which manages descriptions and digital representations of objects. Such objects may refer to cultural objects (e.g. the Painting "The Vision of Saint John" by "Domenikos Theotokopoulos", the Publication entitled "The Transaction. Concept: Virtues and Limitations" by "Jim Gray", etc. ).
This system provides a range of services for managing and searching for information about the objects or collections of objects it maintains.

We first modeled the information that pertains to objects and collection of objects as well as their descriptions (metadata). The modeling was done by using the UML language and specifically using Class Diagrams. We selected the domain of a real world application to be: "Libraries".

Next, we created the appropriate XML Schema(s) corresponding to the model we developed. The XML documents that obey the Schema/ s describe one or more objects and are managed by an XML database that we developed for this purpose.

We also developed a client application to create, read, update, and delete items or collections. It is important that every object in the repository is well-formed and valid according to the XML format of their description.

The repository supports searching in the form of XQuery queries. Examples of Questions that are allowed are the following:
• Objects created after 01/01/2000
• Objects created by "John Smith" and are of the type "TEXT"
• Items that belong to the thematic category "Web Services" and were published at a conference of "ACM"
• Objects that belong to the thematic category "Impressionism" and were created in "France" or "Italy".
. ... etc.

Finally, the system supports retrieving object descriptions according to the Dublin Core (DC) metadata standard. The conversion of internal (in the Database) metadata to this standard was done using XSLT technology.

Technologies: 
1. Java, Java Swing / Java Servlets / Java Server Pages
2. eXist XML Database
3. XMLBeans (XML Binding) Framework
4. XML, XSLT, XQUERY, XPATH
