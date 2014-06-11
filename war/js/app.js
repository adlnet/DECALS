var searchResults={};
var searchResultsI=0;
var count = 0;
var t = null;

function ac() {
    clearTimeout(t);
    t=setTimeout(function(){acx(++count);},2000);
}

function acx(counter) {
    if (count != counter)
        return;
    var xhr = new XMLHttpRequest();
    var searchText = $('#search')[0].value;
    xhr.open('GET', "http://metaglance.service.eduworks.com/semIdxAc?text="+searchText, true);

    $('.TESuggestMini').hide();
    xhr.onreadystatechange = function(e) {
      if (this.readyState == 4 && this.status == 200) {
        if (count != counter)
            return;
        var binStr = this.response;
        var o = JSON.parse(binStr);
        $('#TESuggest').html("Try another search term.");
        $('.TESuggest').html("");
        if (o != null)
        {
            for (var word in o)
            {
                var wordObj = o[word];
                for (var i in wordObj)
                {
                    var suggestion = wordObj[i];
                    searchResults[++searchResultsI] = suggestion;
                    if (suggestion.type == "Word")
                    {
                        $('#TESuggest').append("<a class='TELink finger' json='"+ searchResultsI +"'>Contains the word: <strong>"+suggestion.word+"</strong></a><br>");
                        $('#TESuggest1').append("<a class='TELink finger' json='"+ searchResultsI +"'><strong>"+suggestion.word+"</strong></a><br>");
                        $('#TESuggest').show();
                        $('#TESuggest1').show();
                        $('#TESuggest1x').show();
                    }
                    else if (suggestion.type == "Dictionary")
                    {
                        $('#TESuggest').append("<a class='TELink finger' json='"+ searchResultsI +"'>Search by meaning: <strong>"+suggestion.definition+"</strong></a><br>");
                        $('#TESuggest2').append("<a class='TELink finger' json='"+ searchResultsI +"'><strong>"+suggestion.definition+"</strong></a><br>");
                        $('#TESuggest').show();
                        $('#TESuggest2').show();
                        $('#TESuggest2x').show();
                    }
                    else if (suggestion.type == "Keyword")
                    {
                        $('#TESuggest').append("<a class='TELink finger' json='"+ searchResultsI +"'>Search by keyword: <strong>"+suggestion.word+"</strong></a><br>");
                        $('#TESuggest3').append("<a class='TELink finger' json='"+searchResultsI +"'><strong>"+suggestion.word+"</strong></a><br>");
                        $('#TESuggest').show();
                        $('#TESuggest3').show();
                        $('#TESuggest3x').show();
                    }
                    else if (suggestion.type == "Topic")
                    {
                        $('#TESuggest').append("<a class='TELink finger' json='"+ searchResultsI +"'>Described by topic: <strong>"+suggestion.word+"</strong></a><br>");
                        $('#TESuggest4').append("<a class='TELink finger' json='"+ searchResultsI +"'><strong>"+suggestion.word+"</strong></a><br>");
                        $('#TESuggest').show();
                        $('#TESuggest4').show();
                        $('#TESuggest4x').show();
                    }
                    else if (suggestion.type == "Example")
                    {
                        $('#TESuggest').append("<a class='TELink finger' json='"+ searchResultsI +"'>Similar to document: <strong>"+suggestion.word+"</strong></a><br>");
                        $('#TESuggest5').append("<a class='TELink finger' json='"+ searchResultsI +"'><strong>"+suggestion.word+"</strong></a><br>");
                        $('#TESuggest').show();
                        $('#TESuggest5').show();
                        $('#TESuggest5x').show();
                    }
                    else if (suggestion.type == "Sentence")
                    {
                        $('#TESuggest').append("<a class='TELink finger' json='"+ searchResultsI +"'>Contains the phrase: <strong>"+suggestion.word+"</strong></a><br>");
                        $('#TESuggest6').append("<a class='TELink finger' json='"+ searchResultsI +"'><strong>"+suggestion.word+"</strong></a><br>");
                        $('#TESuggest').show();
                        $('#TESuggest6').show();
                        $('#TESuggest6x').show();
                    }
                }
                if (wordObj.length > 50)
                {
                    $('.TESuggestMain').hide();
                }
                else
                {
                    $('.TESuggestMini').hide();
                    $('.TESuggestMain').show();
                }
            }
        }
        else
        {
        }
      }
    };
    
    xhr.send();
}





