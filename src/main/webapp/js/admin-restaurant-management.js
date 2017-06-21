var removeOwnerValidator;
var confirmOwnerValidator;
function initGetListUserRequestOwner (data) {
	$.ajax({
		url: 'getListResquestOwner.html',
        type: 'POST',
        dataType : "json",
        data: {restaurantId: data.restaurantId},
        
        success: function(data, textStatus, xhr) {
        	$('#cb-requestOwner option').remove();
        	
			$.each(data.listUser, function(val, text) {
				$('#cb-requestOwner').append(
			        $('<option></option>').val(text.idUser).html(text.userName)
			    );
			});
			$('#cb-requestOwner :nth-child(0)').prop('selected', true);
			var requestOwner = data.requestOwner;
			if(requestOwner) {
				$('#requestEmail').val(requestOwner.email);
				$('#requestText').val(requestOwner.requestContent);
			}
			
			
        }
	})
}

function initDataByChangeUserRequestOption (userId, resId) {
	$.ajax({
		url: 'getResquestOwner.html',
        type: 'POST',
        dataType : "json",
        data: {restaurantId: resId, userId : userId},
        
        success: function(data, textStatus, xhr) {
			var requestOwner = data.requestOwner;
			$('#requestEmail').val(requestOwner.email);
			$('#requestText').val(requestOwner.requestContent);
        },
	})
}

var clonePopUp = null;
function showPopup (title, msg, type) {
	if (type == "updated") {
		bootbox.dialog({
			message: msg,
			title: function(){
				return '<p>'+'<i class="fa fa-info-circle" aria-hidden="true" style="margin-right: 8px;">'+'</i>'+ title +'</p>';
			},
			buttons: {
				success: {
					label: "OK",
					className: "btn-success",
					callback: function () {
					}
				}
			}
		});
	}
	else if (type == "added") {
		bootbox.dialog({
			message: msg,
			title: function(){
				return '<p>'+'<i class="fa fa-info-circle" aria-hidden="true" style="margin-right: 8px;">'+'</i>'+ title +'</p>';
			},
			buttons: {
				success: {
					label: "OK",
					className: "btn-success",
					callback: function () {
					}
				}
			}
		});
	}
    else if (type == "failed") {
       bootbox.dialog({
	        message: msg,
	        title: function(){
	        	return '<p>'+'<i class="fa fa-exclamation-triangle" aria-hidden="true" style="margin-right: 8px;">'+'</i>'+ title+'</p>';
	        },
	        buttons: {
	            success: {
	                label: "OK",
	                className: "btn-success",
	                callback: function () {
	                	// debugger;
	                	// this;
	                }
	            }
	        }
	    });
    }
}


//function show confirm owner popup
function showConfirm (data, title) {
	//bien doi gia tri form
	$("#restaurant-id").val(data.restaurantId);
	$("#restaurant-name").val(data.name);
	initGetListUserRequestOwner(data);

	bootbox.dialog ({
		title: function(){
			return '<p>'+'<i class="fa fa-info-circle" aria-hidden="true" style="margin-right: 8px;">'+'</i>'+ title +'</p>';
		},
		message: $('#inputBox'),
		//show : false,
		buttons: {
			reset: {
				label: "Reset",
				className: "btn-default",
				callback: function () {
					$('#requestForm')[0].reset();
					$('#requestForm')
					$("#restaurant-id").val(data.restaurantId);
					$("#restaurant-name").val(data.name);
					confirmOwnerValidator.resetForm();
					initGetListUserRequestOwner(data);
					return false;
				}
			},
			reject: {
				label: "Reject Owner",
				className: "btn-warning",
				callback: function (a) {
					if($('#requestForm').valid()) {
						var restId = $("#restaurant-id").val();
						var userId = $("#cb-requestOwner").val();
						var answer = $("#requestAnswer").val();
						showLoading();
						$.ajax({
							url: 'rejectResquestOwner.html',
					        type: 'POST',
					        dataType : "json",
					        data: {
						        	restaurantId: restId,
						        	userId: userId,
						        	answer: answer,
					        	},
					        
					        success: function(data, textStatus, xhr) {
					        	$('#request-owner-datatable').DataTable().draw();
					        }
						}).success(function() {
							showPopup(crud.information, "Reject Owner Success!", crud.added);
						}).fail(function () {
							showPopup(crud.information, "Reject Owner Failure!", crud.error);
						})
						.always(function() {
						    hideLoading();
						});
					} else {
						return false;
					}
				}
			},
			accept: {
				label: "Accept Owner",
				className: "btn-success",
				callback: function (a) {
					if($('#requestForm').valid()) {
						var restId = $("#restaurant-id").val();
						var userId = $("#cb-requestOwner").val();
						var answer = $("#requestAnswer").val();
						showLoading();
						$.ajax({
							url: 'acceptResquestOwner.html',
					        type: 'POST',
							async: true,
					        dataType : "json",
					        data: {
						        	restaurantId: restId,
						        	userId: userId,
						        	answer: answer,
					        	},
					        
					        success: function(data, textStatus, xhr) {
					        	$('#request-owner-datatable').DataTable().draw();
					        }
						}).success(function() {
							showPopup(crud.information, "Accept Owner Success!", crud.added);
						}).fail(function () {
							showPopup(crud.information, "Accept Owner Failure!", crud.error);
						})
						.always(function() {
						    hideLoading();
						});
					} else {
						return false;
					}
				}
			}
		}
	}).on('hide.bs.modal', function(e) {
		confirmOwnerValidator.resetForm();
		clonePopUp = $('#inputBox').clone();//.appendTo('#requestDialog');
	}).on('hidden.bs.modal', function(e) {
		confirmOwnerValidator.resetForm();
		clonePopUp.appendTo('#requestDialog');
	});
}

