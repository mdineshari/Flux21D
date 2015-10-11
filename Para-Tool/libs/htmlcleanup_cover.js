fs=require('fs'); //FileSystem
var argv=require('optimist').argv; //Arguments
var writetofile=require('./writetofile'); //file writer
var readfile=require('./readfile'); //file writer
var mkdirp=require('mkdirp'); //Creating Directory
var cheerio=require('cheerio');
var cssParser = require('css-parse');
var underscore=require('./underscore.js'); //file writer


module.exports = function (input, output)
{
var pclassess = "";
var lsremoved ="";
var match = "";
var divregex = "";
var newmaindiv = "";
var newdiv = "";
var firstfile=0;
var cssDom ="";
var cssupdate1 = "";
/* Main Variables */
	var content=readfile(input);
	var $=cheerio.load(content);
	var varleftvalue=[];
/* Main Variables */

/* HTML File Names */
	var regexhtmlfilename = /[A-z0-9]+\_[A-z]+\-/g;
	var root = input.substr(0,input.lastIndexOf('\\'));
	var htmlfilename = input.replace(root + '\\', '');
	var htmlnames = htmlfilename.match(regexhtmlfilename);
	var fnhtml = htmlfilename.replace(htmlnames + "1", 'back_cover').replace('.xhtml','.html').replace(htmlnames + "2", 'cover').replace('.xhtml','.html').replace(htmlnames + "3", 'page000').replace('.xhtml','.html').replace(htmlnames + "4", 'advert').replace('.xhtml','.html');
	var htmlfolder = htmlfilename.replace(htmlnames + "1", 'back_cover').replace('.xhtml','').replace(htmlnames + "2", 'cover').replace('.xhtml','').replace(htmlnames + "3", 'page000').replace('.xhtml','').replace(htmlnames + "4", 'advert').replace('.xhtml','');
	var productfolder = htmlfilename.replace(htmlnames + "1", 'back_cover_page').replace('.xhtml','').replace(htmlnames + "2", 'front_cover_page').replace('.xhtml','').replace(htmlnames + "3", 'page000').replace('.xhtml','').replace(htmlnames + "4", 'pageadvt').replace('.xhtml','');
	var Containerclass = htmlfilename.replace(htmlnames + "1", 'backcover').replace('.xhtml','').replace(htmlnames + "2", 'frontcover').replace('.xhtml','').replace(htmlnames + "3", 'page000Container').replace('.xhtml','').replace(htmlnames + "4", 'advert').replace('.xhtml','');
	var cssfolder = htmlfilename.replace(htmlnames + "1", 'back_cover').replace('.xhtml','').replace(htmlnames + "2", 'cover').replace('.xhtml','').replace(htmlnames + "3", 'page000').replace('.xhtml','').replace(htmlnames + "4", 'advt').replace('.xhtml','');
	var cssfilename = htmlfolder.replace(htmlfolder, 'css_' + cssfolder);
	

/* HTML File Names */
try {
 for(var i=0;i<$('link[rel="stylesheet"]').length;i++)
 {


 if(firstfile==0)
 {
 
 /* Template Css updates */
	 var cssfile = $('link[rel="stylesheet"]').eq(i).attr('href');
	 var csspath = root + "\\" + cssfile;
	 var csscontent=readfile(csspath);
	 var fontregex = /font\-family\:.*?\;/g;
	 var fontmatch = csscontent.match(fontregex);
	 var fontfamily = underscore.uniq(fontmatch, false);
	 var imgregex = /\/.*?\.jpg/g;
	 var image = content.match(imgregex);

	 var varfontfamily="";
	 	fontfamily.forEach (function (elem,i) {
		 try {
		 var regex1 = new RegExp(" ","g");
		 var fontface=elem.split(';')[0].split('",')[0].split('font-family:"')[1].replace(regex1,"__").replace("-","_");
		 var fontface1=elem.split(';')[0].split('",')[0].split('font-family:"')[1].replace('"','');
		 varfontfamily = varfontfamily + '@font-face { font-family: "' + fontface1 + '"; font-style: normal; font-weight: normal; src: url(\''+ '../fonts/' + fontface + '.otf\') }' + "\n"; //Font face variable
		 }
		 catch(e){}
	 }); 
		/*var newtempcss=root + "\\" + $('link[rel="stylesheet"]').attr('href');
	 var newcsscontent=readfile(newtempcss);
	 cssDom = cssParser(newcsscontent);	 
	 var idelement = "<root>\n"
		cssDom.stylesheet.rules.forEach(function(e,j){
		var varclassdecaration="";
		
		e.selectors.forEach(function(e2,k){
			e.declarations.forEach(function(e1,l){
				varclassdecaration = varclassdecaration + e1.property +":" + e1.value+";";
				});
		idelement = idelement + '\n<css rules="' + e2 +'">\n'+varclassdecaration+'\n</css>';
		varclassdecaration="";
	});
	
	});
	idelement = idelement + "\n</root>";
	var $$$ = cheerio.load(idelement);
	$$$('css[rules="@font-face"]').each(function(e,i) {
	var fontupdate = $$$(this).html();
	var newfontupdate = fontupdate.replace(/\"\.\.\/font/g,'\'../fonts').replace('")','\')').replace(/font\-weight\:[A-z0-9]+\;/g,'font-weight:normal;').replace('font-family:','font-family:"').replace(';font-style','"; font-style').replace(/font\-style\:[A-z0-9]+\;/g,'font-style:normal;').replace(/""/g,'"').replace(/;/g,'; ').replace(/;  /g,'; ');
	varfontfamily = varfontfamily+ "\n@font-face {" + newfontupdate + "}\n";
	});*/
	 var bodystyle = $('body').attr('style');	//body style
	 var bodyid = $('body').attr('id');
	 fs.appendFileSync(output + "\\css\\" + "template.css" ,varfontfamily);	//Font declaration has been added in the template.css

 /* Template Css updates */

 }
 }
	 $('body').removeAttr('style').removeAttr('lang').removeAttr('xml:lang').removeAttr('id');
	 $('meta[charset="utf-8"]').remove();
	 $('title').text(htmlfolder);
 /* Open Template css */
	  var newtempcss=root + "\\" + $('link[rel="stylesheet"]').attr('href');
	  var newcsscontent=readfile(newtempcss);
	  cssDom = cssParser(newcsscontent);

	  
 /* Open Template css */
var idelements = "<root>\n"
		cssDom.stylesheet.rules.forEach(function(e,j){
		var varclassdecaration="";
		
		e.selectors.forEach(function(e2,k){
			e.declarations.forEach(function(e1,l){
				varclassdecaration = varclassdecaration + e1.property +":" + e1.value+";";
				});
		idelements = idelements + '\n<css rules="' + e2 +'">\n'+varclassdecaration+'\n</css>';
		varclassdecaration="";
	});
	
	});
idelements = idelements + "\n</root>";
var $$ = cheerio.load(idelements);

$('img').parent().remove();
$('body').prepend('<div class="img_container"><img src="../../images/'+htmlfolder+'.jpg"/></div>');

spanclassupdated('span');
spanclassupdated1('span');
spannthchild('p');

 removeid('div');
 removeid('span');
 removeid('img');
 removeid('p');

 removeclass('span');
 removeclass('p');
 removeclass('img');
 removeclass('div');
 spanclass('p');
 divclasses('div');
 removespan('span');
 pfontstyles('p');
 $('span').removeAttr('class');
 $('span').removeAttr('style');
 $('span').removeAttr('lang');
 $('span').removeAttr('xml:lang');
 cssupdate('p');
 cssupdate('div');
 cssupdate('img');
 $('div').removeAttr('style');
 $('img').removeAttr('style');
 $('p').removeAttr('style');
    for(var i=0;i<$('link[rel="stylesheet"]').length;i++)
 {
 if(i==0)
 {

 $('link[rel="stylesheet"]').eq(i).attr('href',"../../css/template.css");
 }
 else
 {
 $('link[rel="stylesheet"]').eq(i).attr('href',"../../css/template"+i+".css");
 }
 }

$('head').append('<link href="../../css/' + cssfilename + '.css" rel="stylesheet" type="text/css"></link>');
var body = $('body').html();
$('body').html("");
var newpid = bodyid.split('_');
$('body').prepend('\n<div class="EB_' + newpid[0] + '_' + productfolder +'">\n<div class="page">\n<div class="' + Containerclass + '">'  + body + '\n</div>\n</div>\n</div>\n');
/* $('img').each(function(i,e) {
$(this).attr('src',$(this).attr('src').replace($(this).attr('src'),'../../' + $(this).attr('src')).replace('/image/','/images/'));
});
 */var newcontent = $.xml().replace(/\<span\>/g,'').replace(/\<\/span\>/g,'');
 
fs.writeFileSync(output + "\\temphtml\\" + fnhtml ,newcontent);

/* Remove id Function */

function removeid(vartag)
{
	$(vartag +'[id]').each(function(i,e)
	{
		var divid = $(this).attr('id');
		var cssattr=$$('css[rules=#'+ divid +']').html();
			if(cssattr!=null)
				{
					if($(vartag+'[id="'+divid+'"]').attr('style') == null)
						{
							$(vartag+'[id="'+divid+'"]').attr("style",cssattr);
						}
					else
						{
							$(vartag+'[id="'+divid+'"]').css(cssattr);
						}
				}
			else
				{
					
				}
	});
}

/* Remove class Function */


function removeclass(varclass)
{
	$(varclass +'[class]').each(function(i,e) {
		var divclass = $(this).attr('class');
		var divclass1 = divclass.split(" ").forEach(function(el,ind) {
		var cssattr=$$('css[rules="' + varclass + '.' + el +'"]').html();
			if(cssattr!=null)
				{
					if($(varclass+'[class="'+divclass+'"]').attr('style') == null)
						{
							$(varclass+'[class="'+divclass+'"]').attr("style",cssattr);
						}
					else
						{
							$(varclass+'[class="'+divclass+'"]').css(cssattr);
						}
				}
			else
				{
				}
	});
});
}


/* function removeclass(varclass)
{
	for(var i=0;i<$(varclass +'[class]').length;i++)
	{
		var divclass = $(varclass).eq(i).attr('class');
		var varclassdecaration="";
		try {
		var newclass = $(varclass).eq(i).attr('class').split(" ");
		newclass.forEach(function(elem,i) {
		cssDom.stylesheet.rules.forEach(function(e,i){
		e.selectors.forEach(function(e2,i){
			if(e2==varclass + "." +newclass)
				{
				
				e.declarations.forEach(function(e1,i){
				varclassdecaration = varclassdecaration + e1.property +":" + e1.value+";";
				});

			if($(varclass+'[class="'+newclass+'"]').attr('style') == null)
				{
				$(varclass+'[class="'+newclass+'"]').attr('style',varclassdecaration);
				}
			else
				{
				$(varclass+'[class="'+newclass+'"]').css(varclassdecaration);
				}
			}
			else
			{
			}
		});
		});
		});
		}
		catch(e) {}
	}

	}
 */

/* Function for Span level class into Para level */
		function spanclass(pclass) 
		{
for(var i=0;i<$(pclass).length;i++)
{
//var widthvalue=[];
	for(var j=0;j<$(pclass).eq(i).children('[style]').length;j++)
	{
	//	widthvalue.push(parseInt($('p').eq(i).children('[style]').eq(j).attr('style').split("left:")[1].split("px;")[0]));
		varattrwidth=$(pclass).eq(i).children('[style]').eq(0).attr('style');
	}

	$(pclass).eq(i).children('[style]').removeAttr('id');
	$(pclass).eq(i).children('[style]').removeAttr('style');
	$(pclass).removeAttr('id');
	$('img').removeAttr('id');
	$('img').removeAttr('class');
	//$('span').removeAttr('class');
	$(pclass).eq(i).attr('style',varattrwidth);
	//$(pclass).eq(i).css('width',Math.max.apply(null, widthvalue) + "px");
	$(pclass).eq(i).attr('class','para para'+i+"");

}

}

	function divclasses(divclass)
	{
	for(var i=0;i<$(divclass).length;i++)
	{
	$(divclass).eq(i).attr('class','section section'+i+"");
	$('div').removeAttr('id');
	}
	}


function removespan(varspan) {
	$(varspan +'[class]').each(function(i,e) {
		var classspan=$(this).attr('class');
		var spanobject = $(this);
		var spanclass = $(this).attr('class').split(' ');
		spanclass.forEach(function(elem,i) {


		var cssattr=$$('css[rules="span.'+ elem +'"]').html();
		
		if(cssattr!=null) {
		
			if($(varspan +'[class=\''+classspan+'\']').attr('style') == null) {
				$(varspan +'[class=\''+classspan+'\']').attr("style",cssattr);
			}
			else {
				$(varspan +'[class=\''+classspan+'\']').css(cssattr);
			}
		}
		else {}
	});
	});
}

	
	
			function pfontstyles(pspanclass) 
		{
for(var i=0;i<$(pspanclass).length;i++)
{
	for(var j=0;j<$(pspanclass).eq(i).children('[style]').length;j++)
	{
		varattrwidth=$(pspanclass).eq(i).children('[style]').eq(0).attr('style');
	}
	$(pspanclass).eq(i).css('style',varattrwidth);
}
}

	function cssupdate(cssclasses) 
	{
	
		for(var i=0;i<$(cssclasses+'[style]').length;i++)
		{
		
		if ($(cssclasses+'[style]').eq(i).attr('style')==undefined ) {
		}
		else {
		
		var styles = $(cssclasses+'[style]').eq(i).attr('style');
		try {
		var cclass = $(cssclasses+'[style]').eq(i).attr('class').split(" ")[1];
		cssupdate1 = cssupdate1 + "\n." + Containerclass + " ." + cclass + " { " + styles + " }\n ";
		cssupdate0 = cssupdate1.replace(/\, sans-serif/g,'').replace(/\, serif/g,'');
		cssupdate2 = cssupdate0.replace(/style\: /g,'');
		fs.writeFileSync(output + "\\css\\" + cssfilename + ".css" ,cssupdate2);
		}
		catch(e){console.log(e);}
		}
		
		}
 	}

}
catch(e) {console.log(e);}
}


