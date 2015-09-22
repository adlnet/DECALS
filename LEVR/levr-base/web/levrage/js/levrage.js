var baseUrl = "";
var security = "";

function start()
{
    baseUrl = new String(window.location);
    baseUrl = baseUrl.replace("levrage","api/custom");
    $('#securityModal').foundation('reveal', 'open');
}

function getResolvers()
{
    $.getJSON(baseUrl+"levrage/resolvers?security="+security,function(data){
        $('#resolverList').html("");
        for (var key in data)
        {
            if (key.indexOf("c") == 0 && data[key.substring(1)] != undefined && JSON.stringify(data[key.substring(1)]) == JSON.stringify(data[key]))
                continue;
            
            $('#resolverList').append('<div class="functionalSpec">'+constructResolver(key,data[key])+'</div>');
        }
        refresh();
        events();
    });
}

function getFiles()
{
    $.getJSON(baseUrl+"levrage/files?security="+security,function(data){
        $('#servlets').html("");
        for (var key in data)
        {
            var input = data[key];
            parsex = "";
            pfileDeclare(key,input);
            parse(input);
            $('#servlets').append(parsex);
            console.info("Parsed " + key);
        }
        refresh();
        events();
    });
}

function saveFile(filename,data)
{
    var formData = new FormData();
    formData.append(filename, data);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', baseUrl+"levrage/save?security="+security+"&filename="+filename, true);
    
    xhr.onreadystatechange = function(e) {
      if (this.readyState == 4 && this.status == 200) {
        var binStr = this.response;
             
          window.alert("Saved file.");
      }
      else if (this.status == 500) {
          window.alert("Failed to save file.");
      }
    };
    
    xhr.send(formData);  // multipart/form-data
}

function indent(parenStack,parens)
{
    var j = parens;
    for (var i in parenStack)
        j += parenStack[i];
    var s = "";
    while (j-- > 0)
        s += "\t";
    return s;
}
function toRs2(file)
{
    var stack = [];
    var curr = file.next();
    var last = curr;
    var string = "";
    var parenStack = [];
    var parens = 0;
    while (!last.hasClass("file") && curr != null)
    {
        if (curr.prev().children(".variableGroupName").length > 0)
            string += "\n}";
        if (curr.prev().hasClass("rs2function"))
            string += ";\n";
        if (curr.prev().hasClass("webservice"))
            string += ";\n\n";
        if (curr.hasClass("variableName"))
            string += curr.text();
        if (curr.hasClass("function") && string.charAt(string.length-1) != '.' && curr.siblings(".variableReference").length > 0)
            string += ".";
        if (curr.hasClass("variableReferenceText"))
            string += curr.text();
        if (curr.hasClass("parameter") && (curr.prev().hasClass("parameter") || curr.prev().hasClass("variableGroupReference")))
            string += ",";
        if (curr.hasClass("parameterContainer") && curr.parent().hasClass("webservice"))
            string += curr.text();
        if (curr.hasClass("variableGroupReferenceText"))
            string += curr.text();
        if (curr.hasClass("parameterName") && curr.parent().siblings(".parameter").length > 0)
            string += "\n"+indent(parenStack,parens);
        if (curr.hasClass("parameterName"))
            string += curr.text()+"=";
        if (curr.hasClass("variableGroupName"))
            string += curr.text()+"={";
        if (curr.hasClass("literal"))
            string += "\""+curr.text()+"\"";
        if (curr.hasClass("variableName") && curr.parent().hasClass("rs2function"))
            string += " = ";
        if (curr.hasClass("functionName"))
        {
            if (curr.parent().prev(".dot").attr("id") == curr.parent().parent().children(".dot").first().attr("id") && !curr.parent().prev(".dot").prev().hasClass("variableReference"))
                string += "#";
            string += curr.text()+"(";
            parens++;
        }
        if (curr.hasClass("dot") && string.charAt(string.length-1) != '.' && curr.parent().children(".dot").first().attr("id") != curr.attr("id") && curr.parent().children(".dot").last().attr("id") != curr.attr("id"))
            string += ".";
            
        last = curr;
        if (curr.children().length > 0)
        {
            stack.push(curr);
            parenStack.push(parens);
            parens = 0;
            curr = curr.children().first();
        }
        else if (curr.next().length == 0)
        {
            if (stack.length > 0)
            {
                while (curr.next().length == 0 && stack.length > 0)
                {
                    while (parens-- > 0)
                    {
                        if (parens != -1 && curr.siblings(".parameter").length > 0)
                            string += "\n"+indent(parenStack,parens);
                        string += ")";                        
                    }
                    parens = parenStack.pop();
                    curr = stack.pop();
                }
                curr = curr.next();
            }
            else
                curr = null;
        }
        else
            curr = curr.next();
    }
    string = string.trim();
    if (string.charAt(string.length-1) != ';')
        string += ";";
    return string;
}

