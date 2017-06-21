var oTable;
jQuery( document ).ready(function() {
	oTable = jQuery('#application-detail-datatable').DataTable({
		dom: '<"top">rt<"bottom"ip><"clear">',
		pagingType: 'simple_numbers',
		language: {
			processing: "<div class='loading-spokes'><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div><div class='spoke-container'><div class='spoke'></div></div></div>" //add a loading image,simply putting <img src="loader.gif" /> tag.
		},
		processing: true,
		serverSide: true,
		ordering : true,
		searching: true,
		pageLength: 10,
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
		columns : [
			{
				orderable: false,
				render: function(obj, msg, data, col) {
					var index = col.row + col.settings._iDisplayStart + 1;
					return "<div class='cnt-center text-bold'>" + index +  "</div>";
				},
				class : "stt",
				width: "0.5%"
			},
			
			{
				orderable: false,
				width : "30%",
				data : "itemName"
			},
			
			{
				orderable: true,
				width : "20%",
				name : "item_code",
				data : "itemCode"
			},
			
			{
				orderable: false,
				width : "5%",
				render: function(obj, msg, data, col) {
					return "<div class='cnt-center'>" + data.boughtItems +  "</div>";
				}
			},
			{
				orderable: false,
				width : "5%",
				render: function(obj, msg, data, col) {
					return "<div class='cnt-center'>" + data.revenue +  "</div>";
				}
			},
			{
				orderable: false,
				width : "5%",
				render : function(obj, msg, data, col) {
					return '<div class="cnt-center"><a href="#infoPopup" class="showPopup">Update</a></div>';
				}
			}
		]
	});

	/* Click event handler */
	$(document).on('click', '#application-detail-datatable tbody tr', function (event,row,x,y,z) {
		//remove validator
		jQuery("#itemNameErr").hide();
		var aData = oTable.row(row).data();
		jQuery("#appNameInput").val(aData.itemName);
		jQuery(".detail").text(aData.itemCode);
		jQuery("#idItem").text(aData.id);
		jQuery("#itemNameHide").text(aData.itemName);
	});
	jQuery('#btnSave').click(function () {
		var item=jQuery("#appNameInput").val();
		if( item!=null&&item!=="") {
			jQuery.ajax( {
				url : "updateItem.json",
				data : {
					"idItem" : jQuery("#idItem").text(),
					"itemName": jQuery("#appNameInput").val(),
					"itemCode":jQuery(".detail").text()
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
		} else {
			jQuery("#itemNameErr").show();
		}
	});

	jQuery('#btnDelete').click(function () {
		var dl = confirm("Do you want to delete this item?");
		if (dl == true) {
			jQuery.ajax({
				url: "deleteItem",
				data: {
					"idItem": jQuery("#idItem").text()
				},
				type: "POST"
			})
				.done(function (msg) {
					jQuery.notify("Delete Successful!", "success", {
						autoHide: true,
						autoHideDelay: 5000,
						showDuration: 1000,
						hideDuration: 1000
					});
					jQuery.fancybox.close();
				})
				.fail(function () {
					jQuery.notify("Delete Failed!", "error", {
						autoHide: true,
						autoHideDelay: 5000,
						showDuration: 1000,
						hideDuration: 1000
					});
				})
				.always(function () {
					jQuery(
						'#application-detail-datatable')
						.DataTable().ajax
						.reload();
				});
		}
	});

	jQuery('#btnReset').click(function () {
		jQuery("#appNameInput").val(jQuery("#itemNameHide").text());
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
	var appId=jQuery(".appIdHide").text();
	param['appId']=appId;
	return JSON.stringify(param);
}
