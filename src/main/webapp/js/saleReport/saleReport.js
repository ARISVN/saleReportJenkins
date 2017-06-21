var chart = null;
var oTableApps = null;
var oTableItems = null;
var itemMode = false;
//var itemMode2 = 0;

function processParam() {
	var result = true;
	var mode = jQuery('#modeInput').val();

	var statisticType = jQuery('#statisticType').val();
	var timeType = jQuery('#timeType').val();
	var dateFrom = jQuery('#dateFrom').val();
	var dateTo = jQuery('#dateTo').val();
	var viewType =  jQuery('.unitview ').filter('.active').val();
	var chartType = jQuery('.chartType').filter('.active').val(); 
	var appName = jQuery("#appNameInput").val();
	
	if(viewType == null || viewType == '') {
		viewType = "1";
	}
	
	var appId = "";
	var itemId = "";
	
	var appIdArray = jQuery('#selectedApp').val();
	if(!appIdArray){
		return false;
	}
	var length = appIdArray.length;
	if(length > 0) {
		appId = appIdArray[0];
		for (var i = 1; i < length; i++) {
			appId += "," + appIdArray[i];
		}
	}
	
	
	if(jQuery("#modeInput").val() == "item") {
		itemId = appId;
		appId = "";
	}
	
	/*if(itemMode2 == 1) {
		itemMode2 =2;
	}*/
	
	
	// Set value
	jQuery('#appId').val(appId);
	jQuery('#itemId').val(itemId);
	jQuery('#appNameInput').val(appName);
	jQuery('#viewType').val(viewType);
	jQuery('#chartType').val(chartType);
	
	return result;
}

function drawChart() {
	if(jQuery("#skipSaveHiddenDate").val() != 1) {
		setHiddenDate();
	} else {
		jQuery("#skipSaveHiddenDate").val("");
	}
	
	// Check condition
	if(!validateCondition()){
		return ;
	}
	
	if(!processParam()) {
		return;
	}
	
	var mode = jQuery('#modeInput').val();
	
	if(mode == "item") {
		iniItemTable();
	} else{
		iniAppTable();
	}
	
	
	var statisticType = jQuery('#statisticType').val();
	var timeType = jQuery('#timeType').val();
//	var dateFrom = jQuery('#dateFrom').val();
//	var dateTo = jQuery('#dateTo').val();
	var dateFrom = jQuery('#dateFromHidden').val();
	var dateTo = jQuery('#dateToHidden').val();
	
	var viewType =  jQuery('#viewType').val();
	var chartType = jQuery('#chartType').val();
	var appName = jQuery("#appNameInput").val();
	var appId = jQuery("#appId").val();
	var itemId = jQuery('#itemId').val();
	
	
	
	jQuery.ajax({
		url : "getChartData",
		data: {
				'statisticType': statisticType,
				'timeType': timeType,
				'dateFrom': dateFrom,
				'dateTo': dateTo,
				'appId': appId,
				'itemId': itemId,
				'appName': appName,
				'viewType': viewType,
				'chartType': chartType,
				'mode': mode
			},
		type : "POST",
		success : function(result) {
			console.log(result);
			// var obj = JSON.parse(result);
			
			if(chart) {
				chart.destroy();
			}
			
			// Draw chart
			var line = eval(result);
			var labels = new Array();
			var length = line.length;
			if(length == 0) {
				return;
			}
			
			if(chartType == 2) {
				
				for (var i = 0; i < length; i++) {
					labels.push(line[i][0][2]);
				}
				
				chart = jQuery.jqplot('chart', line, {
					axes : {
						xaxis : {
							renderer: jQuery.jqplot.CategoryAxisRenderer,
							labelRenderer: jQuery.jqplot.CanvasAxisLabelRenderer,
							tickRenderer : jQuery.jqplot.CanvasAxisTickRenderer,
							tickOptions: {
								formatter: function(format, value) {
									return value;
								},
								angle: -30
							}
						},
						yaxis : {
							
						}
					},
					legend: {
					    show: true,
						location: 'e',
					    placement: 'outside',
					    labels: labels
					},
					highlighter : {
						show : true,
						showTooltip: true,
						tooltipContentEditor: tooltipContentEditor
//						useAxesFormatters: true,
//						tooltipAxes: 'xy',
//						sizeAdjust : 7.5
					},
					cursor : {
						show : false
					}
				});
			}else {
//				labels = eval(line[length - 1]);
				var ticks= eval(line[length - 2]);
				labels= eval(line[length - 1]);
				
				line.splice(line.length-2,2);
//				if(line[0].length > 7) {
//					
//				}
				chart = jQuery.jqplot('chart', line, {
			        // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
			        animate: !jQuery.jqplot.use_excanvas,
			        seriesDefaults:{
			            renderer:jQuery.jqplot.BarRenderer,
			            pointLabels: { show: true }
			        },
			        axesDefaults: {
			            tickRenderer: jQuery.jqplot.CanvasAxisTickRenderer 
			        },
			        axes: {
			            xaxis: {
			                renderer: jQuery.jqplot.CategoryAxisRenderer,
			                tickOptions: {
								angle: -30
							},
			                ticks: ticks
			                
			            }
			        },
			        legend: {
					    show: true,
						location: 'e',
						placement: 'outside',
					    labels: labels
					},
			        highlighter: { show: false }
			    });
				
			}

			jQuery(window).bind('resize', function(event, ui) {
				chart.replot( { resetAxes: true } );
			});
		}
			
	});
}

function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {
    // display series_label, x-axis_tick, y-axis value
	//    return plot.series[seriesIndex]["label"] + ", " + plot.data[seriesIndex][pointIndex];
	return plot.data[seriesIndex][pointIndex][0] + ", " + plot.data[seriesIndex][pointIndex][1];
}

function validateCondition() {
	var result = true;
	if((!jQuery('#dateFrom').val())) {
		jQuery('#date1 .text-noti').css('display','block');
		result = false;
	}
	if(!jQuery('#dateTo').val() && !jQuery('#dateTo').hasClass('bl-hidden')) {
		jQuery('#date2 .text-noti').css('display','block');
		result = false;
	}
	if(jQuery('span.placeholder').text() == 'Select items') {
		jQuery('#mutliSelect').parent().find('.text-noti').css('display','block');
		result = false;
	}
	
	return result;
}

function resetAllView(){
	jQuery("#dateTo").datepicker("enable");
  	jQuery('#dateFrom').datepicker("enable");
  	jQuery("#dateTo").closest('.two-block').removeClass('mg-left');	
  	jQuery("#dateTo").addClass("bl-hidden");
  	jQuery('#date2 .text-noti').css('display','none');  	
  	var date = jQuery( ".datetimepicker" ).datepicker("option", { 
  		changeMonth : true, 
  		changeYear : true, 
  		dateFormat : "yy/mm",
  		onClose: function() {
        var iMonth = jQuery("#ui-datepicker-div .ui-datepicker-month :selected").val();
        var iYear = jQuery("#ui-datepicker-div .ui-datepicker-year :selected").val();
        jQuery(this).datepicker('setDate', new Date(iYear, iMonth, 1));
     	}
  	}).focus(function () {
        jQuery(".ui-datepicker-calendar").hide();		        
        jQuery(".ui-datepicker-month").show();
    });
	var minDate =  jQuery('#minAppReleaseDate').val();
	 if(minDate!=""){
		 jQuery('#dateFrom').datepicker("option", "minDate",new Date(minDate));
		 
	 }
  	jQuery("#dateFrom").datepicker('setDate', new Date());
  	jQuery(".agendaWeek-button").hide();
  	jQuery(".month-button").hide();
  	jQuery(".agendaDay-button").show();
  	jQuery(".year-button").hide();
  	resetButton();
  	
}

/**
 * @Author dung.dd
 * @Date 2015-12-03
 * get app id from url
 * @param 
 * @returns {String}
 */
