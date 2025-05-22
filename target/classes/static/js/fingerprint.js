(function () {

    var sessionId = "";

    function select(str) {
        return document.querySelector(str);
    }

    function alertMessage(message, type, alert) {
        let alerts = select("#alerts");

        let el = document.createElement("p")
        el.innerHTML = message
        el.classList.add(type)
        alerts.append(el)
        setTimeout(() => alerts.innerHTML = '', 5000)

        if (alert) alert(message);
    }

    async function connect(username) {
        return new Promise((resolve, reject) => {
            let stompClient = Stomp.over(new SockJS('/init'));
            stompClient.connect({}, (frame) => resolve(stompClient));
        })
    }

    async function disconnect() {
        if (stompClient != null && stompClient.connected) {
            const ws = stompClient.ws;
            ws.close();
        }
        // console.log("Disconnected");
    }

    function setConnected(connected) {
        select("#login").disabled = connected;
        select("#disconnect").disabled = !connected;
        select("#capture").disabled = !connected;

        select("#conversationDiv").style.visibility = connected ? "visible" : "hidden";
        select("#responseText").innerHTML = connected ? "Select a finger from below list to enroll" : "Not connected! Please check device and try again...";
        select("#responseDiv").className = connected ? "badge badge-pill badge-secondary" : "badge badge-pill badge-warning";
    }

    function stompSubscribe(stompClient, endpoint, callback) {
        stompClient.subscribe(endpoint, callback)
        return stompClient
    }

    function stompClientSendMessage(stompClient, endpoint, message) {
        stompClient.send(endpoint, {}, message)
        return stompClient
    }

    function displayResult(stompClient, sessionId, message) {

        let responseText = select("#responseText");
        let imageDiv = select("#images");
        let statusBar = document.querySelector(".completed-capture");

        if (message.type === "image") {
            var imageTag = document.createElement('img');
            imageTag.src = "data:image/png;base64," + message.result;
            imageTag.style.width = "35px";
            imageDiv.appendChild(imageTag);

            statusBar ? statusBar.innerHTML = "<span style='font-weight: 100;height: 20px;color: black;' class='badge badge-pill badge-warning waitingForConnection'>Processing fingerprint capture...</span>" : "";
            responseText.innerHTML = "Processing request...";
            return;

        } else if (message.type === "template") {
            statusBar ? statusBar.innerHTML = "<span style='font-weight: 100;height: 20px;color: black;' class='badge badge-pill badge-warning waitingForConnection'>Submitting fingerprint data...</span>" : "";

            stompClient.send("/digital-persona/save", {},
                JSON.stringify({ finger: message.finger, patient: message.patient, fingerPrint: message.result })
            );
            setTimeout(() => {
                document.querySelector(".modal-body .container").innerHTML = `
                <img src="assets/swiftechuganda/images/done-web.png" style="width: 145px;" />
                <h3>Enrollment successfully!</h3>
                <span>Click outside to close this window.</span>
                <style>
                  .modal-body .container{
                    display: flex;
                    flex-direction: column;
                    height: 100%;
                    background: #fff;
                    margin: auto;
                    width: 100%;
                    align-items: center;
                  }
                  h3{
                    font-weight: 600;
                    margin-top: 0;
                    color: #005400;
                }
                </style>
                `;
            }, 100);

        } else {
            if (responseText !== null)
                responseText.innerHTML = message.result;
        }
        if (message.result?.includes("Failed to enroll")) {
            responseText.style.background = "#f44336";
            document.querySelector(".completed-capture").innerHTML = "";
        }

    }

    function deleteFingerPrints(patientId) {
        jq.post(`${socketBaseUrl}/delete`, {
            patient: patientId
        }, function (response) {
            if (response.search("Patient fingerprint Deleted") > -1) {
                var message;
                message = '{"result":"Patient fingerprint Deleted"}';
                displayResult(JSON.parse(message));
            }
        });
    }

    //To guarantee that our page is completely loaded before we execute anything
    window.addEventListener('load', function (event) {
        let chatUsersList = [];
        let connectButton = select("#login");
        let disconnectButton = select("#disconnect");
        let sendButton = select("#capture");
        let responseText = select("#responseText");
        let imageDiv = select("#images");
        let sendNumIndex = select("#selectIndex");

        connectButton.addEventListener('click', () => {
            let username = select("#username").value;

            if (username == null || username === '') {
                alertMessage('Patient Id cannot be empty!!!', 'bg-danger')
            } else {
                connect(username)
                    .then((stompClient) => {
                        var url = stompClient.ws._transport.url;

                        url = url.replace(`ws://${stompClient.ws.url}`, "");
                        url = url.replace("/websocket", "");
                        url = url.replace(/^[0-9]+\//, "");
                        const sessionId = url;
                        console.log("Your current session is: " + sessionId);

                        // jq("#connect").click();
                        setConnected(true);

                        return stompClient;
                    })
                    .then((stompClient) => {

                        connectButton.disabled = true;
                        disconnectButton.disabled = false;

                        connectButton.addEventListener("click", connect);
                        disconnectButton.addEventListener("click", disconnect);

                        sendButton.addEventListener("click", (event) => {
                            const patientId = select("#username").value;

                            if (patientId === null || patientId === "")
                                return alertMessage("Please register a patient to enroll fingerprint", 'bg-danger', true)

                            responseText.innerHTML = "";
                            imageDiv.innerHTML = "";

                            const finger = sendNumIndex ? sendNumIndex.value : 5;

                            stompClientSendMessage(stompClient, "/digital-persona/enroll",
                                JSON.stringify({ finger: finger, patient: patientId }));
                        });
                        return stompClient;
                    })
                    .then((stompClient) => stompSubscribe(stompClient, "/topic/showResult", (data) => {
                        displayResult(stompClient, sessionId, JSON.parse(data.body));
                    }))
                    .then((stompClient) => stompSubscribe(stompClient, "/queue/error", (data) => {
                        alertMessage(chatUsersList.filter(x => x != username), JSON.parse(data.body), 'bg-danger');
                    }))
            }
        }, true)
    });
})();
