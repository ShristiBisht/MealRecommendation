document.addEventListener('DOMContentLoaded', function() {

    const mealForm = document.getElementById('mealForm');
    if (mealForm) {
        mealForm.addEventListener('submit', function(event) {
        event.preventDefault(); // prevent the default form submit
        const cuisine = document.getElementById('cuisine').value;
        const city = document.getElementById('city').value;
        if(sessionStorage.getItem('requestID')){
            
            requestId=sessionStorage.getItem('requestID')
        }
        else{
            const requestId = generateUUID();
            sessionStorage.setItem('requestID',requestId);
        }

            fetch(`/submitForm?cuisine=${cuisine}&city=${city}&requestId=${requestId}`).then(() => {
                // Establish WebSocket connection after submitting the form
                const socket = new SockJS('/ws');
                const stompClient = Stomp.over(socket);
                stompClient.connect({}, function(frame) {
                    stompClient.subscribe(`/topic/meal-results/${requestId}`, function(message) {
                        console.log("Submitting form with:", { cuisine, city, requestId });
                        console.log("message",message.body)
                        const results = JSON.parse(message.body);
                        console.log("results",results)
                        displayResults(results);
                    });
                    stompClient.send(`/app/getResult/${requestId}`, {}, JSON.stringify({
                        city: city,
                        cuisine: cuisine
                    }));
                });
            });
         });
    } else {
        console.error('Meal form not found!');
    }
});

// Generate unique UUID for each request
function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

// Display results
function displayResults(results) {
    const resultsDiv = document.getElementById('results');
    results.forEach(restaurant => {
        const restaurantDiv = document.createElement('div');
        restaurantDiv.innerHTML = `
            <h3>${restaurant.name}</h3>
            <p>Health Score: ${restaurant.healthScore}</p>
        `;
        resultsDiv.appendChild(restaurantDiv);
    });
}
