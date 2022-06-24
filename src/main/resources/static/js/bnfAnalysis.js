var compartment1Chart ;
var compartment2Chart ;
var compartment3Chart ;
var compartment4Chart ;
var compartment1Ctx = document.getElementById("compartment1").getContext("2d");
var compartment2Ctx = document.getElementById("compartment2").getContext("2d");
var compartment3Ctx = document.getElementById("compartment3").getContext("2d");
var compartment4Ctx = document.getElementById("compartment4").getContext("2d");
$(document).ready(function () {
//    applyDataTable();
    compartment1Chart = initChart(compartment1Ctx, 'Compartment-1');
    compartment2Chart = initChart(compartment2Ctx, 'Compartment-2');
    compartment3Chart = initChart(compartment3Ctx, 'Compartment-3');
    compartment4Chart = initChart(compartment4Ctx, 'Compartment-4');
    loadChartData();
});

$(".refresh").on('click', function(){
    loadChartData();
});

$(".reset").on('click', function(){
    var compartmentNo = $(this).data('compartment');
    if(compartmentNo == 1) {
        compartment1Chart.resetZoom();
    } else if(compartmentNo == 2) {
        compartment2Chart.resetZoom();
    } else if(compartmentNo == 3) {
        compartment3Chart.resetZoom();
    } else if(compartmentNo == 4) {
        compartment4Chart.resetZoom();
    }
});

var lineColors = ['#2196f3', '#4CAF50', '#03f8fc', '#fc03d7'];
function loadChartData() {
var niftyLabels = [];
var niftyPrices = [];

var compartment1 = [];
var compartment1Labels = [];
var compartment1MainLabels = [];
var compartment2 = [];
var compartment2Labels = [];
var compartment2MainLabels = [];
var compartment3 = [];
var compartment3Labels = [];
var compartment3MainLabels = [];
var compartment4 = [];
var compartment4Labels = [];
var compartment4MainLabels = [];
$.ajax({
        url: '/bank/refreshAnalysis',
        method: 'get',
        dataType: 'json', // type of response data
        timeout: 5000,     // timeout milliseconds
        success: function (data,status,xhr) {   // success callback function
            var insertedTimeList = data["insertedTimeList"];
            compartment1MainLabels = insertedTimeList;
            compartment2MainLabels = insertedTimeList;
            compartment3MainLabels = insertedTimeList;
            compartment4MainLabels = insertedTimeList;
            $('#cmp').text(data['niftySpot']);
            $('#atmStrike').text(data['niftyATM']);
            $('#currentDate').text(data['currentDate']);
            $.each(data["compartment1"], function(i, item) {
                compartment1Labels.push(i);
                var dataTmp = [];
                $.each(insertedTimeList, function(jIndex, timeVal) {
                    dataTmp.push(item[timeVal]);
                });
//                console.log(i+" Count: "+dataTmp.length+", Label count: "+compartment1MainLabels.length);
                compartment1.push(dataTmp);
            });
//            console.log("compartment1 Dataset length: "+compartment1.length);

            $.each(data["compartment2"], function(i, item) {
                compartment2Labels.push(i);
                var dataTmp = [];
                $.each(insertedTimeList, function(jIndex, timeVal) {
                    dataTmp.push(item[timeVal]);
                });
//                console.log(i+" Count: "+dataTmp.length+", Label count: "+compartment2MainLabels.length);
                compartment2.push(dataTmp);
            });
//            console.log("compartment2 Dataset length: "+compartment2.length);

            $.each(data["compartment3"], function(i, item) {
                compartment3Labels.push(i);
                var dataTmp = [];
                $.each(insertedTimeList, function(jIndex, timeVal) {
                    dataTmp.push(item[timeVal]);
                });
//                console.log(i+" Count: "+dataTmp.length+", Label count: "+compartment3MainLabels.length);
                compartment3.push(dataTmp);
            });
//            console.log("compartment3 Dataset length: "+compartment3.length);

            $.each(data["compartment4"], function(i, item) {
                compartment4Labels.push(i);
                var dataTmp = [];
                $.each(insertedTimeList, function(jIndex, timeVal) {
                    dataTmp.push(item[timeVal]);
                });
//                console.log(i+" Count: "+dataTmp.length+", Label count: "+compartment4MainLabels.length);
                compartment4.push(dataTmp);
            });
//            console.log("compartment4 Dataset length: "+compartment4.length);

        },
        error: function (jqXhr, textStatus, errorMessage) { // error callback
            alert('Error: ' + errorMessage);
        },
        complete: function (data) {
            compartment1Chart.data.labels = compartment1MainLabels;
            var compartment1Datasets = [];
            $.each(compartment1, function(index, dataSet){
                compartment1Datasets.push(prepareDataSet(compartment1[index], index, compartment1Labels[index]));
            });
            compartment1Chart.data.datasets = compartment1Datasets;
            compartment1Chart.update();

            compartment2Chart.data.labels = compartment2MainLabels;
            var compartment2Datasets = [];
            $.each(compartment2, function(index, dataSet){
                compartment2Datasets.push(prepareDataSet(compartment2[index], index, compartment2Labels[index]));
            });
            compartment2Chart.data.datasets = compartment2Datasets;
            compartment2Chart.update();

            compartment3Chart.data.labels = compartment3MainLabels;
            var compartment3Datasets = [];
            $.each(compartment3, function(index, dataSet){
                compartment3Datasets.push(prepareDataSet(compartment3[index], index, compartment3Labels[index]));
            });
            compartment3Chart.data.datasets = compartment3Datasets;
            compartment3Chart.update();

            compartment4Chart.data.labels = compartment4MainLabels;
            var compartment4Datasets = [];
            $.each(compartment4, function(index, dataSet){
                compartment4Datasets.push(prepareDataSet(compartment4[index], index, compartment4Labels[index]));
            });
            compartment4Chart.data.datasets = compartment4Datasets;
            compartment4Chart.update();
         }
    });
}
function prepareDataSet(data, index, lbl) {
//    console.log('Data: '+data+' Index: '+index+' Label: '+lbl);
    return {
              label: lbl, // Name the series
              data: data, // Specify the data values array
              fill: false,
              borderColor: lineColors[index], // Add custom color border (Line)
              backgroundColor: lineColors[index], // Add custom color background (Points and Fill)
              borderWidth: 1 // Specify bar border width
          }
}
function initChart(chartContext, chartLabel) {
//console.log("Init chart");
    return new Chart(chartContext, {
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
                 },
                 plugins: {
                      zoom: {
                          zoom: {
                            wheel: {
                              enabled: true // SET SCROOL ZOOM TO TRUE
                            },
                            mode: "xy",
                            speed: 100,
                            onZoomComplete({chart}) {
                              // This update is needed to display up to date zoom level in the title.
                              // Without this, previous zoom level is displayed.
                              // The reason is: title uses the same beforeUpdate hook, and is evaluated before zoom.
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

function resetZoom() {
  compartment1Chart.resetZoom();
}
