package name.malchooni.conversion;

import name.malchooni.conversion.exception.ConversionException;

/**
 * 프로퍼티 오브젝트
 */
public class Property {

    // ebest res 파일 루트 경로
    private final String resRootPath;
    // VO 자바 파일 생성 위치
    private final String outputRootPath;
    // 생성 자바 파일 패키지명 (선택)
    private final String packagePrefixName;

    public Property(String[] args) throws ConversionException {

        if (args == null || args.length < 2) {
            throw new ConversionException("필수 파라미터 없음");
        }

        this.resRootPath = args[0];
        this.outputRootPath = args[1];
        if (args.length == 3 && !args[2].isEmpty()) {
            this.packagePrefixName = args[2];
        } else {
            this.packagePrefixName = "name.malchooni.conversion";
        }
    }

    public String getResRootPath() {
        return resRootPath;
    }

    public String getOutputRootPath() {
        return outputRootPath;
    }

    public String getPackagePrefixName() {
        return packagePrefixName;
    }
}
