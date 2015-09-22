
function setupTypeahead(e, objectives){
	$wnd.$(e).typeahead({
		hint: true,
		highlight: true,
		minLength: 1
	},
	{
		name: "Learning Objectives",
		source: objectives
	});
}


function slowShowDiv(divId){
	$('#' + divId).show("slow");
} 

function hideDiv(divId){
	$('#' + divId).hide();
}

function toggleDivSlow(divId) {
	$('#' + divId).toggle( "slow" );
}

function initListSort(listId) {
	var el = document.getElementById(listId);
    var sortable = Sortable.create(el);
}

function resetAddCollectionItemModal() {
	document.getElementById('addCollectionItemForm').reset();
	document.getElementById('addCollectionItemExists').style.display='none';
	document.getElementById('addCollectionItemSubmitButtons').style.display='inline';	
}

/* Contribution Edit metadata: Switch text to editable input form */
$("#editContentMetadataContainer .meta-value.editable, #editContentDescriptionContainer .meta-value.editable").off('click').live('click', function() {	
	var $textObj = $(this);
	var $inputObj = $(this).next();
	var $objValue = $(this).text();
	if ($.trim($objValue)=="Click to edit") {
		$objValue=null;
	}
	$inputObj.val($objValue);
	$textObj.fadeOut(50, function() {
	$inputObj.fadeIn(50);
		$inputObj.focus();
		$inputObj.select();
	});
	return false;
});

/* Edit Metadata Screen: Switch input form back to text */
$("#editContentMetadataContainer .ecmSection .value-input, #editContentDescriptionContainer .value-input").off('blur').live('blur', function() {
	var $textObj = $(this).prev();
	var $inputObj = $(this);
	var $objValue = $(this).val();
	if ($.trim($objValue)=="") {
		$objValue = "Click to edit";
	}	
	$textObj.html($objValue);
	$inputObj.fadeOut(50, function() {
		$textObj.fadeIn(50);
	});
});

/* Object Details Screen: Show more details */
$("#editContentMetadataHide").off('click').live('click', function() {
	$(this).slideUp('fast');
	$('#editContentMetadataShow').slideDown('fast');
	return false;
});

/* Object Details Screen: Show more details */
$("#editContentMetadataContainer .ecmSection a.ecmSectionHeader").off('click').live('click', function() {
	var $section = $(this).parent();
	var $content = $(this).next();
	
	if (!(typeof isIE=="undefined")) {
		if ($section.hasClass('collapsed')) {
			$section.removeClass('collapsed');
			$content.show();
		} else {
			$section.addClass('collapsed');
			$content.hide();
		}
	} else {
		if ($section.hasClass('collapsed')) {
			$section.removeClass('collapsed');
			$content.slideDown('fast');
		} else {
			$section.addClass('collapsed');
			$content.slideUp('fast');
		}
	}
	return false;
});

/***********************************Learning objective stuff***********************************/

//DECALS isn't currently using the display-objective-list and project-objective-list but I am going to leave it in here for now

/* Objective globals */
var EDIT_SCREEN = "edit";
var DETAIL_SCREEN = "detail";
var PROJECT_SCREEN = "project";
var editingObjectiveIndex = "-1";

/* Objective string format: <objID1><objectiveDescDelimiter><objDescription1><objectiveDelimiter><objID2><objectiveDescDelimiter><objDescription2><objectiveDelimiter> */
var objectiveDescDelimiter = "<DESC>";
var objectiveDelimiter = "<OBJ>";

/* Object Editor / Render existing objectives */
function listObjectives(nodeObjectives, targetDiv) {
	var objList, obj, i; 
	if (targetDiv == "display-objective-list") {
		var screen = EDIT_SCREEN;
	}
	else if (targetDiv == "editContentObjectivesList") {
		var screen = DETAIL_SCREEN;
	}
	else if (targetDiv == "project-objective-list") {
		var screen = PROJECT_SCREEN;
	}
	else alert("Unknown objective list target.");
	
	editingObjectiveIndex = "-1";
	document.getElementById(targetDiv).innerHTML="";
	var objBlock = "";
	var target = $('#'+targetDiv);
	var place = target.children().length;
	if ((nodeObjectives != null) && (nodeObjectives != "Click to edit")) {
		objList = nodeObjectives.split(objectiveDelimiter);
		for (i=0; i<objList.length ; i++) {
			obj = objList[i].split(objectiveDescDelimiter);
			if ((obj[0]!="")&&(obj[0]!="Click to edit")) {
				if (objList.length > 1) {
					objBlock = createObjectiveElement(false, place.toString(), obj[0], obj[1], screen);
				} else
					objBlock = createObjectiveElement(false, place.toString(), obj[0], "", screen);
					
				if (place > 0) {
					target.children().last().after(objBlock);
				} else {
					target.append(objBlock);
				}	
				place++;
			}
		}
	}
}

