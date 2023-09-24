
<h1 align="center">
  <br>
  
CSV-to-JSON-Converter
  <br>
</h1>


** About The Project CSV to JSON Converter**

**Overview:**

The "CSV to JSON Converter" is a Java program designed to transform CSV files into JSON format. This utility offers a streamlined way to process CSV data, ensuring it is correctly formatted and converting it into structured JSON objects. It also handles exceptions related to missing attributes and values in the CSV files.

**Project Structure:**

1. **CSVFileInvalidException and CSVDataMissing Exceptions:** Custom exception classes that handle missing attribute and missing values exceptions, respectively.

2. **CsvToJson Class:** The main class responsible for processing CSV files, converting them to JSON, and handling exceptions.

**Functionality:**

- **CSV to JSON Conversion:** The program scans a specified directory for CSV files and processes them one by one. It reads the first line of each CSV file to identify attribute names and then processes the data records to convert them into JSON objects.

- **Error Handling:**
  - Missing Attribute Check: The program checks if any attributes are missing in the CSV files. If any attributes are missing, it throws a `CSVFileInvalidException`.
  - Missing Data Handling: If missing values are found within the CSV data records, those records are skipped during JSON conversion. The missing data is logged in a separate log file (`LogFile.log`) for reference.

- **JSON Output:** The converted JSON objects are written to corresponding JSON files with the same name as the source CSV file but with a `.json` extension.

- **Existing File Handling:** Before processing, any existing JSON files in the output directory are deleted to ensure a clean slate.

- **User Interaction:** The program provides a user-friendly interface that allows users to view the generated JSON files by entering the desired file name. It provides up to two chances to open and view the JSON files.

**Usage:**

1. Place the CSV files you want to convert to JSON in the specified directory.

2. Compile and run the `CsvToJson` program.

3. The program will process the CSV files, convert them to JSON, and log any missing data.

4. JSON files will be created in the same directory as the source CSV files.

5. Users can view the generated JSON files by entering the desired file name when prompted.

This CSV to JSON Converter provides a convenient way to convert and manage data, ensuring that missing attributes and values are properly handled while producing structured JSON output.
