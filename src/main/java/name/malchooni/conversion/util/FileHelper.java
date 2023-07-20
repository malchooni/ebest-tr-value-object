package name.malchooni.conversion.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 파일 유틸
 */
public class FileHelper {

    private FileHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 재귀적 디렉토리 생성
     *
     * @param parentPath 루트 경로
     * @param packageArr 패키지명 ( 배열 )
     * @param idx        인덱스 번호
     * @return packageArr 마지막 배열의 이름으로 생성된 디렉토리 File 객체
     */
    public static File createDirectory(File parentPath, String[] packageArr, int idx) throws IOException {
        if (idx >= packageArr.length) {
            return parentPath;
        }

        File dir = new File(parentPath, packageArr[idx]);
        if (!dir.isDirectory()) {
            Files.createDirectory(dir.toPath());
        }

        return createDirectory(dir, packageArr, idx + 1);
    }
}
