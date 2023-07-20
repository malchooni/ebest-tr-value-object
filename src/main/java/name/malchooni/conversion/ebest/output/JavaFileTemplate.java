package name.malchooni.conversion.ebest.output;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import name.malchooni.conversion.ebest.output.vo.TemplateData;
import name.malchooni.conversion.ebest.res.ResFileData;
import name.malchooni.conversion.exception.ConversionException;
import name.malchooni.conversion.util.FileHelper;

/**
 * 자바 파일 템플릿
 */
public class JavaFileTemplate {

    private static final String PACKAGE = "package [PACKAGE_PREFIX].vo.[TR_NAME];";
    private static final String IMPORTS = "import [IMPORT];";
    private static final String DESCRIPTION = "// [DESCRIPTION]";
    private static final String CLASS_NAME = "public class [CLASS_NAME] {";
    private static final String MEMBER_DESCRIPTION = "[SPACE]// [DESCRIPTION]";
    private static final String MEMBER_VARIABLE = "[SPACE]private [DATA_TYPE] [COL_NAME];";
    private static final String SET_METHOD = "[SPACE]public void set[METHOD_NAME]([DATA_TYPE] [COL_NAME]) {[LF][SPACE][SPACE]this.[COL_NAME] = [COL_NAME];[LF][SPACE]}";
    private static final String GET_METHOD = "[SPACE]public [DATA_TYPE] get[METHOD_NAME]() {[LF][SPACE][SPACE]return [COL_NAME];[LF][SPACE]}";
    private static final String END_BLOCK = "}";

    private final TemplateData templateData;

    public JavaFileTemplate(TemplateData templateData) {
        this.templateData = templateData;
    }

    /**
     * 자바 파일 생성
     *
     * @param parentPath 생성할 디렉토리 경로
     */
    public void createFile(File parentPath) throws Exception {
        File targetRoot = FileHelper.createDirectory(parentPath, new String[]{"vo", this.templateData.getTrName().toLowerCase()}, 0);
        String className = this.templateData.getClassName().substring(0, 1).toUpperCase() + this.templateData.getClassName().substring(1);
        File newFile = new File(targetRoot, className + ".java");
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(this.replacePackage(this.templateData.getPackagePrefix(), this.templateData.getTrName()));
            fos.write(System.lineSeparator().getBytes());
            fos.write(System.lineSeparator().getBytes());
            if (!this.templateData.getImports().isEmpty()) {
                fos.write(this.replaceImports(this.templateData.getImports()));
                fos.write(System.lineSeparator().getBytes());
            }
            if (this.templateData.getDescription() != null) {
                fos.write(this.replaceDescription(this.templateData.getDescription()));
                fos.write(System.lineSeparator().getBytes());
            }
            fos.write(this.replaceClassName(this.templateData.getClassName()));
            fos.write(System.lineSeparator().getBytes());
            fos.write(this.replaceVariable(this.templateData.getColumns()));
            fos.write(END_BLOCK.getBytes());
            fos.write(System.lineSeparator().getBytes());
            fos.flush();
        } catch (Exception e) {
            throw new ConversionException("create file exception.", e);
        }
    }

    /**
     * 패키지명 변환
     */
    private byte[] replacePackage(String packagePrefix, String trName) {
        String packageName = PACKAGE.replace("[PACKAGE_PREFIX]", packagePrefix).replace("[TR_NAME]", trName);
        return packageName.toLowerCase().getBytes();
    }

    /**
     * 임포트 목록 변환
     */
    private byte[] replaceImports(List<String> importList) {
        StringBuilder sb = new StringBuilder();
        for (String importStr : importList) {
            sb.append(IMPORTS.replace("[IMPORT]", importStr)).append(System.lineSeparator());
        }
        return sb.toString().getBytes();
    }

    /**
     * 주석 변환
     */
    private byte[] replaceDescription(String description) {
        return DESCRIPTION.replace("[DESCRIPTION]", description).getBytes();
    }

    /**
     * 클래스명 변환
     */
    private byte[] replaceClassName(String className) {
        return CLASS_NAME.replace("[CLASS_NAME]", className.substring(0, 1).toUpperCase() + className.substring(1)).getBytes();
    }

    /**
     * 맴버 변수, set, get 메소드 변환
     */
    private byte[] replaceVariable(List<Map<String, String>> columns) {
        StringBuilder member = new StringBuilder();
        StringBuilder setMethod = new StringBuilder();
        StringBuilder getMethod = new StringBuilder();
        for (Map<String, String> column : columns) {

            String memberDescription = column.get(ResFileData.DESC);
            if (memberDescription != null) {
                member.append(MEMBER_DESCRIPTION.replace("[SPACE]", "  ")
                                .replace("[DESCRIPTION]", column.get(ResFileData.DESC)))
                        .append(System.lineSeparator());
            }

            member.append(MEMBER_VARIABLE.replace("[SPACE]", "  ")
                            .replace("[DATA_TYPE]", this.getDataType(column.get(ResFileData.TYPE)))
                            .replace("[COL_NAME]", this.getColumnName(column.get(ResFileData.COLUMN))))
                    .append(System.lineSeparator());

            String methodName = column.get(ResFileData.COLUMN);
            methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

            setMethod.append(
                    SET_METHOD.replace("[SPACE]", "  ")
                            .replace("[METHOD_NAME]", methodName)
                            .replace("[DATA_TYPE]", this.getDataType(column.get(ResFileData.TYPE)))
                            .replace("[COL_NAME]", this.getColumnName(column.get(ResFileData.COLUMN)))
                            .replace("[LF]", System.lineSeparator())
            ).append(System.lineSeparator()).append(System.lineSeparator());

            getMethod.append(
                    GET_METHOD.replace("[SPACE]", "  ")
                            .replace("[METHOD_NAME]", methodName)
                            .replace("[DATA_TYPE]", this.getDataType(column.get(ResFileData.TYPE)))
                            .replace("[COL_NAME]", this.getColumnName(column.get(ResFileData.COLUMN)))
                            .replace("[LF]", System.lineSeparator())
            ).append(System.lineSeparator()).append(System.lineSeparator());
        }

        return (member + System.lineSeparator() + setMethod + getMethod).getBytes();
    }

    /**
     * 데이터타입 변환 char -> string
     */
    private String getDataType(String dataType) {
        if (dataType.equals("char")) {
            return "String";
        } else {
            return dataType;
        }
    }

    /**
     * 카멜 표기법 변환
     */
    private String getColumnName(String columnName) {
        return columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
    }
}
