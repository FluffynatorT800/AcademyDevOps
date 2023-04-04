This file contains the scripts for the Frontend REST functions

The 'const csrfToken' gets the csrf token for the authorization of the fetch requests 
```
const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
```

myFunction: </br>
A generic "get all" function which retrives all the entries in the database in a JSON format </br>
This fetch request can be executed withou a csrf token. 
```
function myFunction() {
        
    function getData(url, cb) {
    fetch(url)
      .then(response => response.json())
      .then(result => cb(result));
    }

    getData("http://40.115.21.0:8080/customer/", (data) => document.getElementById("json").innerHTML = JSON.stringify(data, undefined, 2));     
}
```
const formADD: </br>
This constant contains the functions to get the user input from the html form </br>
and post the data to the database. This operation needs to transmit the csrf token </br>
in the header of the request. After a successfull post the myFunction is triggerd to reload the entries </br>
```
const formADD = document.querySelector('.form');

formADD.addEventListener('submit', event => {
    event.preventDefault();
    const formData = new FormData(formADD);
    const data = Object.fromEntries(formData);
    const addCommand = fetch("http://40.115.21.0:8080/customer/id", {
      method: 'POST',
      headers: {
        'Accept' : 'application/json',
        'Content-Type': 'application/json',
        'X-XSRF-TOKEN': csrfToken
      },
      body: JSON.stringify(data)  
    })
    .then(function(response){
        if(response.status === 200){
            myFunction()
        };
    })
})
```
deleteIt: </br>
This function takes user input to delete a single database entry identified by the id number </br>
It also transmits the csrf token in the header and triggers the myFunciton on success.
```
async function deleteIt() {
    const docId = document.getElementById("id").value
    const deleteCommand = await fetch("http://40.115.21.0:8080/customer/" + docId,
    {
        method: 'Delete',
        headers: {
          'X-XSRF-TOKEN': csrfToken
        }
    });  
    const loadList = myFunction();
}

```