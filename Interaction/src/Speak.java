import java.io.*;
import java.util.*;

public class Speak {

    protected File scriptFile;
    protected Process process;
    protected String[] voices;
    protected int voiceIndex = 0;
    //protected int pitch = 0; // -10 to +10
    protected int volume = 50; // 0 to 100
    protected int rate = 100; // 80 to 400
    private String sayThis = "";

    public static void main(String[] args) throws IOException {
        new Speak().test();
    }

    public void test() {
        voices = populateVoiceArray();
        int numVoices = voices.length;
        if (numVoices == 0) {
            return;
        }
        Random random = new Random();
        for (int i = 0; i < 10 && i < numVoices; i++) {
            voiceIndex = random.nextInt(numVoices);
            volume = random.nextInt(100);
            System.out.println("Voice: " + voices[voiceIndex]
                    + "  Volume: " + volume);
            speakAndWait("Hello World");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Speak() {//String sayThis) throws IOException {
//        voices = populateVoiceArray();
//        this.sayThis = sayThis;
    }

    protected String[] populateVoiceArray() {
        Scanner input = null;
        try {
            Process p;// = Runtime.getRuntime().exec("say -v victoria i could not translate that");
            if(sayThis.contains("hypotension")) {
                p = Runtime.getRuntime().exec("say -v victoria also means low blood pressure");
            } else if(sayThis.contains("hypertension")) {
                p = Runtime.getRuntime().exec("say -v victoria also means high blood pressure");
            } else if(sayThis.contains("appendectomy")){
                p = Runtime.getRuntime().exec("say -v victoria this is the removal of the appendix");
            } else if(sayThis.contains("biopsy")){
                p = Runtime.getRuntime().exec("say -v victoria this is a test used to help diagnose cancer in a particular region");
            }  else if(sayThis.contains("hysterectomy")){
                p = Runtime.getRuntime().exec("say -v victoria this is the removal of a woman's uterus");
            }  else if(sayThis.contains("dream team")){
                p = Runtime.getRuntime().exec("say -v victoria the dream team deserves to win");
            }  else if(sayThis.contains("atherosclerosis")){
                p = Runtime.getRuntime().exec("say -v victoria this is a disease where plaque buildup makes the inside of an artery more narrow");
            } else {
                p = Runtime.getRuntime().exec("say -v victoria i could not translate that");
            }

            new ProcessReader(p, true); // consume the error stream
            input = new Scanner(p.getInputStream());
            List<String> voiceList = new ArrayList<String>();
            while (input.hasNextLine()) {
                voiceList.add(input.nextLine());
            }
            return voiceList.toArray(new String[0]);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new String[]{};
    }

    protected void makeScriptFile() {
        String speakScript = ""
                + "on run argv\n"
                + "  set theCall to (item 1 of argv)\n"
                + "  set theVoice to (item 2 of argv)\n"
                + "  set theVolume to (item 3 of argv)\n"
                + "  set theRate to (item 4 of argv)\n"
                + "  set oldVolume to output volume of (get volume settings)\n"
                + "  set volume output volume ((oldVolume * theVolume) / 100)\n"
                + "  say -v theVoice  -r theRate theCall with waiting until completion\n"
                + "  set volume output volume (oldVolume)\n"
                + "end run\n";
        FileWriter fw = null;
        try {
            scriptFile = File.createTempFile("speech", ".scpt");
            scriptFile.deleteOnExit();
            fw = new java.io.FileWriter(scriptFile);
            fw.write(speakScript);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void speakAndWait(String call) {
        process = speak(call);
        if (process != null) {
            try {
                process.waitFor();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected Process speak(String call) {
        process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"osascript",
                    scriptFile.getAbsolutePath(),
                    "\"" + call + "\"",
                    "\"" + voices[voiceIndex] + "\"",
                    "" + volume,
                    "" + rate
            });
            new ProcessReader(process, false); // consume the output stream
            new ProcessReader(process, true); // consume the error stream
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return process;
    }

    public void setSayThis(String sayThis) {
        this.sayThis = sayThis;
    }
}

class ProcessReader {

    public ProcessReader(Process process, final boolean errorStream) {
        final InputStream processStream = errorStream
                ? process.getErrorStream()
                : process.getInputStream();
        new Thread() {

            @Override
            public void run() {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(processStream));
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (errorStream) {
                            System.err.println(line);
                        } else {
                            System.out.println(line);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
}