
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;



//attribute missing exception
/**
 * Custom exception class that handles the missing attribute exception
 *
 */
class CSVFileInvalidException extends Exception
{
    CSVFileInvalidException()
    {
        super("File " +  "\"" + CsvToJson.fileName + "\"" + " is invalid: Field is missing.");
    }

    CSVFileInvalidException(String s)
    {
        super(s);
    }

    public String getMessage()
    {
        return super.getMessage();
    }

}
//value missing exception
/**
 * Custom exception class that handles the missing values exception
 */
class CSVDataMissing extends Exception
{
    CSVDataMissing()
    {
        super("File "+ "\"" + CsvToJson.fileName + "\"" +" line "+ CsvToJson.line + " not converted to json: missing data");
    }

    CSVDataMissing(String s)
    {
        super(s);
    }

    public String getMessage()
    {
        return super.getMessage();
    }

}

/**
 * Custom class that converts the input csv file to json
 */
public class CsvToJson {
    static String fileName = "";
    static int rem = 2;
    //line is for counting line and keeping track of it
    static int line = 0;

    //function to process valid files to json format
    /**
     * method processing files is used to process everything, where it splits the given file, if it catches any exception such as file having missing
     * values or missing attributes it handles it, where if its a missing attribute exception the file is not created to json
     * and if its a missing value exception it handles it by skipping that row and entering other rows of the input file and
     * converts it to json.
     *
     * @param sc the scanner object of the file to be open, fileName the file name of the given file
     * @return returns void
     */
    public static void processingFiles(Scanner sc, String fileName){
        StringTokenizer stringTokenizer = null;
        String toBeStored = "";
        String attribute[] = sc.nextLine().split(",");
        PrintWriter pw = null;
        String errorCatcher = "";
        PrintWriter writeLogFile = null;
        File file = null;
        File logfile = null;
        int count = 0;

        StringTokenizer stringTokenizerForFileName = new StringTokenizer(fileName, ".");
        String jsonFileName =  stringTokenizerForFileName.nextToken()+".json";

        try {

            //opening log file and will be used further to enter any log details if any exception occurs
            logfile = new File("/Users/konarkshah/Desktop/CarRentaManagement/out/production/CarRentaManagement/Files/LogFile"+".log");
            writeLogFile = new PrintWriter(new FileOutputStream(logfile, true));

            //checks if any attribute are missing if yes throws an error
            for (int i=0;i<attribute.length;i++)
            {
                if (attribute[i]=="")
                {
                    count++;
                }
            }

            if(count>0)
            {
                throw new CSVFileInvalidException();
            }

            file  = new File("/Users/konarkshah/Desktop/CarRentaManagement/out/production/CarRentaManagement/Files/" + jsonFileName);
            if(file.exists()){
                throw new Exception("Could not open " +  "\""+ jsonFileName + "\"" + " file already exists");
            }
            //if no error occurs it starts traversing through all lines
            else{
                pw = new PrintWriter(new FileOutputStream(file));
                int k=0;
                boolean flag = false;
                boolean isMissing = false;
                pw.println("[");
                while (sc.hasNextLine())
                {
                    String record[] = sc.nextLine().split(",");
                    isMissing = false;
                    line++;
                    toBeStored = "";
                    for (int i=0;i<record.length;i++)
                    {
                        if(k==0)
                        {
                            toBeStored =  toBeStored + "  {\n";
                        }

                        if(record[i]==""){
                            record[i] = "***";
                            isMissing = true;

                        }
                        char splitRecord[] = record[i].toCharArray();
                        if (splitRecord[0] == '"') {
                            String temp = record[i];
                            for (int j = i + 1; j < record.length; j++) {
                                char nestedSplitRecord[] = record[j].toCharArray();

                                if (nestedSplitRecord[nestedSplitRecord.length - 1] == '"') {
                                    temp = temp + "," + record[j];
                                    if (k != attribute.length - 1) {
                                        toBeStored = toBeStored + "    " + "\"" + attribute[k] + "\"" + ": " + temp + ",\n";
                                    } else {
                                        toBeStored = toBeStored + "    " + "\"" + attribute[k] + "\"" + ": " + temp+"\n";
                                    }
                                    i = j;
                                    break;
                                } else {
                                    temp = temp + "," + record[j];

                                }
                            }
                        }
                        //case for not adding "" to values where its digit
                        else {
                            for (char c : splitRecord) {
                                if (Character.isDigit(c)) {
                                    flag = true;
                                } else {
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag == true && k != attribute.length - 1) {
                                toBeStored = toBeStored + "    " + "\"" + attribute[k] + "\"" + ": " + record[i] + ",\n";
                            } else if (flag == false && k != attribute.length - 1) {
                                toBeStored = toBeStored + "    " + "\"" + attribute[k] + "\"" + ": " + "\"" + record[i] + "\"" + ",\n";
                            } else if (flag == true && k == attribute.length - 1) {
                                toBeStored = toBeStored + "    " + "\"" + attribute[k] + "\"" + ": " + record[i]+"\n";
                            } else if (flag == false && k == attribute.length - 1) {
                                toBeStored = toBeStored + "    " + "\"" + attribute[k] + "\"" + ": " + record[i]+"\n";
                            }
                        }

                        //handling case of adding , in all json objects except the last

                        if(attribute.length-1==k) {
                            k=0;
                            if (sc.hasNextLine()) {
                                toBeStored = toBeStored + "  },";
                            } else {
                                toBeStored = toBeStored + "  }";
                            }
                        }
                        else
                        {
                            k++;
                        }

                    }
                    if(!isMissing){
                        pw.println(toBeStored);
                    }

                    //if there are any missing values they will be written in log file instead of json
                    else{
                        try{
                            errorCatcher = toBeStored;
                            if(errorCatcher.length()!=0) {
                                throw new CSVDataMissing();
                            }
                        } catch (CSVDataMissing e) {
                            System.out.println(e);
                            writeLogFile.println("In file "+jsonFileName+" line "+line);
                            String missingValuesAttributeName = "";
                            for(int i =0; i< record.length; i++){
                                if (Objects.equals(record[i], "***"))
                                {
                                    missingValuesAttributeName = missingValuesAttributeName + attribute[i];
                                }
                                else
                                {
                                    writeLogFile.print(record[i] + " ");
                                }
                            }

                            writeLogFile.println();
                            writeLogFile.println("Missing: " + missingValuesAttributeName);
                        }
                    }
                }
                pw.println("]");
            }
        }
        catch (CSVFileInvalidException e)
        {
        //writes the log of all the missing attribute
            writeLogFile.println("File " + "\"" + fileName + "\"" + " is invalid");
            writeLogFile.println("Missing Field: "+(attribute.length-count)+" detected, "+count+" missing");
            for (int i=0;i<attribute.length;i++)
            {
                if (attribute[i]=="" && attribute.length-1!=i)
                {
                    writeLogFile.print("***"+",");
                }
                else if (attribute.length-1>i)
                {
                    writeLogFile.print(attribute[i]+",");
                }
                else if (attribute[i]=="" && attribute.length-1==i) {

                } else if (attribute.length-1==i)
                {
                    writeLogFile.print(attribute[i]);
                }
            }
            writeLogFile.println();
            deleteJSONFile();
            System.out.println(e);
            System.out.println("File is not converted to JSON.");

            if (writeLogFile!=null)
            {
                writeLogFile.close();
            }
            System.exit(0);
        }
        catch (Exception e){
            System.out.println("Could not open " + "\"" +jsonFileName  + "\"" + " file already exists");
            if (writeLogFile!=null)
            {
                writeLogFile.close();
            }
            System.exit(0);
        }
        finally {

            if (writeLogFile!=null)
            {
                writeLogFile.close();
            }

            if(pw!=null) {
                pw.close();
            }
        }
    }
    /**
     * deletes the json files in the directory. It is called twice once at the start of the program to remove any existing json file
     * and again when an missing attribute exception is called and all the existing created files are to be deleted.
     *
     * @return returns void
     */
    public static void deleteJSONFile(){
        File folder1  = new File("/Users/konarkshah/Desktop/CarRentaManagement/out/production/CarRentaManagement/Files/");
        for(File fileEntry1 : folder1.listFiles())
        {
            if(fileEntry1.getName().endsWith(".json")){
                fileEntry1.delete();
            }
        }
    }
    /**
     * Starting point of the program
     *
     * @return returns void
     */
    public static void main(String[] args) {
        deleteJSONFile();
        Scanner sc = new Scanner(System.in);
        final File folder = new File("/Users/konarkshah/Desktop/CarRentaManagement/out/production/CarRentaManagement/Files/");
        Scanner scanner = null;
        for(final File fileEntry : folder.listFiles())
        {
            fileName = fileEntry.getName();
            if(fileEntry.getName().endsWith(".csv"))
            {
                File fIn = new File(fileEntry.getPath());
                try {
                    scanner = new Scanner(fIn);
                }
                catch (FileNotFoundException e)
                {
                    System.out.println("Could not open input file " + "\"" + fileEntry.getName() + "\"" + " for reading. Please check if file exists! Program will terminate after closing any opened files");
                    System.exit(0);
                }
                processingFiles(scanner, fileEntry.getName());
            }

        }

        scanner.close();

        while(rem!=0){
            System.out.print("Enter the name of the file you want to open output file: ");
            Scanner sc1 = new Scanner(System.in);
            String fileToOpen = sc1.nextLine();
            try{
                FileReader f = new FileReader("/Users/konarkshah/Desktop/CarRentaManagement/out/production/CarRentaManagement/Files/" + fileToOpen);
                BufferedReader br = new BufferedReader(f);
                String str = "";
                String line = br.readLine();
                while (line != null){
                    str +=line + "\n";
                    line = br.readLine();
                }
                System.out.println(str);
                System.exit(0);
            }
            catch (FileNotFoundException e){
                System.out.println("Could not open output file " + "\"" + fileToOpen + "\"" +"  for reading. Please check if file exists!");
                System.out.println(rem-1 + " chance remaining");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            rem--;
        }
    }
}