function parse(input)
{
    try{
        var cstream = new org.antlr.runtime.ANTLRStringStream(input);
        var lexer = new resolverv2jsLexer(cstream);
        var tstream = new org.antlr.runtime.CommonTokenStream(lexer);
        var parser = new resolverv2jsParser(tstream);
        
        var foo = parser.parse();
    }
    catch(err)
    {
        console.error("Could not parse " + input + ": " + err);
    }
    return parsex;
}

var parsex = "";

function pfileDeclare(fileName,input)
{
    parsex += fileDeclare(fileName,input);
}
function fileDeclare(fileName,input)
{
    return '<div class="label fileSave">Upload</div><div class="label file deselected">'+fileName+'</div>';
}
function pservletDeclare(servletName,functionName)
{
    parsex += servletDeclare(servletName,functionName);
}
function servletDeclare(servletName,functionName)
{
    return '<span class="removeWebService">-</span><div class="webservice"><div class="label parameterName" contenteditable="true">'+servletName+'</div><div class="parameterContainer">'+functionName+'</div></div>';
}
function pvariableDeclare(functionName)
{
    parsex += variableDeclare(functionName);
}
function variableDeclare(functionName)
{
    return '<span class="addWebService">+</span> <span class="addVariable">+</span> <span class="removeVariable">x</span><div class="label secondary rs2function"><span class="variableName" contenteditable="true">'+functionName+'</span>';
}
function pvariableGroupDeclare(functionName)
{
    parsex += variableGroupDeclare(functionName);
}
function variableGroupDeclare(functionName)
{
    return '<div class="label secondary rs2function"><span class="variableGroupName">'+functionName+'</span>';
}
function pvariableDeclareEnd()
{
    parsex += variableDeclareEnd();
}
function variableDeclareEnd()
{
    return '</div>';
}
function pparameterBegin(parameterName)
{
    parsex += parameterBegin(parameterName);
}
function parameterBegin(parameterName)
{
    var extraClass="";
    if (parameterName.indexOf("?") == 0)
    {
        parameterName = parameterName.substring(1);
        extraClass = " optional";
    }
    return '<div class="parameter'+extraClass+'"><span class="removeParameter">x</span><div class="parameterName" contenteditable="true">'+parameterName+'</div><div class="parameterContainer">';
}
function pparameterEnd()
{
    parsex += parameterEnd();
}
function parameterEnd()
{
    return '</div></div>';
}
function pfunctionDeclare(functionName)
{
    parsex += functionDeclare(functionName);
}
function functionDeclare(functionName)
{
    return dotx() + '<div class="function"><span class="removeFunction">x</span><span class="anotherParameter">+</span><div class="functionName">'+functionName.replace('#','')+'</div>';
}
function pfunctionDeclareEnd()
{
    parsex += functionDeclareEnd();
}
function functionDeclareEnd()
{
    return '</div>'+dotx();
}
function pstringLiteral(literal)
{
    parsex += stringLiteral(literal);
}
function stringLiteral(literal)
{
    return '<div class="literal" contenteditable="true">'+literal.substr(1,literal.length-2)+'</div>';
}
function pdot()
{
}
function pdotx()
{
    if (parsex.lastIndexOf('<span class="dot">&#9660;</span>') != parsex.length-'<span class="dot">&#9660;</span>'.length)
        parsex += dotx();
}
function dotx()
{
    return '<span class="dot">&#9660;</span>';
}
function pvariableReference(variableName)
{
    parsex += variableReference(variableName);
}
function variableReference(variableName)
{
    return '<div class="variableReference"><span class="variableReferenceText">'+variableName+'</span> <span class="removeVariableReference">x</span></div>'+dotx();
}
function pvariableGroupReference(variableName)
{
    parsex += variableGroupReference(variableName);
}
function variableGroupReference(variableName)
{
    return '<div class="variableGroupReference"><span class="variableGroupReferenceText">'+variableName+'</span> <span class="removeVariableReference">x</span></div>';
}

