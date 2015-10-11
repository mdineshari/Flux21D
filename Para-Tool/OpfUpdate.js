fs=require('fs'); //File System
var argv=require ('optimist').argv; //Passing Arguments
var readfile=require('./libs/readfile'); //file writer
var mkdirp=require('mkdirp'); //Creating Directory
var cheerio=require('cheerio');
var opfpath = argv.o + "\\temp\\OEBPS\\";
var output = argv.o + "\\output\\";
var spawn = require('child_process').spawn;

var opffile = readfile(opfpath + "\\" + "content.opf");

var $$$ = cheerio.load(opffile);
opfupdate('item');

opffile=opffile.replace('<manifest>','<manifest>\n<item id="cover" href="html/cover/cover.html" media-type="application/xhtml+xml" />\n<item id="page000" href="html/page000/page000.html" media-type="application/xhtml+xml" />\n<item id="advert" href="html/advert/advert.html" media-type="application/xhtml+xml" />\n<item id="backcover" href="html/back_cover/back_cover.html" media-type="application/xhtml+xml" />').replace('<spine>','<spine>\n<itemref idref="cover" />\n<itemref idref="page000" />').replace('</spine>','\n<itemref idref="advert" />\n<itemref idref="backcover" />\n</spine>');
fs.writeFileSync(output + "content.opf",opffile);

function opfupdate(opfmanifest) {
$$$('item[media-type="application/xhtml+xml"]').each(function(i,e) {
var itemids = $$$(this).attr('id').replace(/[A-z0-9]+\_[A-z0-9]+\-/g,'');
var itemids1 = "page"+ padDigits(itemids, 3);
var regex1 = new RegExp($$$(this).attr('id') + '"',"g");
opffile=opffile.replace(regex1,itemids1 + '"');
var regex1 = new RegExp($$$(this).attr('id') + '\.xhtml',"g");
opffile=opffile.replace(regex1,itemids1 + '.xhtml');
opffile=opffile.replace('href="'+itemids1+'.xhtml"','href="html/'+itemids1+"/"+itemids1+'.html"');
});

}


function padDigits(number, digits) {
    return Array(Math.max(digits - String(number).length + 1, 0)).join(0) + number;
}
