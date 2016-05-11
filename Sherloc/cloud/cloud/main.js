
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:

  Parse.Cloud.afterSave("Message", function(request) {
  var receiver = request.object.get("ReceiverId");
  var query = new Parse.Query(Parse.User);
  query.equalTo("username", receiver);
  // Find devices associated with these users
  var pushQuery = new Parse.Query(Parse.Installation);
  // need to have users linked to installations
  pushQuery.matchesQuery("user", query);
  // var query = new Parse
	Parse.Push.send({
	    where: pushQuery,
	    data: {
	            alert: "You got a Message!"
	    }
	}, {
	    success: function () {
	        
	    },
	    error: function (error) {
	        
	    }
	});
});