//function show delete popup
function showDelete (data ,title) {
	bootbox.dialog ({
		title: function(){
			return '<p>'+'<i class="fa fa-info-circle" aria-hidden="true" style="margin-right: 8px;">'+'</i>'+ title +'</p>';
		},
		message: $('#popupDelete'),
		buttons: {
			reset: {
				label: "Reset",
				className: "btn-default",
				callback: function () {
					$('#remove-owner-form')[0].reset();
					removeOwnerValidator.resetForm();
					return false;
				}
			},
			remove: {
				label: "Remove Owner",
				className: "btn-warning",
				callback: function () {
					if($('#remove-owner-form').valid()) {
						showLoading();
						$.ajax({
							url: 'removeOwner.html',
					        type: 'POST',
					        dataType: 'json',
					        data:{
					        	restaurantId: $(data).attr('resId'),
					        	userId: $(data).attr('userId'),
					        	answer: $("#remove-owner-content").val()
					        },
					        success: function(rs, textStatus, xhr) {
					        	if(rs.msg) {
					        		$('#request-owner-datatable').DataTable().draw();
					        	}
								showPopup(crud.information, "Remove Owner Success!", crud.added);
					        }
						}).fail(function () {
							showPopup(crud.information, "Remove Owner Failure!", crud.error);
						}).always (function() {
							hideLoading();
						});
					} else {
						return false;
					}
					
				}
			}
		}
	}).on('hide.bs.modal', function(e) {
		removeOwnerValidator.resetForm();
		clonePopUp = $('#popupDelete').clone();
	}).on('hidden.bs.modal', function(e) {
		removeOwnerValidator.resetForm();
		clonePopUp.appendTo('#deleteDialog');
	});

}