function compressObjectives(elementID) {  
	var tempStr, tempSplit, objTitle, objDesc, i; 
	var nodeObjectives = "";
	var objects = $("#"+elementID+" .delete");
	for (i=0 ; i<objects.length ; i++) {
		tempStr = objects[i].id;
		tempSplit = tempStr.split("-");
		objTitle = "#objTitleInput-"+tempSplit[1];
		objDesc = "#objDescrInput-"+tempSplit[1];
		if ($(objTitle).val() != "")
			nodeObjectives = nodeObjectives+$(objTitle).val()+objectiveDescDelimiter+$(objDesc).val()+objectiveDelimiter;
	}
	return nodeObjectives;
}

function createObjectiveElement(editable, indexStr, objectiveTitle, objectiveDesc, screen) {
	var objBlock, textVisibility, inputVisibility;
	if (screen == EDIT_SCREEN) {
		var textStyle = "objective meta-value full-width editable";
	} else if (screen == DETAIL_SCREEN) {
		var textStyle = "objective meta-value editable";		
	} else if (screen == PROJECT_SCREEN) {
		var textStyle = "objective meta-value full-width editable";		
	}
	
	if (editable) { // show inputs
		textVisibility = 'style="display:none;"';
		inputVisibility = '';
	} else { // show text
		textVisibility = '';
		inputVisibility = 'style="display:none;"';
	}
	if (!objectiveTitle) {
		objectiveTitle = "Click to edit";
	}
	if (editingObjectiveIndex*1 >= 0) {
	    indexStr = editingObjectiveIndex ;
	}

	objBlock = '<li>';
	objBlock = objBlock + '<a id="objDelete-' + indexStr +'" href="#" title="Remove" class="delete"></a>'; // Delete button
	objBlock = objBlock + '<p id="objText-' + indexStr +'" '+textVisibility+' class="'+textStyle+'" title="'+ objectiveTitle +' - '+ objectiveDesc +'">'+objectiveTitle+'</p>';
	objBlock = objBlock + '<input id="objTitleInput-' + indexStr +'" '+inputVisibility+' type="text" value="'+ objectiveTitle +'">';
	objBlock = objBlock + '<input id="objDescrInput-' + indexStr +'" '+inputVisibility+' type="text" value="'+ objectiveDesc +'">';
	objBlock = objBlock + '</li>';
	
	return objBlock;
}

function toggleCreateObjectiveForm(action, screen) {
	if (screen == EDIT_SCREEN) {
		var targetDiv = "#createObjectiveWrapper";
		var targetTitle = "#newObjectiveTitle";
		var targetList = "#display-objective-list";
	} 
	else if (screen == DETAIL_SCREEN) {
		var targetDiv = "#editContentCreateObjectiveWrapper";
		var targetTitle = "#editContentNewObjectiveTitle";		
		var targetList = "#editContentObjectivesList";
	}
	else if (screen == PROJECT_SCREEN) {
		var targetDiv = "#projectCreateObjectiveWrapper";
		var targetTitle = "#projectNewObjectiveTitle";		
		var targetList = "#project-objective-list";
	}
	else alert("Unknown screen type for objective form.");

	switch(action) {
		case 'open': 
			resetCreateObjectiveForm(screen);
			$(targetDiv).slideDown('fast', function() {
				$(targetTitle).focus();
			});
			break;
		case 'apply':
			if (validateCreateObjectiveForm(screen)) {
				applyCreateObjectiveForm(screen);
				$(targetDiv).slideUp('fast', function() {});
				editingObjectiveIndex = "-1";
				resetCreateObjectiveForm(screen);
			}
			break;
		case 'cancel':
			if (editingObjectiveIndex*1 >= 0) {
			    var objTitle = targetList+" #objTitleInput-"+editingObjectiveIndex;
			    var objDesc = targetList+" #objDescrInput-"+editingObjectiveIndex;
			    var objBlockNew = createObjectiveElement(false, editingObjectiveIndex, $(objTitle).val(), $(objDesc).val(), screen);
			    $(objTitle).closest('li').replaceWith(objBlockNew);
			    editingObjectiveIndex = "-1";
			}
			$(targetDiv).slideUp('fast', function() {});
			resetCreateObjectiveForm(screen);
			break;
	}
}

