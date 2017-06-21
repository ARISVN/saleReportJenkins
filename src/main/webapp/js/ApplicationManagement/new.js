var validator;
jQuery(document).ready(function(){
	validator = jQuery('form[name="formSubmit"').validate({
		errorElement: "span",
		errorPlacement: function(error, element) { 
		    error.insertAfter(element);
			element.parent().find('span').addClass('text-red text-noti');
		 },
		errorClass:'text-danger',
		rules : {
			appName : {
				required : true,
				minlength : 5
			},
			packages : {
				required : true,
				minlength : 5
			},
			releaveDate : {
				required : true
			},
			appCode : {
				required : true,
				minlength : 5,
				pattern: /^\w[\.\w]*\.([^.]+)$/
			},
			type : {
				required : true
			}
		},
		messages : {
			appName : {
				required : "Application Name can't blank!",
				minlength : "Min length 5 character!"
			},
			packages : {
				required : "Packages Name or SKU can't blank!",
				minlength : "Min length 5 character!"
			},
			appCode : {
				required : "Application code can't blank!",
				minlength : "Min length 5 character!",
				pattern : "Application code invalid format [/^[a-z]+\.[a-z]{2,4}$/]"
			},
			type : {
				required : "Please Select Type of appliction.",
			}
		}
	});
	
	
	jQuery('.datetimepicker').datepicker({dateFormat: 'yy/mm/dd'});
	jQuery('.datetimepicker').datepicker().datepicker("setDate", new Date());
	jQuery('#appName').focus();
	// reset click
	jQuery('#btnReset').click(function(){
		 jQuery("form[name='formSubmit']")[0].reset();
		 jQuery('.datetimepicker').datepicker().datepicker("setDate", new Date());
		 jQuery('.datetimepicker').datepicker("option", "releaveDate", "yyyy/mm/dd");
		 jQuery('.datetimepicker').datepicker("option", "maxDate",new Date());
		 validator.resetForm();
	});
	jQuery('#submit').click(function(event){
		jQuery("form[name='formSubmit']").submit(function(){
		});
	});
})