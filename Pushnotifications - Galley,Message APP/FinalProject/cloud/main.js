
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.afterSave("Album", function(request) {
    if(request.object.updatedAt.getTime() == request.object.createdAt.getTime()) {
        Parse.Push.send({
            channels: ["all"],
            data: {
                alert: "A new album has been saved"
            }
        }, {
            success: function() {

            },
            error: function(error) {
                console.log(error);
            }
        });
    }
});

Parse.Cloud.afterSave(Parse.User, function(request) {
    if(request.object.updatedAt.getTime() == request.object.createdAt.getTime()) {
        Parse.Push.send({
            channels: ["all"],
            data: {
                alert: "A new user has signed up"
            }
        }, {
            success: function() {

            },
            error: function(error) {
                console.log(error);
            }
        });
    }
});

Parse.Cloud.afterSave("Message", function(request) {
    if(request.object.updatedAt.getTime() == request.object.createdAt.getTime()) {
        var to = request.object.get("to");
        var toId = "";
        to.fetch({
            success: function(to) {
                toId = to.id;
            }
        });
        var from = request.object.get("from");
        Parse.Push.send({
            channels: [toId],
            data: {
                alert: "New message from " + from
            }
        }, {
            success: function() {

            },
            error: function(error) {
                console.log(error);
            }
        });
    }
});