function constructResolver(resolverName,data)
{
    var str = '';
    str += '<span class="dot">▼</span>'+'<div class="function">';
    str += '<span class="removeFunction">x</span><span class="anotherParameter">+</span><div class="functionName" function="'+resolverName+'">'+resolverName+'</div>';
    if (data.description == undefined) data.description = "";
    str += '<div class="description">'+data.description.replace("<","&lt;").replace(">","&gt;").replace(/\n/g,"<br>")+'</div>';
    for (var variableName in data.variables)
    {
        str += constructParameter(variableName.replace("<","&lt;").replace(">","&gt;"),data.variables[variableName]);
    }
    str += '</div>'+'<span class="dot">▼</span>';
    return str;
}

function constructParameter(name,type){
    var extraClass="";
    if (name.indexOf("?") == 0)
    {
        name = name.substring(1);
        extraClass = " optional";
    }
    var str = "";
        str += '<div class="parameter'+extraClass+'"><span class="removeParameter">-</span>';
        str += '<div class="parameterName" contenteditable="true">'+name+'</div>';
        str += '<div class="type">'+type+'</div>';
        str += '<div class="parameterContainer"><div class="literal" contenteditable="true"></div></div>';
        str += '</div>';
    return str;
}

function drop(ev)
{
    ev.preventDefault();
    var type = ev.originalEvent.dataTransfer.types[0];
    var target = $(ev.target);
    var data=ev.originalEvent.dataTransfer.getData(type);
    if (type == "text/id" && target.hasClass("dot"))
    {
        var html = $("#"+data).parent().html();
        if (!$("#"+data).parent().hasClass("functionalSpec"))
            $("#"+data).html('<div class="literal" contenteditable="true"></div>');
        target.before('<span class="dot">▼</span>');
        target.before(html).attr("id",null);
        //$("#"+ev.target.getAttribute("id")).next().attr("id",null);
    }
    else if (type == "text/id")
    {
        var html = $("#"+data).parent().html();
        if (!$("#"+data).parent().hasClass("functionalSpec"))
            $("#"+data).html('<div class="literal" contenteditable="true"></div>');
        target.parent().children(".parameterContainer").html(html);
        target.parent().children(".parameterContainer").children(".function").attr("id",null);
    }
    if (type == "text/variablename")
    {
        if ($(ev.target).parent().hasClass("rs2function"))
        {
            $("#"+data).parent().insertBefore(target.parent());
        }
        else if (ev.target.getAttribute("droptype") == "VariableReference")
        {
            target.html($("#"+data).children(".variableName").html());
        }
        else if (ev.target.getAttribute("droptype") == "WebService")
        {
            target.parent().children(".parameterContainer").html($("#"+data).children(".variableName").html());
            if (target.hasClass("success"))
                target.click();
        }
        else if (ev.target.getAttribute("droptype") == "Dot")
        {
            target.before(variableReference($("#"+data+" .variableName").html()));
        }
        else
        {
            target.parent().children(".parameterContainer").html(variableReference($("#"+data+" .variableName").html()));
        }
    }
    if (type == "text/variablegroupname" && ev.target.getAttribute("droptype") == "Group")
    {
        if (data != "")
            target.after(variableGroupReference(data));
    }
    refresh();
}

