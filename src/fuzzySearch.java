// --== CS400 File Headearr Information ==--
// Name: <Liangqi Cai>
// Email: <lcai42@wisc.edu>
// Team: <JB>
// TA: <Harper>
// Lecturer: <Florian>
// Notes to Grader: <optional extra notes>

package Application;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import com.google.common.io.Files;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;


public class fuzzySearch {
    /**
     * This method will return the article that the user wants and a list of articles related to
     * user’s input. (More related articles will show first).
     *
     * @param searchInput - string that user used to search
     * @return a linkedlist of articleItems according to the search term
     */

    public static LinkedList<ArticleItem> search(String searchInput) {

        ArticleList articleList = new ArticleList();
        File file = new File("data.json");
        if (file.exists() && !file.isDirectory()) {
            articleList.loadFromFile("data.json");
        }

        int n = articleList.size();
        Pair arr[] = new Pair[n];

        for (int i = 0; i < n; i++) {
            double stringDifference = compareStrings(searchInput, articleList.get(i).getTitle());
            arr[i] = new Pair(articleList.get(i), stringDifference);
        }
        compare(arr, n);
        LinkedList<ArticleItem> resultList = new LinkedList<ArticleItem>();
        for (int j = n - 1; j >= 0; j--) {
            resultList.add(arr[j].x);
        }
        return resultList;
    }


    /**
     * The private helper method is used for search method for sorting the resultList (More related
     * articles will show first)
     *
     * @param arr - array of ArticleItem-relatedPoint pair
     * @param n   - size of the array
     */
    private static void compare(Pair[] arr, int n) {
        Arrays.sort(arr, new Comparator<Pair>() {
            @Override
            // override compare method to sort the second element, which is type double.
            public int compare(Pair p1, Pair p2) {
                if (p1.y < p2.y) {
                    return -1;
                }
                if (p1.y > p2.y) {
                    return 1;
                }
                return 0;
            }
        });
    }


    /**
     * The private helper method is used for comparing two strings, more related string will return a
     * smaller double value
     *
     * @param str1 - first string
     * @param str2 - second string
     * @return double value representing string similarity.
     */

    private static double compareStrings(String str1, String str2) {
        ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
        ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());
        int intersection = 0;
        int union = pairs1.size() + pairs2.size();
        for (int i = 0; i < pairs1.size(); i++) {
            Object pair1 = pairs1.get(i);
            for (int j = 0; j < pairs2.size(); j++) {
                Object pair2 = pairs2.get(j);
                if (pair1.equals(pair2)) {
                    intersection++;
                    pairs2.remove(j);
                    break;
                }
            }
        }
        return (2.0 * intersection) / union;
    }

    /**
     * The private helper method is used for creating an array of adjacent letter pairs in order to
     * comparing two strings
     *
     * @param str - input string
     * @return array of letter pairs, letters are adjacent
     */

    private static String[] letterPairs(String str) {
        int numPairs = str.length() - 1;
        String[] pairs = new String[numPairs];
        for (int i = 0; i < numPairs; i++) {
            pairs[i] = str.substring(i, i + 2);
        }
        return pairs;
    }

    /**
     * The private helper method is used for creating an array of two character Strings in order to
     * comparing two strings
     *
     * @param str - input string
     * @return array of two character Strings
     */
    @SuppressWarnings("unchecked") private static ArrayList wordLetterPairs(String str) {
        ArrayList allPairs = new ArrayList();
        String[] wordsInString = str.split("\\s");
        for (int w = 0; w < wordsInString.length; w++) {
            String[] pairs = letterPairs(wordsInString[w]);
            for (int p = 0; p < pairs.length; p++) {
                allPairs.add(pairs[p]);
            }
        }
        return allPairs;
    }


}
