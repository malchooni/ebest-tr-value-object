package name.malchooni.conversion.ebest.res;

import name.malchooni.conversion.exception.MessageException;

import java.io.File;
import java.util.regex.Pattern;

/**
 * RES file to data map
 */
public class ResMessageExecutor {

    private String resRootPath;
    private final ResDataRepository resDataRepository;

    public ResMessageExecutor() {
        this.resDataRepository = new ResDataRepository();
    }

    /**
     * res file 읽어 레파지토리에 적재
     */
    public void loadResFiles() throws Exception {

        if (this.resRootPath == null) {
            throw new MessageException("res root path is null.");
        }

        File resRoot = new File(this.resRootPath);
        if (!resRoot.isDirectory()) {
            throw new MessageException("this path is not directory. [" + this.resRootPath + "]");
        }

        File[] files = resRoot.listFiles(file -> file.isFile() &&
                !Pattern.compile("^([\\S]+(_[1-2]\\.(?i)(res))$)").matcher(file.getName()).matches());

        if (files == null || files.length < 1) {
            throw new MessageException("not found res file");
        }

        ResFileReader resFileReader = new ResFileReader();
        int successCount = 0;
        for (File resFile : files) {
            try {
                ResFileData resFileData = resFileReader.load(resFile);
                this.resDataRepository.putResFileData(resFileData.getName(), resFileData);
                successCount++;

                System.out.println("[" + resFileData.getName() + "] file loaded..");
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println(successCount + "res file loaded..");
    }

    public void setResRootPath(String resRootPath) {
        this.resRootPath = resRootPath;
    }

    public ResDataRepository getResDataRepository() {
        return this.resDataRepository;
    }
}