var oTable;
showLoading();
$(document).ready(function() {
	// validate signup form on keyup and submit
	removeOwnerValidator = $('#remove-owner-form').validate(
		{
			errorElement: "span",
			errorPlacement: function(error, element) { 
			    error.insertAfter(element);
			 },
			errorClass:'text-danger',
			rules : {
				content : {
					required : true,
				}
			},
			messages: {
				content: {
					required: "Content is required!"
				}
			}
		});
	confirmOwnerValidator = $('#requestForm').validate(
			{
				errorElement: "span",
				errorPlacement: function(error, element) { 
				    error.insertAfter(element);
				 },
				errorClass:'text-danger',
				rules : {
					answer : {
						required : true,
					}
				},
				messages: {
					answer: {
						required: "Answer is required."
					}
				}
			});
		oTable = $('#request-owner-datatable').DataTable(
			{
				"dom": '<"top">rt<"bottom"ip><"clear">',
		        "pagingType": 'simple_numbers',
				"language": {
					"processing": "<div class='loading-spokes'><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div></div>" //add a loading image,simply putting <img src="loader.gif" /> tag.
				},
				"processing": true,
		        "serverSide": true,
		        "ordering" : true,
		        "searching": true,
		        "order" :[
		            [1,"asc"]
		        ],
		        "pageLength": 10,
		        "displayStart": 0,
		        "responsive": true,
				"sort" : "position",
				
				"bStateSave" : false,
				// Default: Page display length
				"ajax": {
			          headers: {
			            'Accept': 'application/json',
			            'Content-Type': 'application/json'
			          },
				    url: "data.html",
				    'type': 'POST',
				    "data": function(data, setting) {
				    	var param = buildPostParamDatatable(data);
				    	param['searchQuery'] = $('#txtSearchResName').val();
				    	param['filter'] = $('#slSearchRes').val();
				    	return JSON.stringify(param);
				    }
				},
				"columns" : [
					    {
					    	orderable: false,
					    	render: function(obj, msg, data, col) {
					        var index = col.row + col.settings._iDisplayStart + 1;
					        return index;
//					    		return data.restaurantId;
					    	},
				            "width": "5%",
				        },
				        {
							orderable: true,
                            width : "30%",
							"mData" : "appCode",
							render : function(obj, msg, data, col) {
								return "<a href='"+$("#pageContext").val()+"/user/restaurant_detail/index.html?id="+data.restaurantId+"'>"+data.name+"</a>";
							}
						},
						{
							orderable: false,
                            width : "20%",
							"mData" : "appName",
						},
						{
							orderable: true,
                            width : "10%",
							"mData" : "price",
						},
						{
							orderable: false,
                            width : "20%",
							"mData" : "ownerUserName"
						},
						{
							orderable: false,
                            width : "9%",
							render : function(obj, msg, data, col) {
								var str = "";
								
								if(data.owner == 1) {
									str = "<div style='text-align: center'>" + "<input class='btn btn-danger btn-xs' userId='"+data.userId+"' resId='"+data.restaurantId+"' type='button' value='Remove Owner' rel='"
											+ data.ownerUserName
											+ "'></div>";
								} else if (data.owner == 2){
									str = "<div style='text-align: center'>" + "<input class='btn btn-danger btn-xs' type='button' resId='"+data.restaurantId+"' value='Confirm Owner' rel='"
									+ data.ownerUserName
									+ "' ></div>";
								}
								return str;
							}
						},
						{
							orderable: false,
                            width : "6%",
							render : function(obj, msg, data, col) {
								return "<div style='text-align: center'>" 
										+ "<input class='btnRemove btn btn-danger btn-xs' type='button' res='"+data.name+"' value='Delete' rel="
										+ data.restaurantId
										+ "></div>";
							}
						}

				]
			});

			//hide page length menu
			$(".dataTables_length").hide();
					
	$('#txtSearchResName').keypress(function(event){
		 
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if(keycode == '13'){
			$('#request-owner-datatable').DataTable().draw();
		}
	 
	});
	$('body').on('change', "#slSearchRes", function (event) {
		  $('#request-owner-datatable').DataTable().draw();
	});
	
	$('body').on( 'click', '.btn-xs[value="Confirm Owner"]', function () {
		var currentRow = $(this).parents('tr');
	    var dataRow = oTable.row(currentRow).data();
		showConfirm(dataRow, "Confirm Owner");
		return false;
	})
	$('#contact-form').submit(function(event) {


		/* Act on the event */
		
	});

	// popup deletebuttonDelete
	$('table').on('click', '.btn-xs[value="Remove Owner"]', function(){
		var _this = this;
		showDelete ( _this ,"Delete Confirm");
	})
	
	$('body').on('click','.btnRemove',function(event) {
		var me = $(this);
		bootbox.confirm("Do you want to delete restarant: "+ $(this).attr('res')+ "?",
		function(result) {
			if (result) {
				showLoading();
				$.ajax({
					url: 'delete.html',
			        type: 'POST',
			        dataType: 'json',
			        data:{
			        	restaurantId: $(me).attr("rel")
			        },
			        success: function(data, textStatus, xhr) {
			  		  $('#request-owner-datatable').DataTable().draw();
			        },
			}).success(function() {
						showPopup("Information", "Deleted Restaurant Successfully!", "updated");
					})
				.fail(function() {
						showPopup("Error", "Deleted Restaurant Fail!", "failed");
					})
				.always(function() {
					$('#user-management-datatable').DataTable().ajax.reload();
					hideLoading();
				});
			} else {
				event.preventDefault();
			}
		});
	});
	
	$('body').on('change', "#cb-requestOwner", function (event) {
		var userId = $(this).val();
		var resId = $('#restaurant-id').val();
		initDataByChangeUserRequestOption(userId, resId);
	});
	hideLoading();
});
