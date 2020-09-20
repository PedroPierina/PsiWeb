var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#posts").show();
    }
    else {
        $("#posts").hide();
    }
    $("#post").html("");
}

function connect() {
    var socket = new SockJS('/feed');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/posts"', function (post) {
        	console.log("Entrei chama Post");
            showPost(JSON.parse(post.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendPost() {
	var title = document.getElementById('title').value;
	var body = document.getElementById('body').value;
	
    stompClient.send("/app/feed", {}, JSON.stringify({'title': title, 'body': body}));
}

function showPost(message) {
	console.log("Entrei ShowPost");
	
	
    $("#post").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendPost(); });
});