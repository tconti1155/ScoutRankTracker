# ScoutRankTracker
A scout rank tracker for scouts to keep tract of ranks they completed
This is an android application that uses expandable list view to keep track of each requirement for each rank in boy scouts of america.
I created this application to learn more about android. The goal is to have a simple application that is free for scouts while
they transverse there way to eagle. 

It starts off with the user have access to all the ranks and being to click on which requirements they have done for each rank. 
When the user hits a requirement(reg), it first checks to see if the reg has been completed. then stores the reg using a boolean array 
and storing it into a SQLite database. The user does have ability to reclick a reg to un-highlight as complete.

when the user returns to the application they can click on review button and it brings up all there previously highlight regs.

Things to still work on:

I would like to add the ability to have check mark appear when they finish all the regs for a rank. 
I would also like a way to keep there information highlighted between moving ranks and different applications. Though that is limited to the 
ExpandableListView I chose to use. 

