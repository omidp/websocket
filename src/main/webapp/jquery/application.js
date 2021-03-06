$(function () {
	"use strict";
var content = jQuery("#content");
var logged = false;
var author = null;
var myName = false;
var input = jQuery("#input");
var status = $('#status');
var socket = $.atmosphere;
var request = {
	url : document.location.toString() + 'chat',
	contentType : "application/json",
	logLevel : 'debug',
	transport : 'websocket',
	fallbackTransport : 'long-polling'
};
request.onOpen = function(response) {
	content.html($('<p>', {
		text : 'Atmosphere connected using ' + response.transport
	}));
	input.removeAttr('disabled').focus();
	status.text('Choose name:');
};
request.onMessage = function(response) {
	var message = response.responseBody;
	try {
		var json = JSON.parse(message);
	} catch (e) {
		console.log('This doesnf2379804bc0adb0c2c6c1ac94');
		return;
	}
	if (!logged) {
		logged = true;
		status.text(myName + ': ').css('color', 'blue');
		//input.removeAttr('disabled').focus();
	} else {
		//input.removeAttr('disabled');
		var me = json.author == author;
		var date = typeof (json.time) == 'string' ? parseInt(json.time)
				: json.time;
		addMessage(json.author, json.message, me ? 'blue' : 'black', new Date());
	}
};
request.onError = function(response) {
	content
			.html($(
					'<p>',
					{
						text : 'Sorry, but theref2379804bc0adb0c2c6c1ac9450b0a838eedecdf#39;s some problem with your '
								+ 'socket or the server is down'
					}));
};
var subSocket = socket.subscribe(request);

input.keydown(function(e) {
	if (e.keyCode === 13) {
		var msg = $(this).val();

		// First message is always the author's name
		if (author == null) {
			author = msg;
		}

		subSocket.push(JSON.stringify({
			author : author,
			message : msg
		}));
		$(this).val('');

		//input.attr('disabled', 'disabled');
		if (myName === false) {
			myName = msg;
		}
	}
});
function addMessage(author, message, color, datetime) {
	content.append('<p><span style="color:'
			+ color
			+ '">'
			+ author
			+ '</span> @ '
			+ +(datetime.getHours() < 10 ? '0' + datetime.getHours() : datetime
					.getHours())
			+ ':'
			+ (datetime.getMinutes() < 10 ? '0' + datetime.getMinutes()
					: datetime.getMinutes()) + ': ' + message + '</p>');
}
});
