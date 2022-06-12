var ctx = document.getElementById("niftyAnalysisChart").getContext('2d');

var myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: ["Tokyo",	"Mumbai",	"Mexico City",	"Shanghai",	"Sao Paulo",	"New York",	"Karachi","Buenos Aires",	"Delhi","Moscow"],
        datasets: [{
            label: 'Series 1', // Name the series
            data: [500,	50,	2424,	14040,	14141,	4111,	4544,	47,	5555, 6811], // Specify the data values array
            fill: false,
            borderColor: '#2196f3', // Add custom color border (Line)
            backgroundColor: '#2196f3', // Add custom color background (Points and Fill)
            borderWidth: 1 // Specify bar border width
        },
        {
            label: 'Series 2', // Name the series
            data: [1288,	88942,	44545,	7588,	99,	242,	1417,	5504,	75, 457], // Specify the data values array
            fill: false,
            borderColor: '#4CAF50', // Add custom color border (Line)
            backgroundColor: '#4CAF50', // Add custom color background (Points and Fill)
            borderWidth: 1 // Specify bar border width
        }]
    },
    options: {
      responsive: true, // Instruct chart js to respond nicely.
      maintainAspectRatio: false, // Add to prevent default behaviour of full-width/height
    }
});