function allowDrop(ev)
{
    var dropType = ev.target.getAttribute("droptype");
    if (ev.originalEvent.dataTransfer.types[0] == "text/id")
    {
        if (dropType == "Dot")
        {
            ev.preventDefault();
            ev.originalEvent.dataTransfer.dropEffect = "move";
        }
        else if (dropType == "Parameter")
        {
            ev.preventDefault();
            ev.originalEvent.dataTransfer.dropEffect = "move";
        }
    }
    else if (ev.originalEvent.dataTransfer.types[0] == "text/variablename")
    {
        if (dropType == "Parameter")
        {
            ev.preventDefault();
            ev.originalEvent.dataTransfer.dropEffect = "link";
        }
        else if (dropType == "VariableReference")
        {
            ev.preventDefault();
            ev.originalEvent.dataTransfer.dropEffect = "link";
        }
        else if (dropType == "WebService")
        {
            ev.preventDefault();
            ev.originalEvent.dataTransfer.dropEffect = "link";
        }
        else if (dropType == "Dot")
        {
            if ($(ev.target).parent().children(".dot").first().attr("id") == $(ev.target).attr("id"))
            {
                if (!$(ev.target).prev().hasClass("variableReference"))
                {
                    ev.preventDefault();
                    ev.originalEvent.dataTransfer.dropEffect = "link";
                }
            }
        }
        else if (dropType == "Move")
        {
            ev.preventDefault();
            ev.originalEvent.dataTransfer.dropEffect = "move";
        }
    }
    else if (ev.originalEvent.dataTransfer.types[0] == "text/variablegroupname" && dropType == "Group")
    {
        ev.preventDefault();
        ev.originalEvent.dataTransfer.dropEffect = "link";
    }
}

function drag(ev)
{
    if (ev.target.className.indexOf("variableGroupName") != -1)
        ev.originalEvent.dataTransfer.setData("text/variablegroupname",ev.target.innerHTML);
    else if (ev.target.className.indexOf("rs2function") != -1)
        ev.originalEvent.dataTransfer.setData("text/variablename",ev.target.id);
    else
        ev.originalEvent.dataTransfer.setData("text/id",ev.target.parentElement.id);
}

