import java.io.*;

import org.css.cssparser.CParser;
import org.css.cssparser.CSSNode;
import org.fusesource.jansi.AnsiConsole;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpreadMerger {
	/**
	 * @param args
	 */
	static ArrayList<File> cssFileList = new ArrayList<File>();
	static ArrayList<File> htmlFileList = new ArrayList<File>();
	static ArrayList<File> smilFileList = new ArrayList<File>();
	static File contentOpf;
	static String width;
	static String height;
	static ArrayList<File>removableFiles=new ArrayList<File>();
	
	String getTemplateAsString() {
		String s = new Scanner(
				getClass().getResourceAsStream("/template.html"), "UTF-8")
				.useDelimiter("\\A").next();
		return s;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, String> propMap = new HashMap<String, String>();

		File currentPath = new File(".");
		File inputProp = new File(currentPath.getCanonicalPath() + "/prop.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputProp), "UTF-8"));
		String line = "";
		while ((line = in.readLine()) != null) {
			if (!line.isEmpty() && line.contains("=")) {
				String lineSp[] = line.split("=");
				propMap.put(lineSp[0], lineSp[1]);
			}
		}
		in.close();
		File left = new File(propMap.get("leftpage"));
		File rght = new File(propMap.get("rghtpage"));

		if ((left.exists() && rght.exists())
				&& (left.isFile() && rght.isFile())) {
			File masterDir = left.getParentFile().getParentFile()
					.getParentFile();
			feedFiles(masterDir);
			width = propMap.get("pagewidthpx");
			height = propMap.get("pageheightpx");
			if (left.exists() && rght.exists()) {
				merge(left, rght);
				processIndex(left, rght);
				processContent_Opf(left, rght);
			}
			cleanUp();
			System.out.println("Process Completed!");
			
		} else {
			System.out.println("Error: Process Failed! invalid input provided");
		}

	}
	
	//removing unwanted files
	public static void cleanUp(){
		Iterator<File>itr=removableFiles.iterator();
		while(itr.hasNext()){
			File f=itr.next();
			if(f.getName().endsWith(".html")){
				File parent=f.getParentFile();
				f.delete();
				if(parent.isDirectory()){
					parent.delete();
				}
			}
			else{
				f.delete();
			}
		}
	}
	
	public static void merge(File left, File rght) throws Exception {
		processHtml(left, rght);
		File lsmil = getSmilFile(left.getName().replace(".html", ".smil"));
		File rsmil = getSmilFile(rght.getName().replace(".html", ".smil"));
		processSmil(lsmil, rsmil);
	}

	public static String processCSS(File css1, File css2) throws Exception {
		
		removableFiles.add(css1);
		removableFiles.add(css2);
		
		CParser leftCss = new CParser(css1);
		leftCss.parse();
		Iterator<CSSNode> itr2 = leftCss.cssNodes.iterator();
		while (itr2.hasNext()) {
			CSSNode n = itr2.next();
			String selector = n.selector;
			if (selector.equals(".page")) {
				itr2.remove();
			}
		}

		String pageContainer1 = getPageContainer(css1.getName());
		int found1 = 0;
		String temp1 = "";

		Iterator<CSSNode> itr1 = leftCss.cssNodes.iterator();
		while (itr1.hasNext()) {
			CSSNode node = itr1.next();
			String selector = node.selector;
			if (selector.equals(pageContainer1)) {
				node.setAttr("position", "absolute");
				String left = width + "px";
				node.setAttr("left", "0px");
				node.setAttr("width", width + "px");
				node.setAttr("height", height + "px");
				found1 = 1;
			}
		}

		if (found1 == 0) {
			CSSNode pageContNode = new CSSNode(pageContainer1 + "{}");
			pageContNode.setAttr("position", "absolute");
			pageContNode.setAttr("left", "0px");
			pageContNode.setAttr("width", width + "px");
			pageContNode.setAttr("height", height + "px");
			leftCss.addCSSNode(pageContNode);
		}

		String leftCssStr = leftCss.toCSS();

		CParser rghtCss = new CParser(css2);
		rghtCss.parse();

		Iterator<CSSNode> itr3 = rghtCss.cssNodes.iterator();
		while (itr3.hasNext()) {
			CSSNode n = itr3.next();
			String selector = n.selector;
			if (selector.equals(".page")) {
				itr3.remove();
			}
		}
		// get the view port add half left

		String pageContainer = getPageContainer(css2.getName());

		int found = 0;

		String temp = "";

		Iterator<CSSNode> itr = rghtCss.cssNodes.iterator();
		while (itr.hasNext()) {
			CSSNode node = itr.next();
			String selector = node.selector;
			if (selector.equals(pageContainer)) {
				node.setAttr("position", "absolute");
				String left = width + "px";
				node.setAttr("left", left);
				node.setAttr("width", width + "px");
				node.setAttr("height", height + "px");
				found = 1;
			}
		}

		if (found == 0) {
			CSSNode pageContNode = new CSSNode(pageContainer + "{}");
			pageContNode.setAttr("position", "absolute");
			pageContNode.setAttr("left", width + "px");
			pageContNode.setAttr("width", width + "px");
			pageContNode.setAttr("height", height + "px");
			rghtCss.addCSSNode(pageContNode);
		}

		/*
		 * temp = "\n.page div" + pageContainer + "\n{position:absolute;left:" +
		 * width + "px;top:0px}\n.page{width:" + (Integer.parseInt(width) * 2) +
		 * "px;}\n";
		 */
		
		CSSNode pageNode = new CSSNode("body{}");
		//pageNode.setAttr("position", "absolute");
		pageNode.setAttr("width", (Integer.parseInt(width) * 2) + "px");
		pageNode.setAttr("height", height+"px");
		rghtCss.addCSSNode(pageNode);
		String cssName = css1.getName().replace(".css", "") +"_"+css2.getName().replace("css_", "").replace("page", "");
		String rghtCssStr = rghtCss.toCSS();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(css2.getParentFile() + "/"
						+ cssName)), "UTF-8"));
		out.write(leftCssStr + "/*RIGHT PAGE CSS*/" + temp + rghtCssStr);
		out.close();
		System.out
				.println("Css merged and updated with .page and .pageContainer width & height");

		return cssName;
	}

	public static String getPageContainer(String pageCss) {
		String pageContainer = "";
		if (pageCss.contains("page") && pageCss.contains("css_")) {
			// System.out.println(pageCss);
			pageContainer = "."
					+ pageCss.substring(pageCss.lastIndexOf("css_") + 4,
							pageCss.lastIndexOf(".")) + "Container ";
		}
		if (pageCss.equalsIgnoreCase("css_advt.css")) {
			pageContainer = ".advert ";
		} else if (pageCss.equalsIgnoreCase("css_back_cover.css")) {
			pageContainer = ".backcover ";
		} else if (pageCss.equalsIgnoreCase("css_toc.css")) {
			pageContainer = ".toc ";
		} else if (pageCss.equalsIgnoreCase("css_frontmatter.css")) {
			pageContainer = ".frontmatter ";
		} else if (pageCss.equalsIgnoreCase("frontmatter.css")) {
			pageContainer = ".frontmatter ";
		} else if (pageCss.equalsIgnoreCase("css_cover.css")) {
			pageContainer = ".frontcover ";
		}
		// System.out.println("PGCONTAINER:"+pageContainer);
		return pageContainer;
	}

	public static void processSmil(File smil1, File smil2) throws Exception {
		
		removableFiles.add(smil1);
		removableFiles.add(smil2);
		
		Document mergedSmil = Jsoup
				.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?><smil xmlns=\"http://www.w3.org/ns/SMIL\" version=\"3.0\"><body></body></smil>",
						"", Parser.xmlParser());
		Element body = mergedSmil.select("body").first();
		// tommorow task start
		String smil1Str = Utils.getFileAsString(smil1, "UTF-8");
		
		String mergedSmilName = smil1.getName().replace(".smil", "")
				+ smil2.getName().replace("page", "");

		smil1Str = smil1Str.replace("&", "[[AMP]]");
		Document doc = Jsoup.parse(smil1Str, "", Parser.xmlParser());
		Elements pars = doc.select("par>text");
		Iterator<Element> parsItr = pars.iterator();
		while (parsItr.hasNext()) {
			Element e = parsItr.next();
			String href = e.attr("src");
			String temp = href.substring(0, href.lastIndexOf("#") + 1);
			String temp2 = temp;
			temp2 = temp2 + "left";
			//replace
			href = href.replace(temp, temp2);
			e.attr("src", href);
			Element par = e.parent();
			body.appendChild(par);
		}

		String smil2Str = Utils.getFileAsString(smil2, "UTF-8");
		smil2Str = smil2Str.replace("&", "[[AMP]]");
		Document doc2 = Jsoup.parse(smil2Str, "", Parser.xmlParser());
		Elements pars2 = doc2.select("par>text");
		Iterator<Element> parsItr2 = pars2.iterator();
		while (parsItr2.hasNext()) {
			Element e = parsItr2.next();
			String href = e.attr("src");
			String temp = href.substring(0, href.lastIndexOf("#") + 1);
			String temp2 = temp;
			temp2 = temp2 + "rght";
			href = href.replace(temp, temp2);
			e.attr("src", href);
			Element par = e.parent();
			body.appendChild(par);
		}

		// System.out.println(mergedSmil);
		
		Elements mergedDocEle=mergedSmil.select("text");
		Iterator<Element>mergedDocEleItr=mergedDocEle.iterator();
		while(mergedDocEleItr.hasNext()){
			Element e=mergedDocEleItr.next();
			String src=e.attr("src");
			//System.out.println(">1>"+src);
			src=updateSrc(mergedSmilName.replace(".smil", ""),src);
			//System.out.println(">2>"+src);
			e.attr("src",src);
		}
		
		
		
		File mergedSmilFile = new File(smil1.getParentFile() + "/"
				+ mergedSmilName);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(mergedSmilFile), "UTF-8"));

		String finalSmilStr = mergedSmil.html()
				.replace("clipbegin", "clipBegin")
				.replace("clipend", "clipEnd");
		out.write(finalSmilStr);
		out.close();
		System.out.println("Smil files merged and ids updated");
	}
	
	public static String updateSrc(String fileName,String str){
		//format - ../html/page_028/page028.html_skipped#leftword166_and
		Pattern p =Pattern.compile("\\.\\./html/([^/]+)/([^\\.]+)");
		String str1=str;
			Matcher m=p.matcher(str1);
			if(m.find()){
				str1=str1.replace(m.group(1)+"/", fileName+"/");
				str1=str1.replace(m.group(2)+".html",fileName+".html");
			}
		return str1;
	}

	public static void processIndex(File leftHtml, File rghtHtml)
			throws Exception {
		// current
		String leftHtmlName = leftHtml.getName();
		String rghtHtmlName = rghtHtml.getName();
		String mergedFileName = leftHtml.getName().replace(".html", "") 
				+ rghtHtml.getName().replace("page", "");
		String mergedFileDirName = mergedFileName.replace(".html", "");

		Iterator<File> itr = htmlFileList.iterator();
		while (itr.hasNext()) {
			File f = itr.next();
			// if (f.getName().equals("toc.html")
			// || f.getName().equals("toc.xhtml")) {
			System.out.println("Updated references:"+f);
			String tocStr = Utils.getFileAsString(f, "UTF-8");

			tocStr = tocStr.replace("&", "[[AMP]]");
			Document doc = Jsoup.parse(tocStr, "", Parser.xmlParser());
			doc.outputSettings().prettyPrint(false);

			Elements links = doc.select("a[href]");
			Iterator<Element> linksItr = links.iterator();
			while (linksItr.hasNext()) {
				Element e = linksItr.next();
				String href = e.attr("href");
				if (href.contains("/" + leftHtmlName)) {
					href = "../" + mergedFileDirName + "/" + mergedFileName;
					e.attr("href", href);

				} else if (href.contains(rghtHtmlName)) {
					href = "../" + mergedFileDirName + "/" + mergedFileName;
					e.attr("href", href);
				}
			}

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f), "UTF-8"));
			out.write(doc.html().replace("[[AMP]]", "&"));
			out.close();

		}
		// update index.html
		System.out.println("Cross-references updated in html.");
	}

	public static void processContent_Opf(File leftHtml, File rghtHtml)
			throws Exception {
		// manifest - remove left and rght page - add spread page.
		// remove smil file reference.
		// spine - remove references for left and rght - add spread page ref and
		// attribute.

		if (contentOpf != null) {
			String leftHtmlName = leftHtml.getName();
			String rghtHtmlName = rghtHtml.getName();
			String mergedFileName = leftHtml.getName().replace(".html", "")+ rghtHtml.getName().replace("page", "");
			String smilFileName=mergedFileName.replace(".html", ".smil");
			String mergedFileDirName = mergedFileName.replace(".html", "");
			String opfStr = Utils.getFileAsString(contentOpf, "UTF-8");
			opfStr = opfStr.replace("&", "[[AMP]]");
			Document opfDoc = Jsoup.parse(opfStr, "", Parser.xmlParser());
			opfDoc.outputSettings().prettyPrint(false);
			Elements manifestItems = opfDoc.select("manifest>item");
			Iterator<Element> manifestItemsItr = manifestItems.iterator();
			Elements manifest = opfDoc.select("manifest");
			Elements spine = opfDoc.select("spine");

			int tFlag = 0;

			while (manifestItemsItr.hasNext()) {
				Element e = manifestItemsItr.next();
				String href = e.attr("href");
				if (href.contains("/" + leftHtmlName)
						|| href.contains("/" + rghtHtmlName)) {
					tFlag++;
					// System.out.println(href);
					String media = e.attr("media-overlay");
					if (tFlag == 1) {
						if(!media.isEmpty() && manifestItems.select("item[id=" + media + "]").size()>0){
							manifestItems.select("item[id=" + media + "]").first()
									.remove();
						}else{
							//System.out.println("Warn : smil not found for the item:"+leftHtmlName);
						}
						
						if(spine.select("itemref[idref=" + e.attr("id") + "]").size()>0){
							spine.select("itemref[idref=" + e.attr("id") + "]")
								.first().remove();
						}
						else{
							//System.out.println("Warn : spine item not found for:"+leftHtmlName);
						}
					}
					if (tFlag == 2) {
							if(!media.isEmpty() && manifestItems.select("item[id=" + media + "]").size()>0){
								manifestItems.select("item[id=" + media + "]").first()
										.remove();
							}
							else{
								//System.out.println("Warn : smil not found for the item:"+leftHtmlName);
							}
							manifest.append("<item id=\""
									+ mergedFileDirName
									+ "\" href=\"html/"
									+ mergedFileDirName
									+ "/"
									+ mergedFileName
									+ "\" media-type=\"application/xhtml+xml\" media-overlay=\"smil-"+smilFileName.replace(".smil","")+"\" />"+System.getProperty("line.separator"));
							
							
							if(spine.select("itemref[idref=" + e.attr("id") + "]").size()>0){
								Element ee = spine.select("itemref[idref=" + e.attr("id") + "]").first();
								ee.attr("idref", mergedFileDirName);
								ee.attr("property", "rendition:spread");
							}
							else{
								//System.out.println("Warn : spine item not found for:"+leftHtmlName);
							}
					}
					e.remove();
				}
			}
			
			//add mergedSmil
			
			String smilM="<item id=\"smil-"+smilFileName.replace(".smil","")+"\" href=\""+"audio/"+smilFileName+"\" media-type=\"application/smil+xml\" />"+System.getProperty("line.separator");
			
			manifest.append(smilM);
			
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(contentOpf), "UTF-8"));
			out.write(opfDoc.html().replace("[[AMP]]", "&"));
			out.close();
			System.out.println("content.opf updated.");
		}
	}
	
	
	public static void processHtml(File left, File rght) throws Exception {
		
		
		removableFiles.add(left);
		removableFiles.add(rght);
		
		String leftStr = Utils.getFileAsString(left, "UTF-8");
		leftStr = leftStr.replace("&", "[[AMP]]");

		String rghtStr = Utils.getFileAsString(rght, "UTF-8");
		rghtStr = rghtStr.replace("&", "[[AMP]]");

		Document leftDoc = Jsoup.parse(leftStr, "", Parser.xmlParser());
		leftDoc.outputSettings().prettyPrint(false);
		Document rghtDoc = Jsoup.parse(rghtStr, "", Parser.xmlParser());
		rghtDoc.outputSettings().prettyPrint(false);

		Element leftBody = leftDoc.select("div").first();
		Element rghtBody = rghtDoc.select("div").first();
		Element templateCss = leftDoc.select("link[rel=stylesheet]").first();
		
		Element leftCss = leftDoc.select("link[rel=stylesheet]").last();
		String leftCssFname = leftCss.attr("href");
		leftCssFname = leftCssFname.substring(
				leftCssFname.lastIndexOf("/") + 1, leftCssFname.length());

		Element rghtCss = rghtDoc.select("link[rel=stylesheet]").last();
		String rghtCssFname = rghtCss.attr("href");
		rghtCssFname = rghtCssFname.substring(
				rghtCssFname.lastIndexOf("/") + 1, rghtCssFname.length());

		String productCode = leftDoc.select("div").first().attr("class");

		// System.out.println(leftBody.html());
		// System.out.println(rghtBody.html());
		Document mergedHtml = Jsoup.parse(new SpreadMerger()
				.getTemplateAsString().replace("&", "[[AMP]]"), "", Parser
				.xmlParser());
		mergedHtml.outputSettings().prettyPrint(false);
		
		Element page = mergedHtml.select(".page").first();
		
		Element productCodeEle=mergedHtml.select("div").first();
		productCodeEle.attr("class",productCode);
		

		
		Iterator<Element> leftBodyItr = leftBody.select("span[id]").iterator();
		while (leftBodyItr.hasNext()) {
			Element span = leftBodyItr.next();
			String id = span.attr("id");
			id = "left" + id;
			span.attr("id", id);
		}

		Iterator<Element> rghtBodyItr = rghtBody.select("span[id]").iterator();
		while (rghtBodyItr.hasNext()) {
			Element span = rghtBodyItr.next();
			String id = span.attr("id");
			id = "rght" + id;
			span.attr("id", id);
		}

		Element leftpageContainer=getPageContainer(leftBody);
		Element rghtpageContainer=getPageContainer(rghtBody);
		
		
		page.appendChild(leftpageContainer);
		page.appendChild(rghtpageContainer);

		
		
		
		Element viewPortM=mergedHtml.select("[name=viewport]").first();
		
		//width=684, height=888
		viewPortM.attr("content","width="+(Integer.parseInt(width)*2)+", height="+height);
		
		
		
		// System.out.println(mergedHtml);
		// css linking

		File lcss = getCssFile(leftCssFname);
		File rcss = getCssFile(rghtCssFname);
		
		Element viewPort = leftDoc.select("meta[name=viewport]").first();
		// getViewPort(viewPort);

		String cssName = processCSS(lcss, rcss);
		Element secondMergedCss = mergedHtml.select("link[rel=stylesheet]")
				.last();
		String href = secondMergedCss.attr("href") + cssName;
		
		secondMergedCss.attr("href", href);
		// writing
		String mergedHtmlFileName = left.getName().replace(".html", "") + rght.getName().replace("page", "");
		File parDir = new File(left.getParentFile().getParentFile()
				.getCanonicalPath()
				+ "/" + mergedHtmlFileName.replace(".html", ""));
		parDir.mkdir();
		File mergedHtmlFile = new File(parDir.getCanonicalPath() + "/"
				+ mergedHtmlFileName);

		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(mergedHtmlFile), "UTF-8"));

		String finalHtml = mergedHtml.html();
		finalHtml = finalHtml.replace("[[AMP]]", "&");
		out.write(finalHtml);
		out.close();
		System.out.println("Htmls merged");
		// complete the cssparser api changes and continue. Completed
	}

	static Element getPageContainer(Element doc){		
		
		Pattern p=Pattern.compile("page[0-9]+Container");

		Iterator<Element>kk=doc.select("[class]").iterator();
		while(kk.hasNext()){
			Element e=kk.next();
			String class_=e.attr("class");
			Matcher m=p.matcher(class_);
			if(m.find()){
				return e;
			}else if(class_.equals("toc") || class_.equals("frontmatter") || class_.equals("glos_cont") ||class_.equals("index") || class_.equals("advert")){
				return e;
			}
		}
		return null;
	}
	
	static void feedFiles(File dir) {
		File[] list = dir.listFiles();
		for (int k = 0; k < list.length; k++) {
			if (list[k].isFile() && list[k].getName().endsWith(".css")) {
				cssFileList.add(list[k]);
			} else if (list[k].isFile() && list[k].getName().endsWith(".smil")) {
				smilFileList.add(list[k]);
			} else if (list[k].isFile() && list[k].getName().endsWith(".html")) {
				htmlFileList.add(list[k]);
			} else if (list[k].isFile()
					&& list[k].getName().equals("content.opf")) {
				contentOpf = list[k];
			} else if (list[k].isDirectory()) {
				feedFiles(list[k]);
			}
		}
	}

	static File getCssFile(String fileName) {
		Iterator<File> itr = cssFileList.iterator();
		while (itr.hasNext()) {
			File f = itr.next();
			String fName = f.getName();
			if (fName.equals(fileName)) {
				return f;
			}
		}
		return null;
	}

	static File getSmilFile(String fileName) {
		Iterator<File> itr = smilFileList.iterator();
		while (itr.hasNext()) {
			File f = itr.next();
			String fName = f.getName();
			if (fName.equals(fileName)) {
				return f;
			}
		}
		return null;
	}

	/*
	 * fall back is viewport width searches for .page div for width. if width is
	 * in px assigns to width.
	 */

	// reference commented.
	static void getViewPort(Element metatag) throws Exception {
		String content = metatag.attr("content");
		content = content.substring(0, content.indexOf(",")).replace("width=","");
		width = content;
		Iterator<File> cssItr = cssFileList.iterator();
		while (cssItr.hasNext()) {
			File f = cssItr.next();
			if (f.getName().equals("template.css")) {
				CParser parser = new CParser(f);
				parser.parse();
				Iterator<CSSNode> nodeItr = parser.cssNodes.iterator();
				while (nodeItr.hasNext()) {
					CSSNode node = nodeItr.next();
					if (node.selector.equals("body")) {
						String wid = node.getAttr("width");
						if (!width.contains("px")) {

						} else {
							width = wid.replaceAll("[^0-9]", "");
						}
					}
				}
			}
		}

	}
}
