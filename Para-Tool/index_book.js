fs=require('fs'); //File System
var argv=require ('optimist').argv; //Passing Arguments
var readfile=require('./libs/readfile'); //file writer
var mkdirp=require('mkdirp'); //Creating Directory
var ncp = require('ncp').ncp;
var output1 = argv.o + "\\temp\\OEBPS";
var input = output1;
var output = argv.o + "\\output";
var mainprocess = require('./libs/htmlcleanup_book.js');
var copy = require('ncp');

var dir = fs.readdirSync(output1);
try{
dir.forEach(file);
function file(element, index, array) {
if (element.match(".xhtml")) {
var htmlfile = input + "\\" + element;
var regexhtmlfilename = /[A-z0-9]+\_[A-z]+\-/g;
var htmlnames = element.match(regexhtmlfilename);
var fnhtml1 = element.replace(htmlnames, 'page').replace('.xhtml','');
var fnhtml = element.replace(htmlnames, 'page').replace('.xhtml','.html');

mainprocess (htmlfile, output);

}
}
}
catch(e)
{
console.log(e);
}