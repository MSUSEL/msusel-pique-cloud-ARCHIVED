import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.json.*;

public class ConvertJsonToCSVTest {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_LIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNBOLD = "\u001B[21m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_STOP_UNDERLINE = "\u001B[24m";
    public static final String ANSI_BLINK = "\u001B[5m";

    public static void main(String[] args) throws JSONException, IOException {
        System.out.println(ANSI_BLUE + "Skny Started");
        String skny_folder_location = "src/main/resources/Snky/Docker_Offical_Images";
        String skny_result_location = "src/main/resources/Snky_CSV_Results";
        String skny_key = "vulnerabilities";
        convertJsonToCSV(skny_folder_location, skny_result_location, skny_key);
        System.out.println(ANSI_RESET + "Skny Done \n");

        String trivy_folder_location = "src/main/resources/Trivy/Docker_Offical_Images";
        String trivy_result_location = "src/main/resources/Trivy_CSV_Results";
        String trivy_key = "Results";
        System.out.println(ANSI_BLUE + "Trivy Started");
        convertJsonToCSV(trivy_folder_location, trivy_result_location, trivy_key);
        System.out.println(ANSI_RESET + "Trivy Done \n");
        }

        private static File[] getFileList(String folder_location) {
            File folder = new File(folder_location);
            File[] listOfFiles = folder.listFiles();
            return listOfFiles;
        }

        private static String createFileString(File item) throws IOException {
            File file = item.getAbsoluteFile();
            String jsonArrayString = new String(Files.readAllBytes(file.toPath()));
            return jsonArrayString;
        }

        private static String convertFileName(String str_json, String location) {
            String csv_file_name = str_json.replace(".json",".csv");
            return location + "/" + csv_file_name;
        }

        public static void convertJsonToCSV(String skny_folder_location, String skny_result_location, String key) throws IOException {
            File[] listOfFiles = getFileList(skny_folder_location);
            int without_result = 0;
            int with_result = 0;
            System.out.println(ANSI_WHITE+ "Total number of files is: " + listOfFiles.length);

            for (int i = 0; i < listOfFiles.length; i++) {
                String jsonArrayString = createFileString(listOfFiles[i]);
                String json_file_name = listOfFiles[i].getName();
                String csv_file_name = convertFileName(json_file_name, skny_result_location);
                System.out.println(ANSI_CYAN + "Start Processing Json File " + json_file_name);
                if (!jsonArrayString.isEmpty()) {
                    if (jsonArrayString.charAt(0) == '{') {
                        JSONObject jsonObject = new JSONObject(jsonArrayString);
                        if (jsonObject.has(key)) {
                            JSONArray jsonArray = jsonObject.getJSONArray(key);
                            File csv = new File(csv_file_name);
                            String vulnerabilities_string = CDL.toString(jsonArray);
                            FileUtils.writeStringToFile(csv, vulnerabilities_string);
                            System.out.println( ANSI_GREEN + "Data has been written to "+ csv_file_name);
                            with_result++;
                        }else {
                            without_result++;
                            System.out.println(ANSI_RED + json_file_name + "No Results");
                        }
                    } else if (jsonArrayString.charAt(0) == '[') {
                        JSONArray jsonArray1 = new JSONArray(jsonArrayString);
                        JSONObject jsonObject = jsonArray1.getJSONObject(0);
                        JSONArray jsonArray2 = jsonObject.getJSONArray(key);
                        File csv = new File(csv_file_name);
                        String vulnerabilities_string = CDL.toString(jsonArray2);
                        FileUtils.writeStringToFile(csv, vulnerabilities_string);
                        System.out.println( ANSI_YELLOW + "Data has been written to "+ csv_file_name);
                        with_result++;
                    }
                }else{
                    without_result++;
                    System.out.println(ANSI_RED + "WARNING EMPTY FILE " + json_file_name);
                }
                System.out.println(ANSI_LIGHT_YELLOW + "Total number of files without results: " + without_result);
                System.out.println(ANSI_LIGHT_YELLOW + "Total number of files with results: " + with_result);
            }
        }
    }

