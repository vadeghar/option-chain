/* globals Chart:false, feather:false */
var niftyChartOiDayChangeData=[];
var niftyChartOiNetChangeData=[];
var niftyChartLabels=[];

var niftyPeChartOiDayChangeData=[];
var niftyPeChartOiNetChangeData=[];
var niftyPeChartLabels=[];

var bankNiftyChartOiDayChangeData=[];
var bankNiftyChartOiNetChangeData=[];
var bankNiftyChartLabels=[];

var bankNiftyPeChartOiDayChangeData=[];
var bankNiftyPeChartOiNetChangeData=[];
var bankNiftyPeChartLabels=[];

var ctx = document.getElementById('niftyChart');
var niftyPeChartCtx = document.getElementById('niftyPeChart');
var ctx2 = document.getElementById('bankNiftyChart');
var bankNiftyPeChartCtx = document.getElementById('bankNiftyPeChart');
  // eslint-disable-next-line no-unused-vars
  var niftyChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: [],
      datasets: [{
        data: [],
        label: "Total Change in OI",
        backgroundColor: "rgba(75,192,192,0.4)",
        borderColor: "rgba(75,192,192,1)",
        lineTension: 0,
        borderJoinStyle: 'miter',
        backgroundColor: 'transparent',
        borderColor: '#007bff',
        borderWidth: 4,
        pointBackgroundColor: '#007bff',
        pointBorderWidth: 1,
        pointHoverRadius: 7

      },
      {
          data: [],
          label: "Change in OI from last refresh",

          lineTension: 0,
          backgroundColor: 'transparent',
          borderColor: '#00FF00',
          borderWidth: 4,
          pointBackgroundColor: '#00FF00',
          pointBorderWidth: 1,
          pointHoverRadius: 7,
          title: {
              display: true,
              text: 'Custom Chart Title'
          }
        }]
    },
    options: {
      scales: {
        yAxes: [{
          ticks: {
            beginAtZero: false,
            callback: function(value, index, values) {
                return value+" Lots"
            }
          }
        }]
      },
      legend: {
        display: true
      }
    }
  });

  var niftyPeChart = new Chart(niftyPeChartCtx, {
      type: 'line',
      data: {
        labels: [],
        datasets: [{
          data: [],
          label: "Total Change in OI",
          backgroundColor: "rgba(75,192,192,0.4)",
          borderColor: "rgba(75,192,192,1)",
          lineTension: 0,
          borderJoinStyle: 'miter',
          backgroundColor: 'transparent',
          borderColor: '#007bff',
          borderWidth: 4,
          pointBackgroundColor: '#007bff',
          pointBorderWidth: 1,
          pointHoverRadius: 7

        },
        {
            data: [],
            label: "Change in OI from last refresh",

            lineTension: 0,
            backgroundColor: 'transparent',
            borderColor: '#00FF00',
            borderWidth: 4,
            pointBackgroundColor: '#00FF00',
            pointBorderWidth: 1,
            pointHoverRadius: 7,
            title: {
                display: true,
                text: 'Custom Chart Title'
            }
          }]
      },
      options: {
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: false,
              callback: function(value, index, values) {
                  return value+" Lots"
              }
            }
          }]
        },
        legend: {
          display: true
        }
      }
    });

  var bankNiftyChart = new Chart(ctx2, {
      type: 'line',
      data: {
        labels: [],
        datasets: [{
          data: [],
          label: "Total Change in OI",
          backgroundColor: "rgba(75,192,192,0.4)",
          borderColor: "rgba(75,192,192,1)",
          lineTension: 0,
          borderJoinStyle: 'miter',
          backgroundColor: 'transparent',
          borderColor: '#007bff',
          borderWidth: 4,
          pointBackgroundColor: '#007bff',
          pointBorderWidth: 1,
          pointHoverRadius: 7

        },
        {
            data: [],
            label: "Change in OI from last refresh",

            lineTension: 0,
            backgroundColor: 'transparent',
            borderColor: '#00FF00',
            borderWidth: 4,
            pointBackgroundColor: '#00FF00',
            pointBorderWidth: 1,
            pointHoverRadius: 7,
            title: {
                display: true,
                text: 'Custom Chart Title'
            }
          }]
      },
      options: {
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: false,
              callback: function(value, index, values) {
                  return value+" Lots"
              }
            }
          }]
        },
        legend: {
          display: true
        }
      }
    });

    var bankNiftyPeChart = new Chart(bankNiftyPeChartCtx, {
          type: 'line',
          data: {
            labels: [],
            datasets: [{
              data: [],
              label: "Total Change in OI",
              backgroundColor: "rgba(75,192,192,0.4)",
              borderColor: "rgba(75,192,192,1)",
              lineTension: 0,
              borderJoinStyle: 'miter',
              backgroundColor: 'transparent',
              borderColor: '#007bff',
              borderWidth: 4,
              pointBackgroundColor: '#007bff',
              pointBorderWidth: 1,
              pointHoverRadius: 7

            },
            {
                data: [],
                label: "Change in OI from last refresh",

                lineTension: 0,
                backgroundColor: 'transparent',
                borderColor: '#00FF00',
                borderWidth: 4,
                pointBackgroundColor: '#00FF00',
                pointBorderWidth: 1,
                pointHoverRadius: 7,
                title: {
                    display: true,
                    text: 'Custom Chart Title'
                }
              }]
          },
          options: {
            scales: {
              yAxes: [{
                ticks: {
                  beginAtZero: false,
                  callback: function(value, index, values) {
                      return value+" Lots"
                  }
                }
              }]
            },
            legend: {
              display: true
            }
          }
        });

