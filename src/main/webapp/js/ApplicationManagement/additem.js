var validateForm;
jQuery( document ).ready(function() {
	jQuery(".text-noti").hide();
	//check os depending on appName
	var type = jQuery("#type_os").val();
	checkOs(type);
	
	var id = location.search.substring(4);
	if(id != ""){
		jQuery("#appName").val(id);
	}else{
		jQuery("#appName").val("");
		jQuery("#type_ios").prop("checked", true)
	}
	
	// disable type div
	jQuery("#div_type *").attr("disabled", "disabled").off('click');
	// appName change
	jQuery("#appName").change(function() {
		var type_os = jQuery('option:selected', this).attr('data');
		checkOs(type_os);
	});
	

	validateForm = jQuery('#add-item').validate({
		errorElement: "span",
		errorPlacement: function(error, element) { 
		    error.insertAfter(element);
			element.parent().find('span').addClass('text-red text-noti');
		 },
		errorClass:'text-danger',
		rules : {
			appName : {
				required : true
			},
			itemName : {
				required : true,
				minlength : 5
			},
			packages : {
				required : true,
				minlength : 5
			},
			type : {
				required : true
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
				required : "Application Name can't be blank!",
				minlength : "Min length 5 character!"
			},
			itemName : {
				required : "Item Name can't be blank!",
				minlength : "Min length 5 character!"
			},
			packages : {
				required : "Packages Name or SKU can't be blank!",
				minlength : "Min length 5 character!"
			},
			appCode : {
				required : "Application code can't be blank!",
				minlength : "Min length 5 character!",
				pattern : "Application code invalid format [/^[a-z]+\.[a-z]{2,4}$/]"
			},
			type : {
				required : "Please Select Type of appliction.",
			}
		}
	});
	
	// reSet button click
	jQuery("#btn_reset").click(function(){
		reset();
	});
	
});
function reset(){
	jQuery(".text-noti").hide();
	var id = location.search.substring(4);
	if(id != ""){
		jQuery("#appName").val(id);
	}else{
		jQuery("#appName").val("");
		jQuery("#type_ios").prop("checked", true)
	}
	var type = jQuery("#type_os").val();
	checkOs(type);
	jQuery("#packages").val("");
	jQuery("#itemName").val("");
	validateForm.resetForm();

}

function checkOs(v){
	if(v == "1"){
		jQuery("#type_android").prop("checked", true)	
	}
	else{
		jQuery("#type_ios").prop("checked", true)
	}
}