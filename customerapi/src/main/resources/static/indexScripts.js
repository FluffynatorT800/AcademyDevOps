const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

function myFunction() {
        
    function getData(url, cb) {
    fetch(url)
      .then(response => response.json())
      .then(result => cb(result));
    }

    getData("http://40.115.21.0:8080/customer/", (data) => document.getElementById("json").innerHTML = JSON.stringify(data, undefined, 2));     
}


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
        }
        else {
          document.getElementById("addError").style.visibility = "visible";
          setTimeout(function(){document.getElementById("addError").style.visibility = "hidden"}, 10000)
        };
    })
})

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

const putAdd = document.querySelector('.form2');

putAdd.addEventListener('submit', event => {
    event.preventDefault();
    const putId = document.getElementById("putId").value
    const formData = new FormData(putAdd);
    const data = Object.fromEntries(formData);
    const putCommand = fetch("http://40.115.21.0:8080/customer/" + putId, {
      method: 'PUT',
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
        } 
        else {
          document.getElementById("addError").style.visibility = "visible";
          setTimeout(function(){document.getElementById("addError").style.visibility = "hidden"}, 10000)
        };
    })
})