function validateCreateObjectiveForm(screen) {
	var alertText = "";
	if (screen == EDIT_SCREEN) {
		var newTitle = $('#newObjectiveTitle').val();
		var newDesc = $('#newObjectiveDesc').val();		
	} 
	else if (screen == DETAIL_SCREEN) {
		var newTitle = $('#editContentNewObjectiveTitle').val();
		var newDesc = $('#editContentNewObjectiveDesc').val();
	}
	else if (screen == PROJECT_SCREEN) {
		var newTitle = $('#projectNewObjectiveTitle').val();
		var newDesc = $('#projectNewObjectiveDesc').val();
	}
	
	if(newTitle=="") {
		alertText = "Your objective is missing a title.";
	} 
	else if(newDesc=="") {
		alertText = "The description must be filled in.";
	} 
	else if((newTitle.indexOf(objectiveDelimiter) >= 0) || (newTitle.indexOf(objectiveDescDelimiter) >= 0)) {
		alertText = "Illegal use of delimiter in title.";
	}
	else if((newDesc.indexOf(objectiveDelimiter) >= 0) || (newDesc.indexOf(objectiveDescDelimiter) >= 0)) {
		alertText = "Illegal use of delimiter in description.";
	}
	if (!alertText=="") {
		alert(alertText);
		return false;
	} 
	else {
		return true;
	}
}

function applyCreateObjectiveForm(screen) {
	if (screen == EDIT_SCREEN) {
		var objTitle = $('#newObjectiveTitle').val();
		var objDesc = $('#newObjectiveDesc').val();
		var targetDiv = "#display-objective-list";		
		$('#r-editSave').removeClass('white');
		$('#r-editSave').addClass('blue');
		$('#r-save-alert').removeClass('hide');
	} 
	else if (screen == DETAIL_SCREEN) {
		var objTitle = $('#editContentNewObjectiveTitle').val();
		var objDesc = $('#editContentNewObjectiveDesc').val();
		var targetDiv = "#editContentObjectivesList";		
		$('#r-detailEditUpdate').removeClass('white');
		$('#r-detailEditUpdate').addClass('blue');
		$('#r-detailSaveAlert').removeClass('hide');
	}
	else if (screen == PROJECT_SCREEN) {
		var objTitle = $('#projectNewObjectiveTitle').val();
		var objDesc = $('#projectNewObjectiveDesc').val();
		var targetDiv = "#project-objective-list";		
	}
	
	var objBlock = "";
	if (editingObjectiveIndex*1 >= 0) {
		objBlock = createObjectiveElement(false, editingObjectiveIndex, objTitle, objDesc, screen);
		var editText = targetDiv+" #objText-"+editingObjectiveIndex;
		$(editText).closest("li").replaceWith(objBlock);
		editingObjectiveIndex = "-1";		
	} else {
		var target = $(targetDiv);
		objBlock = createObjectiveElement(false, target.children().length, objTitle, objDesc, screen);
		if (target.children().length > 0) {
			target.children().last().after(objBlock);
		} else {
			target.append(objBlock);
		}		
	}
}

function resetCreateObjectiveForm(screen) {
	if (screen == EDIT_SCREEN) {
		$('#newObjectiveTitle').val('');
		$('#newObjectiveDesc').val('');
	} 
	else if (screen == DETAIL_SCREEN) {
		$('#editContentNewObjectiveTitle').val('');
		$('#editContentNewObjectiveDesc').val('');
	}
	else if (screen == PROJECT_SCREEN) {
		$('#projectNewObjectiveTitle').val('');
		$('#projectNewObjectiveDesc').val('');
	}
}

/* ---- Learning Objectives Handlers (Object Details Screen) */
/* Object Details Screen /  Edit objective  */
$('#editContentObjectivesList .objective').unbind("click").live('click', function() {
	var objectiveBlock = $(this).closest('li');
	var obj = $(this).closest('p');
	var objID = obj[0].id;
	if (editingObjectiveIndex*1 >= 0) {
		toggleCreateObjectiveForm('cancel', DETAIL_SCREEN);
	}
	temp = objID.split("-");
    editingObjectiveIndex = temp[1];
    var objText = "#editContentObjectivesList #objText-"+editingObjectiveIndex ;
	var objTitle = "#editContentObjectivesList #objTitleInput-"+editingObjectiveIndex ;
	var objDesc = "#editContentObjectivesList #objDescrInput-"+editingObjectiveIndex ;
	toggleCreateObjectiveForm('open', DETAIL_SCREEN);
	$('#editContentNewObjectiveTitle').val($(objTitle).val());
    $('#editContentNewObjectiveDesc').val($(objDesc).val());
    objectiveBlock.slideUp('fast', function() {});
	return false;
});

