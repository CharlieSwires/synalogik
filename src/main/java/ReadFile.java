import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    static ReadFile rf = null;
    static final int  MAX_LENGTH_OF_WORD = 100;
    static final String ls = "\n";
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage java ReadFile <filename>");
            System.exit(0);
        }
        //Standard read file into a string with EOL characters
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        // delete the last new line separator
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        String content = stringBuilder.toString();
        System.out.println(results(preprocess(content)));
    }
    protected static String preprocess (String input) {
        //Join up words using a -EOL
        input = input.replaceAll("-"+ls, "");
        //replace EOL with <space>
        input = input.replaceAll(ls, " ");
        //replace "
        input = input.replaceAll("\"", "");
        //replace ,.()
        input = input.replaceAll("[,.)(]", "");
        System.out.println("Input="+input);
        return input;
    }

    class ListOfWords {
        private ArrayList<String> listOfWords;

        public ArrayList<String> getListOfWords() {
            return listOfWords;
        }

        public void setListOfWords(ArrayList<String> listOfWords) {
            this.listOfWords = listOfWords;
        }
    }

    class Index {
        public int index;
    }
    public static String results(String content) {
        rf = new ReadFile();
        StringBuilder sb = new StringBuilder();
        //lists of words
        ListOfWords[] listOfWordsGroupedBySize = new ListOfWords[MAX_LENGTH_OF_WORD];
        for(int i = 0; i < MAX_LENGTH_OF_WORD; i++) {
            ListOfWords listOfWords = rf.new ListOfWords();
            listOfWords.setListOfWords(new ArrayList<String>());
            listOfWordsGroupedBySize[i] = listOfWords;
        }
        String word = null;
        Index index = rf.new Index();
        index.index = 0;
        Integer wordCount = 0;
        Long sum = 0l;
        while((word = readWord(content,index))!=null) {
            wordCount++;
            if(word.length() < MAX_LENGTH_OF_WORD) {
                listOfWordsGroupedBySize[word.length()].getListOfWords().add(word);
                sum += word.length();
            } else {
                System.out.println("MAX_LENGTH_OF_WORD exceeded");
            }
        }
        sb.append("Word count="+wordCount+ls);

        Double average = sum / (1.0*wordCount);
        sb.append("Average word length="+average+ls);
        int max = 0;
        for(int i = 1; i<MAX_LENGTH_OF_WORD;i++) {
            if(listOfWordsGroupedBySize[i].getListOfWords().size()> 0) {
                sb.append("Number of words of length "+i+" is "+listOfWordsGroupedBySize[i].getListOfWords().size()+ls);
                if (listOfWordsGroupedBySize[i].getListOfWords().size() > max) {
                    max = listOfWordsGroupedBySize[i].getListOfWords().size();
                }
            }
        }
        List<Integer> matches = new ArrayList<Integer>();
        sb.append("The most frequently occuring word length is "+max+", for word lengths of ");
        int matchCount = 0;
        for(int i = 1; i<MAX_LENGTH_OF_WORD;i++) {
            if(listOfWordsGroupedBySize[i].getListOfWords().size() == max) {
                matches.add(i);
                matchCount++;
                if (matchCount == 1 ) {
                    sb.append(i);
                }else {
                    sb.append(" & "+ i);
                }
            }
        }
        sb.append(ls);
        return sb.toString();
    }

    private static String readWord(String content, Index index) {
        StringBuilder returnString = new StringBuilder();
        if (index.index < content.length()) {

            for(int i = index.index; i < content.length();) {
                if (content.charAt(i) == ' ') {
                    index.index = i+1;
                    return returnString.toString();
                }
                returnString.append(content.charAt(i++));
            }
            index.index = content.length();
            return returnString.toString();
        } else {
            return null;
        }
    }

}
