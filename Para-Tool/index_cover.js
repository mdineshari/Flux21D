fs=require('fs'); //File System
var argv=require ('optimist').argv; //Passing Arguments
var readfile=require('./libs/readfile'); //file writer
var mkdirp=require('mkdirp'); //Creating Directory
var ncp = require('ncp').ncp;
var input = argv.o + "\\cover\\OEBPS";
var output = argv.o + "\\output";
var mainprocess = require('./libs/htmlcleanup_cover.js');
var copy = require('ncp');

var dir = fs.readdirSync(input);
try{
dir.forEach(file);
function file(element, index, array) {
if (element.match(".xhtml")) {
var htmlfile = input + "\\" + element;
var regexhtmlfilename = /[A-z0-9]+\_[A-z]+\-/g;
var htmlnames = element.match(regexhtmlfilename);
var fnhtml = element.replace(htmlnames + "1", 'backcover').replace('.xhtml','.html').replace(htmlnames + "2", 'cover').replace('.xhtml','.html').replace(htmlnames + "3", 'page000').replace('.xhtml','.html').replace(htmlnames + "4", 'pageadvert').replace('.xhtml','.html');
var fnhtml1 = element.replace(htmlnames + "1", 'backcover').replace('.xhtml','').replace(htmlnames + "2", 'cover').replace('.xhtml','').replace(htmlnames + "3", 'page000').replace('.xhtml','').replace(htmlnames + "4", 'pageadvert').replace('.xhtml','');

mainprocess (htmlfile, output);

}
}
}
catch(e)
{
console.log(e);
}