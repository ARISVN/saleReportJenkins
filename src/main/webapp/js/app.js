/**
 * Created by User on 11/19/2015.
 */
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

    if(data.order && data.order.length >0){
        colIndex = data.order[0].column;
        param.orderColumn = data.columns[colIndex].data;
        param.orderDirection = data.order[0].dir;
    }
    return param;
}