var id = 0;
var once = false;
function events()
{
    if (once == true) return;
    once = true;
    
    //Droppable
    $('#mainPanel').delegate('.parameterName',"drop",drop);
    $('#mainPanel').delegate('.parameterName',"dragover",allowDrop);
    $('#mainPanel').delegate('.variableReference',"drop",drop);
    $('#mainPanel').delegate('.variableReference',"dragover",allowDrop);
    $('#mainPanel').delegate('.functionName',"drop",drop);
    $('#mainPanel').delegate('.functionName',"dragover",allowDrop);
    $('#mainPanel').delegate('.dot',"drop",drop);
    $('#mainPanel').delegate('.dot',"dragover",allowDrop);
    $('#servlets').delegate('.rs2function',"drop",drop);
    $('#servlets').delegate('.rs2function',"dragover",allowDrop);
    $('#servlets').delegate('.webservice',"drop",drop);
    $('#servlets').delegate('.webservice',"dragover",allowDrop);
    
    //Draggable
    $('body').delegate('.functionName',"dragstart",drag);
    $('body').delegate('.literal',"dragstart",drag);
    $('#servlets').delegate('.rs2function',"dragstart",drag);
    $('#servlets').delegate('.variableGroupName',"dragstart",drag);
    
    //Functionality
    $('#mainPanel').delegate('.anotherParameter',"click",function(){
        $(this).parent().append(constructParameter("obj","Object"));
        refresh();
    });
    $('#mainPanel').delegate('.removeFunction',"click",function(){
        $(this).parent().prev().remove();
        $(this).parent().remove();
        refresh();
    });
    $('#mainPanel').delegate('.removeParameter',"click",function(){
        $(this).parent().remove();
        refresh();
    });
    $('#mainPanel').delegate('.removeVariableReference',"click",function(){
        $(this).parent().remove();
        refresh();
    });
    //Hover over Internal Function
    $('#mainPanel').delegate('.functionName',"mouseover",function(){
        var v = $('.functionalSpec .functionName[function="'+$(this).html()+'"')
        if (v[0])
        {
            v[0].scrollIntoView();
            v.parent().addClass("hovered");
        }
    });
    $('#mainPanel').delegate('.functionName',"mouseout",function(){
        var v = $('.functionalSpec .functionName[function="'+$(this).html()+'"')
        if (v[0])
        {
            v.parent().removeClass("hovered");
        }
    });
    //Select Variable
    $('#servlets').delegate('.rs2function.secondary',"click",function(){
        deselectVariable();
        deselectWebService();
        if (deselectFile()) return;
        saveTo = $(this);
        saveTo.addClass("success");
        saveTo.removeClass("secondary");
        var html = $(this).html();
        $("#mainPanel").html(html);
        refresh();
    });
    //Select File
    $('#servlets').delegate('.file.deselected',"click",function(){
        deselectVariable();
        deselectWebService();
        if (deselectFile()) return;
        saveTo = $(this);
        saveTo.addClass("selected");
        saveTo.removeClass("deselected");
        $("#mainPanel").html("<textarea>"+toRs2($(this))+"</textarea>");
        refresh();
    });
    $('#servlets').delegate('.fileSave',"click",function(){;
        deselectVariable();
        deselectWebService();
        if (deselectFile()) return;
        
        saveFile($(this).next().html(),toRs2($(this).next()));
    });
    //Select Web Service
    $('#servlets').delegate('.webservice .parameterName',"click",function(){
        deselectVariable();
        deselectWebService();
        if (deselectFile()) return;
        saveTo = $(this);
        saveTo.addClass("success");
        $("#servlets .rs2function").removeClass("success");
        $("#servlets .rs2function").addClass("secondary");
        var variableName = $(this).parent().children(".parameterContainer").html();
        var prev = $(this).parent().prev();
        var i = 5000;
        while (prev != null && i > 0)
        {
            if (prev.hasClass("rs2function") && prev.children(".variableName").html()==variableName)
            {
                prev.addClass("success");
                prev.removeClass("secondary");
                break;
            }
            i--;
            prev = prev.prev();
        }
    });
    $('#servlets').delegate('.addVariable',"click",function(){
        $(this).prev().before(
            variableDeclare("new")+dotx()+
            variableDeclareEnd()
        );
    });
    $('#servlets').delegate('.addWebService',"click",function(){;
        $(this).next().next().next().after(
            servletDeclare("/new",$(this).next(".rs2function").children(".variableName").html())
        );
    });
    $('#servlets').delegate('.removeVariable',"click",function(){
        $(this).next().remove();
        $(this).prev().remove();
        $(this).prev().remove();
        $(this).remove();
    });
    $('#servlets').delegate('.removeWebService',"click",function(){
        if ($(this).next().children(".parameterName").hasClass("success"))
        {
            $("#servlets .rs2function").removeClass("success");
            $("#servlets .rs2function").addClass("secondary");
        }
        $(this).next().remove();
        $(this).remove();
    });
}

