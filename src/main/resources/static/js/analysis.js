var niftyChart ;
var bankNiftyChart ;
var niftyCtx = document.getElementById("niftyAnalysisChart").getContext('2d');
//var bankNiftyCtx = document.getElementById('bankNiftyChart');
$(document).ready(function () {
//    applyDataTable();
    niftyChart = initChart(niftyCtx, 'Nifty');
    //bankNiftyChart = initChart(bankNiftyCtx, 'Bank Nifty');
    loadChartData();
});

$(".refresh").on('click', function(){
    loadChartData();
});
var lineColors = ['#2196f3', '#4CAF50', '#03f8fc', '#fc03d7'];
function loadChartData() {
var niftyLabels = [];
var niftyPrices = [];
var bankNiftyLabels = [];
var bankNiftyPrices = [];
var segment1 = [];
var segment1Labels = [];
var segment1MainLabels = [];

$.ajax({
        url: '/refreshAnalysis',
        method: 'get',
        dataType: 'json', // type of response data
        timeout: 500,     // timeout milliseconds
        success: function (data,status,xhr) {   // success callback function
            $.each(data["segment1"], function(i, item) {
                console.log("Data "+i+" : "+JSON.stringify(item.length));
                segment1Labels.push(i);
                var dataTmp = [];
                $.each(item, function(j, ite) {
                    dataTmp.push(ite.curChangeInOi);
                    segment1MainLabels.push(ite.updatedAtSource);
                });
                segment1.push(dataTmp);
            });
            console.log("Dataset length: "+segment1.length);
        },
        error: function (jqXhr, textStatus, errorMessage) { // error callback
            alert('Error: ' + errorMessage);
        },
        complete: function (data) {
            niftyChart.data.labels = segment1MainLabels;
            var datasets = [];
                $.each(segment1, function(index, dataSet){
                    datasets.push(prepareDataSet(segment1[index], index, segment1Labels[index]));
                });
                niftyChart.data.datasets = datasets;
                niftyChart.update();
         }
    });
}
function prepareDataSet(data, index, lbl) {
    return {
              label: lbl, // Name the series
              data: data, // Specify the data values array
              fill: false,
              borderColor: lineColors[index], // Add custom color border (Line)
              backgroundColor: lineColors[index], // Add custom color background (Points and Fill)
              borderWidth: 1 // Specify bar border width
          }
}
function initChart(niftyCtx, chartLabel) {
    return new Chart(niftyCtx, {
               type: 'line',
               data: {
                   labels: [],
                   datasets: []
               },
               options: {
                 responsive: true, // Instruct chart js to respond nicely.
                 maintainAspectRatio: false, // Add to prevent default behaviour of full-width/height
                 elements: {
                     point:{
                         radius: 0
                     }
                 }
               }
           })
}
