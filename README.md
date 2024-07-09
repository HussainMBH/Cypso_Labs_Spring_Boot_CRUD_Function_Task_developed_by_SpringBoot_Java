It's a spring boot crud operation system.
Just git clone and take project in your local.
can test API in Postman.
i used Workbench mySQL for data store, in there created taskapp database, and created one table that's name is task

01) Get All Tasks : Method Get = http://localhost:8080/tasks/tasks
02) Add a Task: Method POST = http://localhost:8080/tasks/task/add
    Body: JSON File = {
    "title": "old task",
    "description": "new task description.",
    "status": "procedure going"
    }

03) Update a Task : Method PUT = http://localhost:8080/tasks/update/id
    Body: JSON File = {
    "title": "old task updated",
    "description": "This is a new task description.",
    "status": "Finished"
    }

04) Delete a Task : Method DELETE = http://localhost:8080/tasks/delete/id
05) Get Task by Id : Method GET = http://localhost:8080/tasks/id
