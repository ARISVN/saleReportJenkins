"# saleReportJenkins" ;
1.Create Application– Business Logic
-This UI is used to add new application (for both android).
-Form  Item will be displayed base on type of application
-Save funtion: Add New Appliction
   +Vaidate data  (client and server)
   +Save app data into database
   +System will run function get data from release date to present from ituneConnect (use Autoingestion) and google play (use gsutil). (File to database)
   +Set  Job  run  daily to get new data
   +Redirect to application management UI
2.Add Item for Application – Business Logic
-This UI is used to add new Item for application (for both android).
-Form  Item will be displayed base on type of application
-Save funtion: Add New item for  appliction
	+Vaidate data  (client and server)
	+Save app item data into database
	+Redirect to application management UI
3.List of Application– Business Logic
-This UI is used to display list of application (for both android).
-From this UI, user can 
	+Move to detais application page
	+Move to sale report page
	+Move to edit applicaton page
	+Move to add item page
4.Application Details – Business Logic
-This UI is used to display details of application (for both android).
-From this UI, user can 
	+View application details 
	+Move to pop up edit item



   
