// Create IE + others compatible event handler
var eventMethod = window.addEventListener ? "addEventListener" : "attachEvent";
var eventer = window[eventMethod];
var messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";

// Listen to message from child window
eventer(messageEvent,function(e) {
    var event = e.data;
    console.log('parent received message!:  ', event.eventName, event.data);
    if (window[event.eventName]){
        window[event.eventName].apply(window, event.data);
    }
},false);

function notifyIframeZkComponent(cmp_id, slot_id){
    var iframeCollection = document.getElementsByTagName('iframe');
    var id="";
    for(index in iframeCollection){
        if(iframeCollection[index].className!=null&&iframeCollection[index].className=='liveEditBrowser'&&iframeCollection[index].offsetHeight!=0){
            id=iframeCollection[index].id;
            break;
        }
    }
    var payload = JSON.stringify({
        cmp_id:cmp_id,
        slot_id:slot_id
    });

    comm.sendUser(id,'defaultCallback',payload);
}

function notifyIframeAboutUrlChange(url, pagePk, userPk, jaloSessionId){
    var iframeCollection = document.getElementsByTagName('iframe');
    var id="";
    for(index in iframeCollection){
        if(iframeCollection[index].className!=null&&iframeCollection[index].className=='liveEditBrowser'&&iframeCollection[index].offsetHeight!=0){
            id=iframeCollection[index].id;
            break;
        }
    }
    comm.sendUser(id,'urlChange',url,pagePk,userPk,jaloSessionId)
}


