fs=require('fs'); //File System
var argv=require ('optimist').argv; //Passing Arguments
var readfile=require('./libs/readfile'); //file writer
var mkdirp=require('mkdirp'); //Creating Directory
var cheerio=require('cheerio');
var output1 = argv.o + "\\output";
var output = argv.o + "\\output\\temphtml";
var output3 = argv.o;
var output2 = output3 + "\\output\\temphtml";
var opfpath = argv.o + "\\temp\\OEBPS\\";
var spawn = require('child_process').spawn;

/*  fs.readdir(output, function (err, files) {
	files.forEach(function (file) {
		fs.lstat(output + '\\' + file, function (err, stats) {
			if (!err && !stats.isDirectory()) {
				var htmlfile = output + '\\' + file;
					var pagewisefolder = file.replace('.html','');
					mkdirp(output1 + '/html/' + pagewisefolder, function(err) {
					//path was created unless there was error
					});
					var cp = spawn(process.env.comspec, ['/c', 'xslt.bat', htmlfile, output3 +"\\xslthtml\\" + file]);
					cp.stdout.on("data", function(data) {
				    //console.log(data.toString());
					});
					cp.stderr.on("data", function(data) {
					//console.error(data.toString());
					});
				}
			});
	});
}); */


 fs.readdir(output2, function (err, files) {
 console.log(files);
	files.forEach(function (file) {
		fs.lstat(output2 + '\\' + file, function (err, stats) {
			if (!err && !stats.isDirectory()) {
					var htmlfile = output2 + '\\' + file;
					var pagewisefolder = file.replace('.html','');
					mkdirp(output1 + '/html/' + pagewisefolder, function(err) {
					//path was created unless there was error
					});
					var htmlcontent = readfile(htmlfile);
					var newhtmlcontent1 = htmlcontent.replace(/\<div class\=\"section section[0-9]+\"\/\>/g,'').replace(/\<span name\=\"\"\/\>/g,'<span></span>');
					var newhtmlcontent = newhtmlcontent1.replace(/\>\</g,'>\n<');
					var $ = cheerio.load(newhtmlcontent);
					$('span').removeAttr('xmlns');
					$('span').removeAttr('id');
					var titletag = $('title').text();
					$('head span').replaceWith('\n');
					$('title').text(titletag);
$('span[name=""]').each(function() {
    var $this = $(this);
    if($this.html().replace(/\s|&nbsp;/g, '').length == 0)
        $this.remove();
});
					fs.writeFileSync(output1 + "\\html\\" + pagewisefolder + "\\" + file ,$.xml());
				}
			});
	});
});

