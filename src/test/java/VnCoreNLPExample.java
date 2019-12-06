import vn.corenlp.wordsegmenter.WordSegmenter;
import vn.pipeline.*;

import java.awt.List;
import java.io.*;
import java.util.ArrayList;
public class VnCoreNLPExample {
    public static void main(String[] args) throws IOException {
    
        // "wseg", "pos", "ner", and "parse" refer to as word segmentation, POS tagging, NER and dependency parsing, respectively. 
        String[] annotators = {"wseg", "pos", "ner", "parse"}; 
        VnCoreNLP pipeline = new VnCoreNLP(annotators); 
    
//        String str = "Ông Nguyễn Khắc Chúc  đang làm việc tại Đại học Quốc gia Hà Nội. "
//                    + "Bà Lan, vợ ông Chúc, cũng làm việc tại đây."; 
        String str = "Tôi cần mua bảo hiểm một xe Kia Morning và hai xe Mazda CX5.";
        Annotation annotation = new Annotation(str); 
        pipeline.annotate(annotation); 

        
        System.out.println(annotation.toString());
        // 1    Ông                 Nc  O       4   sub 
        // 2    Nguyễn_Khắc_Chúc    Np  B-PER   1   nmod
        // 3    đang                R   O       4   adv
        // 4    làm_việc            V   O       0   root
        // ...
        
        //Write to file
        //PrintStream outputPrinter = new PrintStream("output.txt");
        //pipeline.printToFile(annotation, outputPrinter); 
    
        // You can also get a single sentence to analyze individually 
        Sentence firstSentence = annotation.getSentences().get(0);
        System.out.println(firstSentence.getWordSegmentedSentence());
        ArrayList<Word> words = (ArrayList) firstSentence.getWords();
        ArrayList<String> amountList = new ArrayList<String>();
        ArrayList<String> nameList  = new ArrayList<String>();
        for (Word word : words) {
        	System.out.println(word.getDepLabel());
        	System.out.println(word.getForm());
        	System.out.println(word.getNerLabel());
        	System.out.println(word.getPosTag());
        	if (word.getPosTag().equalsIgnoreCase("M")) {
        		amountList.add(word.getForm());
        	}
        	if (word.getPosTag().equalsIgnoreCase("Np")) {
        		nameList.add(word.getForm());
        	}
        }
        ArrayList<Item> items = new ArrayList<VnCoreNLPExample.Item>();
        for (int i=0; i<nameList.size(); i++) {
        	Item itemTmp = new VnCoreNLPExample.Item();
        	int amount = 0;
        	try {
        		amount = Integer.parseInt(amountList.get(i));
        	} catch (Exception e) {
				// TODO: handle exception
        		amount = convertQuantity(amountList.get(i));
        		System.out.println(amount);
			}
  
        	itemTmp.setAmount(amount);
        	itemTmp.setName(nameList.get(i));
        	items.add(itemTmp);
        }
        

    }
    
    private static class Item{
    	int amount;
    	String name;
    	
		public Item() {
			super();
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
    	
    }
    
    public static int convertQuantity(String quantity) {
    	int res = 0;
    	String quantityList[] = {"không","một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín", "mười"};
    	for (int i=0; i<quantityList.length; i++ ) {
    		if (quantityList[i].equalsIgnoreCase(quantity)) {
    			res = i;
    			break;
    		}
    	}
    	
    	return res;
    }
}