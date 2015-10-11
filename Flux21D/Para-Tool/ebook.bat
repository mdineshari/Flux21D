rmdir /S /Q %2\book
rmdir /S /Q %2\cover
mkdir %2\output
mkdir %2\output\temphtml
mkdir %2\xslthtml
mkdir %2\output\html
mkdir %2\output\images
mkdir %2\output\css
mkdir %2\output\thumbnails
mkdir %2\output\search_index
mkdir %2\output\spread_index
mkdir %2\output\fonts
mkdir %2\output\audio


"unzip.exe" %1 -d %2\temp
"unzip.exe" %3 -d %2\cover



rem copy %2\temp\OEBPS\image\*.* %2\output\images
rem copy %2\cover\OEBPS\image\*.* %2\output\images
copy %6\*.* %2\output\fonts

node index_book.js -o %2
node index_cover.js -o %2

node spanname.js -o %2

node spanname.js -o %2

rmdir /S /Q %2\output\temphtml
rmdir /S /Q %2\xslthtml

node OpfUpdate.js -o %2

java -jar SpreadJsonCreator.jar %2\output

java -jar createJson.jar %2\output

java -jar SpreadMergerBatch.jar %2\output %4 %5

node finalcleanup.js -i %2

rmdir /S /Q %2\cover
rmdir /S /Q %2\temp