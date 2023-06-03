const CLIENT_ENDPOINT = "/api/clients"
const CLIENT_DIALOG_ID = "#client_create_dialog"
const CLIENT_CREATE_FORM = "#client_create_form"

const getClients = async () => {
    try {
        const {data} = await axios.get(CLIENT_ENDPOINT)
        renderTable(data)
        return Promise.resolve(data)
    } catch (e) {
        return Promise.reject(e)
    }
}

const createClient = async (isRandom) => {
    let clientJson = {}

    try {
        clientJson = isRandom ? createRandom() : await createFromForm()

        await postClient(clientJson)
        await getClients()

        resetForm(CLIENT_CREATE_FORM)
        closeClientDialog()
        alert("Client created")
    } catch (e) {
        alert(e)
        console.error(e)
    }
}

const postClient = async (payload) => {
    try {
        await axios.post(CLIENT_ENDPOINT, payload)
        return Promise.resolve()
    } catch (e) {
        return Promise.reject(e)
    }
}

const openClientDialog = () => $(CLIENT_DIALOG_ID).show();

const closeClientDialog = () => {
    $(CLIENT_DIALOG_ID).hide()
    resetForm(CLIENT_CREATE_FORM)
}

const renderTable = clients => {
    let html = ""

    clients.forEach(client => {
        html += `
        <tr>
        <td>${client.id}</td>
        <td>${client.name}</td>
        <td>${client.username}</td>
        <td>${client.password}</td>
        <td>${client.address.label}</td>
        <td>${client.phones.map(p => p.number).join("<p></p>")}</td>
        </tr>
`
    })

    html += "<tr>";
    $("#clients").html(html)
}

const resetForm = (name) => $(name).trigger("reset");

const createRandom = () => {
    const random = (Math.random() + 1).toString(36).substring(7);

    return {
        name: random,
        username: random,
        password: random,
        address: random,
        phones: [{
            number: random
        }]
    }
};

const createFromForm = async () => {
    const data = {}

    $(CLIENT_CREATE_FORM).serializeArray().forEach(field => data[field.name] = field.value)
    data.phones.split(",").map(p => data.phones = [{number: p}])

    for (let key in data) {
        if (data[key] === "") {
            return Promise.reject("Ensure that fields is non empty")
        }
    }

    return Promise.resolve(data)
}