function getAppIdParam() {
	var appId = "";
	var url = window.location.href;
	if(url.toLowerCase().indexOf("?appid=") > 0) {
		appId = url.split("?appId=")[1];
	}
	return appId;
}


/**
 * @Author nghiant
 * @Date 2015-11-25
 * get List Application to combox
 * @param 
 * @return 
 */
function initApplicationData() {
	jQuery.ajax({
		url : "getLstApplication",
		type : "POST",
		success : function(result) {
			var obj = JSON.parse(result);
			jQuery('#selectedApp option').remove();
			var cbApplication = jQuery("#selectedApp");
			jQuery.each(obj.data, function(val, text) {
				cbApplication.append(jQuery('<option></option>')
						.val(text.appId).html(text.nameApp));
			});

			if (jQuery().multipleSelect) {
				jQuery('#selectedApp').multipleSelect({
					width : '100%',
					selectAll : false,
					placeholder : "Select items"
				});
				
				jQuery("#mutliSelect ul li input[type='checkbox']").change(function(){
					showNameFromMultiSelect();
				});
				
				
				var appId = getAppIdParam();
				if(appId != "") {
					jQuery("#selectedApp").multipleSelect("setSelects", [appId]);
					showNameFromMultiSelect();
					drawChart();
				}
//				if(!(jQuery("#modeInput").val() == "item")) {
//					iniAppTable();
//				}
			}
			
		},
		error: function() {
			if(chart != null) {
				chart.destroy();
			}
		}
		
	});
}

function showNameFromMultiSelect() {
	
	var appName=jQuery('.appNameHide').text();//Can lam o thang hide
	var select=jQuery("#selectedApp").multipleSelect("getSelects", "text");
	var showAppName;
	if(appName!=""){
		showAppName=appName+","+select;
	}
	else{
		showAppName=select;
	}
	var appName=jQuery('.appName').empty();
	jQuery('.appName').text(showAppName);
	jQuery('#mutliSelect').parent().find('.text-noti').css('display','none');
}



/**
 * @Author dung
 * @Date 2015-11-29
 * get List Item to combox
 * @param 
 * @return 
 */
function initItemData(appId, appName) {
	jQuery.ajax({
		url : "getLstItem",
		type : "POST",
		data: {"appId": appId, "appName": appName},
		success : function(result) {
			debugger;
			var obj = JSON.parse(result);
			if(obj.data.length == null || obj.data.length == 0) {
				if(chart != null) {
					chart.destroy();
				}
			}
			jQuery('#selectedApp option').remove();
			var cbApplication = jQuery("#selectedApp");
			jQuery.each(obj.data, function(val, text) {
				cbApplication.append(jQuery('<option></option>')
						.val(text.itemId).html(text.itemName));
			});
			jQuery('.ms-drop').html('');
			
			if (jQuery().multipleSelect) {
				jQuery('#selectedApp').multipleSelect({
					width : '100%',
					selectAll : false,
					placeholder : "Select items"
				});

				jQuery("#mutliSelect ul li input[type='checkbox']").change(function(){
					showNameFromMultiSelect();
				});
				
				var idList = new Array();
				jQuery.each(jQuery("#selectedApp option"), function( index, element ) {
					idList.push(jQuery(element).val());
				});
				jQuery("#selectedApp").multipleSelect("setSelects", idList);
				showNameFromMultiSelect();
				drawChart();
				// iniItemTable();
			}
		},
		error: function() {
			if(chart != null) {
				chart.destroy();
			}
		}
	});
}


function resetBtn(){
	if(jQuery(".agendaWeek-button").hasClass("active")){
  		jQuery(".agendaWeek-button").removeClass("active");
  	}
  	if(jQuery(".month-button").hasClass("active")){
  		jQuery(".month-button").removeClass("active");
  	}
  	if(jQuery(".agendaDay-button").hasClass("active")){
  		jQuery(".agendaDay-button").removeClass("active");
  	}
  	if(jQuery(".year-button").hasClass("active")){
  		jQuery(".year-button").removeClass("active");
  	}	  		
}
/**
 * @Author nghiant
 * @Date 2015-11-25
 * check date is what day in week
 * @param date
 * @return weekDay
 */
function checkDayIs(date) {
	var weekday = new Array(7);
	weekday[0] = "Sunday";
	weekday[1] = "Monday";
	weekday[2] = "Tuesday";
	weekday[3] = "Wednesday";
	weekday[4] = "Thursday";
	weekday[5] = "Friday";
	weekday[6] = "Saturday";
	return weekday[date.getDay()];
}

/**
 * @Author dung.dd
 * @Date 2015-12-03
 * Get Min App Release Date 
 * @param date
 * @return weekDay
 */
function getMinAppReleaseDate() {
	jQuery.ajax({
		url : "getMinAppReleaseDate",
		type : "POST",
		success : function(result) {
			jQuery('#minAppReleaseDate').val(result);
		}
	});
}


function reloadDataTable() {
	var mode = jQuery('#modeInput').val();
	// call function to get DataTable
	if(mode == 'item') {
		oTableItems.ajax.reload();
	} else {
		oTableApps.ajax.reload();
	}
}