function search() {
    var formData = new FormData();
    var searchObj=[];
    $('.TESelectedLink').each(function(){
        var r = searchResults[this.getAttribute("json")];
        r.index=this.getAttribute("json");
        searchObj.push(r);
    });
        
    formData.append("search", JSON.stringify(searchObj));

    var xhr = new XMLHttpRequest();
    xhr.open('POST', "http://metaglance.service.eduworks.com/semIdxSearch", true);

    xhr.onreadystatechange = function(e) {
      if (this.readyState == 4 && this.status == 200) {
        var binStr = this.response;
        console.log(binStr);
          var o = JSON.parse(binStr);
          $('#resultRecords').html("");
        for (var source in o)
        {
            for (var k in o[source])
            {
                if (!isInteger(k))
                {
                    for (var j in o[source][k])
                        addSearchResult(o[source][k][j],source);
                }
                else
                    addSearchResult(o[source][k],source);
            }
        }
        var list = $('#resultRecords');
        var listItems = list.find('li').sort(function(a,b){ return $(b).attr('data-count') - $(a).attr('data-count'); });
        list.find('li').remove();
        list.append(listItems);
      }
    };
    
    xhr.send(formData);
}

function addSearchResult(result,source) {
    var url = result.word;
    var source = JSON.parse(source);
    // if ($(".TESearchResult[href='"+url+"']").length == 0)
        // $('#TESearchResultsBox').append(
            // "<li class='tooltipx TESearchResult' data-count='0' href='"+url+"'><a href='"+url+"'>"+url+"</a><span class='classic'></span></li>");
    //$(".TESearchResult[href='"+url+"'] .classic").append(typeIndex[source.type]+": "+source.word+"<br>");
    // $(".TESearchResult[href='"+url+"']").attr("data-count",parseInt($(".TESearchResult[href='"+url+"']").attr("data-count"))+1);
    if ($(".TESearchResult[href='"+url+"']").length == 0)
        $('.resultRecords').append($(
                '<div class="row result">\
                    <div class="preview medium-2 hide-for-small-only columns">\
                        <a target="_blank" href="http://example.com"><img src="images/thumbnail-search-result.jpg">\
                        <div class="rollover">\
                            <p><strong>Key Words:</strong> N/A</p>\
                            <p><strong>Context:</strong> N/A</p>\
                        </div></a>\
                    </div>\
                    <div id="resultRowContainer">\
                        <div class="medium-7 small-8 columns">\
                            <h5><a class="title TESearchResult" target="_blank" href="' + url + '">Resource</a> <span class="tools"><a class="button tiny secondary" title="Download this document"><i class="fa fa-download"></i></a> <a title="Find similar" class="button tiny secondary"><i class="fa fa-search"></i> Similar</a></span></h5>\
                            <p class="link">' + url + '</p>\
                            <p class="description">N/A</p>\
                            <p class="source"><strong>Source:</strong> <em>N/A</em></p>\
                        </div>\
                        <div class="paradata medium-3 small-4 columns">\
                            <p>Reading Level: N/A</p>\
                            <p>Technical Level: N/A</p>\
                            <p>Bloom\'s Taxonomy: N/A</p>\
                            <p class="reviews"><span class="stars"><span class="rating" style="width:70%;"></span></span> 0 reviews</p>\
                            <p class="views">0 views</p>\
                        </div>\
                    </div>\
                </div>'));
}

var typeIndex={
    "Word":"Contains the word",
    "Dictionary":"Search by meaning",
    "Keyword":"Search by keyword",
    "Topic":"Described by topic",
    "Example":"Similar to document",
    "Sentence":"Contains the phrase"
};

//function init()
//{
//    $(".TEContainer").delegate(".TESelectedLink","click",function(){
//        $(this).remove();
//    });
//    $(".TEContainer").delegate(".TELink","click",function(){
//        var srI=this.getAttribute("json");
//        var suggestion = searchResults[srI];    
//       
//        $('#TESelected').append("<a class='TESelectedLink' json='"+srI+"'>"+typeIndex[suggestion.type]+": <strong>"+(suggestion.word?suggestion.word:"")+(suggestion.definition?": "+suggestion.definition:"")+"</strong><br></a>");
//        
//        search();
//    });
//}

var reInteger = /^\d+$/;

function isInteger (s)
{    
    return reInteger.test(s)
}

var entityMap = {
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': '&quot;',
    "'": '&#39;',
    "/": '&#x2F;'
};

function escapeHtml(string) {
    return String(string).replace(/[&<>"'\/]/g, function (s) {
        return entityMap[s];
    });
}

/***************From decals-ui.js********************/
function hideAllScreens() 
{
    $('.screen').hide();
}
function showScreen(name) 
{
    hideAllScreens();
    $('body').removeClass().addClass(name);
    $('.screen.'+name).show();
}
function navigate() 
{
    // show screen content
    // hide other screens
}

