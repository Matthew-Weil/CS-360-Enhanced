# Overview

There are two file projects here
- The original Artifcact subnmitted for completion of the final project for CS-360
- The enhanced Artifact submitted for completion of the final project for CS-499

-------
## Algorithms and Datastructures Enhancement

<a href="https://github.com/Matthew-Weil/CS-360-Enhanced" target="_blank">Artifact Repository</a>

&emsp;This artifact was created for the completion of the final project of CS-360: Mobile Architecture and Programming. I was tasked to create an inventory management mobile application that would run on Android OS. It is needed to provide the user with the ability to track items as well as their quantity. They needed to be able to add and remove items from their tracker, as well as modify the quantity of any item. The algorithm I used for the creation and deletion of items within the inventory did not offer enough functionality for the user to use the app up to its full potential. I added the ability for the user to bulk delete items from the database shown in Figure 4, which got rid of an unsightly deleted button on every single item row.

![image](weil-artifact-2-figure-1.jpg)

**Figure 4**

&emsp;The user can delete items by clicking the select button and tapping on all of the items they would like to delete. Once those items are selected, they can click the delete button to permanently delete the selected items from the database.

&emsp;I also changed how a user will add an item to the database by creating a new pop-up window, allowing them to enter item information before it is added to the database. This new screen can be seen in Figure 5.

![image](weil-artifact-2-figure-2.jpg)

**Figure 5**

&emsp;Originally, a default item with a name of ‘item name #’ and quantity ‘0’ would be immediately added to the database when the user clicks to add a new item. The creation of this pop-up window allows the user to avoid the creation of that item and instead simply create the item they desire.

&emsp;I added this item to my portfolio because it is a display of my understanding of developing an application around user needs and executing those ideas in a complete and clean manner. This project is one where we were not provided with any sort of template, all of the code was written by me. Since the project is quite large with many lines of code, I thought that it would be a good item to showcase my ability to write complete applications. The completeness of my implementation completely covers all possible scenarios that a user could possibly do while deleting or adding an item. For example, when a user is selecting items to delete but decides they do not want to delete anymore items and click the select button again, the select list is cleared and the items are set back to the default color. This segment of code is shown in Figure 6.

![image](weil-artifact-2-figure-3.jpg)

**Figure 6**

&emsp;I originally set out to complete the course outcome of outcome number 4:	Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices for the purpose of implementing computer solutions that deliver value and accomplish industry-specific goals. I would say that I successfully completed this goal by implementing new and better solutions to adding and deleting an item from the database. However, as I completed this project I continued to find myself accomplishing the goals of number 3: Design and evaluate computing solutions that solve a given problem using algorithmic principles and computer science practices and standards appropriate to its solution, while managing the trade-offs involved in design choice. By adding this completely new algorithm for thuse user to create and  The user experience is greatly improved by the techniques that I used, which will increase the desire that a user would have to use the app.
	
&emsp;As I completed this artifact, I continued to learn more about Android studio and using its data structures and objects. I used a TableLayout to display the inventory information, so I learned a lot about how its specific rows work together and how to access the information within each row. I also learned a lot about callbacks and creating sections of code that need to be run when a certain condition is met. These callbacks make the UI usable and customizable by the user since they are able to decide when a section of code needs to be run by tapping a button. Overall, the process of creating this artifact was enjoyable and educational.

--------------

## Database Enhancement

<a href="https://github.com/Matthew-Weil/CS-360-Enhanced" target="_blank">Artifact Repository</a>

&emsp;This project was originally from CS-360 Mobile architecture and design. It was the final project where I was tasked to create a mobile application on android studio to track inventory items with separate user accounts. Prior to my improvement of this project, every user would have their own inventory showing only their items. In a real-world application, there will be times where multiple users need to access and modify the quantity of the same items. For instance, in a single warehouse there may be many user accounts, but they are all working with the same inventory and need to be able to see the modifications of other users. To facilitate this functionality, I added groups to the application, any user can join a group and now look at group items instead of their own. Now every employee within the warehouse can be in the same group and modify the same items.
	
&emsp;This artifact is all about modifying a database to enhance the user experience. I needed to create new tables within my database, new SQLite queries, and parse and process information from the database for display to the user. I also needed to create a new user interface page to allow a user to join a specific group instead of only their own database. I included this artifact because it was a good reflection of my understanding of databases and my ability to create and modify them.
	
&emsp;To create the group functionality, I created a new ‘groups’ table within the database. This table needed to have a many-to-many relationship with the ‘user’ table, so I also created a bridge table called ‘groupUserBridgeTable’. I used an Android Room Database to store these tables and create SQLite database queries to get data back to my main application. An example of the groupDao SQLite queries is shown in Figure 7.

![image](weil-artifcat-3-figure-1.jpg)

**Figure 7**

&emsp;Since I was using a bridge table to allow the many-to-many relationship, it was sometimes necessary for me to user types of JOIN’s to get the proper information from the database.
	
&emsp;In addition to the modification of the database itself, I also needed to create a new page where the user can join or create a new group. This page was accessed from the bottom left of the main application menu bar, which is always present for a logged in user. An example of the group page is shown in Figure 8.

![image](weil-artifact-3-figure-2.jpg)

**Figure 8**

&emsp;It is still possible to use the application without a group at all, focusing solely on your own inventory. Within my Inventory Management file, I added logic to check if the user was in a group, to display the corresponding items (group items / user items). This logic is shown in Figure 9.

![image](weil-artifact-3-figure-3.jpg)

**Figure 9**

&emsp;This logic checks every group that the user could possibly be in. If the list of groups is NOT empty, then it will get every item from the user’s groups. If the list of groups is empty, then it will simply grab only the items that belong to the user itself. Items is eventually displayed within a TableLayout on the inventory information page.

&emsp;I did meet the course outcomes that I planned to meet as well as some additional ones through the creation of this artifact. Much of it was focused on modifying the database to allow for groups to be seamlessly integrated. One of the course outcomes accomplished was number one, to enable diverse audiences to support organization decision-making. The creation of groups allows many new users to find functionality within the app. It still allows users to use the app if they want to only use the inventory for themselves, but also adds new functionality to join a group. This artifact also completed number 5, develop a security mindset that anticipates adversarial exploits. SQL injections are something that I always kept in mind while creating this artifact, proper string parsing and proper SQLite querying was used to prevent this from happening. 

&emsp;While making this artifact I did learn a lot about more complicated queries such as using different types of JOIN’s within my queries. It is something I have done before, but not to the extent that I needed to for this project. It also helped me to further understand the complexity of creating full-scale projects and the necessity of modularity within a project. There were many times that I needed to run specific functions over again, called in a different spot. Overall, this project is a great display of my coding ability, and large-scale project development. 
