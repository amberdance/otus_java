let stompClient = null;
const CHAT_LINE_SELECTOR = "chatLine";
const ROOM_ID_SELECTOR = "roomId";
const MESSAGE_SELECTOR = "message";

const setConnected = (connected) => {
    const connectBtn = document.getElementById("connect");
    const disconnectBtn = document.getElementById("disconnect");

    connectBtn.disabled = connected;
    disconnectBtn.disabled = !connected;
    const chatLine = document.getElementById(CHAT_LINE_SELECTOR);
    chatLine.hidden = !connected;
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);

        const roomId = document.getElementById(ROOM_ID_SELECTOR).value;
        console.log(`Connected to roomId: ${roomId} frame:${frame}`);

        stompClient.subscribe(`/topic/response.${roomId}`, (message) => showMessage(JSON.parse(message.body).message));
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }

    setConnected(false);
}

const sendMsg = () => {
    const roomId = document.getElementById(ROOM_ID_SELECTOR).value;
    const message = document.getElementById(MESSAGE_SELECTOR).value;

    stompClient.send(`/app/message.${roomId}`, {}, JSON.stringify({'message': message}))
}

const showMessage = (message) => {
    const chatLine = document.getElementById(CHAT_LINE_SELECTOR);
    const newRow = chatLine.insertRow(-1);
    const newCell = newRow.insertCell(0);
    const newText = document.createTextNode(message);

    newCell.appendChild(newText);
}
