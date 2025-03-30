async function fetchWeatherAndTime() {
    try {
        const response = await fetch('/api/weather-time');
        const data = await response.text();
        document.getElementById('weather-time').innerText = data;
    } catch (error) {
        console.error("Error fetching data:", error);
        document.getElementById('weather-time').innerText = "Failed to load data";
    }
}

// Първоначално зареждане (използваме .then() за избягване на предупреждението)
fetchWeatherAndTime().catch(console.error);

// Автоматично обновяване на всеки 30 секунди
setInterval(() => {
    fetchWeatherAndTime().catch(console.error);
}, 30000);