(function () {
  'use strict'
  feather.replace({ 'aria-hidden': 'true' });
})();
/*
 * DataTable customization. Add these attributes to the <table> element, as necessary.
 * data-table-mode : "<mode>"                     -- Dom display mode (default is "normal")
 *    - "normal"   - Show lengthMenu, filter, export button, pagination
 *    - "export"   - Show export button only
 *    - "pages"    - Show pages and length menu on bottom
 * data-table-filename : "exportFileNameHere"     -- Export filename (default is page title)
 * data-export-columns : "exportColumnIndexesHere" -- Example: data-export-columns = "0,1,2,3" (default it exports all columns)
 * data-sum-columns    : "sumUpTheColumnsHere" -- Example: data-sum-columns="0,1,2,..,N" - Multiple columns can be given here
 */
var clickCountForPdf = 0;
var clickCountForExcel = 0;
function buildDatatableOptions(tableObject) {
	var fileNameExtended = $('#appendFileNameForExport').val();
	var exportFileName= $(tableObject).data("table-filename")+ (fileNameExtended != null ? fileNameExtended : "");
	var tableMode= $(tableObject).data("table-mode");
	//Export customized number of columns
	//Collecting export columns indexes
	var exportData = $(tableObject).data("export-columns");
	var exportedColumns = [];
	//gets the data table 'show entries' selection from cookie
//	var tableLength = $.cookie('tableLength');
//	if(!tableLength){
//		tableLength = $(tableObject).data("table-length");
//	}
//	var displayLengthCookieVal = parseInt(tableLength);


	if (exportData != null)
		if(exportData.toString().indexOf(",")>-1)
			exportedColumns = exportData.split(",");
		else
			exportedColumns = exportData;
	else
		exportedColumns.push(":visible");

	//===========================================
	//Output exported data formatting
	//===========================================
	var buttonCommon = {
			exportOptions : {
				columns : exportedColumns,
				format : {
					body : function(data, row, column, node) {
						data = data.replace(/[$,]/g, '');
						data = data.replace(/<.*?>/g, '');
						data = data.replace('&nbsp;','');
						return $.trim(data);
					}
				}
			}
		};
	var pdfButton = {
			exportOptions: {
				columns : exportedColumns,
			}
	}
	if (tableMode==null)
		tableMode= "normal";
	// Determines how custom elements are shown around the table - See https://datatables.net/reference/option/dom
	var domValue;
	if (tableMode==="normal") // With Pages, lengthMenu, filter, export
		domValue= "<'row'<'col-sm-6'l><'col-sm-5'f><'col-sm-1'B>>t<'row'<'col-sm-12'r>><'row'<'col-sm-5'i><'col-sm-7'p>>";
	else if (tableMode==="export") // Export only at top (No filter/export/pages)
		domValue= "<'row'<'col-sm-12'B>>t";
	else if (tableMode==="pages") // Pages/lengthMenu only at bottom (No filter/export)
		domValue= "t<'row'<'col-sm-12'r>><'row'<'col-sm-3'l><'col-sm-4'i><'col-sm-5'p>>";
	else if (tableMode==="withoutExport")//With Pages, lengthMenu, filter
		domValue= "<'row'<'col-sm-6'l><'col-sm-6'f>>t<'row'<'col-sm-12'r>><'row'<'col-sm-5'i><'col-sm-7'p>>"
	else
		domValue= null; // Use datatables default
	var dataTableOptions = {

		//retrieve or reloads based on the state of datatable
		retrieve : true,

		//set display length based on cookie value
		// sets value for 'show entries' select box
		displayLength: 7,

		// Page length (rows) menu options
		lengthMenu : [ [7, 10, 25, 50, 100, 1000000],//WF#116903 - setting 'All' to randomly big number. so that when initialized with 'All' fetches all records.
				[7, 10, 25, 50, 100, "All" ] ],

		// User sorting (default true)
		ordering : true,

		// Sort order (default none)
		// order : [ [ 0, "asc" ], [ 1, "desc" ] ],
		//TODO Need to discuss
		order : [ ], // order attribute must be empty for disable default sorting.
        lengthChange: false,
        searching: false,
        info: false,
		// Column definitions (default no special formatting)
		// See https://datatables.net/forums/discussion/33723/new-user-help-columns-vs-columndefs-for-client-side-rendering-of-ajax-data
		// To customize a column, add the specific class name (listed in targets below) to the <th> tag of the table column
		columnDefs: [
			  { targets: 'no-sort', orderable: false },
			  { targets: 'no-display', visible: false },
			  { targets: 'no-search', searchable: false }
			],

		// Auto-resize table on size change
		//responsive : true, //WF#144504 2019.03.28 - KMC01 - Turn off Responsive since we do not add / remove columns usually. Should increase performance.

		// How custom elements show up around the table
		dom : domValue,

		// Export buttons (default single Export button supporting excel and pdf; all columns exported)
		buttons : [ {
			extend : 'collection',
			text : 'Export',
			autoClose: true,
			buttons : [
				$.extend( false, {},buttonCommon, {
				extend : 'excel',
				title: (exportFileName && exportFileName.length>0)?exportFileName:null,
				action:  function excelHtml5Action(e, dt, button, config){
					var that = this;
					if(clickCountForExcel != 0)
						$.fn.dataTableExt.buttons.excelHtml5.action.call(that, e, dt, button, config);
					else
						createExcelHtml5Action(that, e, dt, button, config);
				},
			}), $.extend( false, {},pdfButton, {
				extend : 'pdf',
				title: (exportFileName && exportFileName.length>0)?exportFileName:null,
				action:  function pdfHtml5Action(e, dt, button, config){
					var that = this;
					if(clickCountForPdf != 0)
						$.fn.dataTableExt.buttons.pdfHtml5.action.call(that, e, dt, button, config);
					else
						createPdfHtml5Action(that, e, dt, button, config);
				},
			}), ]
		} ],
		// Search text
		language: {
			search: "Filter :"
		}
	};

	// If method 'customDataTableInit" exists, call it
	var customFunction = window["customDataTableInit"];
	if(typeof customFunction === 'function')
		customFunction(dataTableOptions, tableObject); //Replaced 'this' object with 'dataTableOptions' to apply customized options

	return dataTableOptions;
};
// Apply DataTable formatting to all applicable tables
// Note: This can be run multiple times without problem
function applyDataTable() {
	$('.app-data-table:not(.dataTable)').each(function(index) {
		var dataTableOptions = buildDatatableOptions($(this));
		//dataTableOptions = applyFooterCallback($(this), dataTableOptions);
		$(this).DataTable( dataTableOptions );
		//show the hidden initialized table
		$(this).show();
	});
}