function deselectVariable()
{
    if (saveTo != null && saveTo.hasClass("rs2function"))
    {
        $("#mainPanel .variableName").html(saveTo.children(".variableName").html());
        saveTo.html($("#mainPanel").html());
        saveTo.removeClass("success");
        saveTo.addClass("secondary");
        $("#mainPanel").html("");
        saveTo = null;
    }
}
function deselectWebService()
{
    if (saveTo != null && saveTo.parent().hasClass("webservice"))
    {
        saveTo.removeClass("success");
        $("#servlets .rs2function").removeClass("success");
        $("#servlets .rs2function").addClass("secondary");
        saveTo = null;
    }
}
function deselectFile()
{
    if (saveTo != null && saveTo.hasClass("file"))
    {
        var input = $("#mainPanel textarea")[0].value;
        parsex = "";
        parse(input);
        
        while (saveTo.next().length > 0 && !saveTo.next().hasClass("fileSave"))
            saveTo.next().remove();
    
        saveTo.after(parsex);
        console.info("Parsed improv");
        saveTo.removeClass("selected");
        saveTo.addClass("deselected");
        
        saveTo = null;
        $("#mainPanel").html("");
        refresh();
        return true;
    }
    return false;
}

var saveTo;

function refresh()
{
    //Cleanup
    $('.dot').each(function(o){
        if ($(this).next() != null && $(this).next().hasClass('dot'))
            $(this).next().remove();
    });
    
    //Identifiable
    $('#mainPanel .parameterName').each(function(){
        if ($(this).attr("id") == null || $(this).attr("id").indexOf("fun") != -1)
            $(this).attr("id","obj"+id++);
    });
    $('.functionalSpec .parameterName').each(function(){
        if ($(this).attr("id") == null)
            $(this).attr("id","fun"+id++);
    });
    $('.rs2function .variableName').each(function(){
        if ($(this).attr("id") == null)
            $(this).attr("id","rs2"+id++);
    });
    $('.rs2function').each(function(){
        if ($(this).attr("id") == null)
            $(this).attr("id","rs2"+id++);
    });
    $('.webservice .parameterName').each(function(){
        if ($(this).attr("id") == null)
            $(this).attr("id","wsf"+id++);
    });
    $('#mainPanel .functionName').each(function(){
        if ($(this).attr("id") == null || $(this).attr("id").indexOf("fun") != -1)
            $(this).attr("id","obj"+id++);
    });
    $('#mainPanel .function').each(function(){
        if ($(this).attr("id") == null || $(this).attr("id").indexOf("fun") != -1)
            $(this).attr("id","obj"+id++);
    });
    $('.dot').each(function(){
        if ($(this).attr("id") == null || $(this).attr("id").indexOf("fun") != -1)
            $(this).attr("id","obj"+id++);
    });
    $('#mainPanel .variableReference').each(function(){
        if ($(this).attr("id") == null || $(this).attr("id").indexOf("fun") != -1)
            $(this).attr("id","obj"+id++);
    });
    $('.variable .function').each(function(){
        if ($(this).attr("id") == null || $(this).attr("id").indexOf("fun") != -1)
            $(this).attr("id","obj"+id++);
    });
    $('.functionalSpec .function').each(function(){
        if ($(this).attr("id") == null)
            $(this).attr("id","fun"+id++);
    });
    $('.literal').each(function(){
        if ($(this).attr("id") == null)
            $(this).attr("id","obj"+id++);
    });
    
    //Droppable
    $('#mainPanel .parameterName').attr("droptype","Parameter");
    $('#mainPanel .dot').attr("droptype","Dot");
    $('#mainPanel .functionName').attr("droptype","Group");
    $('.rs2function .variableName').attr("droptype","Move");
    $('#mainPanel .variableName').attr("droptype","Move");
    $('#servlets .webservice .parameterName').attr("droptype","WebService");
    $('#mainPanel .variableReference').attr("droptype","VariableReference");
    
    //Draggable    
    $('.functionName').attr("draggable","true");
    $('#servlets .rs2function').attr("draggable","true");
    $('#servlets .variableGroupName').attr("draggable","true");
    $('.literal').attr("draggable","true");    
}