jQuery(document).ready(function() {
	
	getMinAppReleaseDate();
	resetAllView();
	if(jQuery("#modeInput").val() == "item") {
		// iniItemTable();
	} else {
		initApplicationData();
//		iniAppTable();
	}
	
	//hide page length menu
	 jQuery(".dataTables_length").hide();
	 
	 jQuery('body').on('click' , "#timeType", function(){
		 jQuery("#btnPrev").prop('disabled', false);
	     jQuery("#btnNext").prop('disabled', false);
		 jQuery("#btnPrev").removeClass('btn-disable');
		 jQuery("#btnNext").removeClass('btn-disable');
	 });
	 
	jQuery('body').on('click' , ".unitview", function(){
			 var viewType = jQuery(this).val();
			 var range = jQuery("#timeType").val();
			 if(range == 5 && viewType == 4) {
				 jQuery("#btnPrev").prop('disabled', true);
			     jQuery("#btnNext").prop('disabled', true);
				 jQuery("#btnPrev").addClass('btn-disable');
				 jQuery("#btnNext").addClass('btn-disable');
			 } else {
				 jQuery("#btnPrev").prop('disabled', false);
			     jQuery("#btnNext").prop('disabled', false);
				 jQuery("#btnPrev").removeClass('btn-disable');
				 jQuery("#btnNext").removeClass('btn-disable');
			 }
			 jQuery('#viewType').val(viewType);
			 drawChart();
	});
	
	// Choose chart type: columns or lines
	jQuery('body').on('click' , ".chartType", function(){
		if(!jQuery(this).hasClass("active")) {
			jQuery(this).addClass('active');
		}
		
		var otherChartType = jQuery(this).next();
		if(!jQuery(otherChartType).hasClass('chartType')) {
			otherChartType = jQuery(this).prev()
		}
			
		if(jQuery(otherChartType).hasClass("active")) {
			jQuery(otherChartType).removeClass('active');
		}
		 jQuery('#chartType').val(jQuery(this).val());
		 drawChart();
		 
	});
	
	 jQuery('body').on('click' , "#btnPrev", function(){
		 jQuery("#skipSaveHiddenDate").val("1");
		 var range = jQuery("#timeType").val();
		 var minDate =  jQuery('#minAppReleaseDate').val();
		 var parts =minDate.split('/');
		 var newMinDate = new Date(parts[0],parts[1]-1,parts[2]); 
		//please put attention to the month (parts[0]), Javascript counts months from 0:
		// January - 0, February - 1, etc
		 var from = new Date(jQuery("#dateFromHidden").val());
		 var fYear = from.getFullYear();
		 var fMonth = from.getMonth();
		 var fDate = from.getDate();
		 
		 var to = new Date(jQuery("#dateToHidden").val());
		 var tYear = to.getFullYear();
		 var tMonth = to.getMonth();
		 var tDate = to.getDate();
		 
		 if(range == 1) { // year 
			 jQuery("#btnNext").prop('disabled', false);
			 jQuery("#btnNext").removeClass('btn-disable');
			 var viewType = jQuery('.unitview.active').val();
			 var timeDiff = Math.abs(new Date().getTime() - newMinDate.getTime());
		     var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
		     var iYear = jQuery("#ui-datepicker-div .ui-datepicker-year :selected").val();
			 switch (viewType) {
			    case '1':{ // day
					 if(typeof iYear == 'undefined') {
						 iYear = new Date().getFullYear();
					 }
					 var strDate = new Date(fYear, fMonth - 1, 1);
					 var eDate = new Date(tYear, tMonth , 0);
					 if(strDate.getFullYear() <= newMinDate.getFullYear() && strDate.getMonth() < newMinDate.getMonth()) {
						 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 return;
					 }
					 
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',strDate));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',eDate));
					 if (iYear != strDate.getFullYear()) {
						 jQuery('#dateFrom').datepicker().datepicker("setDate", to);
					 }
					 break;
			    }	
			    case '3': //month
			    {
			    	nFrom = new Date(fYear -1 , 0, 1);
			    	nTo = new Date(fYear - 1, 12, 0);
			    	if(diffDays > 365) {
				    	 jQuery("#btnPrev").prop('disabled', false);
						 jQuery("#btnPrev").removeClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', false);
						 jQuery("#btnNext").removeClass('btn-disable');
						 
						 if(nFrom.getFullYear() <= newMinDate.getFullYear() && nFrom.getMonth() < newMinDate.getMonth()){
							 jQuery("#btnPrev").prop('disabled', true);
							 jQuery("#btnPrev").addClass('btn-disable');
							 return;
						 }
						 if (iYear != nFrom.getFullYear()) {
							 jQuery('#dateFrom').datepicker().datepicker("setDate", nFrom);
						 }
				     } else {
				    	 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
				     }
			    	
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 break;
			    }			       
			    case '4': {
			    	jQuery("#btnNext").prop('disabled', true);
			    	jQuery("#btnNext").addClass('btn-disable');
			    	jQuery("#btnPrev").prop('disabled', true);
			    	jQuery("#btnPrev").addClass('btn-disable');
					var nFrom = new Date(newMinDate.getFullYear(), 0, 1);
					var nTo = new Date(new Date().getFullYear(), 12, 0);
					jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
			    	break;
			    }
			}
		 } else if(range == 2) { // month 
			 jQuery("#btnNext").prop('disabled', false);
			 jQuery("#btnNext").removeClass('btn-disable');
			 var iYear = jQuery("#ui-datepicker-div .ui-datepicker-year :selected").val();
			 var iMonth = jQuery("#ui-datepicker-div .ui-datepicker-month :selected").val();
			 var select = new Date(iYear, (iMonth/1 - 1), 1);
			 if(typeof iYear == 'undefined') {
				 iYear = new Date().getFullYear();
			 }
			 
			 if(select.getFullYear() <= newMinDate.getFullYear() && select.getMonth() < newMinDate.getMonth()) {
				 jQuery("#btnPrev").prop('disabled', true);
				 jQuery("#btnPrev").addClass('btn-disable');
				 return;
			 }
			 var strDate = new Date(fYear, fMonth - 1, 1);
			 var eDate = new Date(tYear, tMonth , 0);
			 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',strDate));
			 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',eDate));
			 jQuery('#dateFrom').datepicker('setDate', strDate);
		 } else if(range == 3) { // week 
			 jQuery("#btnNext").prop('disabled', false);
			 jQuery("#btnNext").removeClass('btn-disable');
			 var nFrom = new Date(fYear, fMonth, fDate - 7);
			 var nTo = new Date(fYear, fMonth , fDate - 1);
			 if(nFrom.getFullYear() <= newMinDate. getFullYear() && getWeekFromDate(nFrom) < getWeekFromDate(newMinDate)) {
				 jQuery("#btnPrev").prop('disabled', true);
				 jQuery("#btnPrev").addClass('btn-disable');
				 return;
			 }
			 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',nFrom));
			 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',nTo));
			 jQuery('#dateFrom').datepicker('setDate', nFrom);
		 } else if(range == 4) { // range
			 var viewType = jQuery('.unitview.active').val();
			 var timeDiff = Math.abs(to.getTime() - from.getTime());
		     var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
			 var diffWeek = Math.ceil(timeDiff / (1000 * 3600 * 24 * 7));
			 var nFrom = from;
			 var nTo = to;
			 switch (viewType) {
			    case '1':{ // day
			    	
				     if(diffDays > 30) {
				    	 nFrom = new Date(fYear, fMonth, fDate- 30);
				    	 nTo = new Date(tYear, tMonth, tDate - 30);
				    	 
				    	 jQuery("#btnPrev").prop('disabled', false);
						 jQuery("#btnPrev").removeClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', false);
						 jQuery("#btnNext").removeClass('btn-disable');
						 
						 if(nFrom.getFullYear() <= newMinDate.getFullYear() && nFrom.getMonth() < newMinDate.getMonth()) {
							 jQuery("#btnPrev").prop('disabled', true);
							 jQuery("#btnPrev").addClass('btn-disable');
							 return;
						 }
				     } else {
				    	 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
				     }
				     
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 break;
			    }
			    case '2':
			    {
			    	if(diffWeek > 26) {
				    	 nFrom = new Date(fYear, fMonth, from.getTime() - diffWeek*26 + diffDays);
				    	 nTo = new Date(tYear, tMonth, to.getTime() - diffWeek*26 + diffDays);
				    	 
				    	 jQuery("#btnPrev").prop('disabled', false);
						 jQuery("#btnPrev").removeClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', false);
						 jQuery("#btnNext").removeClass('btn-disable');
						 
						 if(nFrom.getTime() - from.getTime() < diffWeek * 26){
							 jQuery("#btnPrev").prop('disabled', true);
							 jQuery("#btnPrev").addClass('btn-disable');
							 return;
						 }
				     } else {
				    	 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
				     }
			    	 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 break;
			    }	
			    case '3':
			    {
			    	if(diffDays > 365) {
				    	 nFrom = new Date(fYear, fMonth- 1, 1);
				    	 nTo = new Date(fYear, fMonth, 0);
				    	 
				    	 jQuery("#btnPrev").prop('disabled', false);
						 jQuery("#btnPrev").removeClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', false);
						 jQuery("#btnNext").removeClass('btn-disable');
						 
						 if(nFrom.getFullYear() <= from.getFullYear() && nFrom.getMonth() < from.getMonth()){
							 jQuery("#btnPrev").prop('disabled', true);
							 jQuery("#btnPrev").addClass('btn-disable');
							 return;
						 }
				     } else {
				    	 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
				     }
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 break;
			    }			       
			    case '4': {
			    	jQuery("#btnNext").prop('disabled', true);
			    	jQuery("#btnNext").addClass('btn-disable');
			    	jQuery("#btnPrev").prop('disabled', true);
			    	jQuery("#btnPrev").addClass('btn-disable');
					var nFrom = new Date(newMinDate.getFullYear(), 0, 1);
					var nTo = new Date(new Date().getFullYear(), 12, 0);
					jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
			    	break;
			    }
			}
		 }else if(range == 5) {
			 var viewType = jQuery('.unitview.active').val();
			 if(typeof viewType == 'undefined' || viewType == '') {
				 alert('Please select unit view');
				 return;
			 }
			 switch (viewType) {
			    case '1':{
			    	jQuery("#btnNext").prop('disabled', false);
					jQuery("#btnNext").removeClass('btn-disable');
					 var nFrom = new Date(fYear, fMonth -1, 1);
					 var nTo = new Date(fYear, fMonth, 0);
					 
					 if(fYear <= newMinDate.getFullYear() && fMonth -1 < newMinDate.getMonth()) {
						 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 return;
					 }
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 jQuery('#dateFrom').datepicker('setDate', nFrom);
					 jQuery('#dateTo').datepicker('setDate', nTo);
					 break;
			    }
			    case '3':
			    {
			    	jQuery("#btnNext").prop('disabled', false);
					jQuery("#btnNext").removeClass('btn-disable');
					 var from = jQuery('#dateFrom').datepicker("getDate");
					 var nFrom = new Date(fYear -1, 0, 1);
					 var nTo = new Date(fYear- 1, 12, 0);
					 if(fYear -1 <= newMinDate.getFullYear()) {
						 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 return;
					 }
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 jQuery('#dateFrom').datepicker('setDate', nFrom);
					 jQuery('#dateTo').datepicker('setDate', nTo);
					 break;
			    }			       
			    case '4': {
			    	jQuery("#btnNext").prop('disabled', true);
			    	jQuery("#btnNext").addClass('btn-disable');
			    	jQuery("#btnPrev").prop('disabled', true);
			    	jQuery("#btnPrev").addClass('btn-disable');
			    	var from = jQuery('#dateFrom').datepicker("getDate");
					var nFrom = new Date(newMinDate.getFullYear(), 0, 1);
					var nTo = new Date(new Date().getFullYear(), 12, 0);
					jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					jQuery('#dateFrom').datepicker('setDate', nFrom);
					jQuery('#dateTo').datepicker('setDate', nTo);
			    	break;
			    }
			}
		 }
		 drawChart();
		 
	 });
	 
	 jQuery('body').on('click' , "#btnNext", function() {
		 jQuery("#skipSaveHiddenDate").val("1");
		 var range = jQuery("#timeType").val();
		 var minDate =  jQuery('#minAppReleaseDate').val();
		 var parts =minDate.split('/');
		 var newMinDate = new Date(parts[0],parts[1]-1,parts[2]); 
		//please put attention to the month (parts[0]), Javascript counts months from 0:
		// January - 0, February - 1, etc
		 var from = new Date(jQuery("#dateFromHidden").val());
		 var fYear = from.getFullYear();
		 var fMonth = from.getMonth();
		 var fDate = from.getDate();
		 
		 var to = new Date(jQuery("#dateToHidden").val());
		 var tYear = to.getFullYear();
		 var tMonth = to.getMonth();
		 var tDate = to.getDate();
		 if(range == 1) {// year
			 jQuery("#btnPrev").prop('disabled', false);
			 jQuery("#btnPrev").removeClass('btn-disable');
			 var viewType = jQuery('.unitview.active').val();
			 var timeDiff = Math.abs(new Date().getTime() - newMinDate.getTime());
		     var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
		     var iYear = jQuery("#ui-datepicker-div .ui-datepicker-year :selected").val();
			 switch (viewType) {
			    case '1':{ // day
					 if(typeof iYear == 'undefined') {
						 iYear = new Date().getFullYear();
					 }
					 if(new Date(fYear, fMonth + 1, 1).getTime() > new Date().getTime()) {
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
						 return;
					 }
					 var strDate = new Date(fYear, fMonth +1, 1);
					 var eDate = new Date(tYear, tMonth + 2 , 0);
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',strDate));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',eDate));
					 if (iYear != strDate.getFullYear()) {
						 jQuery('.datetimepicker').datepicker().datepicker("setDate", to);
					 }
					 break;
			    }	
			    case '3': //month
			    {
			    	nFrom = new Date(fYear + 1 , 0, 1);
			    	nTo = new Date(fYear + 1, 12, 0);
			    	if(diffDays > 365) {
				    	 jQuery("#btnPrev").prop('disabled', false);
						 jQuery("#btnPrev").removeClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', false);
						 jQuery("#btnNext").removeClass('btn-disable');
						 
						 if(nTo.getFullYear() > new Date().getFullYear()){
							 jQuery("#btnNext").prop('disabled', true);
							 jQuery("#btnNext").addClass('btn-disable');
							 return;
						 }
						 if (iYear != nFrom.getFullYear()) {
							 jQuery('#dateFrom').datepicker().datepicker("setDate", nFrom);
						 }
				     } else {
				    	 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
				     }
			    	
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 break;
			    }			       
			    case '4': {
			    	jQuery("#btnNext").prop('disabled', true);
			    	jQuery("#btnNext").addClass('btn-disable');
			    	jQuery("#btnPrev").prop('disabled', true);
			    	jQuery("#btnPrev").addClass('btn-disable');
					var nFrom = new Date(newMinDate.getFullYear(), 0, 1);
					var nTo = new Date(new Date().getFullYear(), 12, 0);
					jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
			    	break;
			    }
			}
		 
		 } else if(range == 2) { // month
			 jQuery("#btnPrev").prop('disabled', false);
			 jQuery("#btnPrev").removeClass('btn-disable');
			 var iYear = jQuery("#ui-datepicker-div .ui-datepicker-year :selected").val();
			 var iMonth = jQuery("#ui-datepicker-div .ui-datepicker-month :selected").val();
			 var select = new Date(iYear, (iMonth/1 + 1), 1);
			 if(typeof iYear == 'undefined') {
				 iYear = new Date().getFullYear();
			 }
			 
			 if(select.getFullYear() > new Date().getFullYear() 
					 || (select.getFullYear() == new Date().getFullYear() && select.getMonth() > new Date().getMonth())) {
				 jQuery("#btnNext").prop('disabled', true);
				 jQuery("#btnNext").addClass('btn-disable');
				 return;
			 }
			 var strDate = new Date(fYear, fMonth + 1 , 1);
			 var eDate = new Date(tYear, tMonth + 2 , 0);
			 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',strDate));
			 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',eDate));
			 jQuery('#dateFrom').datepicker('setDate', strDate);
		 }else if(range == 3) { // week
			 jQuery("#btnPrev").prop('disabled', false);
			 jQuery("#btnPrev").removeClass('btn-disable');
			 var nFrom = new Date(tYear, tMonth, tDate + 1);
			 var nTo = new Date(tYear, tMonth , tDate + 7);
			 if(nTo.getFullYear() >= new Date.getFullYear() && getWeekFromDate(nFrom) > getWeekFromDate(new Date)) {
				 jQuery("#btnNext").prop('disabled', true);
				 jQuery("#btnNext").addClass('btn-disable');
				 return;
			 }
			 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',nFrom));
			 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',nTo));
			 jQuery('#dateFrom').datepicker('setDate', nFrom);
		 }else if(range == 4) { // range
			 var viewType = jQuery('.unitview.active').val();
			 var timeDiff = Math.abs(to.getTime() - from.getTime());
		     var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
			 var diffWeek = Math.ceil(timeDiff / (1000 * 3600 * 24 * 7));
			 var nFrom = from;
			 var nTo = to;
			 switch (viewType) {
			    case '1':{ // day
				     if(diffDays > 30) {
				    	 nFrom = new Date(fYear, fMonth, fDate + 30);
				    	 nTo = new Date(tYear, tMonth, tDate + 30);
				    	 
				    	 jQuery("#btnPrev").prop('disabled', false);
						 jQuery("#btnPrev").removeClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', false);
						 jQuery("#btnNext").removeClass('btn-disable');
						 
						 if(nFrom.getFullYear() >= new Date().getFullYear() && nFrom.getMonth() > new Date().getMonth()) {
							 jQuery("#btnNext").prop('disabled', true);
							 jQuery("#btnNext").addClass('btn-disable');
							 return;
						 }
				     } else {
				    	 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
				     }
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 break;
			    }
			    case '2':
			    {
			    	if(diffWeek > 26) {
				    	 nFrom = new Date(fYear, fMonth, from.getTime() + diffWeek*26 + diffDays);
				    	 nTo = new Date(tYear, tMonth, to.getTime() + diffWeek*26 + diffDays);
				    	 jQuery("#btnPrev").prop('disabled', false);
						 jQuery("#btnPrev").removeClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', false);
						 jQuery("#btnNext").removeClass('btn-disable');
						 
						 if(new Date.getTime() - nTo.getTime() < diffWeek * 26){
							 jQuery("#btnNext").prop('disabled', true);
							 jQuery("#btnNext").addClass('btn-disable');
							 return;
						 }
				     } else {
				    	 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
				     }
			    	 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 break;
			    }	
			    case '3':
			    {
			    	if(diffDays > 365) {
				    	 nFrom = new Date(fYear, fMonth, 1);
				    	 nTo = new Date(fYear, fMonth + 1, 0);
				    	 
				    	 jQuery("#btnPrev").prop('disabled', false);
						 jQuery("#btnPrev").removeClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', false);
						 jQuery("#btnNext").removeClass('btn-disable');
						 
						 if(nTo.getFullYear() > new Date().getFullYear() && nTo.getMonth() > new Date().getMonth()){
							 jQuery("#btnNext").prop('disabled', true);
							 jQuery("#btnNext").addClass('btn-disable');
							 return;
						 }
				     } else {
				    	 jQuery("#btnPrev").prop('disabled', true);
						 jQuery("#btnPrev").addClass('btn-disable');
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
				     }
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 break;
			    }			       
			    case '4': {
			    	jQuery("#btnNext").prop('disabled', true);
			    	jQuery("#btnNext").addClass('btn-disable');
			    	jQuery("#btnPrev").prop('disabled', true);
			    	jQuery("#btnPrev").addClass('btn-disable');
					var nFrom = new Date(fYear.getFullYear(), 0, 1);
					var nTo = new Date(tYear.getFullYear(), 12, 0);
					jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
			    	break;
			    }
			}
		 }else if(range == 5) {
			 var viewType = jQuery('.unitview.active').val();
			 if(typeof viewType == 'undefined' || viewType == '') {
				 alert('Please select unit view');
				 return;
			 }
			 switch (viewType) {
			    case '1':{
			    	jQuery("#btnPrev").prop('disabled', false);
					jQuery("#btnPrev").removeClass('btn-disable');
					 var nFrom = new Date(fYear, fMonth , 1);
					 var nTo = new Date(from.getFullYear(), from.getMonth() + 1 , 0);
					 
					 if(from.getFullYear() > new Date().getFullYear() && tMonth + 1 > new Date().getMonth()) {
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
						 return;
					 }
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 jQuery('#dateFrom').datepicker('setDate', nFrom);
					 jQuery('#dateTo').datepicker('setDate', nTo);
					 break;
			    }
			    case '3':
			    {
			    	jQuery("#btnPrev").prop('disabled', false);
					jQuery("#btnPrev").removeClass('btn-disable');
					 var nFrom = new Date(fYear +1 , 0, 1);
					 var nTo = new Date(fYear + 1, 12, 0);
					 if(tYear + 1 > new Date().getFullYear()) {
						 jQuery("#btnNext").prop('disabled', true);
						 jQuery("#btnNext").addClass('btn-disable');
						 return;
					 }
					 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					 jQuery('#dateFrom').datepicker('setDate', nFrom);
					 jQuery('#dateTo').datepicker('setDate', nTo);
					 break;
			    }			       
			    case '4': {
			    	jQuery("#btnNext").prop('disabled', true);
			    	jQuery("#btnNext").addClass('btn-disable');
			    	jQuery("#btnPrev").prop('disabled', true);
			    	jQuery("#btnPrev").addClass('btn-disable');
			    	var from = jQuery('#dateFrom').datepicker("getDate");
					var nFrom = new Date(newMinDate.getFullYear(), 0, 1);
					var nTo = new Date(new Date().getFullYear(), 12, 0);
					jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nFrom));
					jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd', nTo));
					jQuery('#dateFrom').datepicker('setDate', nFrom);
					jQuery('#dateTo').datepicker('setDate', nTo);
			    	break;
			    }
			}
		 }
		 drawChart();
	 });

	 
	 if(jQuery("#modeInput").val() == "item") {
		 jQuery("#tblItem").show();
		 jQuery("#tblApp").hide();
	 } else {
		 jQuery("#tblItem").hide();
		 jQuery("#tblApp").show();
	 }
	 jQuery('body').on('dblclick' , ".jqplot-table-legend-label", function(){
		 
		 if(jQuery("#modeInput").val() == "item") {
			 jQuery("#modeInput").val("application");
			 jQuery("#tblItem").hide();
			 jQuery("#tblApp").show();
			 // iniAppTable();
		 } else {
			 if(itemMode == false) {
				 itemMode = true;
				 jQuery("#modeInput").val("item");
				 jQuery("#tblItem").show();
				 jQuery("#tblApp").hide();
				 
				 jQuery("#appNameInput").val(jQuery(this).text());
				 var appName = jQuery("#appNameInput").val();
				 if(appName != null && appName != "") {
					 jQuery('#statisticNameDisplay').text(appName);
					 jQuery('#appItemComboLbl').text('Item');
					 jQuery('#appItemSelectedLbl').text('Items');
					 
					 initItemData(null, appName);
					 
				 }
			 }
			 
		 }
		 
//		 jQuery( "form[name='frmSale']" ).attr("action", "report");
//		 jQuery( "form[name='frmSale']" ).submit();
	 });
  	jQuery('.agendaWeek-button').click(function () {
  		resetBtn();
  	  	jQuery(".agendaWeek-button").addClass("active");
  	    var from=jQuery("#dateFrom").datepicker().val();
		var to=jQuery("#dateTo").datepicker().val();
		var dateFrom;
		var dateTo;
		if(from===""){
			dateFrom=new Date();
		}
		else
		{
			dateFrom = new Date(from);
		}
		if(to===""){
			dateTo = new Date();
		}
		else{
			debugger;
			dateTo = new Date(to);
			if(dateTo>new Date()){
				dateTo = new Date();
			}
		}
		
		//-----------------------------
		dateFrom=nextOrPrevDay(dateFrom,1);
		dateTo=nextOrPrevDay(dateTo,2);
		jQuery("#dateFrom").datepicker("setDate", dateFrom);
		//jQuery("#dateTo").datepicker("setDate", dateTo);//dung de set day nhung vi co limit day nen ko the dung ham nay
		var dateS=jQuery.datepicker.formatDate('yy/mm/dd',dateTo);
		jQuery('#dateTo').val(dateS)
		
		
  	});
  	
  	jQuery('#btnChart').click(function () {
  		drawChart();
  		
  		
  	});
  	
  	
    jQuery('.month-button').click(function () {
    	resetBtn();
    	jQuery(".month-button").addClass("active");
    	var from=jQuery("#dateFrom").datepicker().val();
 		var to=jQuery("#dateTo").datepicker().val();
 		var dateFrom;
 		var dateTo;
 		if(from===""){
 			dateFrom=new Date();
 		}
 		else
 		{
 			dateFrom = new Date(from);
 		}
 		if(to===""){
 			dateTo = new Date();
 		}
 		else{
 			dateTo = new Date(to);
 		}
    	
    	if(jQuery( "#timeType" ).val()==="4"){//range
        	
        	dateFrom.setDate(1);
//    		var month=dateTo.getMonth() + 1;
//    		switch (month) {
//    		  case 1,3,5,7,8,10,12:
//    			  dateTo.setDate(31);
//    			  break;
//    		  case 2:
//    			  dateTo.setMonth(3);
//    			  dateTo.setDate(31);
//    			  break;
//    		  case 4,6,9,11:
//    			  dateTo.setDate(30);
//    			  break;
//    		
//    	    }
        	dateTo.setDate(1);
        	dateTo.setMonth(dateTo.getMonth() + 1);
        	dateTo.setDate(dateTo.getDate() - 1);
    		jQuery("#dateFrom").datepicker("setDate", dateFrom);
    		var dateS=jQuery.datepicker.formatDate('yy/mm/dd',dateTo);
    		jQuery('#dateTo').val(dateS)
    	}
    	else if(jQuery( "#timeType" ).val()==="5"){//all time, reset 01/01------>31/12
    		dateFrom=new Date();
    		dateTo = new Date();
        	dateFrom.setMonth(0);
    		dateFrom.setDate(1);
    		dateTo.setMonth(11);
    		dateTo.setDate(31);
    		jQuery("#dateFrom").datepicker("setDate", dateFrom);
    		var dateS=jQuery.datepicker.formatDate('yy/mm/dd',dateTo);
    		jQuery('#dateTo').val(dateS)
    	}
    	
    	
  	});

    jQuery('.agendaDay-button').click(function () {
    	resetBtn();
    	jQuery('.agendaDay-button').addClass("active");
    	
    	if(jQuery( "#timeType" ).val()==="5"){//all time
     		var dateFrom;
     		var dateTo;
     		dateFrom=new Date();
     		dateTo = new Date();
            dateFrom.setDate(1);
//        	var month=dateTo.getMonth() + 1;
//        	switch (month) {
//        		 case 1,3,5,7,8,10,12:
//        			 dateTo.setDate(31);
//        			 break;
//        		 case 2:
//        			 dateTo.setMonth(3);
//        			 dateTo.setDate(31);
//        			 break;
//        		 case 4,6,9,11:
//        			 dateTo.setDate(30);
//        			 break;
//        		
//        	}
           dateTo.setDate(1);
           dateTo.setMonth(dateTo.getMonth() + 1);
           dateTo.setDate(dateTo.getDate() - 1);
           jQuery("#dateFrom").datepicker("setDate", dateFrom);
           var dateS=jQuery.datepicker.formatDate('yy/mm/dd',dateTo);
           jQuery('#dateTo').val(dateS);
    	}
    	else if(jQuery( "#timeType" ).val()==="3"){
    	   var dateFrom;
     	   var dateTo;
     	   dateFrom=new Date();
     	   dateTo = new Date();
           dateFrom=nextOrPrevDay(dateFrom,1);
   		   dateTo=nextOrPrevDay(dateTo,2);
           jQuery("#dateFrom").datepicker("setDate", dateFrom);
           var dateS=jQuery.datepicker.formatDate('yy/mm/dd',dateTo);
           jQuery('#dateTo').val(dateS);
    	}
    	
	});

    jQuery('.year-button').click(function () {
    	resetBtn();
    	jQuery('.year-button').addClass("active");
    	
    	var from=jQuery("#dateFrom").datepicker().val();
 		var to=jQuery("#dateTo").datepicker().val();
 		var dateFrom;
 		var dateTo;
 		if(from===""){
 			dateFrom=new Date();
 		}
 		else
 		{
 			dateFrom = new Date(from);
 		}
 		if(to===""){
 			dateTo = new Date();
 		}
 		else{
 			dateTo = new Date(to);
 		}
    	
    	dateFrom.setMonth(0);
		dateFrom.setDate(1);
		dateTo.setMonth(11);
		dateTo.setDate(31);
		jQuery("#dateFrom").datepicker("setDate", dateFrom);
		var dateS=jQuery.datepicker.formatDate('yy/mm/dd',dateTo);
		jQuery('#dateTo').val(dateS)
	});
	
	jQuery('#dateFrom').change(function(){
		var dateFrom=jQuery("#dateFrom").datepicker().val();
		var dateTo=jQuery("#dateTo").datepicker().val();
		switch (jQuery( "#timeType" ).val()) {
		  case "3":
			  var fromDate=nextOrPrevDay(new Date(dateFrom),1);
			  jQuery("#dateFrom").datepicker("setDate", fromDate);
			  var toDate = nextOrPrevDay(new Date(dateFrom),2);
			  var dateS=jQuery.datepicker.formatDate('yy/mm/dd',toDate);
			  jQuery('#dateTo').val(dateS)
			  break;
		  case "4":
			  if(dateFrom!=""){//limit dateTo
				jQuery('#dateTo').datepicker("option", "minDate",new Date(dateFrom));
			  }
			  check(dateFrom,dateTo);
			  break;
		  case "5":
			  if(dateFrom!=""){//limit dateTo
				jQuery('#dateTo').datepicker("option", "minDate",new Date(dateFrom));
			  }
			  check(dateFrom,dateTo);
			  break;
	    }
		if((!jQuery('#dateFrom').val())) {
			jQuery('#date1 .text-noti').css('display','block');
		} else {
			jQuery('#date1 .text-noti').css('display','none');
		}
	});
	jQuery('#dateTo').change(function(){
		var dateFrom=jQuery("#dateFrom").datepicker().val();
		var dateTo=jQuery("#dateTo").datepicker().val();
		if(dateTo!=""){//limit dateFrom
			jQuery('#dateFrom').datepicker("option", "maxDate",new Date(dateTo));
		}
		else{
			jQuery('#dateFrom').datepicker("option", "maxDate",new Date());
		}
		
		check(dateFrom,dateTo);

		if(!jQuery('#dateTo').val()) {
			jQuery('#date2 .text-noti').css('display','block');
		} else {
			jQuery('#date2 .text-noti').css('display','none');
		}
	});
	
	

	jQuery('.jqplot-table-legend-label').each(function(){
		jQuery(this).click(function(){
			alert('you have clicked on the serie : ' + jQuery(this).html());
		});
	});
	 
	 
	 jQuery('#btnExport').click(function () {
		 if(!validateCondition()) {
			 return;
		 }
		 if(!processParam()) {
			 return;
		 }
		 jQuery("form").submit();
	 });
	 
	 jQuery('#btnRs').click(function () {
		 jQuery("#timeType option[value=2]").attr('selected', true);
		 jQuery('.datetimepicker').datepicker("option", "maxDate",new Date());
		 jQuery("#dateFrom").datepicker("setDate", new Date());
		 resetAllView();
		 jQuery("#selectedApp").multipleSelect("uncheckAll");
		 jQuery('.appName').empty();
		 
		 //select time
		 //date
		 //uncheck
		 //name clear
	 });
	 jQuery('#btnExport').click(function () {
		   jQuery("form").submit();   
		  });

	// Scroll to top
	jQuery("a[href='#top']").click(function() {
		jQuery("html, body").animate({scrollTop: 0}, "slow");
		return false;
	});

	// page top PC

	jQuery("#backTop").hide();

	jQuery(window).on("scroll", function() {

		if (jQuery(this).scrollTop()) {
			jQuery('#backTop').slideDown("fast");
		} else {
			jQuery('#backTop').slideUp("fast");
		}

		scrollHeight = jQuery(document).height();
		scrollPosition = jQuery(window).height() + jQuery(window).scrollTop();
		footHeight = jQuery("#footer-container").innerHeight();

		if (scrollHeight - scrollPosition >= footHeight) {
			jQuery("#backTop").css({
				"position": "absolute",
				"bottom": footHeight + 15
			});
		} else {
			jQuery("#backTop").css({
				"position": "fixed",
				"bottom": "37px"
			});
		}
	});
});