function loadChartData(niftyChart, niftyPeChart, bankNiftyChart, bankNiftyPeChart) {
    $.ajax('/refresh', {
        dataType: 'json', // type of response data
        timeout: 500,     // timeout milliseconds
        success: function (data,status,xhr) {   // success callback function
            $.each(data.niftyCeList, function(i, item) {
                if(i <=6) {
                    console.log('Index: '+i+" strike: "+item.strikePrice);
                    niftyChartLabels.push(item.strikePrice+"CE");
                    niftyChartOiDayChangeData.push(item.changeInOi);
                    niftyChartOiNetChangeData.push(item.netChangeInOi);
                }
            });
            $.each(data.niftyPeList, function(i, item) {
                if(i <=6) {
                    console.log('Index: '+i+" strike: "+item.strikePrice);
                    niftyPeChartLabels.push(item.strikePrice+"PE");
                    niftyPeChartOiDayChangeData.push(item.changeInOi);
                    niftyPeChartOiNetChangeData.push(item.netChangeInOi);
                }
            });
            $.each(data.bankNiftyCeList, function(i, item) {
                if(i <=6) {
                    console.log('Index: '+i+" strike: "+item.strikePrice);
                    bankNiftyChartLabels.push(item.strikePrice+"CE");
                    bankNiftyChartOiDayChangeData.push(item.changeInOi);
                    bankNiftyChartOiNetChangeData.push(item.netChangeInOi);
                }
            });
            $.each(data.bankNiftyPeList, function(i, item) {
                if(i <=6) {
                    console.log('Index: '+i+" strike: "+item.strikePrice);
                    bankNiftyPeChartLabels.push(item.strikePrice+"PE");
                    bankNiftyPeChartOiDayChangeData.push(item.changeInOi);
                    bankNiftyPeChartOiNetChangeData.push(item.netChangeInOi);
                }
            });
        },
        error: function (jqXhr, textStatus, errorMessage) { // error callback
            alert('Error: ' + errorMessage);
        },
        complete: function (data) {
            console.log("niftyChartOiDayChangeLabels: "+niftyChartLabels)
            niftyChart.data.labels = niftyChartLabels;
            niftyChart.data.datasets[0].data = niftyChartOiDayChangeData; // or you can iterate for multiple datasets
            niftyChart.data.datasets[1].data = niftyChartOiNetChangeData;
            niftyChart.update(); // finally update our chart

            niftyPeChart.data.labels = niftyPeChartLabels.reverse();
            niftyPeChart.data.datasets[0].data = niftyPeChartOiDayChangeData.reverse(); // or you can iterate for multiple datasets
            niftyPeChart.data.datasets[1].data = niftyPeChartOiNetChangeData.reverse();
            niftyPeChart.update(); // finally update our chart

            bankNiftyChart.data.labels = bankNiftyChartLabels;
            bankNiftyChart.data.datasets[0].data = bankNiftyChartOiDayChangeData; // or you can iterate for multiple datasets
            bankNiftyChart.data.datasets[1].data = bankNiftyChartOiNetChangeData;
            bankNiftyChart.update(); // finally update our chart

            bankNiftyPeChart.data.labels = bankNiftyPeChartLabels.reverse();;
            bankNiftyPeChart.data.datasets[0].data = bankNiftyPeChartOiDayChangeData.reverse();; // or you can iterate for multiple datasets
            bankNiftyPeChart.data.datasets[1].data = bankNiftyPeChartOiNetChangeData.reverse();
            bankNiftyPeChart.update(); // finally update our chart
         }
    });

}

$(document).ready(function () {
    applyDataTable();
    loadChartData(niftyChart, niftyPeChart, bankNiftyChart, bankNiftyPeChart);
});