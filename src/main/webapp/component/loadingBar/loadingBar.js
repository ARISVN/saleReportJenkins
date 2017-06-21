/*!
 * jQuery lightweight plugin boilerplate
 * Original author: @hanh
 * Licensed under the MIT license
 */

// the semi-colon before the function invocation is a safety
// net against concatenated scripts and/or other plugins
// that are not closed properly.
;
(function($, window, document, undefined) {

    // undefined is used here as the undefined global
    // variable in ECMAScript 3 and is mutable (i.e. it can
    // be changed by someone else). undefined isn't really
    // being passed in so we can ensure that its value is
    // truly undefined. In ES5, undefined can no longer be
    // modified.

    // window and document are passed through as local
    // variables rather than as globals, because this (slightly)
    // quickens the resolution process and can be more
    // efficiently minified (especially when both are
    // regularly referenced in your plugin).

    // Create the defaults once
    var pluginName = "loadingBar",
        defaults = {                   
          loadingClass: 'loading-bar',          // Class added to target while loading
          overlayClass: 'loading-overplay',  // Class added to overlay (style with CSS)
          spinnerClass: 'loading-spinner',  // Class added to loading overlay spinner
          iconClass: 'loading-icon',        // Class added to loading overlay spinner
          textClass: 'loading-text',        // Class added to loading overlay spinner
          loadingText: 'loading'            // Text within loading overlay
        };

    var overlay, elm;

    // The actual plugin constructor
    function Plugin(element, options) {
        var me = this;
        me.element = element;

        // jQuery has an extend method that merges the
        // contents of two or more objects, storing the
        // result in the first object. The first object
        // is generally empty because we don't want to alter
        // the default options for future instances of the plugin
        me.options = $.extend({}, defaults, options);

        me._defaults = defaults;
        me._name = pluginName;

        me.init();
    }

    Plugin.prototype = {
        init: function() {
            // Place initialization logic here
            // You already have access to the DOM element and
            // the options via the instance, e.g. this.element
            // and this.options
            // you can add more functions like the one below and
            // call them like so: this.yourOtherFunction(this.element, this.options).
            // console.log(this.options.remove);
            this.addElement(this.element, this.options);
        },
        addElement: function(el, options) {
          // some logic
            var me = this;
            elm = el;
          if (el.tagName != 'BODY') {
            $(el).css({"position":"relative"});
          }
          else {
            $(el).css({"overflow":"hidden"});
          }
          overlay = '<div class="' + me.options.loadingClass + '">' +
            '<div class="' + me.options.overlayClass + '">' +
            '<div class="' + me.options.spinnerClass + '">' +
            '<div class="' + me.options.iconClass + '"></div>' +
            '<span class="' + me.options.textClass + '">' + me.options.loadingText + '</span>' +
            '</div></div></div>';
        }
    };    

    // A really lightweight plugin wrapper around the constructor,
    // preventing against multiple instantiations
    $.fn[pluginName] = function(options) {
        return this.each(function() {
            if (!$.data(this, "plugin_" + pluginName)) {
                $.data(this, "plugin_" + pluginName,
                        new Plugin(this, options));
            }            
        });
    };
    $.fn.hideLoadingBar = function(options) {
      return this.each(function() {
        if($(this).find('.loading-bar')) {
          $(this).find('.loading-bar').remove();
            if (elm.tagName == 'BODY') {
                $('body').css({"overflow":"auto"});
            }
        }
      });
    };

    $.fn.showLoadingBar = function(options){
      if(overlay){
        $(this).append($(overlay));
          if (elm.tagName != 'BODY') {
              $(elm).find('.loading-bar').css({"position":"absolute"});
              $(elm).find('.loading-overplay').css({"position":"absolute"});
          }
      }
    };
})(jQuery, window, document);