/**
 * @Author nghiant
 * @Date 2015-11-25
 * check dateFrom and dateTo is valid
 * @param dateFrom,dateTo
 * @return 
 */
function check(dateFrom,dateTo){
	var weekSelect=jQuery(".agendaWeek-button").hasClass("active");
	var monthSelect =jQuery(".month-button").hasClass("active");
	var yearSelect=jQuery(".year-button").hasClass("active");
	//Set value unit view if range invalid
	if(dateFrom!="" && dateTo!=""){
		
		var weekDayFrom = checkDayIs(new Date(dateFrom));
		var weekDayTo = checkDayIs(new Date(dateTo));
		
		if(weekDayFrom!="Monday" || weekDayTo!="Sunday"){//week
			if(weekSelect){//dang la week-->day
				resetBtn();
				jQuery(".agendaDay-button").addClass("active");
			}
		}
		
		var dayFrom=new Date(dateFrom).getDate();
		if(dayFrom!=1){
			if(monthSelect){//dang la month-->day
				resetBtn();
				jQuery(".agendaDay-button").addClass("active");
				
			}
			if(yearSelect){//dang la year-->day
				resetBtn();
				jQuery(".agendaDay-button").addClass("active");
				
			}
		}
		else{
			var dayTo = new Date(dateTo).getDate();
			var monthTo=new Date(dateTo).getMonth();
			monthTo=monthTo+1;
			if(monthTo!=12){
				if(yearSelect){//dang la year-->day
					resetBtn();
					jQuery(".agendaDay-button").addClass("active");
					
				}
			}
//			switch (monthTo) {
//			  case 1,3,5,7,8,10,12:
//				 if(dayTo!=31){
//					 if(monthSelect){//dang la month-->day
//						 resetBtn();
//						 jQuery(".agendaDay-button").addClass("active");
//							
//					 }
//				 }
//				  break;
//			  case 2:
//				 
//			  case 4,6,9,11:
//				  if(dayTo!=30){
//						 if(monthSelect){//dang la month-->day
//							 resetBtn();
//							 jQuery(".agendaDay-button").addClass("active")
//								
//						 }
//						 if(yearSelect){//dang la year-->day
//							 resetBtn();
//							 jQuery(".agendaDay-button").addClass("active");
//								
//						 }
//				  }
//				  break;
//		    }
			var toDate = new Date(dateTo);
			toDate.setDate(1);
			toDate.setMonth(toDate.getMonth() + 1);
			toDate.setDate(toDate.getDate() - 1);
			if(dayTo!=toDate.getDate()){
				if(monthSelect){//dang la month-->day
					 resetBtn();
					 jQuery(".agendaDay-button").addClass("active");
						
				 }
				if(yearSelect){//dang la year-->day
					 resetBtn();
					 jQuery(".agendaDay-button").addClass("active");
						
				 }
			}
		}
		
	}

}
/**
 * @Author nghiant
 * @Date 2015-11-25
 * move to first day(Monday) or last day(Sunday) of week
 * @param date,type
 * @return date
 */
