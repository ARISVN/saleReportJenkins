( function( $ ) {
  $( document ).ready(function() {
    if ($().fancybox) {
      $(".showPopup").fancybox({
        autoSize  : false,
        width: 800,
        height: 'auto',
        openEffect  : 'none',
        closeEffect : 'none',
      });
    }

    if ($().DataTable) {
//      $('.listData').DataTable({
//        bFilter: false,
//        bInfo: false
//      });

      $('.managementData').DataTable({
        bFilter: false,
        bInfo: false,
        lengthChange: false
      });

      $('.detailData').DataTable({
        bFilter: false,
        bInfo: false,
        lengthChange: false
      });
    }

    /*if ($().multipleSelect) {
      $('#selectedApp').multipleSelect({
            width: '100%',
            selectAll: false,
            placeholder: "Select items"
        });
    }*/

   /* if ($().jqplot) {
      $.jqplot.config.enablePlugins = true;
          var s1 = [2, 6, 7, 10];
          var ticks = ['a', 'b', 'c', 'd'];
          
          plot1 = $.jqplot('chart1', [s1], {
              // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
              animate: !$.jqplot.use_excanvas,
              seriesDefaults:{
                  renderer:$.jqplot.BarRenderer,
                  pointLabels: { show: true }
              },
              axes: {
                  xaxis: {
                      renderer: $.jqplot.CategoryAxisRenderer,
                      ticks: ticks
                  }
              },
              highlighter: { show: false }
          });
      
          $('#chart1').bind('jqplotDataClick', 
              function (ev, seriesIndex, pointIndex, data) {
                  $('#info1').html('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
              }
          );

        $.jqplot('chart2',  [[[1, 2],[3,5.12],[5,13.1],[7,33.6],[9,85.9],[11,219.9]]]);
    }*/
    
    $('#cssmenu > ul > li > a').click(function() {
      $('#cssmenu li').removeClass('active');
      $(this).closest('li').addClass('active');	
      var checkElement = $(this).next();
      if((checkElement.is('ul')) && (checkElement.is(':visible'))) {
        $(this).closest('li').removeClass('active');
        checkElement.slideUp('normal');
      }
      if((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
        $('#cssmenu ul ul:visible').slideUp('normal');
        checkElement.slideDown('normal');
      }
      if($(this).closest('li').find('ul').children().length == 0) {
        return true;
      } else {
        return false;	
      }		
    });

    if ($().fancybox) {
      $(".showPopup").fancybox({
        autoSize  : false,
        width: 800,
        height: 'auto',
        openEffect  : 'none',
        closeEffect : 'none',
      });
    }
//      if ($().loadingBar) {
//          function hideLoading () {
//              loading.hideLoadingBar();
//          }
//          var loading = null;
//          $('#btn-getData').click(function(event) {
//              /* Act on the event */
//              loading = $('body').loadingBar();
//              loading.showLoadingBar();
//              setTimeout(hideLoading, 2000);
//          });
//      }
    // $(document).bind('click', function (e) {
    //     var $clicked = $(e.target);
    //     if (!$clicked.parents().hasClass("mutliSelect")) $(".mutliSelect #checkboxes").hide();
    // });
  });
} )( jQuery );
