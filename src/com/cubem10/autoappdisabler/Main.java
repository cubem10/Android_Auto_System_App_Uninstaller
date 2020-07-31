package com.cubem10.autoappdisabler;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    static String command, result, adbpath;
    public static void main(String[] args) {
        Scanner adb = new Scanner(System.in);
        System.out.print("adb 경로를 지정해 주세요 (예시: /Users/cubem10/platform-tools). platform-tools는 https://bit.ly/latest-platform-tools 에서 다운받으실 수 있습니다: ");
        adbpath = adb.nextLine();
        Scanner jsoninput = new Scanner(System.in);
        System.out.print("CCSWE Export 형식의 파일의 경로를 지정해 주세요: ");
        String jsonpath = jsoninput.nextLine();
        JSONParser parser = new JSONParser();
        RunCommand cmd = new RunCommand();
        try {

            Object obj = parser.parse(new FileReader(jsonpath));

            JSONObject jsonObject = (JSONObject) obj;

            // loop array
            JSONArray msg = (JSONArray) jsonObject.get("packages");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                String nextiterator = iterator.next();
                System.out.println(nextiterator);
                command = cmd.inputCommand(nextiterator);
                result = cmd.execCommand(command);

                System.out.println(result);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }
}

class RunCommand {
    private StringBuffer buffer;
    private Process process;
    private BufferedReader bufferedReader;
    private StringBuffer readBuffer;

    public String inputCommand(String packagename) {

        buffer = new StringBuffer();

        buffer.append(Main.adbpath + " ");
        buffer.append("shell ");
        buffer.append("pm ");
        buffer.append("uninstall ");
        buffer.append("-k ");
        buffer.append("--user ");
        buffer.append("0 ");
        buffer.append(packagename);

        return buffer.toString();
    }

    public String execCommand(String cmd) {
        try {
            process = Runtime.getRuntime().exec(cmd);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = null;
            readBuffer = new StringBuffer();

            while((line = bufferedReader.readLine()) != null) {
                readBuffer.append(line);
                readBuffer.append("\n");
            }

            return readBuffer.toString();
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