/* Object Details Screen /  Delete objective  */
$('#editContentObjectivesList .delete').unbind('click').live('click', function() {
	var objectiveBlock = $(this).closest('li');
    objectiveBlock.slideUp('fast', function() {
		var count = $("#r-metadataToolbar .section #editContentObjectivesList li").length;
		$(this).closest('li').remove();
	});
	$('#r-detailEditUpdate').removeClass('white');
	$('#r-detailEditUpdate').addClass('blue');
	$('#r-detailSaveAlert').removeClass('hide');
	return false;
});

/* Object Details Screen /  Add existing objective (create new empty input) */
$('#editContentDisplayObjectives #detailAddObjective').unbind('click').live('click', function() {
	alert("'Link to existing objective' feature is not implemented.");
	return false;
});

/* Object Details Screen /  Create new objective, show inputs */
$('#editContentDisplayObjectives #editContentCreateObjective').unbind('click').live('click', function() {
	if (editingObjectiveIndex*1 >= 0) {
		toggleCreateObjectiveForm('cancel', DETAIL_SCREEN);
	}
	toggleCreateObjectiveForm('open', DETAIL_SCREEN);
	return false;
});

/* Object Details Screen /  Apply created objective */
$('#editContentDisplayObjectives #editContentCreateObjectiveApply').unbind('click').live('click', function() {
	toggleCreateObjectiveForm('apply', DETAIL_SCREEN);
	return false;
});

/* Object Details Screen /  Cancel create new objective, hide and clear inputs */
$('#editContentDisplayObjectives #editContentCreateObjectiveCancel').unbind('click').live('click', function() {
	toggleCreateObjectiveForm('cancel', DETAIL_SCREEN);
	return false;
});

/* Object Details Screen /  Show HELP for creating an objective */
$('#editContentDisplayObjectives #editContentCreateObjectiveHelp').unbind('click').live('click', function() {
	var help = "How To Write an Objective \n\n";
	help = help + "Learning objectives are brief descriptions of specific things a learner completing the training will know or be able to do. They should be succinctly expressed using clear action verbs. It is best to think in terms of knowledge and skills that can be directly observed and measured.  Remember, learners should not be asked to read or review material that is not relevant to one of the objectives. Nor should they be assessed on skills or knowledge which is not specifically outlined as important in one or more of the objectives.\n\n";
	help = help + "For each learning objective, you must assign a brief title and a full description.  The full description should contain the condition, behavioral verb, and criteria for meeting the learning objective.";
	alert(help);
	return false;
});
$('#editMetadataCreateObjectiveWrapper #editMetadataCreateObjectiveHelp').unbind('click').live('click', function() {
	var help = "How To Write an Objective \n\n";
	help = help + "Learning objectives are brief descriptions of specific things a learner completing the training will know or be able to do. They should be succinctly expressed using clear action verbs. It is best to think in terms of knowledge and skills that can be directly observed and measured.  Remember, learners should not be asked to read or review material that is not relevant to one of the objectives. Nor should they be assessed on skills or knowledge which is not specifically outlined as important in one or more of the objectives.\n\n";
	help = help + "For each learning objective, you must assign a brief title and a full description.  The full description should contain the condition, behavioral verb, and criteria for meeting the learning objective.";
	alert(help);
	return false;
});



/***************End From decals-ui.js********************/

function boxedCustomAppJavascript() {       
	
	Foundation.libs.abide.settings.patterns.password = /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/;
	
	Foundation.libs.abide.settings.patterns.url = /^(https?|ftp|file|ssh):\/\/([-;:&=\+\$,\w]+@{1})?([-A-Za-z0-9\.]+)+:?(\d+)?((\/[-\+~%\/\.\w]+)?\??([-\+=&;%@\.\w]+)?#?([\w]+)?)?/;
		
    $(document).foundation();
    
    $('#modalRegister').bind('close', function() {
    	$('#registrationErrorContainer').hide();
   	});
    
    $('#modalLogin').bind('close', function() {
    	$('#loginErrorContainer').hide();
   	});
    
    //FROM decals-ui.js
    // Show more details when link is clicked
    $('.infoBox').on('click','.link',function() {
        $(this).toggleClass('collapsed expanded');
        $(this).siblings('.moreInfo').slideToggle();
    });

    // tabs in header toggle search screens
    $('.tabs').on('click','.tab',function() {
        $(this).siblings('.tab').removeClass('active');
        $(this).addClass('active');
    });
    
}


/*************** Competency Stuff ********************/
