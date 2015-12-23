var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');
        document.getElementById("submit").addEventListener("click", submitContact, false);
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

function submitContact() {
  console.log("entered submit");
  var name = document.getElementById("name").value;
  var mobile = document.getElementById("mobile").value;
  var work = document.getElementById("work").value;
  var home = document.getElementById("home").value;
  var email = document.getElementById("email").value;
  var url = document.getElementById("url").value;
  var date = document.getElementById("date").value;

  var contact = navigator.contacts.create();
  contact.displayName = name;
  contact.nickname = name;
  contact.name = {givenName: name, familyName: ""};
  var numbers = [];
  numbers[0] = new ContactField('mobile', mobile, true);
  numbers[1] = new ContactField('work', work, false);
  numbers[2] = new ContactField('home', home, false);
  contact.phoneNumbers = numbers;
  var emails = [];
  emails[0] = new ContactField('email', email, true);
  contact.emails = emails;
  var urls = [];
  urls[0] = new ContactField('url', url, true);
  contact.urls = urls;
  contact.birthday = date;
  contact.save(onSuccess,onError);
};

function onSuccess(contact) {
  alert("Save Success");
};

function onError(contact) {
  alert("Save Error");
};

app.initialize();
