function userAction() {	
    fetch("http://40.115.21.0:8080/customer/")
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            appendData(data);
        })
        .catch(function (err) {
            console.log('error: ' + err);
        });
    function appendData(data) {
        var mainContainer = document.getElementById("myData");
        for (var i = 0; i < data.length; i++) {
            var div = document.createElement("div");
            div.innerHTML = 'Vorname: ' + data[i].vorname + ' Nachname: ' + data[i].nachname;
            mainContainer.appendChild(div);
        }
    }
}	