fs=require('fs'); //File System
var argv=require ('optimist').argv; //Passing Arguments
var readfile=require('./libs/readfile'); //file writer
var mkdirp=require('mkdirp'); //Creating Directory
var ncp = require('ncp').ncp;
var cheerio=require('cheerio');
var input = argv.i + "\\output";
var htmlinput = input + "\\html";


var dir = fs.readdirSync(htmlinput);
var content1 = readfile(input + "\\html\\cover\\cover.html");
var $$ = cheerio.load(content1);
$$('div[class="frontcover"]').append('\n<p class="temppara temppara1"><span name="intro">intro</span></p>\n');
fs.writeFileSync(input + "\\html\\cover\\cover.html" ,$$.xml());

try{
dir.forEach(file);
}

catch(e) {}






function file(element, index, array) {
var htmlfile = htmlinput + "\\" + element + "\\" + element + ".html";
var content = readfile(htmlfile);
var newcontent = content.replace('<head>','<head>\n<meta charset="utf-8" />\n');
fs.writeFileSync(htmlfile ,newcontent);
}