function nextOrPrevDay(date,type){
	var weekDay = checkDayIs(date);
	var day =date.getDate();
	switch (weekDay) {
	  case "Sunday":
		  if(type==1){
			  date.setDate(day-6);
		  }
		  
		  break;
	  case "Monday":
		  if(type==2){
			  date.setDate(day+6);
		  }
		  break;
	  case "Tuesday":
		  if(type==1){
			  date.setDate(day-1);  
		  }
		  else{
			  date.setDate(day+5);
		  }
		  break;
	  case "Wednesday":
		  if(type==1){
			  date.setDate(day-2);
		  }
		  else{
			  date.setDate(day+4);
		  }
		  break;
	  case "Thursday":
		  if(type==1){
			  date.setDate(day-3);
		  }
		  else{
			  date.setDate(day+3);
		  }
		  
		  break;
	  case "Friday":
		  if(type==1){
			  date.setDate(day - 4);
		  }
		  else{
			  date.setDate(day+2);
		  }
		  break;
	  case "Saturday":
		  if(type==1){
			  date.setDate(day-5);
		  }
		  else{
			  date.setDate(day+1);
		  }
		  break;
	}
	return date;
}


//	 jQuery('body').on('dblclick' , ".jqplot-table-legend-label", function(){
//		 
//		 if(jQuery("#modeInput").val() == "item") {
//			 return; // If it is in item mode, just do nothing.
//			 jQuery("#modeInput").val("application");
//			 jQuery("#tblItem").hide();
//			 jQuery("#tblApp").show();
//		 } else {
//			 jQuery("#modeInput").val("item");
//			 jQuery("#tblItem").show();
//			 jQuery("#tblApp").hide();
//		 }
//		 
//		 jQuery("#appNameInput").val(jQuery(this).text());
//		 var appName = jQuery("#appNameInput").val();
//		 if(appName != null && appName != "") {
//			 debugger;
//			 initItemData(null, appName);
//			 itemMode2 = 1;
//			 drawChart();
//		 }
//			 
//	 });
//	 
//	 if(jQuery("#modeInput").val() == "item") {
//		 jQuery("#tblItem").show();
//		 jQuery("#tblApp").hide();
//	 } else {
//		 jQuery("#tblItem").hide();
//		 jQuery("#tblApp").show();
//	 }
//});
 
 
 /**
  * @Author dung.dd
  * @Date 2015-11-29
  * @param appName
  * @return appId
  */
 function getAppIdFromAppName(appName) {
	 jQuery.ajax({
		url : "getAppId",
		type : "POST",
		async : false,
		data: {"appName": appName},
		success : function(appId) {
			alert(appId);
			return appId;
		}
	});
 }
 
 
 /**
  * @Author Hoangpvm
  * @Date 2015-06-2
  * build Post Param Datatable
  * @param data param data
  * @return void
  */
 function buildPostParamDatatable(data) {
 	var param = {};
 	var colIndex =-1;
 	param.displayStart = data.start;
 	param.pageLength = data.length;

 	if(data.search){
 		param.searchQuery = data.search.value;
 		param.searchRegex = data.regex;
 	}

 	if(data.order){
 		colIndex = data.order[0].column;
 		param.orderColumn = data.columns[colIndex].name;
 		param.orderDirection = data.order[0].dir;
 	}
 	if(jQuery("#appId").length > 0) {
 		var appId=jQuery("#appId").val();
 		param['appId']=appId;
 	}
 	
	return JSON.stringify(param);
 }
 
 
 function buildParamDatatable (data) {
	 	var param = {};
	 	var colIndex =-1;
	 	param.displayStart = data.start;
	 	param.pageLength = data.length;

	 	if(data.search){
	 		param.searchQuery = data.search.value;
	 		param.searchRegex = data.regex;
	 	}

	 	if(data.order){
	 		colIndex = data.order[0].column;
	 		param.orderColumn = data.columns[colIndex].name;
	 		param.orderDirection = data.order[0].dir;
	 	}
	 	
		return param;
	 }
 
 function iniItemTable() {
	 if(oTableItems== null) {
		 oTableItems = jQuery('#tbItem').DataTable({
				dom: '<"top">rt<"bottom"ip><"clear">',
				pagingType: 'simple_numbers',
				language: {
					processing: "<div class='loading-spokes'><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div></div>" //add a loading image,simply putting <img src="loader.gif" /> tag.
				},
				processing: true,
				serverSide: true,
				ordering : true,
				searching: true,
				pageLength: 5,
				order: [
					[2,'asc']
				],
				displayStart: 0,
				responsive: true,
				sort : "position",
				stateSave : false,
				ajax: {
					  headers: {
						'Accept': 'application/json',
						'Content-Type': 'application/json'
					  },
					url: "item-data",
					type: 'POST',
					data: function(data, setting) {
						/*var param = buildPostParamDatatable(data);
						param['searchQuery'] = "";
						param['filter'] = "";
						return param;*/
						
						var param = buildParamDatatable(data);
				    	var statisticType = jQuery('#statisticType').val();
				    	var timeType = jQuery('#timeType').val();
				    	var dateFrom = jQuery('#dateFrom').val();
				    	var dateTo = jQuery('#dateTo').val();
				    	var appId = jQuery("#appId").val();
				    	var itemId = jQuery('#itemId').val();
				    	
				    	param['statisticType'] = statisticType;
				    	param['timeType'] = timeType;
				    	param['dateFrom'] = dateFrom;
				    	param['dateTo'] = dateTo;
				    	param['appIdStr'] = appId;
				    	param['itemIdStr'] = itemId;
				    	
				    	return JSON.stringify(param);
					}
				},
				columns : [
					{
						orderable: false,
						render: function(obj, msg, data, col) {
							var index = col.row + col.settings._iDisplayStart + 1;
							return "<div class='cnt-center text-bold'>" + index +  "</div>";
						},
						width: "5%"
					},
					{
						orderable: false,
						width : "20%",
						data : "itemName"
					},
					{
						orderable: false,
						width : "6%",
						render: function(obj, msg, data, col) {
							return "<div class='cnt-center'>" + data.boughtItems +  "</div>";
						}
					},
					{
						orderable: false,
						width : "6%", 
						render: function(obj, msg, data, col) {
							return "<div class='cnt-center'>" + data.revenue +  "</div>";
						}
					}
				]
		 });
	 } else {
		 oTableItems.ajax.reload();
	 }
 }
 
 function iniAppTable() {
	 if(oTableApps == null) {
		 oTableApps = jQuery('#tbData').DataTable({
				dom: '<"top">rt<"bottom"ip><"clear">',
		        pagingType: 'simple_numbers',
				language: {
					processing: "<div class='loading-spokes'><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div></div>" //add a loading image,simply putting <img src="loader.gif" /> tag.
				},
				processing: true,
		        serverSide: true,
		        ordering : true,
		        searching: true,
		        pageLength: jQuery('#totalItemPerPage').val(),

		         order: [
		    			[1,'asc']
		    		],
		        displayStart: 0,
		        responsive: true,
				sort : "position",
				stateSave : false,
				ajax: {
			          headers: {
			            'Accept': 'application/json',
			            'Content-Type': 'application/json'
			          },
				    url: "application-data",
				    type: 'POST',
				    data: function(data, setting) {
				    	
				    	var param = buildParamDatatable(data);
				    	
				    	var statisticType = jQuery('#statisticType').val();
				    	var timeType = jQuery('#timeType').val();
				    	var dateFrom = jQuery('#dateFrom').val();
				    	var dateTo = jQuery('#dateTo').val();
				    	var appId = jQuery("#appId").val();
				    	
				    	param['statisticType'] = statisticType;
				    	param['timeType'] = timeType;
				    	param['dateFrom'] = dateFrom;
				    	param['dateTo'] = dateTo;
				    	param['appIdStr'] = appId;
				    	
				    	return JSON.stringify(param);
				    }
				},
				columns  : [
					    {
					    	orderable: false,
					    	render: function(obj, msg, data, col) {
								var index = col.row + col.settings._iDisplayStart + 1;
								return "<div class='cnt-center text-bold'>" + index + "</div>";
					    	},
				            "width": "1%"
				        },
						{
							orderable: true,
                      width : "30%",
                      name : "app_name",
							render : function(obj, msg, data, col) {
								return "<a href='../application-detail/detail?idApp="+data.appId+"'>"+data.appName+"</a>";
							}
						},
						{
							orderable: false,
                      width : "6%",
                      data : "type",
                      render : function(obj, msg, data, col) {
								if(data.type == "1") {
									return "Android";
								} else if (data.type == "2") {
									return "IOS";
								}else {
									return "Unknown Type";
								}
							}
						},
						{
							orderable: false,
                      width : "7%",
							render : function(obj, msg, data, col) {
								return "<div class='cnt-center'>" + data.releaseDate + "</div>";
							}
						},
						{
							orderable: false,
                      width : "20%",
                      data : "numberOfInstall",
						},
						{
							orderable: false,
                      width : "20%",
                      data : "revenue",
						}
				]
			});
	 } else {
		 oTableApps.ajax.reload();
	 }
 }
 
 /**
  * 
  * @returns {Date}
  */
 function getEndDate() {
	 var endDate =  new Date();
	 var from = jQuery('#dateFrom').val();
	 
	 var timeType = jQuery('#timeType').val() * 1;
	 var viewType = jQuery('#viewType').val() * 1;
	 
	 if(timeType == 3 || timeType == 4 || timeType == 5) {
		 return new Date(jQuery('#dateTo').val());
	 }
	 
	 var dateFrom = new Date(from);
	 var month = 12;
	 if(timeType == 2) { // month
		 month = dateFrom.getMonth() + 1;
	 }
	 var date = 1;
	 var currentYear = endDate.getFullYear();
	 var year = dateFrom.getFullYear();
	 
	 if((timeType == 1 || timeType == 5 ) && year == currentYear && viewType == 1) {
		 month = endDate.getMonth() + 1;
	 }
	
	endDate.setYear(year);
	endDate.setMonth(month);
	endDate.setDate(0);
	 
	 
	 return endDate;
 }
 
 function getStartDate(endDate) {
	 var timeType = jQuery("#timeType").val()
	 var unit = jQuery(".unitview.active").val();
	 var releaseDate = jQuery("#minAppReleaseDate").val();
	 var rYear = "";
	 var rMonth = "";
	 var rDate = "";
	 if(jQuery("#minAppReleaseDate").val() != "") {
		 releaseDate = new Date(releaseDate);
		 rYear = releaseDate.getFullYear();
		 rMonth= releaseDate.getMonth();
		 rDate = releaseDate.getDate();
	 }
	 var eYear = endDate.getFullYear();
	 var eMonth = endDate.getMonth();
	 var eDate = endDate.getDate();
	 switch (timeType) {
	    case "1":{ // year
	    	if(unit == "4") {
	    		
	    		return new Date(rYear, 0 , 1);
	    		
	    	} if(unit == "3") {
	    		
	    		return new Date(eYear, 0 , 1);
	    		
	    	} else {
	    		
	    		return new Date(eYear, eMonth , 1)
	    	};
	    	break;
	    }  
	    case "2":{ // months
	    	return new Date(eYear, eMonth , 1)
	        break;
	    }  
	    case "3":{ // week
	    	var diff = eDate - endDate.getDay() + (endDate.getDay() == 0 ? -6:1);
	    	return new Date(eYear, eMonth, diff);
	        break;
	    }  
	    case "4":{ // range
	    	var dateFrom = jQuery("#dateFrom").datepicker('getDate');
	    	var timeDiff = Math.abs(endDate.getTime() - dateFrom.getTime());
	    	var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
	    	var diffWeek = Math.ceil(timeDiff / (1000 * 3600 * 24 * 7));
	    	switch (unit) {
		        case '4':
		        	return dateFrom;
		            break;
		        case '3':
		        	if(l > 365) {
		            	return new Date(eYear, eMonth -11, 1);
		            } else {
		            	return dateFrom;
		            }
		            break;
		        case '2':
		        	if(diffWeek > 26) {
		            	return new Date(eYear, eMonth, endDate.getTime() - diffWeek*26 + diffDays);
		            } else {
		            	return dateFrom;
		            }
		            break;
		        default:
		        	if(diffDays > 30) {
		            	return new Date(eYear, eMonth, endDate.getDate()-30);
		            } else {
		            	return dateFrom;
		            }
			        break;
		    }
	        break;
	    }  
	    case "5":{ // all time
	    	if(unit == "4") {
	    		
	    		return new Date(rYear, 0 , 1);
	    		
	    	} if(unit == "3") {
	    		
	    		return new Date(eYear, 0 , 1);
	    		
	    	} else {
	    		
	    		return new Date(eYear, eMonth , 1)
	    	};
	        break;
	    }  
	    default:{
	    	day = "Monday";
	        break;
	    }  
	}
 }
 
 function setHiddenDate() {
	 var endDate = getEndDate();
	 var startDate = getStartDate(endDate);
	 console.log("start: " + startDate);
	 console.log("end: " + endDate);
	 jQuery("#dateFromHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',startDate));
	 jQuery("#dateToHidden").val(jQuery.datepicker.formatDate('yy/mm/dd',endDate));
	 
 }
 function getWeekFromDate( d ) { 

	  // Create a copy of this date object  
	  var target  = new Date(d.valueOf());  
	  
	  // ISO week date weeks start on monday  
	  // so correct the day number  
	  var dayNr   = (d.getDay() + 6) % 7;  

	  // Set the target to the thursday of this week so the  
	  // target date is in the right year  
	  target.setDate(target.getDate() - dayNr + 3);  

	  // ISO 8601 states that week 1 is the week  
	  // with january 4th in it  
	  var jan4    = new Date(target.getFullYear(), 0, 4);  

	  // Number of days between target date and january 4th  
	  var dayDiff = (target - jan4) / 86400000;    

	  // Calculate week number: Week 1 (january 4th) plus the    
	  // number of weeks between target date and january 4th    
	  var weekNr = 1 + Math.ceil(dayDiff / 7);    

	  return weekNr;    

	}
