function resetButton(){
	if(jQuery(".agendaWeek-button").hasClass("active")){
  		jQuery(".agendaWeek-button").removeClass("active");
  	}
  	if(jQuery(".month-button").hasClass("active")){
  		jQuery(".month-button").removeClass("active");
  	}
  	if(!jQuery(".agendaDay-button").hasClass("active")){
  		jQuery(".agendaDay-button").addClass("active");
  	}
  	if(jQuery(".year-button").hasClass("active")){
  		jQuery(".year-button").removeClass("active");
  	}	  		
}
jQuery(document).ready(function(){
	jQuery('.datetimepicker').datepicker().datepicker("setDate", new Date());
	jQuery('.datetimepicker').datepicker("option", "dateFormat", "yy/mm/dd");
	jQuery('.datetimepicker').datepicker("option", "maxDate",new Date());
	jQuery( "#timeType" ).change(function() {
	  switch (jQuery(this).val())
		{
		  case "1":// Year	
			jQuery('#dateFrom').datepicker("destroy");
			jQuery('.datetimepicker').datepicker().datepicker("setDate", new Date());
			jQuery("#dateTo").datepicker("enable");
			jQuery('#dateFrom').datepicker("enable");
		  	jQuery("#dateTo").closest('.two-block').removeClass('mg-left');  	 
		  	jQuery("#dateTo").addClass("bl-hidden");
		 	jQuery('#dateTo').hide();
			jQuery('#date2 .text-noti').css('display','none');
		  	jQuery( ".datetimepicker" ).datepicker("option", { 
		  		changeMonth : true, 
		  		changeYear : true, 
		  		dateFormat : "yy",
		  		onClose: function() {
		  		var iMonth = jQuery("#ui-datepicker-div .ui-datepicker-month :selected").val();
		        var iYear = jQuery("#ui-datepicker-div .ui-datepicker-year :selected").val();
		  		jQuery('#dateFrom').datepicker('setDate',  new Date(iYear,iMonth,0));
		     	},
		     	beforeShow : function(input,obj){
		     	   jQuery(this).datepicker( "option", "defaultDate",new Date(this.value) );
		     	   jQuery(this).datepicker('setDate', new Date(this.value));
		     	  }
		  	})
		  	
		  	jQuery(".agendaWeek-button").hide();
		  	jQuery(".month-button").show();
		  	jQuery(".agendaDay-button").show();
		  	jQuery(".year-button").show();
		  	
		  	resetButton();
		  	
		  	
		  break;
		  
		  case "2":	// Month
			  	jQuery('#dateFrom').datepicker("destroy");
				jQuery('.datetimepicker').datepicker().datepicker("setDate", new Date());
			  	jQuery("#dateTo").datepicker("enable");
			  	jQuery('#dateFrom').datepicker("enable");
			  	jQuery("#dateTo").closest('.two-block').removeClass('mg-left');	
			  	jQuery("#dateTo").addClass("bl-hidden");
			  	jQuery('#dateTo').hide();
			  	jQuery('#date2 .text-noti').css('display','none');
			  	var date = jQuery( ".datetimepicker" ).datepicker("option", { 
			  		changeMonth : true, 
			  		changeYear : true, 
			  		dateFormat : "yy/mm",
			  		onClose: function() {
		  			var iMonth = jQuery("#ui-datepicker-div .ui-datepicker-month :selected").val();
			        var iYear = jQuery("#ui-datepicker-div .ui-datepicker-year :selected").val();
			        jQuery(this).datepicker('setDate', new Date(iYear/1, iMonth/1, 1));
			     	},
			     	beforeShow : function(input,obj){
				     	   jQuery(this).datepicker( "option", "defaultDate",new Date(this.value) );
				     	   jQuery(this).datepicker('setDate', new Date(this.value));
				     	  }
			  	})
			  	
			  	
			  	jQuery(".agendaWeek-button").hide();
			  	jQuery(".month-button").hide();
			  	jQuery(".agendaDay-button").show();
			  	jQuery(".year-button").hide();
			  	resetButton();
			  	jQuery("#dateFrom").datepicker("setDate", new Date());
			  break;
		  
		  case "3": // Week
			  jQuery('#dateFrom').datepicker("destroy");
			  jQuery('.datetimepicker').datepicker().datepicker("setDate", new Date());
			  jQuery("#dateTo").datepicker("enable");
			  jQuery('#dateFrom').datepicker("enable");
			  jQuery("#dateTo").closest('.two-block').removeClass('mg-left');
			  //jQuery("#dateTo").addClass("bl-hidden");
			  jQuery('#date2 .text-noti').css('display','none');
			  jQuery( ".datetimepicker" ).datepicker( "option", {dateFormat : "yy/mm/dd"}).focus(function () {
				  jQuery(".ui-datepicker-calendar").show();
				  jQuery(".ui-datepicker-month").show();
			  });	  
			  jQuery('#dateFrom').datepicker("option", "showWeek",true);
			  jQuery('#dateFrom').datepicker("option", "firstDay", 1);
			  
			  jQuery(".agendaWeek-button").hide();
			  jQuery(".month-button").hide();
			  jQuery(".agendaDay-button").show();
			  jQuery(".year-button").hide();
			  
			  var dateFrom;
	     	   var dateTo;
	     	   dateFrom=new Date();
	     	   dateTo = new Date();
	           dateFrom=nextOrPrevDay(dateFrom,1);
	   		   dateTo=nextOrPrevDay(dateTo,2);
	           jQuery("#dateFrom").datepicker("setDate", dateFrom);
	           var dateS=jQuery.datepicker.formatDate('yy/mm/dd',dateTo);
	           jQuery('#dateTo').val(dateS);
	           jQuery('#dateTo').show();
	           jQuery('#dateTo').datepicker("disable");
	           var maxDay=nextOrPrevDay(dateTo,2);
//	           maxDay.setDate(maxDay.getDate()+1);
	           jQuery('.datetimepicker').datepicker("option", "maxDate",maxDay);
			  // Highlight week on hover week number
			  //jQuery(document).on("mouseenter",".ui-datepicker-week-col",
				//  function(){jQuery(this).siblings().find("a").addClass('ui-state-hover');} );
			  //jQuery(document).on("mouseleave",".ui-datepicker-week-col",
				//  function(){jQuery(this).siblings().find("a").removeClass('ui-state-hover');} );
              //
			  //// Select week on click on week number
			  //jQuery(document).on("click",".ui-datepicker-week-col",
				//  function(){
				//	  $first = jQuery(this).siblings().find("a").first();
				//	  $last = jQuery(this).siblings().find("a").last();
				//	  $first.click();
				//	  $parentFirst = $first.parent();
				//	  $parentLast = $last.parent();
				//	  jQuery("#dateFrom").val(
				//		  (Number($parentFirst.data("month"))+1)+"/"+$first.text()+"/"+$parentFirst.data("year")
				//		  + " - " +
				//		  (Number($parentLast.data("month"))+1)+"/"+$last.text()+"/"+$parentLast.data("year")
				//	  );
				//  });
	           
			  resetButton();

			  break;
		  case "4": // range
			  jQuery('#dateFrom').datepicker("destroy");
			  jQuery('.datetimepicker').datepicker().datepicker("setDate", new Date());
			  jQuery("#dateTo").datepicker("enable");
			  jQuery('#dateFrom').datepicker("enable");
			  jQuery("#dateTo").closest('.two-block').addClass('mg-left');
			  jQuery("#dateTo").removeClass("bl-hidden");
			  if(!jQuery('#dateTo').val()) {
				  jQuery('#date2 .text-noti').css('display','block');
			  } else {
				  jQuery('#date2 .text-noti').css('display','none');
			  }
			  var date = jQuery( ".datetimepicker" ).datepicker("option", {
			  		dateFormat : "yy/mm/dd",
			  		onClose: function() {}
			  }).focus(function () {
			        jQuery(".ui-datepicker-calendar").show();
			        jQuery(".ui-datepicker-month").show();
			    });	
			  jQuery('#dateFrom').datepicker("option", "showWeek",false);
			  jQuery('#dateFrom').datepicker("option", "firstDay", 0);
				 
			  jQuery(".agendaWeek-button").show();
			  jQuery(".month-button").show();
			  jQuery(".agendaDay-button").show();
			  jQuery(".year-button").show();
			  
			  jQuery('.datetimepicker').datepicker().datepicker("setDate", new Date());
			  resetButton();
			  jQuery('.datetimepicker').datepicker("option", "maxDate",new Date());
			  break;
			 
		  default://All time
			jQuery("#dateTo").datepicker("disable");
		    jQuery('#dateFrom').datepicker("disable");
			jQuery("#dateTo").closest('.two-block').addClass('mg-left');
		  	jQuery("#dateTo").removeClass("bl-hidden");
			if(!jQuery('#dateTo').val()) {
				jQuery('#date2 .text-noti').css('display','block');
			} else {
				jQuery('#date2 .text-noti').css('display','none');
			}
		  	var date = jQuery( ".datetimepicker" ).datepicker("option", {
		  		dateFormat : "yy/mm/dd",
		  		onClose: function() {}
		  	}).focus(function () {
		        jQuery(".ui-datepicker-calendar").show();
		        jQuery(".ui-datepicker-month").show();
		    });	
		  	 jQuery('#dateFrom').datepicker("option", "showWeek",false);
			 jQuery('#dateFrom').datepicker("option", "firstDay", 0);
			 
			 jQuery(".agendaWeek-button").hide();
			 jQuery(".month-button").show();
			 jQuery(".agendaDay-button").show();
			 jQuery(".year-button").show();
			 
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
	        	
	            dateFrom.setDate(1);
	        	var month=dateTo.getMonth() + 1;
	        	switch (month) {
	        		 case 1,3,5,7,8,10,12:
	        			 dateTo.setDate(31);
	        			 break;
	        		 case 2:
	        			 dateTo.setMonth(3);
	        			 dateTo.setDate(31);
	        			 break;
	        		 case 4,6,9,11:
	        			 dateTo.setDate(30);
	        			 break;
	        		
	        	}
	           jQuery("#dateFrom").datepicker("setDate", dateFrom);
	           var dateS=jQuery.datepicker.formatDate('yy/mm/dd',dateTo);
	           jQuery('#dateTo').val(dateS)
			 resetButton();
		}  
	});
});

