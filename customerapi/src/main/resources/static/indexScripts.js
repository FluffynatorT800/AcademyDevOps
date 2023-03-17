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
        };
    })
})

async function deleteIt() {
    const docId = document.getElementById("id").value
    const deleteCommand = await fetch("http://40.115.21.0:8080/customer/" + docId,
    {
        method: 'Delete'
    });  
    const loadList = myFunction();
}
