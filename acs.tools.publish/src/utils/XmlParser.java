package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import acs.tools.publish.functest.MCalss;


public class XmlParser extends MCalss{
        
                private static final  Logger logger = Logger.getLogger(XmlParser.class);
		
		private String filePath;
		private Document document;
		
		public XmlParser(String filePath){
			
			this.filePath = filePath;
			this.load(this.filePath, null);
		}
		
		private void load(String filePath, Logger logger){
			
			File file = new File(filePath);
			System.out.println(file);
			if (file.exists()) {
					SAXReader sr =  new SAXReader();
					try{
							document = sr.read(file);
					}catch(DocumentException e){
					        logger.info(">>>>>>>>>>>>>::XML文件解析异常！"+filePath+"----------------");

					}
			}else{
			        logger.info(">>>>>>>>>>>>>::XML文件不错在！"+filePath+"----------------");

			}
		}
		
		private Element getElementObject(String elementPath){
			return  (Element) document.selectSingleNode(elementPath);
		}
		
		public String getElementText(String elementPath){
			Element element = getElementObject(elementPath);
			if (element != null){
					return element.getText().trim();
			}else{
				return null;
			}
		}
		
		public Element getElement(String pathString) {
			return (Element) document.selectSingleNode(pathString);
			
		}
		
//		public String getElementText(String pathString){
//			Element element = (Element) document.selectSingleNode(pathString);
//			if (element != null){
//				System.out.println(element.getTextTrim());
////					return element.getTextTrim();
//				return element.getText().trim();
//			}
//			return null;
//		}
		
		@SuppressWarnings("unchecked")
		public List<Element> getElements(String pathString){
			return document.selectNodes(pathString);
		}
		
		public List<String> getElementTestList(String pathString){
			List<String> result = new ArrayList<String>();
			for (Object element : document.selectNodes(pathString)){
				result.add(((Element)element).getTextTrim());
			}
			return result;
		}
		
		public Map<String, String> getChildrenInfo(String elementPath){			
			@SuppressWarnings("unchecked")
			List<Element> elements = this.getElement(elementPath).elements();
			Map<String, String> map = new LinkedHashMap<String, String>();
			for (Element el : elements) {
				map.put(el.getName(), el.getTextTrim());
			}
			return map;
		}
	
		public Map<String, String> getChildrenInfoElement(Element element){			
			@SuppressWarnings("unchecked")
			List<Element> elements = element.elements();
			Map<String, String> map = new LinkedHashMap<String, String>();
			for(Element e : elements){
					map.put(e.getName(), e.getTextTrim());
			}
			return map;
		}
		
		public boolean isExist(String elementPath){
			boolean flag = false;
			Element element = this.getElement(elementPath);
			if(element != null){
				flag = true;
			}
			return flag;
		}
	

}
