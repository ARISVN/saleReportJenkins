var oTable;
var validateForm;
 jQuery( document ).ready(function() {
	 jQuery('.datetimepicker').datepicker().datepicker("setDate", new Date());
	 jQuery('.datetimepicker').datepicker("option", "releaveDate", "dd/mm/yy");
	 jQuery('.datetimepicker').datepicker("option", "maxDate",new Date());
	 validateForm = jQuery('#edit-form').validate({
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
				releaseDate : {
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
	 oTable = jQuery('#application-management-datatable').DataTable({
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
					    url: "data",
					    type: 'POST',
					    data: function(data, setting) {
					    	var param = buildPostParamDatatable(data);
					    	param['searchQuery'] = "";
					    	param['filter'] = "";
					    	return param;
					    }
					},
					columns  : [
							{
								orderable: false,
								width : "1%",
								render : function(obj, msg, data, col) {
									str = "<div class='cnt-center'>"
										+ "<input  type='checkbox' value='"+data.appCode
										+ "@" +data.appId
										+ "@" +data.type
										+ "@" +data.releaseDate + "'";
									if(data.updateStatus == "1") {
										str +=" disabled ";
									}
									str += "/></div>";
									return str
								}

							},
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
	                            width : "6%",
								render : function(obj, msg, data, col) {
									return "<div class='cnt-center'> <a href='../report-statistc?appId="+data.appId+"'>Sale Report</a> </div>";
								}
							},
							{
								orderable: false,
	                            width : "4%",
								render : function(obj, msg, data, col) {
									return "<div class='cnt-center'> <a class='showPopup' rel='"+data.appId+"' href='#editApp'>Edit</a> </div>";
								}
							},
							{
								orderable: false,
	                            width : "5%",
								render : function(obj, msg, data, col) {
									return "<a href='/jenkins/sale-report/application-management/add-item?id="+data.appId+"'>Add Item</a>";
								}
							},
							{
								orderable: false,
	                            width : "10%",
	                            render : function(obj, msg, data, col) {
	                            	
									if(data.updateStatus == "0") {
										return 'Not Get Data Yet';
									} else if (data.updateStatus == "1") {
										return "Getting";
									} else if (data.updateStatus == "2") {
										return data.updateDate;
									}else {
										return "Unknown";
									}
								}
							},
					        {
								orderable: false,
	                            width : "30%",
								data : "appCode",
								visible : false
							},
							{
								orderable: false,
	                            width : "20%",
	                            data : "appId",
								visible : false
							}
					]
				});
				//hide page length menu
	 jQuery(".dataTables_length").hide();
	 
	 jQuery('body').on("click", "#btn-getData", function() {
		 if(jQuery('#application-management-datatable input:checkbox:checked').length == 0) {
			 alert("Please select application to get data!");
			 return;
		 }
		var params = "";
		jQuery.each( jQuery('#application-management-datatable input:checkbox:checked'), function( index, chb ) {
			if(index == 0) {
				params += "" + jQuery(chb).val()
			} else {
				params += "application_" + jQuery(chb).val();
			} 
		});
		jQuery("#params").val(params);
		jQuery("form#getting-data-form").submit();
	 });
	 // Handle click on "Select all" control
	 jQuery('#example-select-all').on('click', function(){
		 // Get all rows with search applied
		 var rows = oTable.rows({ 'search': 'applied' }).nodes();
		 // Check/uncheck checkboxes for all rows in the table
		 jQuery('input[type="checkbox"]:not(:disabled)', rows).prop('checked', this.checked);
	 });
	 jQuery('table tbody').on('click', 'input[type="checkbox"]:not(:disabled)', function(){
		var countcheck = jQuery('table tbody input[type="checkbox"]:checked:not(:disabled)').length;
		var total = jQuery('table tbody input[type="checkbox"]:not(:disabled)').length;
		if (countcheck == total) {
			jQuery("#example-select-all").prop('checked', true);
		}
		if(countcheck != total) {
			jQuery("#example-select-all").prop('checked', false);
		}
	 });
	 
	 if (jQuery().fancybox) {
		 jQuery(".showPopup").fancybox({
	        autoSize  : false,
	        width: 800,
	        height: 'auto',
	        openEffect  : 'none',
	        closeEffect : 'none',
	      });
	    }
	 
	 
	 /* Click event handler */
		$(document).on('click', '#application-management-datatable tbody tr', function (event,row,x,y,z) {
			if(jQuery(event.target).hasClass("showPopup")){
				jQuery("#itemNameErr").hide();
				var aData = oTable.row(row).data();
				jQuery("#appNameInput").val(aData.appName);
				jQuery("#packagesInput").val(aData.appCode);
				jQuery("#appIdInput").val(aData.appId);
				jQuery("#appReleaseDate").val(aData.releaseDate);
				jQuery("input[value='"+aData.type+"']:radio").prop("checked", true);
			}
			
		});
		jQuery('#btnSave').click(function (e) {
			e.preventDefault();
			jQuery("#edit-form").validate();
			var item = jQuery("#appIdInput").val();
			if(jQuery("#edit-form").valid()) {
				if( item!=null && item!=="") {
					jQuery.ajax( {
						url : "update-application",
						data : {
							"appId" : jQuery("#appIdInput").val(),
							"appName": jQuery("#appNameInput").val(),
							"releaseDate" : jQuery("#appReleaseDate").val(),
							"packages" : jQuery("#packagesInput").val(),
							"type":jQuery("input[name='type']:checked").val()
						},
						type : "POST"
					})
						.done( function(msg) {
							jQuery.notify("Save Successful!", "success", {
								autoHide: true,
								autoHideDelay: 5000,
								showDuration: 1000,
								hideDuration: 1000
							});
							jQuery.fancybox.close();
							oTable.ajax.reload();
						})
						.fail(function() {
							jQuery.notify("Save failed!", "error", {
								autoHide: true,
								autoHideDelay: 5000,
								showDuration: 1000,
								hideDuration: 1000
							});
						})
						.always(function() {
							jQuery('#application-detail-datatable').DataTable().ajax.reload();
						});
				}
			}
			
		});

		jQuery('#btnDelete').click(function () {
			var dl = confirm("Do you want to delete this application ?");
			if (dl == true) {
				jQuery.ajax({
					url : "delete-application",
					data : {
						"idApp" : jQuery("#appIdInput").val(),
					},
					type : "POST"
				})
				.done( function(msg) {
					jQuery.notify("Delete Successful!", "success", {
						autoHide: true,
						autoHideDelay: 5000,
						showDuration: 1000,
						hideDuration: 1000
					});
					oTable.ajax.reload();
					jQuery.fancybox.close();
				})
				.fail( function() {
					jQuery.notify("Delete Failed!", "error", {
						autoHide: true,
						autoHideDelay: 5000,
						showDuration: 1000,
						hideDuration: 1000
					});
				})
				.always(function() {
					jQuery(
						'#application-detail-datatable')
						.DataTable().ajax
						.reload();
				});
			}
		});
	 
});


/**
 * @Author Hoangpvm
 * @Date 2015-06-2
 * build Post Param Datatable
 * @param data param data
 * @return void
 */
function buildPostParamDatatable (data) {
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
	return JSON.stringify(param);
}
