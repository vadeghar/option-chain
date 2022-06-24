var niftyChart ;
var bankNiftyChart ;
var niftyCtx = document.getElementById('niftyChart');
var bankNiftyCtx = document.getElementById('bankNiftyChart').getContext("2d");
$(document).ready(function () {
//    applyDataTable();
    niftyChart = initChart(niftyCtx, 'Nifty');
    bankNiftyChart = initChart(bankNiftyCtx, 'Bank Nifty');
    loadChartData();
});

$(".refresh").on('click', function(){
    loadChartData();
});

function loadChartData() {
var niftyLabels = [];
var niftyPrices = [];
var bankNiftyLabels = [];
var bankNiftyPrices = [];
$.ajax({
        url: '/refreshIndex',
        method: 'get',
        dataType: 'json', // type of response data
        timeout: 5000,     // timeout milliseconds
        success: function (data,status,xhr) {   // success callback function
            $.each(data.niftyToday, function(i, item) {
//                console.log('Index: '+i+" strike: "+item.addedTs);
                if(i == 0 || i == (data.niftyToday.length-1)) {
                    niftyLabels.push(item.addedTs);
                } else if(i%2 == 0) {
                    niftyLabels.push(item.addedTs);
                } else {
                    niftyLabels.push('');
                }
                niftyPrices.push(item.lastPrice);
            });

            $.each(data.bankNiftyToday, function(i, item) {
//                console.log('Index: '+i+" strike: "+item.addedTs);
                if(i == 0 || i == (data.niftyToday.length-1)) {
                    bankNiftyLabels.push(item.addedTs);
                } else if(i%2 == 0) {
                    bankNiftyLabels.push(item.addedTs);
                } else {
                    bankNiftyLabels.push('');
                }
                bankNiftyPrices.push(item.lastPrice);
            });

        },
        error: function (jqXhr, textStatus, errorMessage) { // error callback
            alert('Error: ' + errorMessage);
        },
        complete: function (data) {
            niftyChart.data.labels = niftyLabels;
            niftyChart.data.datasets[0].data = niftyPrices;
            niftyChart.update();

            bankNiftyChart.data.labels = bankNiftyLabels;
            bankNiftyChart.data.datasets[0].data = bankNiftyPrices;
            bankNiftyChart.update();
         }
    });
}

function initChart(ctx, chartLabel) {
    return new Chart(ctx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: chartLabel, // Name the series
                data: [], // Specify the data values array
                fill: false,
                borderColor: '#2196f3', // Add custom color border (Line)
                backgroundColor: '#2196f3', // Add custom color background (Points and Fill)
                borderWidth: 1 // Specify bar border width
            }]},
        options: {
            scales: {
              xAxes: [{
                  beginAtZero: false,
                  ticks: {
                     autoSkip: false
                  }
              }]
            },
          responsive: true, // Instruct chart js to respond nicely.
//              maintainAspectRatio: false, // Add to prevent default behaviour of full-width/height
            elements: {
                point:{
                    radius: 1
                }
            },
            plugins: {
              zoom: {
                  zoom: {
                    wheel: {
                      enabled: true
                    },
                    mode: "xy",
                    speed: 100,
                    onZoomComplete({chart}) {
                      chart.update('none');
                    }
                  },
                  pan: {
                    enabled: true,
                    mode: "xy",
                    speed: 100
                  }
                }
            }
        }
    });
}