//Swap out default header with search box version
function showHeaderSearch() 
{
    $('#defaultHeader').hide();
    $('#searchHeader').show();
    $('.TEContainer').show();
    $('#resultRecords').show();
}
function hideHeaderSearch() 
{
    $('#defaultHeader').show();
    $('.TEContainer').hide();
    $('#searchHeader').hide();
    $('#resultRecords').hide();
}

/* TEMPORARY LOGIN for DEMO */
function login() 
{
    window.location = "student.html";
    
    var user = $('#modalLogin input#email').val();
    var role = "student";
    $('#modalLogin').foundation('reveal', 'close');
    if (user.toLowerCase().indexOf("teacher") > -1) role = "teacher";
    
    // TEMP: NEED TO FIX THIS
    
//    $('.username').text(user);
//    $('.show-on-login').show();
//    $('.hide-on-login').hide();
    showDashboard(role);
}

function showDashboard(role)
{
    // TEMP: NEED TO FIX THIS
    window.location = role + ".html";
    
//    showScreen(role);
//    showHeaderSearch();
}

/***************End From decals-ui.js********************/

function boxedCustomAppJavascript() {       
    $(document).foundation();
    showScreen('activeSearch');
    $(".TESelectedContainer").delegate(".TESelectedLink","click",function(){
        $(this).remove();
    });
    $(".TEContainer").delegate(".TELink","click",function(){
        var srI=this.getAttribute("json");
        var suggestion = searchResults[srI];    
       
        $('#TESelected').append("<a href='#' class='TESelectedLink' style=\"background-color:white\" json='"+srI+"'>"+typeIndex[suggestion.type]+": <strong>"+(suggestion.word?suggestion.word:"")+(suggestion.definition?": "+suggestion.definition:"")+"</strong><br></a>");
        
        search();
    });
    
//    $(document).on('close','[data-reveal]', function () {
//        //$modalRegister.bind('reveal:close', function () {
//        	  //console.log('Modal closed');
//        	window.alert("closing...");
//        });
    
    $('#modalRegister').bind('close', function() {
    	$('#registrationErrorContainer').hide();
   	});
    
    $('#modalLogin').bind('close', function() {
    	$('#loginErrorContainer').hide();
   	});
    
    // Show or hide filter context results
    $('.TEContainer .TESuggestMini').on('click','p',function() {
        $(this).parent().toggleClass('full');
    });
    
    //FROM decals-ui.js
    // Show more details when link is clicked
    $('.info-box').on('click','.link',function() {
        $(this).toggleClass('collapsed expanded');
        $(this).siblings('.more-info').slideToggle();
    });

    $('#searchTypes').on('click','li#interactivTab, li#basicTab', function() {
        var tab = $(this);
        if (tab.hasClass("active")) {
            // do nothing
        } else {
            hideHeaderSearch();
            if (tab.is("#interactivTab")) {
                showScreen('activeSearch');
                $('input#search-home').focus();
            } else {
                showScreen('basicSearch'); 
                $('input#search-basic').focus();
            }
        }
    });

    // tabs in header toggle search screens
    $('.tabs').on('click','.tab',function() {
        $(this).siblings('.tab').removeClass('active');
        $(this).addClass('active');
    });

    /* SEARCH */

    // check search input for typing
    $('input#search, input#search-home').each(function () {
        var elem = $(this);
        // Save current value of element
        elem.data('oldVal', elem.val());
        // Look for changes in the value
        elem.bind("propertychange keyup input paste", function (event) {
            // transfer typing from home search to header search
            if (elem.attr("id") == "search-home") {
                showHeaderSearch();
                $('input#search').val(elem.val());
                $('input#search').focus();
            }
            // If value has changed...
            if (elem.data('oldVal') != elem.val()) {
                // Updated stored value
                elem.data('oldVal', elem.val());
                
                // Take action while typing
                if (elem.val().length > 0) {
                    showScreen('typing');
                }
            }
        });
    });
    // Show interactive search results
    $('input#search').change(function() {
        showScreen('results');
        $(this).blur();
        showHeaderSearch();
        $('input#search-home, input#search-basic').val($(this).val());
        $('input#search').on("keydown", function (e) { ac(); });
    });

    // Show basic scearch results
    $('input#search-basic').change(function() {
    	var query = $(this).val();
        query=query.replace(" ","%20");
        window.open('http://free.ed.gov/?page_id=6&query='+query+'&type=index')
    });

    
}
